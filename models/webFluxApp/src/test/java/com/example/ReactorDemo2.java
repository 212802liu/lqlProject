package com.example;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @ClassName : ReactorDemo2  //类名
 * @Description : webFlux 使用2  //描述
 * @Author : liuql  //作者
 * @Date: 2024/11/3  10:34
 */

public class ReactorDemo2 {
    @Test
    void block(){
//
//        Integer integer = Flux.just(1, 2, 4)
//                .map(i -> i + 10)
//                .blockLast();
//        System.out.println(integer);


        List<Integer> integers = Flux.just(1, 2, 4)
                .map(i -> i + 10)
                .collectList()
                .block(); // 也是一种订阅者； BlockingMonoSubscriber

        List<Integer> integers2 = Flux.just(1, 2, 4)
                .map(i -> i + 10)
                .collectList()
                .blockOptional()
//                .orElseGet(ArrayList::new);
                .orElseGet(new Supplier<List<Integer>>() {
                    @Override
                    public List<Integer> get() {
                        return new ArrayList<>();
                    }
                });



        System.out.println("integers = " + integers);
        System.out.println("integers = " + integers2);
    }

    @Test
    void next(){

        Integer block = Flux.just(1, 2, 3)
                .next()
                .block();
        System.out.println(block);
    }

    /**
     * Sinks： 接受器，数据管道，所有数据顺着这个管道往下走的
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    void sinks() throws InterruptedException, IOException {

//       Flux.create(fluxSink -> {
//           fluxSink.next("111")
//       })

//        Sinks.many(); //发送Flux数据。
//        Sinks.one(); //发送Mono数据


        // Sinks： 接受器，数据管道，所有数据顺着这个管道往下走的

        //Sinks.many().unicast(); //单播：  这个管道只能绑定单个订阅者（消费者）
        //Sinks.many().multicast();//多播： 这个管道能绑定多个订阅者
        //Sinks.many().replay();//重放： 这个管道能重放元素。 是否给后来的订阅者把之前的元素依然发给它；

        // 从头消费还是从订阅的那一刻消费；

        Sinks.Many<Object> many = Sinks.many()
                .multicast() //多播
                .onBackpressureBuffer(); //背压队列

        //默认订阅者，从订阅的那一刻开始接元素

        //发布者数据重放； 底层利用队列进行缓存之前数据
//        Sinks.Many<Object> many = Sinks.many().replay().limit(3);

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                many.tryEmitNext("a-"+i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
//
//
//
//        //订阅
        many.asFlux().subscribe(v-> System.out.println("v1 = " + v));

        new Thread(()->{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            many.asFlux().subscribe(v-> System.out.println("v2 = " + v));
        }).start();

        System.in.read();




    }

    @Test
    void TestCache() throws IOException {
        Flux<Integer> cache = Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1)) //不调缓存默认就是缓存所有
                ;
//                .cache(1);   //缓存两个元素； 默认全部缓存


        cache.subscribe();//缓存元素;

        // 最定义订阅者
        new Thread(()->{
            try {
                Thread.sleep(9000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            cache.subscribe(v-> System.out.println("v = " + v));
        }).start();

        System.in.read();
    }

    /**
     * 并发处理
     * @throws IOException
     */
    @Test
    void paralleFlux() throws IOException {
        // 百万数据，8个线程，每个线程处理100，进行分批处理一直处理结束

        Flux.range(1,100)
                .buffer(10)
                .parallel(8)
                .runOn(Schedulers.newParallel("yy"))
                .log()
                .flatMap(list->Flux.fromIterable(list))
                .collectSortedList(Integer::compareTo)
                .subscribe();


        System.in.read();
    }

    /**
     * 线程切换 保存本地变量问题 ： 类似 ThreadLocal
     * 以前 命令式编程:controller -- service -- dao
     * 响应式编程 dao(10：数据源) --> service(10) --> controller(10); 从下游反向传播
     *
     */
    //Context-API： https://projectreactor.io/docs/core/release/reference/#context
    @Test //ThreadLocal在响应式编程中无法使用。
    //响应式中，数据流期间共享数据，Context API: Context：读写 ContextView：只读；
    void threadlocal(){
        //支持Context的中间操作
//        Flux.just(1,2,3)
//                .transformDeferredContextual((flux,context)->{
//                    System.out.println("flux = " + flux);
//                    System.out.println("context = " + context);
//                    return flux.map(i->i+"==>"+context.get("prefix"));
//                })
//                //上游能拿到下游的最近一次数据
//                .contextWrite(Context.of("prefix","哈哈"))
//                //ThreadLocal共享了数据，上游的所有人能看到; Context由下游传播给上游
//                .subscribe(v-> System.out.println("v = " + v));

        // 写入 与 读取 Context 的 相对位置 很重要：因为 Context 是不可变的，它的内容只能被上游的操作符看到
        String key = "message";
        Mono<String> r = Mono.just("Hello")
                // unUsable
                .contextWrite(ctx -> ctx.put(key, "World"))

                .transformDeferredContextual((flux,context)->{
                    System.out.println("flux = " + flux);
                    System.out.println("context = " + context);
                    return  flux.map(ctx->ctx+" "+context.getOrDefault(key,""));
                })
                // 作用于上面context
                .contextWrite(ctx -> ctx.put(key, "reactor"))
                .transformDeferredContextual((flux,context)->{
                    System.out.println("flux = " + flux);
                    System.out.println("context = " + context);
                    return  flux.map(ctx->ctx+" "+context.getOrDefault(key,""));
                })
                // 作用于上面context
                .contextWrite(ctx -> ctx.put(key, "world"))
                // unUsable
                .contextWrite(ctx -> ctx.put(key, "world！"))
                ;

        StepVerifier.create(r)
                .expectNext("Hello reactor world")
                .verifyComplete();
    }



}
