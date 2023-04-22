package com.ray.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.constants.SystemConstants;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.CategoryDto;
import com.ray.domain.entity.Article;
import com.ray.domain.entity.Category;
import com.ray.domain.vo.AdminCategoryVo;
import com.ray.domain.vo.CategoryVo;
import com.ray.domain.vo.PageVo;
import com.ray.mapper.CategoryMapper;
import com.ray.service.ArticleService;
import com.ray.service.CategoryService;
import com.ray.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-03-21 20:49:17
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ArticleService articleService;
    @Override
    public ResponseResult getCategoryList() {
        LambdaQueryWrapper<Article> articleWrapper =new LambdaQueryWrapper<>();
//        查询正常状态的文章
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
//        查询结果用list封装
        List<Article> articleList =articleService.list(articleWrapper);
//        拿出文章的所有的categoryid并且去重（set不允许相同元素）
        Set<Long> categoryIds = articleList.stream().map(Article::getCategoryId).collect(Collectors.toSet());
//        查询category这张表中与categoryid对应的数据
        List<Category> categories = listByIds(categoryIds);
//        筛选出正常状态的分类
        categories=categories.stream().filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus())).collect(Collectors.toList());
//        vo优化
        List<CategoryVo> categoryVos=BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);

    }

    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, SystemConstants.NORMAL);
        List<Category> categories = list(wrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return categoryVos;
    }

    @Override
    public ResponseResult addCategory(CategoryDto categoryDto) {
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        save(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult queryCategory(Long id) {
        Category category = getById(id);
        AdminCategoryVo adminCategoryVo = BeanCopyUtils.copyBean(category, AdminCategoryVo.class);
        return ResponseResult.okResult(adminCategoryVo);

    }

    @Override
    public ResponseResult updateCategory(AdminCategoryVo adminCategoryVo) {
        Category category = BeanCopyUtils.copyBean(adminCategoryVo, Category.class);
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult adminCategoryList(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(status),Category::getStatus, status);
        queryWrapper.like(StringUtils.hasText(name),Category::getName,name);
        Page<Category> page = new Page<>(pageNum, pageSize);
        page(page,queryWrapper);
        List<Category> category = page.getRecords();
        List<AdminCategoryVo> adminCategoryVos = BeanCopyUtils.copyBeanList(category, AdminCategoryVo.class);
        return ResponseResult.okResult(new PageVo(adminCategoryVos,page.getTotal()));
    }
}


