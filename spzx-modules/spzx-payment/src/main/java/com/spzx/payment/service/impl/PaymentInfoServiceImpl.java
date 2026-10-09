package com.spzx.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spzx.common.core.constant.SecurityConstants;
import com.spzx.common.core.context.SecurityContextHolder;
import com.spzx.common.core.domain.R;
import com.spzx.common.core.exception.ServiceException;
import com.spzx.common.rabbit.service.RabbitService;
import com.spzx.order.api.RemoteOrderInfoService;
import com.spzx.order.api.domain.OrderInfo;
import com.spzx.order.api.domain.OrderItem;
import com.spzx.payment.domain.PaymentInfo;
import com.spzx.payment.mapper.PaymentInfoMapper;
import com.spzx.payment.service.IPaymentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 付款信息Service业务层处理
 */
@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements IPaymentInfoService {

    @Autowired
    private RemoteOrderInfoService remoteOrderInfoService;

    @Autowired
    private RabbitService rabbitService;

    @Override
    public PaymentInfo addOrGetPaymentInfo(String orderNo) {
        //根据订单号查询支付记录
        PaymentInfo paymentInfo = baseMapper.selectOne(new LambdaQueryWrapper<PaymentInfo>().eq(PaymentInfo::getOrderNo, orderNo).eq(PaymentInfo::getPaymentStatus,0));
        if(null != paymentInfo){
            return paymentInfo;
        }
        //创建PaymentInfo对象
        paymentInfo = new PaymentInfo();
        paymentInfo.setOrderNo(orderNo);
        //设置支付状态
        paymentInfo.setPaymentStatus("0");
        //设置支付方式：1是微信，2是支付宝
        paymentInfo.setPayType(2);
        //设置用户id
        paymentInfo.setUserId(SecurityContextHolder.getUserId());
        //远程调用订单微服务根据订单号查询订单信息
        R<OrderInfo> orderInfoByOrderNoR = remoteOrderInfoService.getOrderInfoByOrderNo(orderNo, SecurityConstants.INNER);
        if(R.FAIL == orderInfoByOrderNoR.getCode()){
            throw new ServiceException(orderInfoByOrderNoR.getMsg());
        }
        //获取订单信息
        OrderInfo orderInfo = orderInfoByOrderNoR.getData();
        //设置支付的总金额
        paymentInfo.setAmount(orderInfo.getTotalAmount());
        StringBuffer sb = new StringBuffer();
        //获取所有的订单项
        List<OrderItem> orderItemList = orderInfo.getOrderItemList();
        orderItemList.forEach(orderItem -> {
            if(sb.length()>0){
                //添加一个连接符
                sb.append("_");
            }
            sb.append(orderItem.getSkuName());
        });
        //设置支付内容
        paymentInfo.setContent(sb.toString());
        //插入支付记录
        baseMapper.insert(paymentInfo);
        return paymentInfo;
    }
}