package com.mall.cloud.provider.uac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableHystrix
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
@EnableTransactionManagement
public class MallProviderUacApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallProviderUacApplication.class);
    }
}
