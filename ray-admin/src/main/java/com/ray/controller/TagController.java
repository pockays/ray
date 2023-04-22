package com.ray.controller;

import com.ray.domain.ResponseResult;
import com.ray.domain.dto.TagListDto;
import com.ray.domain.vo.PageVo;
import com.ray.domain.vo.TagListVo;
import com.ray.domain.vo.TagVo;
import com.ray.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liuris
 * @create 2023-04-08-20:41
 */
@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;
    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody TagListDto tagListDto){
        return tagService.addTag(tagListDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable("id") Long id) {
        return tagService.deleteTag(id);
    }



    @GetMapping("/{id}")
    public  ResponseResult<TagVo> getTag(@PathVariable("id") Long id){
        return tagService.getTag(id);
    }

    @PutMapping
    public ResponseResult updateTag(@RequestBody TagVo tagVo){ return  tagService.updateTag(tagVo);}

    @GetMapping("/listAllTag")
    public ResponseResult<TagVo> listAllTag() {
        return tagService.listAllTag();
    }
}
