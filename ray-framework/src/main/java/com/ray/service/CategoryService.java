package com.ray.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.CategoryDto;
import com.ray.domain.entity.Category;
import com.ray.domain.vo.AdminCategoryVo;
import com.ray.domain.vo.CategoryVo;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-03-21 20:49:17
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    List<CategoryVo> listAllCategory();

    ResponseResult addCategory(CategoryDto categoryDto);

    ResponseResult queryCategory(Long id);

    ResponseResult updateCategory(AdminCategoryVo adminCategoryDto);

    ResponseResult adminCategoryList(Integer pageNum, Integer pageSize, String name, String status);
}
