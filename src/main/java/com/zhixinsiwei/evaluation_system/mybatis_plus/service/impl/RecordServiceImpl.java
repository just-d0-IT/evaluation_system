package com.zhixinsiwei.evaluation_system.mybatis_plus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationRecords;
import com.zhixinsiwei.evaluation_system.mybatis_plus.repository.RecordMapper;
import com.zhixinsiwei.evaluation_system.mybatis_plus.service.RecordService;
import org.springframework.stereotype.Service;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName RecordServiceImpl.java
 * @Description TODO
 * @createTime 2023年10月06日 10:22:00
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, EvaluationRecords> implements RecordService {
}
