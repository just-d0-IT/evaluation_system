package com.zhixinsiwei.evaluation_system.common.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName QuestionVO.java
 * @Description TODO
 * @createTime 2023年08月27日 10:41:00
 */
@Data
@Builder
public class QuestionVO {
    private String paperId;
    private Integer questionId;
    private String questionDetail;
    private String tips;
}
