package com.ray.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author liuris
 * @create 2023-03-23-15:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sg_article")
public class ArticleListVo {

    private Long id;
    //文章标题
    private String title;
    //文章摘要
    private String summary;
    //分类名
    private String categoryName;
    //缩略图
    private String thumbnail;
    //访问量
    private Long viewCount;

    private Date createTime;

}