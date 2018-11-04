package com.leyou.order.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leyou.auth.entity.UserInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.common.utils.IdWorker;
import com.leyou.item.pojo.Address;
import com.leyou.item.pojo.Sku;
import com.leyou.order.client.AddressClient;
import com.leyou.order.client.GoodsClient;
import com.leyou.order.client.UserClient;
import com.leyou.order.interceptor.LoginInterceptor;
import com.leyou.order.mapper.OrderDetailMapper;
import com.leyou.order.mapper.OrderMapper;
import com.leyou.order.mapper.OrderStatusMapper;
import com.leyou.order.pojo.Order;
import com.leyou.order.pojo.OrderDetail;
import com.leyou.order.pojo.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author: HuYi.Zhang
 * @create: 2018-05-04 10:11
 **/
@Service
public class OrderService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper detailMapper;

    @Autowired
    private OrderStatusMapper statusMapper;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private AddressClient addressClient;

    @Autowired
    private AmqpTemplate amqpTemplate;

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    /**
     * 生成订单
     * @param order
     * @return
     */
    @Transactional
    public Long createOrder(Order order) {
        // 生成orderId
        long orderId = idWorker.nextId();
        order.setOrderId(orderId);
        // 计算总价
        Long totalPrice = 0L;
        // 获取商品详情
        List<OrderDetail> orderDetails = order.getOrderDetails();
        for (OrderDetail d : orderDetails) {
            Sku sku = this.goodsClient.querySkuBySkuId(d.getSkuId()).getBody();
            d.setOrderId(orderId);
            String image = StringUtils.isEmpty(sku.getImages()) ? "" : sku.getImages().split(",")[0];
            d.setImage(image);
            d.setPrice(sku.getPrice());
            d.setTitle(sku.getTitle());
            totalPrice += sku.getPrice()*d.getNum();

            // 调用商品微服务,删减库存
            Boolean boo = this.goodsClient.updateStock(sku.getId(), d.getNum());
            if (!boo) {
                throw new RuntimeException();
            }
        }
        order.setTotalPay(totalPrice);
        order.setActualPay(totalPrice);
        // 获取登录用户
        UserInfo user = LoginInterceptor.getLoginUser();
        // 获取用户的昵称信息
        String nickName;
        ResponseEntity<com.leyou.item.pojo.UserInfo> responseEntity = this.userClient.queryUserInfo(LoginInterceptor.getLoginUser().getId());
        if (!responseEntity.hasBody()) {
            nickName = user.getUsername();
        } else {
            nickName = responseEntity.getBody().getNickname();
        }
        order.setBuyerNick(nickName);
        order.setBuyerRate(false);
        order.setCreateTime(new Date());
        order.setUserId(user.getId());
        // 获取地址数据
        Address address = this.addressClient.queryAddressById(order.getAddressId());
        String[] split = address.getAreaAddress().split("-");
        order.setReceiverState(split[0]);
        order.setReceiverCity(split[1]);
        order.setReceiverDistrict(split[2]);
        order.setReceiverAddress(address.getDetailAddress());
        order.setReceiverZip(address.getReceiveZip());
        // 保存数据
        this.orderMapper.insertSelective(order);

        // 保存订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCreateTime(order.getCreateTime());
        orderStatus.setStatus(1);// 初始状态为未付款
        this.statusMapper.insertSelective(orderStatus);

        // 保存订单详情,使用批量插入功能
        this.detailMapper.insertList(order.getOrderDetails());

        // 发送mq消息通知购物车服务删除购物车中的商品
        orderDetails.forEach(d -> this.sendMessage(d.getSkuId(),"delete"));

        logger.debug("生成订单，订单编号：{}，用户id：{}", orderId, user.getId());

        return orderId;
    }


    /**
     * 通过订单号查询订单
     * @param id
     * @return
     */
    public Order queryById(Long id) {
        // 查询订单
        Order order = this.orderMapper.selectByPrimaryKey(id);

        if (order == null) {
            return null;
        }

        // 查询订单详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderId(id);
        List<OrderDetail> details = this.detailMapper.select(detail);
        order.setOrderDetails(details);

        // 查询订单状态
        OrderStatus status = this.statusMapper.selectByPrimaryKey(order.getOrderId());
        order.setStatus(status.getStatus());
        order.setOrderStatus(status);
        return order;
    }


    /**
     * 查询用户的订单,分页查询,查询对应状态的订单
     * @param page
     * @param rows
     * @param status
     * @return
     */
    public PageResult<Order> queryUserOrderList(Integer page, Integer rows, Integer status) {
        try {
            // 分页
            PageHelper.startPage(page, rows);
            // 获取登录用户
            UserInfo user = LoginInterceptor.getLoginUser();
            // 创建查询条件
            Page<Order> pageInfo = (Page<Order>) this.orderMapper.queryOrderList(user.getId(), status);

            // 查询订单集合

            //Page<Order> pageInfo = (Page<Order>) this.orderMapper.select(order);

            // 遍历，给每个订单添加订单详情和订单状态属性
            for (Order p : pageInfo) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(p.getOrderId());
                // 设置订单详情集合属性
                p.setOrderDetails(this.detailMapper.select(orderDetail));
                // 查询订单状态
                p.setOrderStatus(this.statusMapper.selectByPrimaryKey(p.getOrderId()));
            }

            return new PageResult<>(pageInfo.getTotal(), (long) pageInfo.getPages(), pageInfo);
        } catch (Exception e) {
            logger.error("查询订单出错", e);
            return null;
        }
    }

    /**
     * 更新订单支付状态
     * @param id
     * @param status
     * @return
     */
    @Transactional
    public Boolean updateStatus(Long id, Integer status) {
        OrderStatus record = new OrderStatus();
        record.setOrderId(id);
        record.setStatus(status);
        // 根据状态判断要修改的时间
        switch (status) {
            case 2:
                record.setPaymentTime(new Date());// 付款
                break;
            case 3:
                record.setConsignTime(new Date());// 发货
                break;
            case 4:
                record.setEndTime(new Date());// 确认收获，订单结束
                break;
            case 5:
                record.setCloseTime(new Date());// 交易失败，订单关闭
                break;
            case 6:
                record.setCommentTime(new Date());// 评价时间
                break;
            default:
                return null;
        }
        int count = this.statusMapper.updateByPrimaryKeySelective(record);
        return count == 1;
    }

    /**
     * 当对商品进行增删改后，调用该方法发送消息
     * @param id
     * @param type
     */
    public void sendMessage(Long id,String type){
        try {
            this.amqpTemplate.convertAndSend("cart." + type, id);
        } catch (Exception e) {
            logger.error("{}:商品消息发送失败，id：{}",type,id,e);
        }
    }

}
