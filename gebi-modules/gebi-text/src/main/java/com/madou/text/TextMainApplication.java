package com.madou.text;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @author MA_dou
 * @version 1.0
 * @project gebi-cloud
 * @description service启动类
 * @date 2023/7/25 20:49:42
 */
// todo 如需开启 Redis，须移除 exclude 中的内容
@SpringBootApplication
@EnableDubbo
@Slf4j
@MapperScan("com.madou.text.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class TextMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(TextMainApplication.class, args);
    }
}
