package com.zhixinsiwei.evaluation_system.common.vo;

import lombok.Data;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName EvaluationStatusVO.java
 * @Description TODO
 * @createTime 2023年10月06日 09:06:00
 */
@Data
public class EvaluationStatusVO {
    private int paperId;
    private int questionId;
    private Boolean completeFlag;
}
