package com.madou.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 网关 8099
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GeBiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeBiGatewayApplication.class, args);
    }
}
