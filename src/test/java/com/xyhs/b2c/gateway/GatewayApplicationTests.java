package com.xyhs.b2c.gateway;


import com.xyhs.b2c.security.MD5Encoder;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GatewayApplicationTests {

    @Test
    public void contextLoads() {
        System.out.println(MD5Encoder.encode("123456","user"));
    }

    public static void main(String[] args) {
        System.out.println(MD5Encoder.encode("123456","user"));
    }

}
