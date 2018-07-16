package com.mall.cloud.common.security.core.validate.code.impl;

import com.mall.cloud.common.security.core.validate.code.ValidateCode;
import com.mall.cloud.common.security.core.validate.code.ValidateCodeException;
import com.mall.cloud.common.security.core.validate.code.ValidateCodeRepository;
import com.mall.cloud.common.security.core.validate.code.ValidateCodeType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.concurrent.TimeUnit;

@Component
public class RedisValidateCodeRepository implements ValidateCodeRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisValidateCodeRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 保存验证码
     * @param request          the request
     * @param code             the code
     * @param validateCodeType the validate code type
     */
    @Override
    public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType) {
        String key = buildKey(request, validateCodeType);
        redisTemplate.opsForValue().set(key, code, 3, TimeUnit.MINUTES);
    }

    /**
     * 获取验证码
     * @param request          the request
     * @param validateCodeType the validate code type
     *
     * @return
     */
    @Override
    public ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType) {
        Object value = redisTemplate.opsForValue().get(buildKey(request, validateCodeType));
        if (value == null) {
            return null;
        }
        return (ValidateCode) value;
    }

    /**
     * 移除验证码
     * @param request  the request
     * @param codeType the code type
     */
    @Override
    public void remove(ServletWebRequest request, ValidateCodeType codeType) {
        redisTemplate.delete(buildKey(request, codeType));
    }

    private String buildKey(ServletWebRequest request, ValidateCodeType validateCodeType) {
        String deviceId = request.getHeader("deviceId");
        if (StringUtils.isBlank(deviceId)) {
            throw new ValidateCodeException("请在请求头中携带deviceId参数");
        }
        return "code:" + validateCodeType.toString().toLowerCase() + ":" + deviceId;
    }
}