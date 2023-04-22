package com.ray.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liuris
 * @create 2023-04-19-11:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto{
    private String name;
    private String description;
    private String status;
}
