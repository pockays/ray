package com.ray.service;

import com.ray.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author liuris
 * @create 2023-04-03-19:25
 */
public interface UploadService {
    ResponseResult uploadImg(MultipartFile img);
}
