package com.example.react.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.*;

@Data
//@ConfigurationProperties(prefix = "user")
@Component
@Schema(description = "个人信息")
public class User {
    @Schema(description = "名称", example = "小明")
    @Value("${user.name1}")
    String name;

    @Schema(description = "年龄", example = "12")
    @Value("${user.age}")
    int age;

    private static Subscription subscription; // 类成员变量

    public static void main(String[] args) {
//        new User().generate();
//        new User().handle();
//        new User().thread1();
        new User().create();


    }

    public static void test(String[] args) throws InterruptedException {
            List<String> list = Arrays.asList("adnm", "admmt", "pot", "xbangd", "weoujgsd");

            Optional<String> max = list.stream().max((x,y)->{ return x.length()>y.length()?1:-1;});
            Optional<String> max1 = list.stream().max((Comparator.comparing(String::length)));
            System.out.println("最长的字符串：" + max.get());


//        subscribeDemo();



        // 限流 缓冲
//        bufferAndLimit();


        //

        Flux<Object> data = Flux.range(1, 10)
                .handle((value, sink) -> {
                    System.out.println("拿到的值：" + value);
                    sink.next("张三：" + value); //可以向下发送数据的通道
                })
                .log();//日志
        data.subscribe();
        // 异常处理
//        errorHandler();

    }

    private  void subscribeDemo() {
        Flux.range(1, 5).subscribe(
                value -> {
                    System.out.println("Received: " + value);
                    if (value % 5 == 0) {
                        System.out.println("Requesting more...");
                        subscription.request(5); // 请求5个元素
                    }
                },
                error -> System.err.println("Error: " + error),
                () -> System.out.println("Completed"),
                sub -> {
                    subscription = sub; // 存储 Subscription
                    subscription.request(5); // 初始请求5个元素
                }
        );

        SampleSubscriber<Integer> ss = new SampleSubscriber<Integer>();
        Flux<Integer> ints = Flux.range(1, 20);
        ints.subscribe(i -> System.out.println(" lambda:"+i),
                error -> System.err.println("Error " + error),
                () -> {System.out.println("Done");},
                s -> ss.request(10));
        ints.subscribe(ss);
        ints.subscribe(System.out::println);


        String[] ar = {"aa","bb"};
        Flux<String> source = Flux.fromArray(ar);

        source.map(String::toUpperCase)
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        System.out.println("first time");
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(String value) {
                        System.out.println(value);
                        request(1);
                    }


                });
    }

    private  void errorHandler() {
        Flux<Integer> ints = Flux.range(1, 5)
                .map(i -> {
                    if (i != 3) return i;
                    throw new RuntimeException("Got to 4");
                });
        System.out.println("只消费正常元素");
        ints.subscribe(i -> System.out.println(i));
        System.out.println("消费正常元素,且处理异常");
        ints.subscribe(i -> System.out.println(i),
                error -> System.err.println("Error: " + error));


        System.out.println("onErrorReturn");
        Flux.just(1, 2, 0, 4)
                .map(i -> "100 / " + i + " = " + (100 / i))
                .onErrorReturn(NullPointerException.class,"哈哈-6666")
                .subscribe(v-> System.out.println("v = " + v),
                        err -> System.out.println("err = " + err),
                        ()-> System.out.println("流结束")); // error handling example

        Flux.just(1, 2, 0, 4).subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                super.hookOnSubscribe(subscription);
            }

            @Override
            protected void hookOnNext(Integer value) {
                super.hookOnNext(value);
            }

            @Override
            protected void hookOnComplete() {
                super.hookOnComplete();
            }

            @Override
            protected void hookOnError(Throwable throwable) {
                super.hookOnError(throwable);
            }

            @Override
            protected void hookOnCancel() {
                super.hookOnCancel();
            }
        });
    }

    private  void bufferAndLimit() {
        //buffer：缓冲
        Flux<List<Integer>> flux1 = Flux.range(1, 10)  //原始流10个
                .buffer(3)
                .log();
//        flux1.subscribe();
//limit：限流
        Flux.range(1, 1000)
                .log()
                //限流触发，看上游是怎么限流获取数据的
                .limitRate(100); //一次预取30个元素； 第一次 request(100)，以后request(75)
//                .subscribe();
    }

    /**
     * 同步地， 逐个地 产生值的方法
     * 你需要提供一个 Supplier<S> 来初始化状态值，而生成器需要 在每一“回合”生成元素后返回新的状态值（供下一回合使用）Callable<S> stateSupplier
     * sink 接收器，水槽   sink.next() 将元素放入水槽（流）. BiFunction<S, SynchronousSink<T>, S> generator
     */
    public void generate(){

        Flux<String> flux = Flux.generate(
                //也可以使用 可变（mutable）类型，AtomicLong
                () -> 0,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3*state);
                    System.out.println("generate thread："+Thread.currentThread().getName());
                    if (state == 7) sink.error(new RuntimeException("7 被拒了哈"));
                    if (state == 10) sink.complete();
                    return state + 1;
                });
        System.out.println("main thread："+Thread.currentThread().getName());


        // 所有操纵作其实都是对 发布者的！！
        flux
                .onErrorReturn("被拒了哈")
                .doOnError(e-> System.out.println("error :"+e))
                .subscribe(System.out::println);

    }

    public void create(){
        MyEventProcessor myEventProcessor = new MyEventProcessor();
        Flux<String> bridge = Flux.create(sink -> {
            myEventProcessor.register(
                    new MyEventListener<String>() {

                        public void onDataChunk(List<String> chunk) {
                            for(String s : chunk) {
                                sink.next(s);
                            }
                        }

                        public void processComplete() {
                            sink.complete();
                        }
                    });
        }, FluxSink.OverflowStrategy.BUFFER);
        bridge.log().subscribe(System.out::println);

        List<String>  data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add("value"+i);
        }
        myEventProcessor.putData(data);
