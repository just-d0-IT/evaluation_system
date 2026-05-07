package com.zhixinsiwei.evaluation_system.mybatis_plus.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationQuestions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName QuestionMapper.java
 * @Description TODO
 * @createTime 2023年08月26日 18:12:00
 */
@Mapper
public interface QuestionMapper extends BaseMapper<EvaluationQuestions> {

    @Select("SELECT * FROM evaluation_questions WHERE paper_id = #{paperId}")
    EvaluationQuestions queryQuestionByPaperId(int paperId);

    @Select("SELECT * FROM evaluation_questions")
    List<EvaluationQuestions> queryAll();

}
