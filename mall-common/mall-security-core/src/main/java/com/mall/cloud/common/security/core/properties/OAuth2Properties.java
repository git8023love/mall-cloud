package com.mall.cloud.common.security.core.properties;

import lombok.Data;

/**
 * OAuth2认证服务器配置
 */
@Data
public class OAuth2Properties {
    /**
     * 使用jwt时为token签名的秘钥
     */
    private String jwtSigningKey = "mall";
    /**
     * 客户端配置
     */
    private OAuth2ClientProperties[] clients = {};
}
