package com.xyhs.b2c.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author ljp
 * @apiNote
 * @date 13:54 2020/2/25
 **/
@Component
public class AuthenticationFailedHandler implements ServerAuthenticationFailureHandler {

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException e) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();
        //设置headers
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        //设置body
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status",401);
        jsonObject.addProperty("message","鉴权失败！");
        byte[]   dataBytes={};
        try {
            ObjectMapper mapper = new ObjectMapper();
            dataBytes=mapper.writeValueAsBytes(jsonObject.toString());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
        return response.writeWith(Mono.just(bodyDataBuffer));
    }
}
