package com.zhixinsiwei.evaluation_system.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName EvaluationAnswersTemp.java
 * @Description 测评回答临时记录表实体类
 * @createTime 2023年10月04日 21:01:00
 */
@Data
@TableName("evaluation_answers_temp")
public class EvaluationAnswersTemp extends BaseEntity {

    private static final long serialVersionUID = 2495426861594425409L;

    /**
     * 试卷id
     */
    private String paperId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 回答详情
     */
    private String answerDetails;
}
