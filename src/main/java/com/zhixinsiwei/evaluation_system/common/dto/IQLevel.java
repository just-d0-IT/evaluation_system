package com.zhixinsiwei.evaluation_system.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName IQLevel.java
 * @Description TODO
 * @createTime 2025年11月29日 15:36:00
 */
@Data
@AllArgsConstructor
public class IQLevel {
    String range;          // 如 130以上
    int baseIQ;            // 档位最低 IQ（如 130）
    String descNew;        // 描述（新）
    int percentile;        // 百分位
    int[] scoreByAge;      // 每个年龄对应的原始分数
    String eval;           // 评估结果
    String summary;
}