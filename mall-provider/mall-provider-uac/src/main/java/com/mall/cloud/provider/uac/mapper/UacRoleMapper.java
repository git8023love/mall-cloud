package com.mall.cloud.provider.uac.mapper;

import com.mall.cloud.provider.uac.model.domain.UacRole;
import com.mall.cloud.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface UacRoleMapper extends MyMapper<UacRole> {

    List<UacRole> selectAllRoleInfoByUserId(Long userId);
}
