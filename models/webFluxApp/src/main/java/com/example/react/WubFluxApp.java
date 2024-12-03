package com.example.react;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class WubFluxApp
{
    public static void main( String[] args )
    {
        ConfigurableApplicationContext context = SpringApplication.run(WubFluxApp.class, args);
//        TBookMapper bookMapper = context.getBean(TBookMapper.class);
//        System.out.println("bookMapper:"+bookMapper);
//         bookMapper.findAll().subscribe(System.out::println);
    }
}
