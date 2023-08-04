package com.madou.gateway.handler;

import com.madou.common.excption.BusinessException;
import com.madou.gateway.utils.WebFluxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author ltyzzz
 * @email ltyzzz2000@gmail.com
 * @date 2023/3/5 20:34
 * 网关返回异常
 */
@Slf4j
@Order(-1)
@Configuration
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }
        String msg;
        if (ex instanceof NotFoundException) {
            msg = "service is not found";
        } else if (ex instanceof BusinessException) {
            BusinessException responseStatusException = (BusinessException) ex;
            msg = responseStatusException.getMessage();
        } else {
            msg = "internal server error";
        }

        log.error("[gateway exception processing]request url:{},exception message:{}", exchange.getRequest().getPath(), ex.getMessage());

        return WebFluxUtils.webFluxResponseWriter(response, msg);
    }
}
