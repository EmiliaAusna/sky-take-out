package com.sky.utils;

import com.alibaba.fastjson.JSONObject;
import com.sky.properties.WeChatProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

@Component
public class WeChatPayUtil {

    @Resource
    private WeChatProperties weChatProperties;

    /**
     * 模拟微信支付下单，不再请求微信服务器
     * @param orderNo 商户订单号
     * @param money 支付金额
     * @param desc 订单描述
     * @param openid 用户openid（模拟可忽略）
     * @return 模拟预付单JSON，格式和微信返回完全一致
     */
    public JSONObject pay(String orderNo, java.math.BigDecimal money, String desc, String openid) {
        JSONObject result = new JSONObject();
        // 模拟微信预付ID
        String prepayId = "mock_prepay_" + UUID.randomUUID().toString().replace("-", "");
        // 模拟package字段
        result.put("package", "prepay_id=" + prepayId);
        // 模拟时间戳、随机串、签名（假签名，格式对齐前端要求）
        result.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        result.put("nonceStr", UUID.randomUUID().toString().substring(0, 16));
        result.put("signType", "RSA");
        result.put("paySign", "mock_sign_" + UUID.randomUUID().toString().substring(0, 32));

        // 模拟订单已支付异常标识（和原业务判断逻辑兼容）
        // result.put("code", "ORDERPAID");

        return result;
    }

    /**
     * 【新增】模拟微信退款接口（商家拒单使用，不走真实微信请求）
     * @param outTradeNo 商户原支付订单号
     * @param outRefundNo 商户退款单号（全局唯一）
     * @param totalFee 原订单总金额
     * @param refundFee 本次退款金额
     * @return 模拟微信退款返回JSON
     */
    public JSONObject refund(String outTradeNo, String outRefundNo,
                             java.math.BigDecimal totalFee, java.math.BigDecimal refundFee) {
        JSONObject resp = new JSONObject();
        // 模拟退款成功状态
        resp.put("refund_status", "SUCCESS");
        resp.put("out_trade_no", outTradeNo);
        resp.put("out_refund_no", outRefundNo);
        resp.put("total_fee", totalFee);
        resp.put("refund_fee", refundFee);
        resp.put("refund_id", "mock_refund_" + UUID.randomUUID().toString().replace("-", ""));
        resp.put("message", "模拟退款成功，未调用微信真实接口");

        return resp;
    }
}