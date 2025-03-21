package com.example.webapp;

import com.alibaba.nacos.common.http.HttpUtils;
import com.example.jyDB.domain.JyViraccAgreementStatus;
import com.example.jyDB.mapper.JyViraccAgreementStatusMapper;
import com.example.webapp.demos.entity.Book;
import com.example.webapp.demos.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import java.io.IOException;
import java.time.Duration;

@SpringBootTest
@Slf4j
class WebAppApplicationTests {


    @Autowired
    JyViraccAgreementStatusMapper jyViraccAgreementStatusMapper;
    @Test
    @Transactional(transactionManager = "jyTransactionManager", propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    void testDataBase(){
        JyViraccAgreementStatus viraccAgreementStatus = jyViraccAgreementStatusMapper.selectByPrimaryKey("TS1311006957671891010GGL");
        // 0
        log.info("更新前："+viraccAgreementStatus.getIdType());
        JyViraccAgreementStatus v = new JyViraccAgreementStatus();
        v.setId(viraccAgreementStatus.getId());
        v.setIdType("2");
        jyViraccAgreementStatusMapper.updateByPrimaryKeySelective(v);

        viraccAgreementStatus = jyViraccAgreementStatusMapper.selectByPrimaryKey("TS1311006957671891010GGL");
        // 2
        log.info("更新后："+viraccAgreementStatus.getIdType());


    }


    @Test
    void testYYYY() {
        // 创建一个日期：2023 年 12 月 31 日
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.DECEMBER, 31, 23, 59, 59);

        // 创建两种不同的日期格式
        SimpleDateFormat yyyyFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat YYYYFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");

        // 格式化输出
        System.out.println("yyyy-MM-dd HH:mm:ss: " + yyyyFormat.format(calendar.getTime())); // 2023-12-31 23:59:59
        System.out.println("YYYY-MM-DD HH:mm:ss: " + YYYYFormat.format(calendar.getTime())); // 2024-12-31 23:59:59
    }

    @Test
    void contextLoad() throws InterruptedException {
        String url = "http://localhost:8080/webapi/webFlux?id=1";
        WebClient webClient = WebClient.create(url);

        System.out.println("doget:"+doGet(url));

        Mono<Book> books = webClient.get() // 创建GET请求
                .retrieve() // 检索响应
                .bodyToMono(Book.class) // 将响应体转换为Flux<Book>
                .doOnNext(book -> System.out.println(book.getAuthor()))
                .onErrorResume(e -> { // 处理错误的情况
                    System.out.println("Error: " + e.getMessage());
                    return Mono.empty(); // 返回一个空的Flux，避免异常终止
                });
        books.block();

        Mono<Book> bookMono = webClient.get()
                .retrieve()
                .bodyToMono(String.class) // 将响应体作为字符串处理
                .flatMap(body -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
//                        Book book = objectMapper.readValue(body, Book.class);
                        List<Book> booksa = objectMapper.readValue(body, new TypeReference<List<Book>>() {});
                        return Mono.just(booksa.get(0));
                    } catch (JsonProcessingException e) {
                        return Mono.error(e);
                    }
                })
                .doOnNext(book -> System.out.println(book.getAuthor()))
                .onErrorResume(e -> {
                    System.out.println("Error: " + e.getMessage());
                    return Mono.empty();
                });

//        books.subscribe(book -> System.out.println(book.getAuthor()));
//        books.subscribe();

/**
 * 归约（reduce），也称缩减，顾名思义，是把一个流缩减成一个值，能实现对集合求和、求乘积和求最值操作。
 *
 * 第一次执行时，accumulator函数的第一个参数为流中的第一个元素，第二个参数为流中元素的第二个元素；
 * 第二次执行时，第一个参数为第一次执行的结果，第二个参数为流中的第三个元素；依次类推。
 * 即为:前一次执行的结果作为后一次执行的第一个参数
 */

        String name = "11";
    }

    public static String doGet(String httpUrl){
        //链接
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        StringBuffer result = new StringBuffer();
        try {
            //创建连接
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            //设置请求方式
            connection.setRequestMethod("GET");
            //设置连接超时时间
            connection.setReadTimeout(15000);
            //开始连接
            connection.connect();
            //获取响应数据
            if (connection.getResponseCode() == 200) {
                //获取返回的数据
                is = connection.getInputStream();
                if (null != is) {
                    br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String temp = null;
                    while (null != (temp = br.readLine())) {
                        result.append(temp);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //关闭远程连接
            connection.disconnect();
        }
        return result.toString();
    }

}
