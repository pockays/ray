package com.ray.domain.entity;


import java.io.Serializable;

import com.mysql.cj.xdevapi.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 文章标签关联表(ArticleTag)表实体类
 *
 * @author makejava
 * @since 2023-04-13 16:26:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sg_article_tag")
public class ArticleTag implements Serializable {
    private static final long serialVersionUID = -6398085434619894919L;
    //文章id@TableId
    private Long articleId;
    //标签id@TableId
    private Long tagId;
}

