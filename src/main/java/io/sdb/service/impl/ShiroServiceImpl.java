package io.sdb.service.impl;

import io.sdb.common.utils.Constant;
import io.sdb.model.SysMenu;
import io.sdb.model.SysUser;
import io.sdb.model.SysUserToken;
import io.sdb.service.ShiroService;
import io.sdb.service.SysMenuService;
import io.sdb.service.SysUserService;
import io.sdb.service.SysUserTokenService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShiroServiceImpl implements ShiroService {

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if(userId == Constant.SUPER_ADMIN){
            List<SysMenu> menuList = sysMenuService.findAll();
            permsList = new ArrayList<>(menuList.size());
            for(SysMenu menu : menuList){
                permsList.add(menu.getPerms());
            }
        }else{
            permsList = sysUserService.queryAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    @Override
    public SysUserToken queryByToken(String token) {
        SysUserToken sysUserToken = new SysUserToken();
        sysUserToken.setToken(token);
        return sysUserTokenService.findFirstByModel(sysUserToken);
    }

    @Override
    public SysUser queryUser(Long userId) {
        return sysUserService.findById(userId);
    }
}
