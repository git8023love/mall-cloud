package com.mall.cloud.provider.uac.service.impl;

import com.mall.cloud.provider.uac.common.base.enums.ErrorCodeEnum;
import com.mall.cloud.provider.uac.model.domain.UacRole;
import com.mall.cloud.provider.uac.service.UacMenuService;
import com.mall.cloud.common.core.support.BaseService;
import com.mall.cloud.provider.uac.api.model.exceptions.UacBizException;
import com.mall.cloud.provider.uac.api.model.vo.MenuVo;
import com.mall.cloud.provider.uac.common.base.constant.GlobalConstant;
import com.mall.cloud.provider.uac.mapper.UacRoleMapper;
import com.mall.cloud.provider.uac.service.UacRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class UacRoleServiceImpl extends BaseService<UacRole> implements UacRoleService {

    @Resource
    private UacRoleMapper uacRoleMapper;

    @Resource
    private UacMenuService uacMenuService;

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<UacRole> findAllRoleInfoByUserId(Long userId) {
        return uacRoleMapper.selectAllRoleInfoByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<MenuVo> getOwnAuthTree(Long userId) {
        if (userId == null) {
            throw new UacBizException(ErrorCodeEnum.UAC10011001);
        }

        return uacMenuService.getMenuVoList(userId, GlobalConstant.Sys.OPER_APPLICATION_ID);
    }
}
