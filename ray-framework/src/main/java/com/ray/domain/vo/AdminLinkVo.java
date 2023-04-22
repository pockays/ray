package com.ray.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liuris
 * @create 2023-04-19-12:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminLinkVo  {
    @TableId
    private Long id;

    private String status;

    private String name;

    private String logo;

    private String description;
    //网站地址
    private String address;

}