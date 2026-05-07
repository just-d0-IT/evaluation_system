package com.zhixinsiwei.evaluation_system.common.dto;

import lombok.Data;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName IQResult.java
 * @Description TODO
 * @createTime 2025年11月29日 15:38:00
 */
@Data
public class IQResult {
    int iq;
    String desc;
    int percentile;
    String eval;
    String summary;
}
