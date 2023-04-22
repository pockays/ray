package com.ray.domain.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author liuris
 * @create 2023-04-16-18:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    //角色名称
    private String roleName;
    //角色权限字符串
    private String roleKey;
    //显示顺序
    private Integer roleSort;
    //角色状态（0正常 1停用）
    private String status;
    //备注
    private String remark;

    private List<Long> menuIds;
}