package com.zhixinsiwei.evaluation_system.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName EvaluationUsers.java
 * @Description 用户信息表
 * @createTime 2026年01月31日 23:44:00
 */
@Data
@TableName("evaluation_users")
public class EvaluationUsers extends BaseEntity {

    private static final long serialVersionUID = -7509244219036084576L;

    /**
     * 唯一身份标识
     */
    private String userKey;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 扩展字段
     */
    private String extJson;

    /**
     * 用户来源
     */
    private String source;

}
