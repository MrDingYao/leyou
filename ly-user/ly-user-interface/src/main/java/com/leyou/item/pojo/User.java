package com.leyou.item.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 20 17:02
 **/
@Getter
@Setter
@ToString
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 4,max = 30,message = "用户名只能在4~30位之间")
    private String username;

    @JsonIgnore
    @Length(min = 6,max = 30,message = "密码只能在6~30位之间")
    private String password;

    @Pattern(regexp = "^1[35678]\\d{9}$",message = "手机号格式不对")
    private String phone;

    private Date created;

    @JsonIgnore
    private String salt;
}
