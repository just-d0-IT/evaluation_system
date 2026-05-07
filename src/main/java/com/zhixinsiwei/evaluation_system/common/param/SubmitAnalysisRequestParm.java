package com.zhixinsiwei.evaluation_system.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName SubmitAnalysisRequestParm.java
 * @Description TODO
 * @createTime 2025年12月28日 20:48:00
 */
@Data
@ApiModel(value = "提交分析对象", description = "测评完成后，提交分析请求参数")
public class SubmitAnalysisRequestParm extends UserEvaluationRequestParam {
    /**
     * 回答详情
     */
    @ApiModelProperty(dataType = "String", required = true, value = "回答详情")
    private String answerDetails;
    /**
     * 年龄
     */
    @ApiModelProperty(dataType = "Integer", required = true, value = "年龄")
    private Integer age;
}
