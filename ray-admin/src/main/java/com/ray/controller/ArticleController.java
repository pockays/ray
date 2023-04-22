package com.ray.controller;

import com.ray.domain.ResponseResult;
import com.ray.domain.dto.AddArticleDto;
import com.ray.domain.dto.AdminArticleDetailDto;
import com.ray.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liuris
 * @create 2023-04-13-15:57
 */
@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    ArticleService articleService;
    @PostMapping
//    添加博文
    public ResponseResult add(@RequestBody AddArticleDto articleDto){
        return articleService.add(articleDto);
    }



//    为了对文章进行管理，需要提供文章列表，在后台需要分页查询文章功能，要求能根据标题和摘要模糊查询。
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum,Integer pageSize,String title,String summary){
        return articleService.adminArticleList(pageNum,pageSize,title,summary);
    }

//    查询文章详情
    @GetMapping("/{id}")
    public ResponseResult adminArticleDetail(@PathVariable("id") Long id){
        return articleService.adminArticleDetail(id);
    }

    @PutMapping
    public ResponseResult update(@RequestBody AddArticleDto articleDto){
        return articleService.add(articleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable("id") Long id){
        return articleService.delete(id);
    }
}
