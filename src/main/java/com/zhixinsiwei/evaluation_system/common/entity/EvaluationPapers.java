package com.zhixinsiwei.evaluation_system.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName EvaluationPapers.java
 * @Description 测评试卷表实体类
 * @createTime 2023年08月26日 15:00:00
 */
@Data
@TableName("evaluation_papers")
public class EvaluationPapers extends BaseEntity {

    private static final long serialVersionUID = -3897742535578754554L;

    /**
     * 试卷名称
     */
    private String paperName;

    /**
     * 试卷价格
     */
    private Double price;

    /**
     * 问题数量
     */
    private Integer questionNumber;
}
