package com.zhixinsiwei.evaluation_system.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName EvaluationQuestions.java
 * @Description 测评问题表实体类
 * @createTime 2023年08月26日 18:10:00
 */
@Data
@TableName("evaluation_questions")
public class EvaluationQuestions extends BaseEntity {

    private static final long serialVersionUID = -7980708730656021356L;

    /**
     * 试卷id
     */
    private String paperId;

    /**
     * 问题id
     */
    private Integer questionId;

    /**
     * 问题详情
     */
    private String questionDetail;

    /**
     * 提示内容
     */
    private String tips;
}
