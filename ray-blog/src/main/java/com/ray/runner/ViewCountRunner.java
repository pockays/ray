package com.ray.runner;

import com.ray.domain.entity.Article;
import com.ray.mapper.ArticleMapper;
import com.ray.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liuris
 * @create 2023-04-06-18:04
 */
@Component
public class ViewCountRunner implements CommandLineRunner {

    @Autowired(required = false)
    private ArticleMapper articleMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {
        //查询博客信息  id  viewCount
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article ->
                     article.getViewCount().intValue()
                ));
        //存储到redis中
        redisCache.setCacheMap("article:viewCount",viewCountMap);
    }
}