package com.mall.cloud.common.security.feign;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mall.oauth2.client")
public class Oauth2ClientProperties {
    private String id;
    private String accessTokenUrl;
    private String clientId;
    private String clientSecret;
    private String clientAuthenticationScheme;
}
