package com.ray.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.constants.SystemConstants;
import com.ray.domain.ResponseResult;
import com.ray.domain.entity.Link;
import com.ray.domain.vo.AdminCategoryVo;
import com.ray.domain.vo.AdminLinkVo;
import com.ray.domain.vo.LinkVo;
import com.ray.domain.vo.PageVo;
import com.ray.exception.SystemException;
import com.ray.exception.enums.AppHttpCodeEnum;
import com.ray.mapper.LinkMapper;
import com.ray.service.LinkService;
import com.ray.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-03-24 18:55:00
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(queryWrapper);
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult LinkList(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),Link::getName,name);
        queryWrapper.eq(StringUtils.hasText(status),Link::getStatus,status);
        Page<Link> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<Link> links = page.getRecords();
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        return ResponseResult.okResult(new PageVo(linkVos,page.getTotal()));

    }

    @Override
    public ResponseResult addLink(LinkVo linkVo) {
        Link link = BeanCopyUtils.copyBean(linkVo, Link.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult queryLink(Long id) {
        Link link = getById(id);
        AdminLinkVo adminLinkVo = BeanCopyUtils.copyBean(link, AdminLinkVo.class);
        return ResponseResult.okResult(adminLinkVo);
    }

    @Override
    public ResponseResult updateLink(AdminLinkVo adminLinkVo) {
        Link link = BeanCopyUtils.copyBean(adminLinkVo, Link.class);
        if(!updateById(link)){
            throw new SystemException(AppHttpCodeEnum. LINK_NO_EXIST);
        }
        return ResponseResult.okResult();
    }


}


