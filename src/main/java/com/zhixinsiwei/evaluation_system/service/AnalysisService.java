package com.zhixinsiwei.evaluation_system.service;

import com.zhixinsiwei.evaluation_system.common.entity.EvaluationRecords;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName AnalysisService.java
 * @Description 分析服务接口
 * @createTime 2023年12月19日 23:46:00
 */
public interface AnalysisService {

    void submitAnalysis(String userId, String paperId, String answerDetails, Integer age);

    List<EvaluationRecords> listAnalysisResult(HttpSession session);

    void reportView(String reportId, HttpServletResponse response);
}
