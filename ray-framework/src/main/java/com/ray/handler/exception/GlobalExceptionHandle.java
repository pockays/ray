package com.ray.handler.exception;
import com.ray.domain.ResponseResult;
import com.ray.exception.enums.AppHttpCodeEnum;
import com.ray.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author liuris
 * @create 2023-03-28-16:50
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandle {
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandle(SystemException e){
        log.error("出现了异常！{}",e);
        return ResponseResult.errorResult(e.getCode(), e.getMsg());
    }
    @ExceptionHandler(Exception.class)
    public ResponseResult ExceptionHandle(Exception e){
        log.error("出现了异常！{}",e);
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
    }
//    本来的自定义权限错误处理器没这个全局优先级高被覆盖了，所以再定义个这个让自定义权限错误处理器能够处理
    @ExceptionHandler(AccessDeniedException.class)
    public void accessDeniedException(AccessDeniedException e) throws AccessDeniedException {
        throw e;
    }

}
