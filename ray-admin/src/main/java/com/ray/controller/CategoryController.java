package com.ray.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.CategoryDto;
import com.ray.domain.entity.Category;
import com.ray.domain.vo.AdminCategoryVo;
import com.ray.domain.vo.CategoryVo;
import com.ray.domain.vo.ExcelCategoryVo;
import com.ray.exception.enums.AppHttpCodeEnum;
import com.ray.service.CategoryService;
import com.ray.utils.BeanCopyUtils;
import com.ray.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author liuris
 * @create 2023-04-12-17:51
 */
@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory() {
        List<CategoryVo> categoryVos = categoryService.listAllCategory();
        return ResponseResult.okResult(categoryVos);
    }

    @PreAuthorize("@ps.hasPermisson('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            WebUtils.setDownLoadHeader("分类.xlsx", response);
            List<Category> categorys = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categorys, ExcelCategoryVo.class);
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);
        } catch (Exception e) {
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    @PostMapping
    public ResponseResult addCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    @GetMapping("/{id}")
    public ResponseResult queryCategory(@PathVariable("id") Long id){
        return categoryService.queryCategory(id);
    }
    @PutMapping
    public ResponseResult updateCategory(@RequestBody AdminCategoryVo adminCategoryVo) {
        return categoryService.updateCategory(adminCategoryVo);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable("id") Long id) {
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/list")
    public ResponseResult  adminCategoryList(Integer pageNum,Integer pageSize,String name,String status){
        return categoryService.adminCategoryList(pageNum,pageSize,name,status);
    }
}

