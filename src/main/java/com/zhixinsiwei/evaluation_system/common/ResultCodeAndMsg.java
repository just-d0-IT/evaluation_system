package com.zhixinsiwei.evaluation_system.common;

import lombok.Getter;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName ResultCodeAndMsg.java
 * @Description 结果状态码及信息
 * @createTime 2023年12月23日 21:23:00
 */
@Getter
public enum ResultCodeAndMsg {

    /**
     * 失败
     */

    ERROR("-1", "system error"),

    /**
     * 成功
     */
    SUCCESS("0", "success"),

    ;

    private final String code;
    private final String message;

    ResultCodeAndMsg(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
