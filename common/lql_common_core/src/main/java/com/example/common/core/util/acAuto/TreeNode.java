package com.example.common.core.util.acAuto;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
//@Getter
//@Setter
public class TreeNode {
    /**
     * 这个值并没有什么意义
     * 只是debug时，能方便知道在哪个节点上
     */
    private char value;
    private int height;

    // 关键词结束标志
    private boolean isKeywordEnd = false;


    private TreeNode failPoint;

    private Map<Character,TreeNode> child = new HashMap<>();

    public TreeNode() {
    }

    public TreeNode( int height,char value) {
        this.value = value;
        this.height = height;
    }


    public TreeNode(int height) {
        this.height = height;
    }

    public void add(Character c, TreeNode subNode){
        child.put(c,subNode);
    }

    public TreeNode getChildNode(Character c){
        return child.get(c);
    }

    /**
     * 有必要 重写toString 方法。
     *
     * 程序可以正常运行！！！。但 debug模式下调试器会 报 Method threw 'java.lang.StackOverflowError' exception. Cannot evaluate xxx.toString()
     * debug模式下调试器会调用这个对象的toString()方法，我是通过lombok的@Data注解来生成toString()方法的。而报错的原因就是我在定义类的时候用了双向的引用，
     * 举个例子就是：对象A引用了对象B 【child】，对象B又反过来引用了对象A 【failPoint】，导致出现了一个环形的引用链，使用toString()方法时，会不断的互相循环调用引用对象的方法，导致栈溢出。
     *
     * 也可以删去@Data注解，用@Getter和@Setter来代替。
     * @return
     */
    @Override
    public String toString(){
        return "value = "+value+" isKeywordEnd = " + isKeywordEnd +" height:"+height+"child"+ child.size()+" fail = ["+failPoint.simpleString()+"]";
    }

    public String simpleString(){
        return "value =  "+value+" isKeywordEnd = " + isKeywordEnd +" height:"+height;
    }



}
