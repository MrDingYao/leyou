package com.leyou.cart.service.impl;

import com.leyou.cart.client.GoodsClient;
import com.leyou.auth.entity.UserInfo;
import com.leyou.cart.client.SpecificationClient;
import com.leyou.cart.filter.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    /**
     * 添加商品至购物车
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
        } else {
            // 如果redis中不存在，则去后台中按照skuId查询具体的sku
            ResponseEntity<Sku> skuResp = this.goodsClient.querySkuBySkuId(skuId);
            // 判断该商品在后台数据库中是否存在
            if (skuResp.getStatusCode() != HttpStatus.OK || !skuResp.hasBody() ) {
                logger.error("添加购物车的商品不存在：skuId:{}",skuId);
                throw new RuntimeException();
            }
            // 获得该商品
            Sku sku = skuResp.getBody();
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
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
        // 将数据存入redis中
        hashOps.put(cart.getSkuId().toString(), Objects.requireNonNull(JsonUtils.serialize(cart)));
    }

    /**
     * 查询用户购物车中的商品
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
        return carts.stream().map(c -> JsonUtils.parse(c.toString(), Cart.class)).collect(Collectors.toList());
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
}
