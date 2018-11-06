package io.sdb.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    SYSTEM_SUCCESS(0,"成功"),//成功
    SYSTM_ERROR(500, "系统错误"),//系统错误
    PARAM_ERROR(1, "参数不正确"),//参数不正确
    APPID_NULL(2, "APPID缺失"),//APPID缺失
    MA_LOGIN_ERROR(1001, "小程序登录失败"),//小程序登录失败
    VOLUNTEER_NOT_FOUND(1002, "未找到志愿者"),//未找到志愿者
    VOLUNTEER_UPLOAD_FILE_BIG(1003, "上传文件太大"),//上传文件太大
    RECVER_CANNOT_NULL(1004, "收件人不能为空"),//收件人不能为空
    CART_CANNOT_NULL(1005, "购物车不能为空"),//购物车不能为空

    PRODUCT_NOT_EXIST(2010, "商品不存在"),

    PRODUCT_STOCK_ERROR(2011, "商品库存不正确"),

    ORDER_NOT_EXIST(2012, "订单不存在"),

    ORDERDETAIL_NOT_EXIST(2013, "订单详情不存在"),

    ORDER_STATUS_ERROR(2014, "订单状态不正确"),

    ORDER_UPDATE_FAIL(2015, "订单更新失败"),

    ORDER_DETAIL_EMPTY(2016, "订单详情为空"),

    ORDER_PAY_STATUS_ERROR(2017, "订单支付状态不正确"),

    CART_EMPTY(2018, "购物车为空"),

    ORDER_OWNER_ERROR(2019, "该订单不属于当前用户"),

    WECHAT_MP_ERROR(2020, "微信公众账号方面错误"),

    WXPAY_NOTIFY_MONEY_VERIFY_ERROR(2021, "微信支付异步通知金额校验不通过"),

    ORDER_CANCEL_SUCCESS(2022, "订单取消成功"),

    ORDER_FINISH_SUCCESS(2023, "订单完结成功"),

    PRODUCT_STATUS_ERROR(2024, "商品状态不正确"),
    ;

    private Integer code;
    private String message;
    ResultEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
