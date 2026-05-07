package com.zhixinsiwei.evaluation_system.service;

import com.zhixinsiwei.evaluation_system.common.entity.EvaluationAnswersTemp;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationPapers;
import com.zhixinsiwei.evaluation_system.common.vo.QuestionVO;

import java.util.List;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName EvaluationService.java
 * @Description 测评服务接口
 * @createTime 2023年08月26日 16:40:00
 */
public interface EvaluationService {
    /**
     * 获取所有测评题目
     *
     * @return
     */
    List<EvaluationPapers> getAllPapers();

    /**
     * 问题查询
     *
     * @param paperId    测评试卷id
     * @param questionId 问题id
     * @return
     */
    QuestionVO queryQuestion(String paperId, String questionId);

    /**
     * 获取用户上次答题记录（继续作答）
     *
     * @param userId
     * @param paperId
     * @return
     */
    EvaluationAnswersTemp getTempAnswer(String userId, String paperId);

    /**
     * 前端每答一题就上传整个答题 JSON -> 更新临时记录
     *
     * @param userId
     * @param paperId
     * @param answerDetails
     */
    void saveTempAnswer(String userId, String paperId, String answerDetails);

    /**
     * 重新开始答题
     *
     * @param userId
     * @param paperId
     */
    void restartEvaluation(String userId, String paperId);

}
