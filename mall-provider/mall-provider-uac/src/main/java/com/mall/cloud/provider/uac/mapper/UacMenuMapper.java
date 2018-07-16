package com.mall.cloud.provider.uac.mapper;

import com.mall.cloud.provider.uac.model.domain.UacMenu;
import com.mall.cloud.provider.uac.api.model.vo.MenuVo;
import com.mall.cloud.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Mapper
public interface UacMenuMapper extends MyMapper<UacMenu> {

    List<UacMenu> selectMenuList(UacMenu uacMenuQuery);

    List<MenuVo> findMenuVoListByUserId(Long userId);

    List<UacMenu> listMenu(Set<Long> menuIdList);
}
