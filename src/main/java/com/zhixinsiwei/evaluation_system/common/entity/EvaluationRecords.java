package com.zhixinsiwei.evaluation_system.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName EvaluationRecords.java
 * @Description 测评记录表实体类
 * @createTime 2023年08月27日 10:46:00
 */
@Data
@TableName("evaluation_records")
public class EvaluationRecords extends BaseEntity {

    private static final long serialVersionUID = -2566583707392134281L;

    /**
     * 试卷id
     */
    private String paperId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 支付金额
     */
    private Double payPrice;

    /**
     * 支付状态0:未支付,1:已支付
     */
    private Integer payStatus;

    /**
     * 测试报告内容
     */
    private String report;

    /**
     * 回答详情
     */
    private String answerDetails;

    /**
     * 总耗时（秒）
     */
    private Integer elapsedTime;
}
