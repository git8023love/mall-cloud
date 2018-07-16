package com.mall.cloud.common.config.properties;

import com.mall.cloud.provider.uac.common.base.constant.GlobalConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = GlobalConstant.ROOT_PREFIX)
public class MallCloudProperties {
    private ZookeeperProperties zk = new ZookeeperProperties();
    private SwaggerProperties swagger = new SwaggerProperties();
    private AliyunProperties aliyun = new AliyunProperties();
}
