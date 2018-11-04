package com.leyou.item.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 30 16:48
 **/
@Getter
@Setter
@ToString
@Table(name = "tb_address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;                    // 用户id

    @Length(min = 4,max = 30,message = "用户名只能在4~30位之间")
    private String name;                    // 用户名字

    @Pattern(regexp = "^1[35678]\\d{9}$",message = "手机号格式不对")
    private String phone;                   // 用户电话

    private String areaAddress;             // 用户地区地址

    private String detailAddress;             // 用户详细地址

    @Pattern(regexp = "^\\d{6}$",message = "邮编格式不对")
    private String receiveZip;              // 邮编

    private Boolean isDefault;              // 是否是默认地址


}
