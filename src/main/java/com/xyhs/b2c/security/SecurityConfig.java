package com.xyhs.b2c.security;

import com.xyhs.b2c.handler.AuthenticationFailedHandler;
import com.xyhs.b2c.handler.AuthenticationSuccessHandler;
import com.xyhs.b2c.handler.BasicServerHttpHandler;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;

import javax.annotation.Resource;

/**
 * @author ljp
 * @apiNote
 * @date 17:12 2020/2/24
 **/
@EnableWebFluxSecurity
public class SecurityConfig {


    @Resource
    private AuthenticationSuccessHandler authenticationSuccessHandler;


    @Resource
    private AuthenticationFailedHandler authenticationFailedHandler;

    @Resource
    private BasicServerHttpHandler basicServerHttpHandler;

    /**
     * security的鉴权排除的url列表
     */
    private static final String[] EXCLUDED_AUTH_PAGES = {
            "/tradeFallback"
    };



    @Bean
    SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .authorizeExchange()
                //无需进行权限过滤的请求路径
                .pathMatchers(EXCLUDED_AUTH_PAGES).permitAll()
                //option 请求默认放行
                .pathMatchers(HttpMethod.OPTIONS.name()).permitAll()
                .anyExchange().authenticated()
                .and()
                .httpBasic()
                .and()
                //启动页面表单登陆,spring security 内置了一个登陆页面/login
                .formLogin().loginPage("/home/page")
                //认证成功
                .authenticationSuccessHandler(authenticationSuccessHandler)
                //登陆验证失败
                .authenticationFailureHandler(authenticationFailedHandler)
                //基于http的接口请求鉴权失败
                .and().exceptionHandling().authenticationEntryPoint(basicServerHttpHandler)
                //必须支持跨域
                .and().csrf().disable()
                .logout().disable();

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return  NoOpPasswordEncoder.getInstance(); //默认
    }

}
