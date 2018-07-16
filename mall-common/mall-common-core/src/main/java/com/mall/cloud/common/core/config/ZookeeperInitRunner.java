package com.mall.cloud.common.core.config;

import com.mall.cloud.common.config.properties.MallCloudProperties;
import com.mall.cloud.common.zk.registry.RegistryCenterFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;

@Component
@Order
@Slf4j
public class ZookeeperInitRunner implements CommandLineRunner {

    @Resource
    private MallCloudProperties mallCloudProperties;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public void run(String... strings) throws Exception {
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        log.info("###ZookeeperInitRunner，init. HostAddress={}, applicationName={}", hostAddress, applicationName);
        RegistryCenterFactory.startup(mallCloudProperties, hostAddress, applicationName);
        log.info("###ZookeeperInitRunner，finish<<<<<<<<<<<<<");
    }
}
