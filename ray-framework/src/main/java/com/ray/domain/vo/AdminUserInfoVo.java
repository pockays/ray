package com.ray.domain.vo;

import jdk.nashorn.internal.ir.debug.PrintVisitor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liuris
 * @create 2023-04-09-18:18
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserInfoVo {
    private List<String> permissons;
    private List<String> roles;
    private UserInfoVo user;
}
