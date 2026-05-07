package com.zhixinsiwei.evaluation_system.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName ApiResponse.java
 * @Description TODO
 * @createTime 2023年08月26日 16:51:00
 */
@Data
@ToString
@AllArgsConstructor
@ApiModel(description = "统一的接口返回数据格式")
public class ApiResponse<T> {
    @ApiModelProperty(
            required = true,
            value = "错误码",
            dataType = "String",
            example = "0",
            position = 1
    )
    private String code;
    @ApiModelProperty(
            required = true,
            value = "返回描述信息",
            dataType = "String",
            example = "SUCCESS",
            position = 2
    )
    private String message;
    @ApiModelProperty(
            value = "返回数据封装",
            position = 3
    )
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse("0", "SUCCESS", data);
    }
}
