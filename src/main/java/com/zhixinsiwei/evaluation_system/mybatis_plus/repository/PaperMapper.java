package com.zhixinsiwei.evaluation_system.mybatis_plus.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationPapers;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName PaperMapper.java
 * @Description TODO
 * @createTime 2023年08月26日 14:57:00
 */
@Mapper
//@Repository
public interface PaperMapper extends BaseMapper<EvaluationPapers> {

    //@Select("SELECT * FROM evaluation_papers")
    //List<EvaluationPapers> getAll();

}
