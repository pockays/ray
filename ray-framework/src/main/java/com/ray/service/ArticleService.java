package com.ray.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.AddArticleDto;
import com.ray.domain.dto.AdminArticleDetailDto;
import com.ray.domain.entity.Article;

/**
 * @author ray
 * @create 2023-03-15-17:40
 */
public interface ArticleService extends IService<Article> {
    ResponseResult hotArticlelist();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto articleDto);

    ResponseResult adminArticleList(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult adminArticleDetail(Long id);

    ResponseResult delete(Long id);
}