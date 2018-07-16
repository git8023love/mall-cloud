package com.mall.cloud.common.security.core.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

/**
 * 默认的SocialUserDetailsService实现
 * 不做任何处理，只在控制台打印一句日志，然后抛出异常，提醒业务系统自己配置SocialUserDetailsService。
 */
@Slf4j
public class DefaultSocialUserDetailsServiceImpl  implements SocialUserDetailsService {
    @Override
    public SocialUserDetails loadUserByUserId(String s) throws UsernameNotFoundException {
        return null;
    }
}
