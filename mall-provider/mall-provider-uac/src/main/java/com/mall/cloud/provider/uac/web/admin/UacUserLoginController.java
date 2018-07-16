package com.mall.cloud.provider.uac.web.admin;

import com.mall.cloud.common.core.support.BaseController;
import com.mall.cloud.provider.uac.service.UacLoginService;
import com.mall.cloud.provider.uac.service.UacUserTokenService;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(value = "Web - UacUserLoginController", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UacUserLoginController extends BaseController {

    @Resource
    private UacLoginService uacLoginService;
    @Resource
    private UacUserTokenService uacUserTokenService;
    //@Resource
    //private ClientDetailsService clientDetailsService;

    private static final String BEARER_TOKEN_TYPE = "Basic ";
}
