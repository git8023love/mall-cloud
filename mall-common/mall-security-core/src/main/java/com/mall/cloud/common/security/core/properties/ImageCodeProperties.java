package com.mall.cloud.common.security.core.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 图片验证码配置项
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageCodeProperties extends SmsCodeProperties {

    ImageCodeProperties() {
        super.setLength(4);
    }
}
