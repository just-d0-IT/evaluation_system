package com.zhixinsiwei.evaluation_system.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zhixinsiwei.evaluation_system.common.ApiResponse;
import com.zhixinsiwei.evaluation_system.common.param.SaveAnswerRequestParam;
import com.zhixinsiwei.evaluation_system.common.param.UserEvaluationRequestParam;
import com.zhixinsiwei.evaluation_system.service.EvaluationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName EvaluationController.java
 * @Description 测评模块接口类
 * @createTime 2023年08月26日 16:44:00
 */
@Api(tags = "测评模块")
@RestController
@RequestMapping("/evaluation")
public class EvaluationController {

    @Resource
    private EvaluationService evaluationService;

    @ApiOperation(value = "获取所有测评试卷信息")
    @ApiOperationSupport(author = "ovo")
    @GetMapping("/queryAllPapers")
    public ApiResponse queryAllPapers() {
        return ApiResponse.success(evaluationService.getAllPapers());
    }

    @ApiOperation(value = "题目查询")
    @ApiOperationSupport(author = "ovo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paperId", dataType = "string", value = "测评试卷编号", required = true, paramType = "path"),
            @ApiImplicitParam(name = "questionId", dataType = "string", value = "问题编号", required = true, paramType = "path")
    })
    @GetMapping("/queryQuestion/{paperId}/{questionId}")
    public ApiResponse queryQuestion(@PathVariable String paperId, @PathVariable String questionId) {
        return ApiResponse.success(evaluationService.queryQuestion(paperId, questionId));
    }

    @ApiOperation(value = "保存临时记录")
    @ApiOperationSupport(author = "ovo")
    @PostMapping("/saveTempAnswer")
    public ApiResponse saveTempAnswer(@RequestBody SaveAnswerRequestParam saveAnswerRequestParam, HttpSession session) {
        String userId = (String) session.getAttribute("uid");
        evaluationService.saveTempAnswer(userId, saveAnswerRequestParam.getPaperId(), saveAnswerRequestParam.getAnswerDetails());
        return ApiResponse.success(null);
    }

    @ApiOperation(value = "获取临时答题详情")
    @ApiOperationSupport(author = "ovo")
    @PostMapping("/getTempAnswer")
    public ApiResponse getTempAnswer(@RequestBody UserEvaluationRequestParam userEvaluationRequestParam, HttpSession session) {
        String userId = (String) session.getAttribute("uid");
        return ApiResponse.success(evaluationService.getTempAnswer(userId, userEvaluationRequestParam.getPaperId()));
    }

    @ApiOperation(value = "重新测评")
    @ApiOperationSupport(author = "ovo")
    @PostMapping("/restartEvaluation")
    public ApiResponse restartEvaluation(@RequestBody UserEvaluationRequestParam userEvaluationRequestParam, HttpSession session) {
        String userId = (String) session.getAttribute("uid");
        evaluationService.restartEvaluation(userId, userEvaluationRequestParam.getPaperId());
        return ApiResponse.success(null);
    }

}
