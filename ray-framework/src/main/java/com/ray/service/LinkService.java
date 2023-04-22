package com.ray.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.domain.ResponseResult;
import com.ray.domain.entity.Link;
import com.ray.domain.vo.AdminCategoryVo;
import com.ray.domain.vo.AdminLinkVo;
import com.ray.domain.vo.LinkVo;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-03-24 18:55:00
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult LinkList(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addLink(LinkVo linkVo);

    ResponseResult queryLink(Long id);

    ResponseResult updateLink(AdminLinkVo adminLinkVo);
}
