package com.mall.cloud.provider.uac.service;

import com.mall.cloud.common.core.support.IService;
import com.mall.cloud.provider.uac.model.domain.UacUser;

public interface UacUserService extends IService<UacUser> {

    UacUser findByLoginName(String loginName);

    UacUser findUserInfoByUserId(Long userId);

    UacUser findUserInfoByLoginName(String loginName);
}
