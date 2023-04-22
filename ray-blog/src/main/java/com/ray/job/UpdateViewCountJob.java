package com.ray.job;

import com.ray.domain.entity.Article;
import com.ray.service.ArticleService;
import com.ray.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liuris
 * @create 2023-04-06-18:15
 */
@Component
public class UpdateViewCountJob {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;
    @Scheduled(cron = "0 0/5 * * * ?")
    public void updateViewCount(){
        System.out.println("定时任务");
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("article:viewCount");
        List<Article> articles = viewCountMap.entrySet().stream().map(entry -> new Article(Long.valueOf(entry.getKey()), Long.valueOf(entry.getValue()))).collect(Collectors.toList());
        articleService.updateBatchById(articles);
    }
}
