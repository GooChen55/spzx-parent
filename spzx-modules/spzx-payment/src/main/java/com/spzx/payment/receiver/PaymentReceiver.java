package com.spzx.payment.receiver;

import com.spzx.payment.service.IAlipayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentReceiver {

    @Autowired
    private IAlipayService alipayService;

}