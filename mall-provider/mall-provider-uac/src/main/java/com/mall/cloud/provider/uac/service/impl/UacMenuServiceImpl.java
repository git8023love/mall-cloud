package com.mall.cloud.provider.uac.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mall.cloud.uac.common.util.PublicUtil;
import com.mall.cloud.provider.uac.model.domain.UacMenu;
import com.mall.cloud.provider.uac.model.enums.UacMenuStatusEnum;
import com.mall.cloud.provider.uac.service.UacMenuService;
import com.mall.cloud.common.core.support.BaseService;
import com.mall.cloud.provider.uac.api.model.vo.MenuVo;
import com.mall.cloud.provider.uac.mapper.UacMenuMapper;
import com.mall.cloud.provider.uac.utils.TreeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional(rollbackFor = Exception.class)
public class UacMenuServiceImpl extends BaseService<UacMenu> implements UacMenuService {
    @Resource
    private UacMenuMapper uacMenuMapper;

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<MenuVo> getMenuVoList(Long userId, Long applicationId) {
        // 1.查询该用户下所有的菜单列表
        List<MenuVo> menuVoList = Lists.newArrayList();
        List<UacMenu> menuList;
        Set<UacMenu> menuSet = Sets.newHashSet();
        // 如果是admin则返回所有的菜单
        if (userId == 1L) {
            // 1.1 查询该用户下所有的菜单列表
            UacMenu uacMenuQuery = new UacMenu();
            uacMenuQuery.setStatus(UacMenuStatusEnum.ENABLE.getType());
            uacMenuQuery.setApplicationId(applicationId);
            uacMenuQuery.setOrderBy(" level asc,number asc");
            menuList = uacMenuMapper.selectMenuList(uacMenuQuery);
        } else {
            // 1.2查询该用户下所有的菜单列表
            menuVoList = uacMenuMapper.findMenuVoListByUserId(userId);
            if (PublicUtil.isEmpty(menuVoList)) {
                return null;
            }
            Set<Long> ids = Sets.newHashSet();
            for (final MenuVo menuVo : menuVoList) {
                ids.add(menuVo.getId());
            }

            List<UacMenu> ownMenuList = this.getMenuList(ids);

            // 查出所有含有菜单的菜单信息
            UacMenu uacMenu = new UacMenu();
            uacMenu.setStatus(UacMenuStatusEnum.ENABLE.getType());
            List<UacMenu> allMenuList = this.selectMenuList(uacMenu);
            Map<Long, UacMenu> map = Maps.newHashMap();
            for (final UacMenu menu : allMenuList) {
                map.put(menu.getId(), menu);
            }

            for (final UacMenu menu : ownMenuList) {
                getPid(menuSet, menu, map);
            }
            menuList = new ArrayList<>(menuSet);
        }
        List<MenuVo> list = getMenuVo(menuList);
        if (PublicUtil.isNotEmpty(menuVoList)) {
            list.addAll(menuVoList);
        }
        // 2.递归成树
        return TreeUtil.getChildMenuVos(list, 0L);
    }

    private List<MenuVo> getMenuVo(List<UacMenu> menuList) {
        List<MenuVo> menuVoList = Lists.newArrayList();
        for (UacMenu uacMenu : menuList) {
            MenuVo menuVo = new MenuVo();
            BeanUtils.copyProperties(uacMenu, menuVo);
            menuVo.setUrl(uacMenu.getUrl());
            menuVo.setMenuName(uacMenu.getMenuName());
            menuVoList.add(menuVo);
        }
        return menuVoList;
    }

    private void getPid(Set<UacMenu> menuSet, UacMenu menu, Map<Long, UacMenu> map) {
        UacMenu parent = map.get(menu.getPid());
        if (parent != null && parent.getId() != 0L) {
            menuSet.add(parent);
            getPid(menuSet, parent, map);
        }
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public List<UacMenu> selectMenuList(UacMenu uacMenu) {
        return null;
    }

    @Override
    public List<UacMenu> getMenuList(final Set<Long> menuIdList) {
        return uacMenuMapper.listMenu(menuIdList);
    }
}
