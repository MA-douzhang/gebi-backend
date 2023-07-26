package com.madou.chart;

import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author MA_dou
 * @version 1.0
 * @project gebi-cloud
 * @description chart启动类
 * @date 2023/7/25 21:33:13
 */
@SpringBootApplication(exclude = {RedisAutoConfiguration.class, DataSourceAutoConfiguration.class})
@EnableDubbo
@Slf4j
public class ChartMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChartMainApplication.class,args);
    }
}
