package io.sdb.service.impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.lly835.bestpay.utils.JsonUtil;
import io.sdb.common.exception.RRException;
import io.sdb.config.WechatPayConfig;
import io.sdb.dto.OrderDTO;
import io.sdb.enums.ResultEnum;
import io.sdb.model.OrderMaster;
import io.sdb.service.OrderMasterService;
import io.sdb.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    OrderMasterService orderService;

    @Autowired
    BestPayService bestPayService;

    @Override
    public PayResponse create(OrderDTO orderDTO) {

        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(orderDTO.getBuyerOpenid());
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderDTO.getOrderId());
        payRequest.setOrderName("sdb");
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        PayResponse payResponse = bestPayService.pay(payRequest);
        log.info("[微信支付] payResponse = {}", payResponse);
        return payResponse;
    }

    @Override
    public OrderMaster refund(String orderId) {

        OrderMaster orderMaster = orderService.findById(orderId);
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setOrderAmount(orderMaster.getOrderAmount().doubleValue());
        refundRequest.setOrderId(orderMaster.getOrderId());
        refundRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        RefundResponse refundResponse = bestPayService.refund(refundRequest);
        orderMaster.setRefundTradeNo(refundResponse.getOutRefundNo());
        orderMaster.setUpdateDate(new Date());
        orderMaster.update();
        return orderMaster;
    }

    @Override
    public PayResponse notify(String notifyData) {
        //1. 验证签名
        //2. 支付的状态
        //3. 支付金额
        //4. 支付人(下单人 == 支付人)

        PayResponse payResponse = bestPayService.asyncNotify(notifyData);

        log.info("【微信支付】异步通知, payResponse={}", JsonUtil.toJson(payResponse));

        //查询订单
        OrderMaster orderMaster = orderService.findById(payResponse.getOrderId());

        //判断订单是否存在
        if (orderMaster == null) {
            log.error("【微信支付】异步通知, 订单不存在, orderId={}", payResponse.getOrderId());
            throw new RRException(ResultEnum.ORDER_NOT_EXIST);
        }

        //判断金额是否一致(0.10   0.1)
        if (payResponse.getOrderAmount() != orderMaster.getOrderAmount().doubleValue()) {
            log.error("【微信支付】异步通知, 订单金额不一致, orderId={}, 微信通知金额={}, 系统金额={}",
                    payResponse.getOrderId(),
                    payResponse.getOrderAmount(),
                    orderMaster.getOrderAmount());
            throw new RRException(ResultEnum.WXPAY_NOTIFY_MONEY_VERIFY_ERROR);
        }

        orderMaster.setPayTradeNo(payResponse.getOutTradeNo());

        //修改订单的支付状态
        orderService.paid(orderMaster);

        return payResponse;
    }
}
