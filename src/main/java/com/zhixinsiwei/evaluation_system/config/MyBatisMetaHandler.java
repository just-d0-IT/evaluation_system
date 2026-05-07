package com.zhixinsiwei.evaluation_system.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zhixinsiwei.evaluation_system.common.constant.MetaConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName MyBatisMetaHandler.java
 * @Description TODO
 * @createTime 2025年12月09日 22:22:00
 */
@Slf4j
@Component
public class MyBatisMetaHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        String userId = getCurrentUserId();

        // 逻辑删除字段初始化
        this.strictInsertFill(metaObject, MetaConstant.DELETE_STATUS, Integer.class, MetaConstant.DEFAULT_NOT_DELETED);

        // 创建人 & 创建时间
        this.strictInsertFill(metaObject, MetaConstant.CREATED_BY, String.class, userId);
        this.strictInsertFill(metaObject, MetaConstant.CREATED_AT, Date.class, new Date());

        // 更新人 & 更新时间
        this.strictInsertFill(metaObject, MetaConstant.UPDATED_BY, String.class, userId);
        this.strictInsertFill(metaObject, MetaConstant.UPDATED_AT, Date.class, new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String userId = getCurrentUserId();

        this.strictUpdateFill(metaObject, MetaConstant.UPDATED_BY, String.class, userId);
        this.strictUpdateFill(metaObject, MetaConstant.UPDATED_AT, Date.class, new Date());
    }

    /**
     * 获取当前登录用户 ID（可根据项目实际情况修改）
     */
    private String getCurrentUserId() {
        try {
            return "";
        } catch (Exception e) {
            return MetaConstant.DEFAULT_SYSTEM_USER;
        }
    }

}
