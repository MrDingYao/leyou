package com.leyou.cart.service.impl;

import com.leyou.cart.client.GoodsClient;
import com.leyou.auth.entity.UserInfo;
import com.leyou.cart.client.SpecificationClient;
import com.leyou.cart.filter.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.cart.pojo.Collection;
import com.leyou.cart.service.ICartService;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.pojo.Spu;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 23 19:42
 **/
@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    private static final String PRE_KEY = "ly:cart:uid:";

    private static final String KEY_PREFIX_COLLECT = "ly:collect:uid:";    //我的收藏前缀

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    /**
     * 添加商品至购物车 TODO 将商品的id和数量存入redis
     * @param cart
     */
    @Override
    public void addCart(Cart cart) {
        // 获得当前的用户信息
        UserInfo user = LoginInterceptor.getLoginUser();
        String key = PRE_KEY + user.getId();
        // 获得redis的map操作对象
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
        Integer num = cart.getNum();
        Long skuId = cart.getSkuId();
        // 判断是否存有该商品
        Boolean hasKey = hashOps.hasKey(skuId.toString());
        // 如果存在，取出原来的该商品数据，增加上此次的数量，再存入
        if (hasKey) {
            String json = Objects.requireNonNull(hashOps.get(skuId.toString())).toString();
            cart = JsonUtils.parse(json, Cart.class);
            cart.setNum(cart.getNum() + num);
        }

        // 将数据存入redis中
        hashOps.put(cart.getSkuId().toString(), Objects.requireNonNull(JsonUtils.serialize(cart)));
    }

    /**
     * 查询用户购物车中的商品 TODO 获得的是商品的skuId和数量,然后去数据库查询该sku的具体信息,
     * @return
     */
    @Override
    public List<Cart> queryCartList() {
        // 获得当前用户的信息
        UserInfo user = LoginInterceptor.getLoginUser();
        String key = PRE_KEY + user.getId();
        if (!this.redisTemplate.hasKey(key)) {
            return null;
        }
        // 获得redis操作对象
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        List<Object> carts = hashOps.values();
        if (CollectionUtils.isEmpty(carts)) {
            return null;
        }
        // 查询具体的商品数据
        List<Cart> cartList = carts.stream().map(c -> JsonUtils.parse(c.toString(), Cart.class)).collect(Collectors.toList());
        // 遍历,查询每个商品的具体数据,并赋值
        cartList.forEach(cart -> {
            getCart(cart);
        });
        return cartList;
    }

    private void getCart(Cart cart) {
        ResponseEntity<Sku> responseEntity = this.goodsClient.querySkuBySkuId(cart.getSkuId());
        Sku sku = responseEntity.getBody();
        cart.setPrice(sku.getPrice());
        cart.setTitle(sku.getTitle());
        cart.setEnable(sku.getEnable());
        // 对ownSpec属性进行处理
        String ownSpec = sku.getOwnSpec();
        // 获得specParam集合
        Spu spu = this.goodsClient.querySpuById(sku.getSpuId());
        List<SpecParam> specParams = this.specificationClient.querySpecParams(null, spu.getCid3(), null, null);
        // 将ownSpec从json格式专成map
        Map<Long, String> parseMap = JsonUtils.parseMap(ownSpec, Long.class, String.class);
        // 获得其中的所有的key，是[4,12,13]
        Set<Long> keys = parseMap.keySet();
        Map<String, String> ownParam = new HashMap<>();
        // 进行匹配，存入map中
        specParams.stream().filter(p -> keys.contains(p.getId())).forEach(p -> ownParam.put(p.getName(),parseMap.get(p.getId())));
        cart.setOwnSpec(JsonUtils.serialize(ownParam));
        cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : sku.getImages().split(",")[0]);
    }

    /**
     * 修改购物车中的商品的数量
     * @param skuId
     * @param num
     */
    @Override
    public void editCart(Long skuId, Integer num) {
        // 获得当前的用户信息
        UserInfo user = LoginInterceptor.getLoginUser();
        String key = PRE_KEY + user.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        // 获得redis中存放的商品的对象
        Cart cart = JsonUtils.parse(hashOps.get(skuId.toString()).toString(), Cart.class);
        // 修改商品的数量
        cart.setNum(num);
        // 重新设置进redis
        hashOps.put(skuId.toString(),JsonUtils.serialize(cart));
    }

    /**
     * 删除购物车中的商品
     * @param skuId
     */
    @Override
    public void deleteCart(Long skuId) {
        UserInfo user = LoginInterceptor.getLoginUser();
        String key = PRE_KEY + user.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        hashOps.delete(skuId.toString());
    }

    /**
     * 删除用户提交订单后的购物车商品
     * @param ids
     */
    @Override
    public void deleteCarts(String ids) {
        String[] skuids = ids.split(",");
        for (String skuid : skuids) {
            this.deleteCart(Long.parseLong(skuid));
        }
    }

    /**
     * 查询用户购物车中商品的数量
     * @return
     */
    @Override
    public Integer queryCartCount() {
        // 先获取当前的用户购物车中的所有商品
        List<Cart> carts = this.queryCartList();
        Integer count;
        if (CollectionUtils.isEmpty(carts)) {
            count = 0;
        } else {
            // 获得所有商品数量之和
            count = carts.stream().map(Cart::getNum).reduce((n1, n2) -> n1 + n2).get();
        }
        return count;
    }

    /**
     * 查询id集合对应的商品的信息
     * @param ids
     * @return
     */
    @Override
    public List<Cart> queryCartsByIds(List<Long> ids) {
        List<Cart> carts = new ArrayList<>();
        ids.forEach(id -> {
            Cart cart = new Cart();
            cart.setSkuId(id);
            carts.add(cart);
        });

        carts.forEach(c -> this.getCart(c));
        return carts;
    }


    /**
     * 查询我的收藏
     * @return
     */
    @Override
    public List<Collection> getCollect() {
        UserInfo userInfo = LoginInterceptor.getLoginUser();


        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(KEY_PREFIX_COLLECT + userInfo.getId());

        List<Object> values = ops.values();

        List<Collection> collections = new ArrayList<>();

        if (null != values) {
            values.forEach(v -> collections.add(JsonUtils.parse(v.toString(), Collection.class)));

        }

        return collections;
    }

    /**
     * 购物车商品添加到我的收藏
     * @param collect
     */
    @Override
    @Transactional
    public void addCollection(Collection collect) {
        //收藏的商品存入reids中
        UserInfo userInfo = LoginInterceptor.getLoginUser();

        collect.setUserId(userInfo.getId());
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(KEY_PREFIX_COLLECT + userInfo.getId());


        ops.put(collect.getSkuId().toString(), JsonUtils.serialize(collect));

        //商品添加成功后从购物车中删除
        deleteCart(collect.getSkuId());
    }

    /**
     * 删除我的收藏中的商品
     * @param skuId
     */
    @Override
    public void deleteCollect(Long skuId) {
        UserInfo user = LoginInterceptor.getLoginUser();
        String key = KEY_PREFIX_COLLECT+ user.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        hashOps.delete(skuId.toString());
    }

    /**
     *
     * @param cart
     */
    @Override
    @Transactional
    public void addCartFromCollect(Cart cart) {
        this.addCart(cart);
        this.deleteCollect(cart.getSkuId());
    }
}
