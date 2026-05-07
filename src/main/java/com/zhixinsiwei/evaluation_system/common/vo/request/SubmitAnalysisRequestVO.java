package com.zhixinsiwei.evaluation_system.common.vo.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName SubmitAnalysisRequestVO.java
 * @Description TODO
 * @createTime 2025年12月28日 14:38:00
 */
@Data
public class SubmitAnalysisRequestVO implements Serializable {

    private static final long serialVersionUID = -4788401341107690933L;

    private String paperId;
    private String userId;
    private String answerDetails;
}
