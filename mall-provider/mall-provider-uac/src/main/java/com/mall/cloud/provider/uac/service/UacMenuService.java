package com.mall.cloud.provider.uac.service;

import com.mall.cloud.provider.uac.model.domain.UacMenu;
import com.mall.cloud.provider.uac.api.model.vo.MenuVo;
import com.mall.cloud.common.core.support.IService;

import java.util.List;
import java.util.Set;

public interface UacMenuService extends IService<UacMenu>{

    List<MenuVo> getMenuVoList(Long userId, Long operApplicationId);

    List<UacMenu> selectMenuList(UacMenu uacMenu);

    List<UacMenu> getMenuList(Set<Long> menuIdList);
}
