package com.example;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * 在SpringBoot1.x版本中，springboot默认使用集成jedis，
 * 在SpringBoot2.x版本中，SpringBoot默认集成lettuce。
 *
 * 1. Jedis使用直连方式连接Redis Server，在多线程环境下存在线程安全问题，需要增加连接池创建Jedis客户端多实例线程安全问题，基于传统I/O模式，是阻塞式传输！
 * 2. Lettuce的连接是基于netty，在多线程环境下不存在线程安全问题，这个连接实例当然也是可伸缩的设计，也可以增加多实例连接，netty本身就基于NIO，从而提供了异步和同步数据访问方式，用于构建非阻塞的反应性应用程序。
 */
@Data
public class Main {
    int a = 10;

    public static void main(String[] args) {
//        expire("key",10);
//        int a = 10;
//        expire("key",a,TimeUnit.SECONDS);
//        System.out.println("Hello world!");
        System.out.println(new Main());

        System.out.println(JSON.toJSONString(new Main(), JSONWriter.Feature.WriteClassName));
    }

    public static boolean expire(String key, int timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    public static boolean expire(String key, long timeout, TimeUnit unit) {
        System.out.println("time" + timeout);
        return true;
    }
}