package com.hcycom.sso.OperationLog;

import java.lang.annotation.*;

/**
 * @Path：
 * @Classname：
 * @Description：操作日志记录注解
 * @Author：yandi
 * @CreateTime：2018/9/17 12:11
 * @ModifyUser：yandi
 * @ModifyRemark：
 * @ModifyTime：2018/9/17 12:11
 */
@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented //生成文档
public @interface OperationLog {
    String value() default "";
}

