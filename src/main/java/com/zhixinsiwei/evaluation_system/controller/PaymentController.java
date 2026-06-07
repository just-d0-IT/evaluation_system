package com.zhixinsiwei.evaluation_system.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.zhixinsiwei.evaluation_system.common.ApiResponse;
import com.zhixinsiwei.evaluation_system.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author ovo
 * @version 1.0.0
 * @ClassName PaymentController.java
 * @Description 支付模块接口类
 * @createTime 2023年08月27日 10:55:00
 */
@Api(tags = "支付模块")
@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    private final String appId = "wxc33bca5ecd103de2"; // 替换为微信公众号的 appId
    private final String appSecret = "dfcc02e41d368218e3d512ef36a72593"; // 替换为微信公众号的 appSecret

    @GetMapping(value = "/getOpenId")
    public ResponseEntity<?> getOpenId(@RequestParam("code") String code) {
        try {
            // 请求微信服务器获取 access_token 和 openId
            String url = String.format(
                    "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                    appId, appSecret, code
            );

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);

            log.info("code is {},response is {}", code, response);

            // 解析微信返回的 JSON 数据
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response);

            if (jsonNode.has("openid")) {
                String openId = jsonNode.get("openid").asText();
                return ResponseEntity.ok(openId);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to get openId");
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while fetching openId");
        }
    }

    @ApiOperation(value = "获取支付状态")
    @ApiOperationSupport(author = "ovo")
    @GetMapping("/getPaymentStatus")
    public ApiResponse getPaymentStatus() {
        return null;
    }

    @ApiOperation(value = "预支付")
    @ApiOperationSupport(author = "ovo")
    @PostMapping("/prepay")
    public ApiResponse prepay(@RequestBody Map<String, Object> requestBody) {
        // 调用业务逻辑服务
        return paymentService.prepay((String) requestBody.get("openId"), (String) requestBody.get("amount"));
    }

    @PostMapping("/notify")
    public ResponseEntity<?> callBack(@RequestBody String body, HttpServletRequest request) {
        int result = paymentService.handleNotify(
                body,
                request.getHeader("Wechatpay-Serial"),
                request.getHeader("Wechatpay-Nonce"),
                request.getHeader("Wechatpay-Signature"),
                request.getHeader("Wechatpay-Timestamp")
        );
        switch (result) {
            case 1:
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            case 2:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            default:
                return ResponseEntity.ok().build();
        }
    }

    @ApiOperation(value = "微信支付")
    @ApiOperationSupport(author = "ovo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recordId", dataType = "string", value = "记录id", required = true, paramType = "path"),
    })
    @GetMapping("/wxPay/{recordId}")
    public ApiResponse wxPay(@PathVariable String recordId, HttpSession session) {
        // 调用业务逻辑服务
        return paymentService.wxPay(recordId,session);
    }
}
