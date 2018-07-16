package com.mall.cloud.provider.uac.mapper;

import com.mall.cloud.provider.uac.model.domain.UacUser;
import com.mall.cloud.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface UacUserMapper extends MyMapper<UacUser> {
    
    UacUser findByLoginName(String loginName);

    UacUser selectUserInfoByUserId(Long userId);

    UacUser findUserInfoByLoginName(String loginName);
}
