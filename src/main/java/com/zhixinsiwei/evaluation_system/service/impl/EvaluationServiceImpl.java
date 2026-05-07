package com.zhixinsiwei.evaluation_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationAnswersTemp;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationPapers;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationQuestions;
import com.zhixinsiwei.evaluation_system.common.vo.QuestionVO;
import com.zhixinsiwei.evaluation_system.mybatis_plus.service.AnswerTempService;
import com.zhixinsiwei.evaluation_system.mybatis_plus.service.PaperService;
import com.zhixinsiwei.evaluation_system.mybatis_plus.service.QuestionService;
import com.zhixinsiwei.evaluation_system.mybatis_plus.service.RecordService;
import com.zhixinsiwei.evaluation_system.service.EvaluationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName EvaluationServiceImpl.java
 * @Description 测评服务实现类
 * @createTime 2023年08月26日 16:41:00
 */
@Slf4j
@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Resource
    private PaperService paperService;

    @Resource
    private RecordService recordService;

    @Resource
    private QuestionService questionService;

    @Resource
    private AnswerTempService answerTempService;

    @Override
    public List<EvaluationPapers> getAllPapers() {
        return paperService.list();
    }

    @Override
    public QuestionVO queryQuestion(String paperId, String questionId) {
        EvaluationQuestions evaluationQuestions = questionService.getOne(
                new LambdaQueryWrapper<EvaluationQuestions>()
                        .eq(EvaluationQuestions::getPaperId, paperId)
                        .eq(EvaluationQuestions::getQuestionId, questionId)
        );
        //题目存在性校验
        if (null == evaluationQuestions) {
            throw new RuntimeException("doesn't contains this question...");
        } else {
            return QuestionVO.builder()
                    .paperId(evaluationQuestions.getPaperId())
                    .questionId(evaluationQuestions.getQuestionId())
                    .questionDetail(evaluationQuestions.getQuestionDetail())
                    .tips(evaluationQuestions.getTips())
                    .build();
        }
    }

    @Override
    public EvaluationAnswersTemp getTempAnswer(String userId, String paperId) {
        return answerTempService.getOne(
                new LambdaQueryWrapper<EvaluationAnswersTemp>()
                        .eq(EvaluationAnswersTemp::getUserId, userId)
                        .eq(EvaluationAnswersTemp::getPaperId, paperId)
        );
    }

    @Override
    public void saveTempAnswer(String userId, String paperId, String answerDetails) {
        EvaluationAnswersTemp exist = getTempAnswer(userId, paperId);
        if (exist == null) {
            // 新建记录
            EvaluationAnswersTemp temp = new EvaluationAnswersTemp();
            temp.setUserId(userId);
            temp.setPaperId(paperId);
            temp.setAnswerDetails(answerDetails);
            answerTempService.save(temp);
        } else {
            // 更新记录
            exist.setAnswerDetails(answerDetails);
            answerTempService.updateById(exist);
        }
    }

    @Override
    public void restartEvaluation(String userId, String paperId) {
        answerTempService.remove(
                new LambdaUpdateWrapper<EvaluationAnswersTemp>()
                        .eq(EvaluationAnswersTemp::getUserId, userId)
                        .eq(EvaluationAnswersTemp::getPaperId, paperId)
        );
    }

}
