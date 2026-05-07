package com.zhixinsiwei.evaluation_system.mybatis_plus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhixinsiwei.evaluation_system.common.entity.EvaluationUsers;
import com.zhixinsiwei.evaluation_system.mybatis_plus.repository.UserEntityMapper;
import com.zhixinsiwei.evaluation_system.mybatis_plus.service.UserEntityService;
import org.springframework.stereotype.Service;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName UserEntityServiceImpl.java
 * @Description TODO
 * @createTime 2026年01月31日 23:49:00
 */
@Service
public class UserEntityServiceImpl extends ServiceImpl<UserEntityMapper, EvaluationUsers> implements UserEntityService {
}
