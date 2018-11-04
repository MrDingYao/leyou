package com.leyou.order.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Transient;
import java.util.List;

/**
 * @Description 订单order的扩展类
 * @Author santu
 * @Date 2018 - 11 - 03 10:09
 **/
@Getter
@Setter
public class OrderBo extends Order {

    @Transient
    private List<OrderDetail> orderDetails;

    @Transient
    private OrderStatus orderStatus;

    @Transient
    private Integer status;

    @Transient
    private Long addressId;
}
