package com.mall.cloud.provider.uac.service.impl;

import com.mall.cloud.provider.uac.service.UacLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UacLoginServiceImpl implements UacLoginService {
}
