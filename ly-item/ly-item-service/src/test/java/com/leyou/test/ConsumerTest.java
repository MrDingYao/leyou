//package com.leyou.test;
//
//import com.leyou.item.pojo.Category;
//import org.junit.Test;
//
//import java.util.function.Consumer;
//
///**
// * @ClassName ConsumerTest
// * @Description TODO
// * @Author santu
// * @Date 2018/9/28 17:57
// * @Version 1.0
// **/
//public class ConsumerTest {
//    @Test
//    public void test1() {
//        Consumer<Integer> consumer1 = n -> System.out.println(n*10);
//        Consumer<Integer> consumer2 = n -> System.out.println(n+10);
//
//        consumer1.andThen(consumer2).accept(5);
//    }
//}
