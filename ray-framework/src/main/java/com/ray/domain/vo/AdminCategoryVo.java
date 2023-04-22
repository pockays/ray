package com.ray.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liuris
 * @create 2023-04-19-11:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCategoryVo {
    private Long id;
    private String name;
    private String description;
    private String status;
}

