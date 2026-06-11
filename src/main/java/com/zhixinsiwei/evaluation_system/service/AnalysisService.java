package com.zhixinsiwei.evaluation_system.service;

import com.zhixinsiwei.evaluation_system.common.entity.EvaluationRecords;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName AnalysisService.java
 * @Description 分析服务接口
 * @createTime 2023年12月19日 23:46:00
 */
public interface AnalysisService {

    String submitAnalysis(String userId, String paperId, String answerDetails, Integer age, Integer elapsedTime);

    List<EvaluationRecords> listAnalysisResult(HttpSession session);

    Map<String, Object> getAnalysisRecord(String recordId, HttpSession session);

    void reportView(String reportId, HttpServletResponse response);
}
