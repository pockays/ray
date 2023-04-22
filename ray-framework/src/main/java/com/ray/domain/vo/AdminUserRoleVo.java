package com.ray.domain.vo;

import com.ray.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liuris
 * @create 2023-04-18-17:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserRoleVo {
    private List<Long> roleIds;
    private List<Role> roles;
    private AdminUserVo user;
}

