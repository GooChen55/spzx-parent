package com.spzx.order.receiver;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rabbitmq.client.Channel;
import com.spzx.common.rabbit.constant.MqConst;
import com.spzx.common.rabbit.service.RabbitService;
import com.spzx.order.api.domain.OrderInfo;
import com.spzx.order.service.IOrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OrderReceiver {

    @Autowired
    private IOrderInfoService orderInfoService;

    @Autowired
    private RabbitService rabbitService;


    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = MqConst.QUEUE_CANCEL_ORDER),
            exchange = @Exchange(value = MqConst.EXCHANGE_CANCEL_ORDER,
                    type = "x-delayed-message",
                    arguments = @Argument(name = "x-delayed-type", value = "direct")),
            key = MqConst.ROUTING_CANCEL_ORDER))
    public void receiverMessage(Long orderId,Message message, Channel channel) {
        //获取消息的标识
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //执行业务
            //获取消息内容
            //将订单中为未支付的订单的状态修改为已取消
//            orderInfoService.update(new LambdaUpdateWrapper<OrderInfo>().eq(OrderInfo::getId,orderId).eq(OrderInfo::getOrderStatus,0).set(OrderInfo::getOrderStatus,-1));
            //调用取消订单的方法
            orderInfoService.cancelOrder(orderId,2);
            //确认消息
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            try {
                //退回消息
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

}