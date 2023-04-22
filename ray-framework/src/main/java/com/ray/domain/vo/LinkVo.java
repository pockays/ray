package com.ray.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author liuris
 * @create 2023-03-24-19:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkVo  {
    @TableId
    private Long id;


    private String name;

    private String logo;

    private String description;
    //网站地址
    private String address;

}