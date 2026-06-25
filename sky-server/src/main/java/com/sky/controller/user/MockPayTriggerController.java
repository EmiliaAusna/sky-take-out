package com.sky.controller;

import com.sky.annotation.IgnoreToken;
import com.sky.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/mock/pay")
@Slf4j
public class MockPayTriggerController {

    @Resource
    private OrderService orderService;

    /**
     * 模拟支付成功，手动触发，替代微信异步回调
     * 访问地址：http://localhost:8080/mock/pay/success?outTradeNo=你的订单号
     */
    @IgnoreToken
    @GetMapping("/success")
    public String mockPaySuccess(@RequestParam String outTradeNo) {
        log.info("模拟触发支付回调，订单号：{}", outTradeNo);
        orderService.paySuccess(outTradeNo);
        return "订单" + outTradeNo + " 模拟支付成功，状态已更新";
    }
}