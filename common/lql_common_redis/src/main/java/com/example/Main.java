package com.example;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import lombok.Data;

import java.util.concurrent.TimeUnit;

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