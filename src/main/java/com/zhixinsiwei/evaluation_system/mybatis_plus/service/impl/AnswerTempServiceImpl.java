package com.zhixinsiwei.evaluation_system.mybatis_plus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationAnswersTemp;
import com.zhixinsiwei.evaluation_system.mybatis_plus.service.AnswerTempService;
import com.zhixinsiwei.evaluation_system.mybatis_plus.repository.AnswerTempMapper;
import org.springframework.stereotype.Service;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName AnswerServiceImpl.java
 * @Description TODO
 * @createTime 2023年10月07日 21:04:00
 */
@Service
public class AnswerTempServiceImpl extends ServiceImpl<AnswerTempMapper, EvaluationAnswersTemp> implements AnswerTempService {
}
