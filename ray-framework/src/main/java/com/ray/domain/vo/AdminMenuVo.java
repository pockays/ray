package com.ray.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ray.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liuris
 * @create 2023-04-16-17:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_menu")
@Accessors(chain = true)
public class AdminMenuVo {
    //菜单ID@TableId
    private Long id;

    @TableField(value = "menuName")
    private String label;

    private Long parentId;

//    这里加泛型？是因为copyChildrenBeanList
    @TableField(exist = false)
    private List<?> children;

}
