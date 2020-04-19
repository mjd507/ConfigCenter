package com.configcenter.admin.aop;

import com.configcenter.admin.service.UserService;
import com.configcenter.admin.util.Role;
import com.configcenter.admin.util.UserUtils;
import com.configcenter.common.util.ApiCode;
import com.configcenter.common.util.CCException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

/**
 * Created by mjd on 2020/4/17 12:51
 */
@Component
@Aspect
public class UserReadWriteCheckAspect {

    @Autowired
    private UserService userService;

    @Pointcut("@annotation(com.configcenter.admin.aop.CheckRead)")
    public void checkRead() {
    }

    @Pointcut("@annotation(com.configcenter.admin.aop.CheckWrite)")
    public void checkWrite() {
    }

    @Before(value = "checkRead()")
    public void beforeRead(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs(); // 获取方法参数
        Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations(); // 获取方法参数注解
        boolean success = false;
        for (int i = 0; i < args.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof CheckParam) {
                    if (args[i] instanceof Integer) {
                        Integer appId = (Integer) args[i];
                        Integer roleType = userService.getRoleTypeByUidAndAppId(UserUtils.getUid(), appId);
                        if (roleType == null) break;
                        boolean hasReadAccess = Role.verifyRead(roleType);
                        if (!hasReadAccess) break;
                        success = true;
                    }
                    break;
                }
            }
        }
        if (!success) {
            throw new CCException(ApiCode.FORBIDDEN.getCode(), "您暂时没有权限操作");
        }
    }

    @Before(value = "checkWrite()")
    public void beforeWrite(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs(); // 获取方法参数
        Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations(); // 获取方法参数注解
        boolean success = false;
        for (int i = 0; i < args.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof CheckParam) {
                    if (args[i] instanceof Integer) {
                        Integer appId = (Integer) args[i];
                        Integer roleType = userService.getRoleTypeByUidAndAppId(UserUtils.getUid(), appId);
                        if (roleType == null) break;
                        boolean hasReadAccess = Role.verifyWrite(roleType);
                        if (!hasReadAccess) break;
                        success = true;
                    }
                    break;
                }
            }
        }
        if (!success) {
            throw new CCException(ApiCode.FORBIDDEN.getCode(), "您暂时没有权限操作");
        }
    }


}
