package com.ray.controller;

import com.ray.domain.ResponseResult;
import com.ray.domain.vo.AdminLinkVo;
import com.ray.domain.vo.LinkVo;
import com.ray.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liuris
 * @create 2023-04-19-10:02
 */
@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    LinkService linkService;
//    友链管理

    @GetMapping("/list")
    public ResponseResult LinkList(Integer pageNum,Integer pageSize,String name,String status){
        return linkService.LinkList(pageNum,pageSize,name,status);
    }

    @PostMapping
    public ResponseResult addLink(@RequestBody  LinkVo linkVo){
        return linkService.addLink(linkVo);
    }

    @GetMapping("/{id}")
    public ResponseResult queryLink(@PathVariable("id") Long id){
        return linkService.queryLink(id);
    }

    @PutMapping
    public ResponseResult updateLink(@RequestBody AdminLinkVo adminLinkVo){
        return linkService.updateLink(adminLinkVo);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable("id") Long id){
        linkService.removeById(id);
        return ResponseResult.okResult();
    }
}
