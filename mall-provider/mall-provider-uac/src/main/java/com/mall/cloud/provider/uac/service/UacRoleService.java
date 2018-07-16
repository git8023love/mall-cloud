package com.mall.cloud.provider.uac.service;

import com.mall.cloud.provider.uac.api.model.vo.MenuVo;
import com.mall.cloud.provider.uac.model.domain.UacRole;
import com.mall.cloud.common.core.support.IService;

import java.util.List;

public interface UacRoleService extends IService<UacRole> {
    
    List<UacRole> findAllRoleInfoByUserId(Long id);

    List<MenuVo> getOwnAuthTree(Long id);
}
