package com.leyou.item.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Description 用户的个人信息表格
 * @Author santu
 * @Date 2018 - 11 - 01 22:28
 **/
@Getter
@Setter
@ToString
@Table(name = "tb_user_info")
public class UserInfo {

    @Id
    private Long userId;                // 用户的id

    private String nickname;            // 用户的昵称

    private String gender;              // 用户的性别

    private Date birthday;              // 用户的生日

    private String province;             // 用户的所在的省

    private String city;             // 用户的所在的市

    private String downtown;             // 用户的所在的县

    private String job;                 // 用户的职业

    private String image;               // 用户的头像
}
