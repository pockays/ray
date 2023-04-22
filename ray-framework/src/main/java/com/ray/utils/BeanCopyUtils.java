package com.ray.utils;

import com.ray.domain.entity.Article;
import com.ray.domain.vo.AdminMenuVo;
import com.ray.domain.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuris
 * @create 2023-03-21-11:03
 */
public class BeanCopyUtils {
    private BeanCopyUtils() {
    }

    public static <V> V copyBean(Object source, Class<V> clazz) {
        V result = null;
        try {
            result = clazz.newInstance();
            BeanUtils.copyProperties(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <T,V> List<V> copyBeanList(List<T>list,Class<V> clazz){
        return list.stream().map(o->copyBean(o,clazz)).collect(Collectors.toList());
    }


}
