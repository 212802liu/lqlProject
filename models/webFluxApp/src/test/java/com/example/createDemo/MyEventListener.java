package com.example.createDemo;

import java.util.List;

/**
 * @author:liuql
 * @create: 2024-11-03 20:05
 * @Description:
 */
public interface MyEventListener<T> {
    void onDataChunk(List<T> chunk);
    void processComplete();
}
