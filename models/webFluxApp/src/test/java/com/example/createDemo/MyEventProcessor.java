package com.example.createDemo;


import java.util.List;

/**
 * @ClassName : MyEventProcessor  //类名
 * @Description :   //描述
 * @Author : liuql  //作者
 * @Date: 2024/11/3  20:04
 */

public  class  MyEventProcessor<T>{

    private MyEventListener<T> myEventListener;

    public void register(MyEventListener<T> stringMyEventListener) {
        this.myEventListener = stringMyEventListener;
    }

    public void putData(List<T> chunk){
        //or you can do something to get a new list
        myEventListener.onDataChunk(chunk);
    }

    // ....
}
