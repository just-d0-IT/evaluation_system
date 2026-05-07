package com.zhixinsiwei.evaluation_system.mybatis_plus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationQuestions;
import com.zhixinsiwei.evaluation_system.mybatis_plus.service.QuestionService;
import com.zhixinsiwei.evaluation_system.mybatis_plus.repository.QuestionMapper;
import org.springframework.stereotype.Service;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName QuestionServiceImpl.java
 * @Description TODO
 * @createTime 2023年10月06日 10:38:00
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, EvaluationQuestions> implements QuestionService {
}
