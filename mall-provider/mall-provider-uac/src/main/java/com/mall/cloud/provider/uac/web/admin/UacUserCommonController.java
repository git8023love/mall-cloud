package com.mall.cloud.provider.uac.web.admin;

import com.mall.cloud.provider.uac.model.domain.UacRole;
import com.mall.cloud.common.core.support.BaseController;
import com.mall.cloud.provider.uac.model.dto.user.CheckLoginNameDto;
import com.mall.cloud.uac.common.util.PublicUtil;
import com.mall.cloud.uac.common.util.wrapper.WrapMapper;
import com.mall.cloud.uac.common.util.wrapper.Wrapper;
import com.mall.cloud.provider.uac.api.model.vo.MenuVo;
import com.mall.cloud.provider.uac.model.domain.UacUser;
import com.mall.cloud.provider.uac.model.vo.UserVo;
import com.mall.cloud.provider.uac.service.UacRoleService;
import com.mall.cloud.provider.uac.service.UacUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Api(value = "Web - UacUserCommonController", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UacUserCommonController extends BaseController {

    @Resource
    private UacUserService uacUserService;
    @Resource
    private UacRoleService uacRoleService;

    @GetMapping(value = "/queryUserInfo/{loginName}")
    @ApiOperation(httpMethod = "GET", value = "根据userId查询用户详细信息")
    public Wrapper<UserVo> queryUserInfo(@PathVariable String loginName) {
        logger.info("根据userId查询用户详细信息");
        UserVo userVo = new UserVo();
        UacUser uacUser = uacUserService.findByLoginName(loginName);
        uacUser = uacUserService.findUserInfoByUserId(uacUser.getId());
        List<UacRole> roleList = uacRoleService.findAllRoleInfoByUserId(uacUser.getId());
        List<MenuVo> authTree = uacRoleService.getOwnAuthTree(uacUser.getId());
        BeanUtils.copyProperties(uacUser, userVo);
        if (PublicUtil.isNotEmpty(roleList)) {
            userVo.setRoles(new HashSet<>(roleList));
        }
        userVo.setAuthTree(authTree);
        return WrapMapper.ok(userVo);
    }

    @GetMapping(value = "/checkLoginName")
    @ApiOperation(httpMethod = "GET", value = "校验登录名唯一性")
    public Wrapper<Boolean> checkLoginName(@ApiParam(name = "loginName", value = "登录名") @RequestBody CheckLoginNameDto checkLoginNameDto) {
        logger.info("校验登录名唯一性 checkLoginNameDto={}", checkLoginNameDto);
        Long id = checkLoginNameDto.getUserId();
        String loginName = checkLoginNameDto.getLoginName();
        Example example = new Example(UacUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("loginName", loginName);
        if (id != null) {
            criteria.andEqualTo("id", id);
        }
        int result = uacUserService.selectCountByExample(example);
        return WrapMapper.ok(result < 1);
    }

}
