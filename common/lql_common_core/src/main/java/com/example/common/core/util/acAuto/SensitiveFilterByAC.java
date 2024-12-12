package com.example.common.core.util.acAuto;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.core.util.sensitiveFilter.SensitiveFilter;
import com.example.common.core.util.sensitiveFilter.StringPointer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 敏感词过滤器
 * 实现算法： AC自动机  （前缀树 + BFS）
 */
public class SensitiveFilterByAC {

    /**
     * 格式：
     *      sm
     *      sb
     *      黄色
     *      ……
     */
    private static final String filePath = "C:\\Users\\lsw\\Desktop\\sensi_words.txt";
    private static final String dataPath = "C:\\Users\\lsw\\Desktop\\testData.txt";
    public static SensitiveFilterByAC DEFAULT = new SensitiveFilterByAC(filePath);

    TreeNode treeRoot = new TreeNode(0);

    /**
     * 初始化 AC自动机
     *
     * @param filePath
     */
    public SensitiveFilterByAC(String filePath) {
        try {
            //1. 构建前缀树
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filePath)
                    //                    SensitiveFilter.class.getClassLoader().getResourceAsStream(filePath) // resource 目录下
                    , StandardCharsets.UTF_8));
            String s;
            while (StrUtil.isNotBlank(s = bf.readLine())) {

                char[] charArray = s.toCharArray();
                TreeNode tempNode = treeRoot;
                for (int i = 0; i < charArray.length; i++) {
                    char c = charArray[i];
                    TreeNode childNode = tempNode.getChildNode(c);

                    if (childNode == null) {
                        //如果不包含这个字符就创建孩子节点
                        childNode = new TreeNode(i + 1, c);
                        tempNode.add(c, childNode);
                    }

                    tempNode = childNode;

                    // 设置结束标志
                    if (i == charArray.length - 1) {
                        tempNode.setKeywordEnd(true);
                    }
                }
            }
            // 2.广度优先，设置所有的fail指针
            treeRoot.setFailPoint(treeRoot);
            BFS_setFailPoint(treeRoot);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 广度优先，逐层设置所有节点的fail指针
     * @param treeNodeTemp2
     */
    private void BFS_setFailPoint(TreeNode treeNodeTemp2) {

        Queue<TreeNode> treeNodeQueue = new ArrayDeque<>();

        while (treeNodeTemp2 != null) {
            Map<Character, TreeNode> child = treeNodeTemp2.getChild();
            // 设置所有子节点的fail指针
            for (Map.Entry<Character, TreeNode> characterTreeNodeEntry : child.entrySet()) {
                characterTreeNodeEntry.getValue().setFailPoint(findFailPoint(treeNodeTemp2, characterTreeNodeEntry.getKey()));
            }

            treeNodeQueue.addAll(treeNodeTemp2.getChild().values());
            treeNodeTemp2 = treeNodeQueue.poll();

        }


    }


    /**
     * 返回 treeNode 的 fail 节点
     * parent ： treeNode 的父节点
     * @param c
     */
    private TreeNode findFailPoint(TreeNode parent,  Character c) {
        // a. 第一层节点，fail指针直接指向根节点
        if (parent.getHeight() == 0) {
            return treeRoot;
        }
        TreeNode tempNode = parent;

        /*
         *  b. 沿着 父节点的fail指针不断向上遍历
         *          1. 父节点的fail指针指向的node节点 可以继续匹配该字符前缀，返回能匹配的node
         *          2. 父节点的fail指针一直向上遍历到根节点也无法匹配，则返回root节点
         *
         */
        while (tempNode.getFailPoint() != null && tempNode != treeRoot) {
            Map<Character, TreeNode> child = tempNode.getFailPoint().getChild();
            if (child.get(c) == null) {
                tempNode = tempNode.getFailPoint();
            } else {
                return child.get(c);
            }
        }
        return treeRoot;
    }

    /**
     * 替换敏感词
     * @param content
     * @return
     */
    public String filter(StringPointer content){
        // 是否已经匹配上 完整敏感词
        boolean isMatch = false;
        TreeNode tempNode = treeRoot;
        int i = 0;
        while ( i < content.length()){
            TreeNode treeNode = tempNode.getChildNode(content.charAt(i));
            // a. 匹配上,继续匹配子节点
            if (treeNode !=null ){
                tempNode = treeNode;
                i++;
                if (treeNode.isKeywordEnd()){
                    //匹配成功，但不一定是最长前缀敏感词！
                    isMatch = true;
                    // a1: 如果恰好匹配到content末尾，则可以直接替换
                    if(i == content.length()){
                        content.fill(i - tempNode.getHeight(),i,'*');
                    }
                }
            }else {
                // d. 根节点也无法匹配时，说明不是敏感词，继续遍历向下content
                if(tempNode == treeRoot){
                    i++;
                }else {
                    //b. 如果已经匹配到最长敏感词了，直接替换.进行下一次匹配
                    if (isMatch){
                        content.fill(i - tempNode.getHeight(),i,'*');
                        // 开始下一轮的匹配
                        tempNode = treeRoot;
                        isMatch = false;

                    }else {
                        // c. 本节点匹配失败且未找到敏感词，重定位到fail指针处尝试匹配
                        tempNode = tempNode.getFailPoint();
                    }

                }

            }
        }

        return content.toString();
    }

    /**
     * filter 代码优化后
     * @param content
     * @return
     */
    public String filterSensitiveWords(StringPointer content) {
        boolean isMatched = false;
        TreeNode currentNode = treeRoot;
        int index = 0;

        while (index < content.length()) {
            char currentChar = content.charAt(index);
            TreeNode nextNode = currentNode.getChildNode(currentChar);

            if (nextNode != null) {
                currentNode = nextNode;
                index++;

                if (nextNode.isKeywordEnd()) {
                    isMatched = true;

                    if (index == content.length()) {
                        content.fill(index - currentNode.getHeight(), index, '*');
                    }
                }
            } else {
                if (isMatched) {
                    content.fill(index - currentNode.getHeight(), index, '*');
                    currentNode = treeRoot;
                    isMatched = false;
                } else if (currentNode != treeRoot) {
                    currentNode = currentNode.getFailPoint();
                } else {
                    index++;
                }
            }
        }

        return content.toString();
    }

    /**
     * AC自动机 执行时间： 307
     * hash+树  执行时间： 33
     * 靠！被爆杀了
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {


        SensitiveFilterByAC aDefault = SensitiveFilterByAC.DEFAULT;
        SensitiveFilter filter1 = SensitiveFilter.DEFAULT;

        List<String> dataList = new ArrayList<>();
        //1. 构建前缀树
        BufferedReader bf = new BufferedReader(new InputStreamReader(
                new FileInputStream(dataPath)
                //                    SensitiveFilter.class.getClassLoader().getResourceAsStream(filePath) // resource 目录下
                , StandardCharsets.UTF_8));
        String s;
        while (StrUtil.isNotBlank(s = bf.readLine())) {
            dataList.add(s);
        }

        dataList.forEach(v -> System.out.println(aDefault.filter(new StringPointer(v))));
        dataList.forEach(v -> System.out.println(filter1.filter(v,'*')));


        long startT2 = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            dataList.forEach(v -> filter1.filter(v,'*'));
        }
        long end2 = System.currentTimeMillis();

        System.out.println("hash+树  执行时间： " + (end2-startT2));


        // 开始时间
        long startT1 = System.currentTimeMillis();
        // 30 * 10000
        for (int i = 0; i < 10000; i++) {
            dataList.forEach(v -> aDefault.filter(new StringPointer(v)));
        }
        long end1 = System.currentTimeMillis();

        System.out.println("AC自动机 执行时间： " + (end1-startT1));






    }
}
