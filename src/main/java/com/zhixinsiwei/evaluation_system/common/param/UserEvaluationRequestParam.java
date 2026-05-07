package com.zhixinsiwei.evaluation_system.common.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName UserEvaluationRequestParam.java
 * @Description TODO
 * @createTime 2023年10月04日 21:20:00
 */
@Data
@ApiModel(value = "用户测评对象", description = "用户测评请求参数")
public class UserEvaluationRequestParam {

    /**
     * 用户id
     */
    @ApiModelProperty(dataType = "String", required = true, value = "用户编号")
    private String userId;

    /**
     * 测评试卷id
     */
    @ApiModelProperty(dataType = "String", required = true, value = "测评试卷编号")
    private String paperId;

    /**
     * 问题id
     */
    @ApiModelProperty(dataType = "Integer", required = true, value = "问题编号")
    private Integer questionId;
}
