package com.ray.service.impl;

import com.ray.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuris
 * @create 2023-04-14-14:43
 */
@Service("ps")
public class PermissionService {
    public boolean hasPermisson(String permisson){
        if(SecurityUtils.isAdmin()){
            return true;
        }

        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permisson);
    }
}
