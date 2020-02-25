package com.xyhs.b2c.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Base64;

/**
 * @author ljp
 * @apiNote
 * @date 13:54 2020/2/25
 **/
@Component
public class AuthenticationSuccessHandler extends WebFilterChainServerAuthenticationSuccessHandler {

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {

        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();
        //设置headers
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        //设置body
        byte[]   dataBytes={};
        ObjectMapper mapper = new ObjectMapper();
        try {
            Object principal =authentication.getPrincipal();
            String user ="";
            String password ="";
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                user = userDetails.getUsername();
                password = userDetails.getPassword();
            }
            byte[] authorization=(user+":"+password).getBytes();
            String token= Base64.getEncoder().encodeToString(authorization);
            httpHeaders.add(HttpHeaders.AUTHORIZATION, token);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userName",user);
            jsonObject.addProperty("password",password);
            dataBytes=mapper.writeValueAsBytes(jsonObject.toString());
        }
        catch (Exception ex){
            ex.printStackTrace();
            JsonObject result = new JsonObject();
            result.addProperty("status", "501");
            result.addProperty("message", "授权异常");
            dataBytes=result.toString().getBytes();
        }
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
        return response.writeWith(Mono.just(bodyDataBuffer));
    }
}
