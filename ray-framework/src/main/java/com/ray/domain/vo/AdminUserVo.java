package com.ray.domain.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liuris
 * @create 2023-04-18-17:22
 */
@Data
@Accessors(chain = true)
public class AdminUserVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    private String userName;
    /**
     * 头像
     */
    private String avatar;

    private String sex;

    private String email;


}