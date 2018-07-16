package com.mall.cloud.common.core.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class CoreHeaderInterceptor extends HandlerInterceptorAdapter {
    /**
     * The constant HEADER_LABEL.
     */
    public static final String HEADER_LABEL = "x-label";
    /**
     * The constant HEADER_LABEL_SPLIT.
     */
    public static final String HEADER_LABEL_SPLIT = ",";

    /**
     * The constant LABEL.
     */
    //public static final HystrixRequestVariableDefault<List<String>> LABEL = new HystrixRequestVariableDefault<>();

    /*private static void initHystrixRequestContext(String labels) {
        log.info("LABEL={}", labels);
        if (!HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.initializeContext();
        }

        if (!StringUtils.isEmpty(labels)) {
            CoreHeaderInterceptor.LABEL.set(Arrays.asList(labels.split(CoreHeaderInterceptor.HEADER_LABEL_SPLIT)));
        } else {
            CoreHeaderInterceptor.LABEL.set(Collections.emptyList());
        }
    }

    private static void shutdownHystrixRequestContext() {
        if (HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.getContextForCurrentThread().shutdown();
        }
    }*/

   /* @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        CoreHeaderInterceptor.initHystrixRequestContext(request.getHeader(CoreHeaderInterceptor.HEADER_LABEL));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        CoreHeaderInterceptor.shutdownHystrixRequestContext();
    }*/
}
