package com.leyou.common.utils;

import java.util.Collection;

/**
 * @Description // 判空的工具类
 * @Author santu
 * @Date 2018 - 10 - 15 23:00
 **/
public class BeanUtils {

    /**
     * 判断集合是否为空
     * @param collection
     * @return
     */
    public static Boolean isNull(Collection collection){
        return collection == null || collection.size() == 0;
    }
}
