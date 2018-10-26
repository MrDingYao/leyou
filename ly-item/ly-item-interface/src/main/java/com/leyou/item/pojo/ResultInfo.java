package com.leyou.item.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description //一个信息的封装类
 * @Author santu
 * @Date 2018 - 10 - 10 19:06
 **/
@Getter
@Setter
public class ResultInfo {
    private Boolean status;     // 状态
    private String message;     // 返回的信息说明
    private Object data;        // 返回的数据
}
