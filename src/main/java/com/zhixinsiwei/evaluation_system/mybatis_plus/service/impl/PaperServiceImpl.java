package com.zhixinsiwei.evaluation_system.mybatis_plus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationPapers;
import com.zhixinsiwei.evaluation_system.mybatis_plus.service.PaperService;
import com.zhixinsiwei.evaluation_system.mybatis_plus.repository.PaperMapper;
import org.springframework.stereotype.Service;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName PaperServiceImpl.java
 * @Description TODO
 * @createTime 2023年10月06日 10:40:00
 */
@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, EvaluationPapers> implements PaperService {
}
