package com.mall.cloud.common.security.core.authentication;

import com.mall.cloud.common.security.core.properties.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 表单登录配置
 */
@Component
public class FormAuthenticationConfig {

    protected final AuthenticationSuccessHandler pcAuthenticationSuccessHandler;

    protected final AuthenticationFailureHandler pcAuthenticationFailureHandler;

    @Autowired
    public FormAuthenticationConfig(AuthenticationSuccessHandler pcAuthenticationSuccessHandler, AuthenticationFailureHandler pcAuthenticationFailureHandler) {
        this.pcAuthenticationSuccessHandler = pcAuthenticationSuccessHandler;
        this.pcAuthenticationFailureHandler = pcAuthenticationFailureHandler;
    }

    public void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_FORM)
                .successHandler(pcAuthenticationSuccessHandler)
                .failureHandler(pcAuthenticationFailureHandler);
    }
}
