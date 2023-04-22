package com.ray.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author liuris
 * @create 2023-03-24-18:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sg_article")
public class ArticleDetailVo {

    private Long id;
    //文章摘要
    private String summary;
    //分类名
    private String categoryName;
    //所属分类id
    private Long categoryId;
    //缩略图
    private String thumbnail;
    //文章内容
    private String content;
    //访问量
    private Long viewCount;

    private Date createTime;

}