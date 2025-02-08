package com.example;

import com.example.createDemo.MyEventListener;
import com.example.createDemo.MyEventProcessor;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @ClassName : ReactorDemo1  //类名
 * @Description : reactor 学习1  //描述
 * @Author : liuql  //作者
 * @Date: 2024/11/3  12:29
 */

public class ReactorDemo1 {
    @Test
    void tt(){
        System.out.println(File.separator);
        System.out.println(File.separatorChar);
    }
    /**
     * - just()：
     *
     * 可以指定序列中包含的全部元素。创建出来的Flux序列在发布这些元素之后会自动结束
     *
     * - fromArray()，fromIterable()，fromStream()：
     *
     * 可以从一个数组，Iterable对象或Stream对象中穿件Flux对象
     *
     * - empty()：
     *
     * 创建一个不包含任何元素，只发布结束消息的序列
     *
     * - error(Throwable error)：
     *
     * 创建一个只包含错误消息的序列
     *
     * - never()：
     *
     * 传建一个不包含任务消息通知的序列
     *
     * - range(int start, int count)：
     *
     * 创建包含从start起始的count个数量的Integer对象的序列
     *
     * - interval(Duration period)和interval(Duration delay, Duration period)：
     *
     * 创建一个包含了从0开始递增的Long对象的序列。其中包含的元素按照指定的间隔来发布。除了间隔时间之外，还可以指定起始元素发布之前的延迟时间
     *
     * - intervalMillis(long period)和intervalMillis(long delay, long period)：
     *
     * 与interval()方法相同，但该方法通过毫秒数来指定时间间隔和延迟时间
     */
    @Test
    void flux_instance(){

        Flux.just(1, 2, 3, 4, 5, 6, 7, 0, 5, 6);
        Flux<String> stringFlux = Flux.just("hello", "world");//字符串

        //fromArray(),fromIterable()和fromStream()：可以从一个数组、Iterable 对象或Stream 对象中创建Flux序列
        Integer[] array = {1,2,3,4};
        Flux.fromArray(array).subscribe(System.out::println);

        List<Integer> integers = Arrays.asList(array);
        Flux.fromIterable(integers).subscribe(System.out::println);

        Stream<Integer> stream = integers.stream();
        Flux.fromStream(stream).subscribe(System.out::println);

        Flux.empty().subscribe(System.out::println);
        Flux.range(1, 10).subscribe(System.out::println);
        Flux.interval(Duration.of(10, ChronoUnit.SECONDS)).subscribe(System.out::println);
//        Flux.intervalMillis(1000).subscirbe(System.out::println);


    }

