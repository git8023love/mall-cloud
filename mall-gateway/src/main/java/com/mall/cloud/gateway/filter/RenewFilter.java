package com.mall.cloud.gateway.filter;

import com.mall.cloud.provider.uac.common.base.enums.ErrorCodeEnum;
import com.mall.cloud.provider.uac.common.base.exception.BusinessException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
@Slf4j
public class RenewFilter extends ZuulFilter {

    private static final int EXPIRES_IN = 60 * 20;

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("RenewFilter - token续租...");
        RequestContext requestContext = RequestContext.getCurrentContext();
        try {
            doSomething(requestContext);
        } catch (Exception e) {
            log.error("RenewFilter - token续租. [FAIL] EXCEPTION={}", e.getMessage(), e);
            throw new BusinessException(ErrorCodeEnum.UAC10011041);
        }
        return null;
    }

    private void doSomething(RequestContext requestContext) {
        HttpServletRequest request = requestContext.getRequest();
        String token = StringUtils.substringAfter(request.getHeader(HttpHeaders.AUTHORIZATION), "bearer ");
        if (StringUtils.isEmpty(token)) {
            return;
        }
        HttpServletResponse servletResponse = requestContext.getResponse();
        servletResponse.addHeader("Renew-Header", "true");

        /*OAuth2AccessToken oAuth2AccessToken = jwtTokenStore.readAccessToken(token);
        int expiresIn = oAuth2AccessToken.getExpiresIn();

        if (expiresIn < EXPIRES_IN) {
            HttpServletResponse servletResponse = requestContext.getResponse();
            servletResponse.addHeader("Renew-Header", "true");
        }*/
    }
}
