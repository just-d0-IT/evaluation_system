package com.zhixinsiwei.evaluation_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationAnswersTemp;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationPapers;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationQuestions;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationRecords;
import com.zhixinsiwei.evaluation_system.mybatis_plus.service.*;
import com.zhixinsiwei.evaluation_system.service.AnalysisService;
import com.zhixinsiwei.evaluation_system.utils.QuestionDetail;
import com.zhixinsiwei.evaluation_system.utils.RuiWenIQCalculatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName AnalysisServiceImpl.java
 * @Description 分析服务实现类
 * @createTime 2023年12月19日 23:46:00
 */
@Slf4j
@Service
public class AnalysisServiceImpl implements AnalysisService {

    @Resource
    private AnswerTempService answerTempService;

    @Resource
    private RecordService recordService;

    @Resource
    private QuestionService questionService;

    @Resource
    private UserEntityService userEntityService;

    @Resource
    private PaperService paperService;

    @Resource(name = "reportExecutor")
    private ThreadPoolExecutor reportExecutor;

    @Override
    public String submitAnalysis(String userId, String paperId, String answerDetails, Integer age, Integer elapsedTime) {
        // 根据 paperId 查询试卷金额
        EvaluationPapers paper = paperService.getById(paperId);
        if (paper == null) {
            throw new RuntimeException("试卷不存在");
        }

        // 存入正式表
        EvaluationRecords record = new EvaluationRecords();
        record.setUserId(userId);
        record.setPaperId(paperId);
        record.setAnswerDetails(answerDetails);
        record.setElapsedTime(elapsedTime);
        record.setPayPrice(paper.getPrice());
        recordService.save(record);
        String recordId = record.getId();

        // 软删除临时数据
        answerTempService.remove(new LambdaUpdateWrapper<EvaluationAnswersTemp>()
                .eq(EvaluationAnswersTemp::getUserId, userId)
                .eq(EvaluationAnswersTemp::getPaperId, paperId)
        );

        // 提交生成报告任务至线程池
        reportExecutor.execute(() -> {
            try {
                // 查数据库
                List<EvaluationQuestions> questionsList =
                        questionService.list(
                                new LambdaQueryWrapper<EvaluationQuestions>()
                                        .eq(EvaluationQuestions::getPaperId, paperId)
                        );
                ObjectMapper objectMapper = new ObjectMapper();
                // 构建 Map
                Map<Integer, QuestionDetail> questionDetailMap =
                        questionsList.stream()
                                .filter(q -> q.getQuestionId() != null)
                                .filter(q -> StringUtils.hasText(q.getQuestionDetail()))
                                .collect(Collectors.toMap(
                                        EvaluationQuestions::getQuestionId,
                                        q -> {
                                            try {
                                                return objectMapper.readValue(q.getQuestionDetail(), QuestionDetail.class);
                                            } catch (Exception exception) {
                                                throw new RuntimeException(exception);
                                            }
                                        },
                                        (oldVal, newVal) -> newVal
                                ));
                String fileName = "/data/report/" + recordId + ".html";
                // String fileName = "D:/" + recordId + ".html";
                // 生成报告
                RuiWenIQCalculatorUtil.generateReport(age, answerDetails, questionDetailMap, fileName, elapsedTime);
                // 更新记录
                recordService.update(new LambdaUpdateWrapper<EvaluationRecords>()
                        .eq(EvaluationRecords::getId, recordId)
                        .set(EvaluationRecords::getReport, "http://jia.szzxsw.cn/api/evaluation_system/analysis/reportView/" + recordId));
            } catch (Exception exception) {
                log.error("generate report failed...", exception);
            }
        });
        return recordId;
    }

    @Override
    public List<EvaluationRecords> listAnalysisResult(HttpSession session) {
        String userId = (String) session.getAttribute("uid");
        return recordService.list(new LambdaQueryWrapper<EvaluationRecords>()
                .eq(EvaluationRecords::getUserId, userId)
                .orderByDesc(EvaluationRecords::getCreatedAt));
    }

    @Override
    public Map<String, Object> getAnalysisRecord(String recordId, HttpSession session) {
        String userId = (String) session.getAttribute("uid");
        EvaluationRecords record = recordService.getById(recordId);
        if (record == null) {
            throw new RuntimeException("记录不存在");
        }
        if (!record.getUserId().equals(userId)) {
            throw new RuntimeException("无权访问该记录");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("id", record.getId());
        result.put("paperId", record.getPaperId());
        result.put("report", record.getReport());
        result.put("payStatus", record.getPayStatus());
        result.put("payPrice", record.getPayPrice());
        result.put("elapsedTime", record.getElapsedTime());
        return result;
    }

    @Override
    public void reportView(String reportId, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        // 校验支付状态
        EvaluationRecords record = recordService.getById(reportId);
        if (record == null || record.getPayStatus() == null || record.getPayStatus() != 1) {
            // 未支付，返回弹窗提示页面
            try (OutputStream out = response.getOutputStream()) {
                String html = "<!DOCTYPE html><html lang=\"zh-CN\"><head><meta charset=\"UTF-8\">"
                        + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                        + "<title>提示</title></head><body>"
                        + "<script>alert('该报告尚未支付，请先完成支付后查看');"
                        + "window.location.href='http://jia.szzxsw.cn/evaluationHistory';</script>"
                        + "</body></html>";
                out.write(html.getBytes("UTF-8"));
            } catch (Exception exception) {
                log.error("report view failed...", exception);
            }
            return;
        }

        String fileName = "/data/report/" + reportId + ".html";
        // String fileName = "D:/" + reportId + ".html";
        File file = new File(fileName);
        try (InputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            IOUtils.copy(in, out);
        } catch (Exception exception) {
            log.error("report view failed...", exception);
        }
    }
}
