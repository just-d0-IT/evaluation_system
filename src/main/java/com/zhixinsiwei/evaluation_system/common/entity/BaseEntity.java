package com.zhixinsiwei.evaluation_system.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName BaseEntity.java
 * @Description 基础实体类
 * @createTime 2025年12月06日 19:27:00
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -414304745155573135L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 删除状态0:未删除,1:删除
     */
    @TableLogic
    @TableField(value = "delete_status", fill = FieldFill.INSERT)
    private Integer deleteStatus;

    /**
     * 创建人
     */
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Date createdAt;

    /**
     * 更新人
     */
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;
}
