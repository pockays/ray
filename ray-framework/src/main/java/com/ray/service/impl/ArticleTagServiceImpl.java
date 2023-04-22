package com.ray.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.domain.entity.Article;
import com.ray.domain.entity.ArticleTag;
import com.ray.mapper.ArticleMapper;
import com.ray.mapper.ArticleTagMapper;
import com.ray.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * @author liuris
 * @create 2023-04-13-16:28
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService  {
}
