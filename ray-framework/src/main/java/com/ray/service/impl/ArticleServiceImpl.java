package com.ray.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.constants.SystemConstants;
import com.ray.domain.dto.AddArticleDto;
import com.ray.domain.dto.AdminArticleDetailDto;
import com.ray.domain.entity.ArticleTag;
import com.ray.domain.entity.Category;
import com.ray.domain.vo.*;
import com.ray.domain.ResponseResult;
import com.ray.domain.entity.Article;
import com.ray.mapper.ArticleMapper;
import com.ray.service.ArticleService;
import com.ray.service.ArticleTagService;
import com.ray.service.CategoryService;
import com.ray.utils.BeanCopyUtils;
import com.ray.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ray
 * @create 2023-03-15-17:51
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ArticleTagService articleTagService;
    @Autowired
    RedisCache redisCache;
    @Override
    public ResponseResult hotArticlelist() {
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.orderByDesc(Article::getViewCount);
        Page<Article> page=new Page(1,10);
        page(page,queryWrapper);
        List<Article> articles = page.getRecords();
        List<HotArticleVo> articleVos=BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(articleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId, categoryId);
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        List<Article> articles=page.getRecords();

//        用articleid查询categoryname然后对article中的categoryname进行设置（方式1）
//        for(Article article:articles){
//            Category category = categoryService.getById(article.getCategoryId());
//            article.setCategoryName((category.getName()));
//        }

//        （方式2） map里的东西的简化看下三更的第22集，尽量减少变量的命名,以及accessors的解释
        articles.stream().map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName())).collect(Collectors.toList());
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        Article article = getById(id);
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        Long categoryId = articleDetailVo.getCategoryId();
        Category category =categoryService.getById(categoryId);
        if(category!=null){
            articleDetailVo.setCategoryName(category.getName());
        }
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue("article:viewCount", id.toString(), 1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult add(AddArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult adminArticleList(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.hasText(title),Article::getTitle, title);
        lambdaQueryWrapper.like(StringUtils.hasText(summary),Article::getSummary,summary);
        lambdaQueryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,lambdaQueryWrapper);
        List<Article> articles=page.getRecords();
        List<AdminArticleVo> adminArticleVo = BeanCopyUtils.copyBeanList(articles, AdminArticleVo.class);
        PageVo pageVo = new PageVo(adminArticleVo,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult adminArticleDetail(Long id) {
        Article article = getById(id);
//        加浏览量
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        AdminArticleDetailDto adminArticleDetailDto = BeanCopyUtils.copyBean(article, AdminArticleDetailDto.class);
//        加tag
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> ArticleTags = articleTagService.list(queryWrapper);
        List<Long> Tag = ArticleTags.stream().map(ArticleTag::getTagId).collect(Collectors.toList());
        adminArticleDetailDto.setTags(Tag);
        return ResponseResult.okResult(adminArticleDetailDto);

    }

    @Override
    public ResponseResult delete(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

}