package com.mall.cloud.provider.uac.service.impl;

import com.mall.cloud.common.core.support.BaseService;
import com.mall.cloud.provider.uac.model.domain.UacUserToken;
import com.mall.cloud.provider.uac.service.UacUserTokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UacUserTokenServiceImpl extends BaseService<UacUserToken> implements UacUserTokenService {
}
