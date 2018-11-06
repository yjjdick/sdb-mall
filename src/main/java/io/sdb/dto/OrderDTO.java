package io.sdb.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.sdb.common.utils.EnumUtil;
import io.sdb.enums.OrderStatusEnum;
import io.sdb.enums.PayStatusEnum;
import io.sdb.model.OrderDetail;
import io.sdb.serializer.Date2LongSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by yjjdick
 * 2017-06-11 18:30
 */
@Data
//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {

    /** 订单id. */
    private String orderId;

    /** 买家名字. */
    private String buyerName;

    /** 买家手机号. */
    private String buyerPhone;

    /** 买家地址. */
    private String buyerAddress;

    /** 买家微信Openid. */
    private String buyerOpenid;

    /** 买家微信Openid. */
    private String buyerId;

    /** 订单总金额. */
    private BigDecimal orderAmount;

    /** 订单状态, 默认为0新下单. */
    private Integer orderStatus;

    /** 支付状态, 默认为0未支付. */
    private Integer payStatus;

    /** 买家备注 */
    private String remark;

    /** 是否需要开发 */
    private Integer needInvoice;

    private Integer invoiceType;
    private String title;
    private String taxNumber;
    private String companyAddress;
    private String telephone;
    private String bankName;
    private String bankAccount;
    private String payTradeNo;
    private String refundTradeNo;

    private String trackingNumber;
    private String deliveryCode;

    /** 创建时间. */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    /** 更新时间. */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    List<OrderDetail> orderDetailList;

    @JsonIgnore
    public OrderStatusEnum getOrderStatusEnum() {
        return EnumUtil.getByCode(orderStatus, OrderStatusEnum.class);
    }

}
