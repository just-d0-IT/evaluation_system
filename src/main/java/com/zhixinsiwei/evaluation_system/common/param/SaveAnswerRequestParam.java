package com.zhixinsiwei.evaluation_system.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName SaveAnswerRequestParam.java
 * @Description TODO
 * @createTime 2023年10月06日 17:19:00
 */
@Data
@ApiModel(value = "保存用户回答对象", description = "保存用户回答请求参数")
public class SaveAnswerRequestParam extends UserEvaluationRequestParam {
    /**
     * 回答详情
     */
    @ApiModelProperty(dataType = "String", required = true, value = "回答详情")
    private String answerDetails;
}
