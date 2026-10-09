package com.spzx.payment.service;

public interface IAlipayService {

    /**
     * 调用支付宝获取支付表单
     * @param orderNo
     * @return
     */
    String submitAlipay(String orderNo);
}