    /**
     * 同步地， 逐个地 产生值的方法
     * 你需要提供一个 Supplier<S> 来初始化状态值，而生成器需要 在每一“回合”生成元素后返回新的状态值（供下一回合使用）Callable<S> stateSupplier
     * sink 接收器，水槽   sink.next() 将元素放入水槽（流）. BiFunction<S, SynchronousSink<T>, S> generator
     */
    @Test
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
//                .onErrorReturn("被拒了哈")
                .doOnError(e-> System.out.println("error :"+e))
                .subscribe(System.out::println);

    }


    void create(){
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
        });
        bridge.log().subscribe(System.out::println);

        List<String>  data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add("value"+i);
        }
        myEventProcessor.putData(data);
    }

    private static Subscription subscription; // 类成员变量

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


    @Test
     void logTest() {
//        Flux.concat(Flux.just(1,2,3),Flux.just(7,8,9))
//                .subscribe(System.out::println);


        Flux.range(1, 7)
//                .log() //日志   onNext(1~7)
                .filter(i -> i > 3) //挑出>3的元素
//                .log() //onNext(4~7)
                .map(i -> "haha-" + i)
                .log()  // onNext(haha-4 ~ 7)
                .subscribe(System.out::println);


    }


    /**
     *  有点像map
     */
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
     * 响应式编程核心：看懂文档弹珠图；
     * 信号： 正常/异常（取消）
     * SignalType：
     *      SUBSCRIBE： 被订阅
     *      REQUEST：  请求了N个元素
     *      CANCEL： 流被取消
     *      ON_SUBSCRIBE：在订阅时候
     *      ON_NEXT： 在元素到达
     *      ON_ERROR： 在流错误
     *      ON_COMPLETE：在流正常完成时
     *      AFTER_TERMINATE：中断以后
     *      CURRENT_CONTEXT：当前上下文
     *      ON_CONTEXT：感知上下文
     * <p>
     * doOnXxx API触发时机
     *      1、doOnNext：每个数据（流的数据）到达的时候触发
     *      2、doOnEach：每个元素（流的数据和信号）到达的时候触发
     *      3、doOnRequest： 消费者请求流元素的时候
     *      4、doOnError：流发生错误
     *      5、doOnSubscribe: 流被订阅的时候
     *      6、doOnTerminate： 发送取消/异常信号中断了流
     *      7、doOnCancle： 流被取消
     *      8、doOnDiscard：流中元素被忽略的时候
     *
     */
    @Test
    public void doOnXxxx() {

        // 关键：doOnNext：表示流中某个元素到达以后触发我一个回调
        // doOnXxx要感知某个流的事件，写在这个流的后面，新流的前面
        Flux.just(1, 2, 3, 4, 5, 6, 7, 0, 8, 9)
                .doOnNext(integer -> System.out.println("元素到达：" + integer)) //元素到达得到时候触发
                .doOnEach(integerSignal -> { //each封装的详细
                    System.out.println("doOnEach.." + integerSignal);
                })//1,2,3,4,5,6,7,0
                .map(integer -> 10 / integer) //10,5,3,
                // 异常没有被处理
                .doOnError(throwable -> {
                    System.out.println("数据库已经保存了异常：" + throwable.getMessage());
                })
                .doOnNext(integer -> System.out.println("元素到哈：" + integer))

                .subscribe(System.out::println);
    }


    @Test
    //Mono<Integer>： 只有一个Integer
    //Flux<Integer>： 有很多Integer
    public void fluxDoOn() throws IOException, InterruptedException {

        //空流:  链式API中，下面的操作符，操作的是上面的流。
        // 事件感知API：当流发生什么事的时候，触发一个回调,系统调用提前定义好的钩子函数（Hook【钩子函数】）；doOnXxx；
        Flux<Integer> flux = Flux.range(1, 7)
                .delayElements(Duration.ofSeconds(1))
                .doOnComplete(() -> {
                    System.out.println("stream:流正常结束...");
                })
                .doOnCancel(() -> {
                    System.out.println("stream:流已被取消...");
                })
                //  这个并没有打印，似乎 hookOnError 小号掉，并发出一个取消信号
                .doOnError(throwable -> {
                    System.out.println("stream:流出错..." + throwable);
                })
                .doOnNext(integer -> {
                    System.out.println("stream: doOnNext..." + integer);
                }); //有一个信号：此时代表完成信号

        flux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                System.out.println("订阅者和发布者绑定好了：" + subscription);
                request(1); //背压
            }

            @Override
            protected void hookOnNext(Integer value) {
                System.out.println("元素到达：" + value);
                if (value < 5) {
                    request(1);
                    if (value == 3) {
                        int i = 10 / 0;
                    }
                } else {
                    cancel();//取消订阅
                }
                ; //继续要元素
            }

            @Override
            protected void hookOnComplete() {
                System.out.println("hook:数据流结束");
            }

            @Override
            protected void hookOnError(Throwable throwable) {
                System.out.println("hook:数据流异常");
            }

            @Override
            protected void hookOnCancel() {
                System.out.println("hook:数据流被取消");
            }

            @Override
            protected void hookFinally(SignalType type) {
                System.out.println("hook:结束信号：" + type);
                // 正常、异常
//                try {
//                    //业务
//                }catch (Exception e){
//
//                }finally {
//                    //结束
//                }
            }
        });

        Thread.sleep(2000);

        System.in.read();
    }


    @Test
    public   void errorHandler() {
        Flux<Integer> ints = Flux.range(1, 5)
                .map(i -> {
                    if (i != 3) return i;
                    throw new RuntimeException("Got to 4");
                });
        System.out.println("只消费正常元素");
//        ints.subscribe(i -> System.out.println(i));
        System.out.println("消费正常元素,且处理异常");
        ints.subscribe(i -> System.out.println(i),
                error -> System.err.println("Error: " + error),
                ()-> System.out.println("异常后，complete 不会打印"));


        System.out.println("onErrorReturn");
        Flux.just(1, 2, 0, 4)
                .map(i -> "100 / " + i + " = " + (100 / i))
                .onErrorReturn(NullPointerException.class,"哈哈-6666")
                // 发生异常时，返回一个默认值，并结束流
//                .onErrorReturn(Exception.class,"哈哈-6666")
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

    /**
     * 缓冲  限流
     */
    @Test
    public   void bufferAndLimit() {
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


    class SampleSubscriber<T> extends BaseSubscriber<T> {

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
}
