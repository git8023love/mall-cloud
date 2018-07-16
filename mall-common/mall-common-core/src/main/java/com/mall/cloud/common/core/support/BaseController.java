package com.mall.cloud.common.core.support;

import com.mall.cloud.provider.uac.common.base.constant.GlobalConstant;
import com.mall.cloud.provider.uac.common.base.dto.LoginAuthDto;
import com.mall.cloud.provider.uac.common.base.enums.ErrorCodeEnum;
import com.mall.cloud.provider.uac.common.base.exception.BusinessException;
import com.mall.cloud.uac.common.util.PublicUtil;
import com.mall.cloud.uac.common.util.ThreadLocalMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected LoginAuthDto getLoginAuthDto() {
        LoginAuthDto loginAuthDto = (LoginAuthDto) ThreadLocalMap.get(GlobalConstant.Sys.TOKEN_AUTH_DTO);
        if (PublicUtil.isEmpty(loginAuthDto)) {
            throw new BusinessException(ErrorCodeEnum.UAC10011041);
        }
        return loginAuthDto;
    }
}
