package com.leyou.common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description //TODO 线程工具类
 * @Author santu
 * @Date 2018 - 10 - 18 20:36
 **/
public class ThreadUtils {

    private static final ExecutorService es = Executors.newFixedThreadPool(10);

    public static void executor(Runnable runnable){
        es.submit(runnable);
    }
}
