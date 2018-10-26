package com.leyou.userservice;

import com.leyou.LyUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description //TODO
 * @Author santu
 * @Date 2018 - 10 - 21 18:43
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyUserService.class)
public class RedisTest {

    @Autowired
    private StringRedisTemplate template;

    @Test
    public void testRedis() {
        // 存储数据
        this.template.opsForValue().set("key1","value1");
        // 获取数据
        String value = this.template.opsForValue().get("key1");
        System.out.println(value);
    }

    @Test
    public void testSetTime() {
        this.template.opsForValue().set("key2","value2",10L, TimeUnit.SECONDS);
        String value = template.opsForValue().get("key2");
        System.out.println(value);
    }

    @Test
    public void testHash() {
        BoundHashOperations<String, Object, Object> user = this.template.boundHashOps("user");
        user.put("name","taotao");
        user.put("password","123");
        Object name = user.get("name");
        System.out.println("name= " + name);
        Map<Object, Object> entries = user.entries();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            System.out.println(entry.getKey() + "===" + entry.getValue());
        }
    }
}