//        Flux.create(sink -> {
//            for(int i = 0; i < 10; i ++)
//                sink.next(i);
//            sink.complete();
//        }).subscribe(System.out::println);

    }

    public void handle(){
        Flux<String> alphabet = Flux.just(65, 66, 32, 9, 99)
                .handle((i, sink) -> {
                    System.out.println("value = "+i);
                    String letter = alphabet(i);
                    if (letter != null)
                        sink.next(letter);
                })
                ;

        alphabet.log().subscribe(System.out::println);
    }
    String alphabet( int i){
        char a = (char) i;
       return String.valueOf(a);

    }

    /**
     * 线程调度
     */
    public void thread1(){
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);

        final Flux<String> flux = Flux
                .range(1, 3)
                .map(i -> 10 + i)
                .log()
                // 改变发布者的线程,后面的线程开始切换
                .publishOn(s)
                .log()
                // 改变订阅者的线程
//                .subscribeOn(Schedulers.single())
                .map(i -> "value " + i)
                ;

        Schedulers.immediate();// 默认： 当前线程执行所有操纵
        Schedulers.single();//使用一个固定的单线程
        Schedulers.boundedElastic();//使用有界的、弹性调度的线程池。非无线扩张
//        Schedulers.fromExecutor(Executors.newSingleThreadExecutor()); // 自定义线程池

        //只要不指定线程池，默认发布者用的线程就是订阅者的线程；
        new Thread(() -> flux.subscribe(System.out::println)).start();
    }

    static class SampleSubscriber<T> extends BaseSubscriber<T> {

        @Override
        public void hookOnSubscribe(Subscription subscription) {
            System.out.println("Subscribed");
            request(1);
        }

        @Override
        public void hookOnNext(T value) {
            System.out.println("SampleSubscriber:"+value);
            request(1);
        }

        @Override
        protected void hookOnCancel() {
            super.hookOnCancel();
        }
    }

    interface MyEventListener<T> {
        void onDataChunk(List<T> chunk);
        void processComplete();
    }
    class myListener{
        public void online(String name){
            System.out.println("user is online:"+name);

        }
    }
    class  MyEventProcessor<T>{

        private  MyEventListener<T> myEventListener;

        public void register(MyEventListener<T> stringMyEventListener) {
            this.myEventListener = stringMyEventListener;
        }

        public void putData(List<T> chunk){
            //or you can do something to get a new list
            myEventListener.onDataChunk(chunk);
        }

        // ....
    }
}
