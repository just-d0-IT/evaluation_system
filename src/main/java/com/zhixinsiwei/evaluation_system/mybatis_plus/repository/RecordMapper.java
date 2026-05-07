package com.zhixinsiwei.evaluation_system.mybatis_plus.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationRecords;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName RecordMapper.java
 * @Description TODO
 * @createTime 2023年08月27日 10:49:00
 */
@Mapper
public interface RecordMapper extends BaseMapper<EvaluationRecords> {

    @Select("SELECT * FROM evaluation_records WHERE paper_id = #{paperId} AND user_id = #{userId}")
    void queryEvaluationStatus();


}
