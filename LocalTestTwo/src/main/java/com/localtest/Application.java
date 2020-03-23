package com.localtest;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * springboot启动类
 */

@EnableScheduling
@SpringBootApplication
public class Application {
    public static void main(String[] args) {

        Class clazz = Application.class;

        SpringApplication.run(clazz, args);

    }
}
