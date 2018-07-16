package com.mall.cloud.provider.uac.service.impl;

import com.mall.cloud.provider.uac.service.UacUserService;
import com.mall.cloud.common.core.support.BaseService;
import com.mall.cloud.provider.uac.mapper.UacUserMapper;
import com.mall.cloud.provider.uac.model.domain.UacUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(rollbackFor = Exception.class)
public class UacUserServiceImpl extends BaseService<UacUser> implements UacUserService {
    @Resource
    private UacUserMapper uacUserMapper;

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public UacUser findByLoginName(String loginName) {
        logger.info("findByLoginName - 根据用户名查询用户信息. loginName={}", loginName);

        return uacUserMapper.findByLoginName(loginName);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public UacUser findUserInfoByUserId(Long userId) {
        return uacUserMapper.selectUserInfoByUserId(userId);
    }

    @Override
    public UacUser findUserInfoByLoginName(String loginName) {
        return uacUserMapper.findUserInfoByLoginName(loginName);
    }
}
