package com.game.webgateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.game.mj.exception.TokenException;
import com.game.mj.model.ResponseVo;
import com.game.mj.util.JWTUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author zheng
 */
@Component
@ConfigurationProperties("com.game.webgateway")
@Data
public class JWTFiltrer implements GlobalFilter, Ordered {
    private String[] filterUrlPaths ;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String filterPath = exchange.getRequest().getURI().getPath();

        if (filterUrlPaths != null && isSkipPath(filterPath)){
            return chain.filter(exchange);
        }
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StringUtils.isEmpty(token)){
            return buildResponse(ResponseVo.fail("404 unauthorized"),exchange,HttpStatus.OK);
        }

        boolean validateUrl = false;
        try {
            validateUrl = JWTUtil.getTokenBody(token) != null ? true : false;
        } catch (TokenException e) {
            e.printStackTrace();
            return buildResponse(ResponseVo.unAuthoried("invalid token"),exchange,HttpStatus.OK);
        }
        if (!validateUrl){
            return buildResponse(ResponseVo.unAuthoried("invalid token"),exchange,HttpStatus.OK);
        }
        return chain.filter(exchange);
    }


    /**
     * 返回response
     * @param vo
     * @param exchange
     * @return
     */
    private Mono<Void>  buildResponse(ResponseVo vo,ServerWebExchange exchange,HttpStatus status){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        byte[] bytes = JSONObject.toJSON(vo).toString().getBytes(StandardCharsets.UTF_8);
        DataBuffer wrap = response.bufferFactory().wrap(bytes);
        return response.writeWith(Flux.just(wrap));

    }

    private boolean isSkipPath(String filterPath) {

        for (String path :filterUrlPaths){
            if (filterPath.startsWith(path)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
