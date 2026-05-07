package com.zhixinsiwei.evaluation_system.common.constant;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName MetaConstant.java
 * @Description MyBatis-Plus 字段填充常量定义
 * @createTime 2025年12月09日 22:23:00
 */
public class MetaConstant {

    /**
     * 逻辑删除字段名
     */
    public static final String DELETE_STATUS = "deleteStatus";

    /**
     * 创建人字段
     */
    public static final String CREATED_BY = "createdBy";

    /**
     * 创建时间字段
     */
    public static final String CREATED_AT = "createdAt";

    /**
     * 更新人字段
     */
    public static final String UPDATED_BY = "updatedBy";

    /**
     * 更新时间字段
     */
    public static final String UPDATED_AT = "updatedAt";

    /**
     * 默认逻辑未删除状态
     */
    public static final Integer DEFAULT_NOT_DELETED = 0;

    /**
     * 默认操作用户（无登录情况下）
     */
    public static final String DEFAULT_SYSTEM_USER = "admin";

}
