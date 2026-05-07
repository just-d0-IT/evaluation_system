package com.zhixinsiwei.evaluation_system.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zhixinsiwei.evaluation_system.common.ApiResponse;
import com.zhixinsiwei.evaluation_system.common.param.SubmitAnalysisRequestParm;
import com.zhixinsiwei.evaluation_system.service.AnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName AnalysisController.java
 * @Description 分析模块接口类
 * @createTime 2023年08月27日 10:54:00
 */
@Api(tags = "分析模块")
@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Resource
    private AnalysisService analysisService;

    @ApiOperation(value = "提交分析")
    @ApiOperationSupport(author = "ovo")
    @PostMapping("/submitAnalysis")
    public ApiResponse submitAnalysis(@RequestBody SubmitAnalysisRequestParm submitAnalysisRequestParm, HttpSession session) {
        String userId = (String) session.getAttribute("uid");
        analysisService.submitAnalysis(userId, submitAnalysisRequestParm.getPaperId(), submitAnalysisRequestParm.getAnswerDetails(), submitAnalysisRequestParm.getAge());
        return ApiResponse.success(null);
    }

    @ApiOperation(value = "获取分析结果列表")
    @ApiOperationSupport(author = "ovo")
    @GetMapping("/listAnalysisResult")
    public ApiResponse listAnalysisResult(HttpSession session) {
        return ApiResponse.success(analysisService.listAnalysisResult(session));
    }

    @ApiOperation(value = "查看报告")
    @ApiOperationSupport(author = "ovo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reportId", dataType = "string", value = "报告id", required = true, paramType = "path")
    })
    @GetMapping("/reportView/{reportId}")
    public void reportView(@PathVariable String reportId, HttpServletResponse response) {
        analysisService.reportView(reportId, response);
    }
}
