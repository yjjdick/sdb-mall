package io.sdb.enums;

import lombok.Getter;

@Getter
public enum SnEnum {
    GOODS(1,"goods"),//货品sn
    PRODUCT(2,"product"),//商品sn
    ORDER_MASTER(3,"order_master"),//订单
    ORDER_DETAIL(4,"order_detail"),//订单详情
    USER(5,"user"),//微信用户
    ;

    private Integer code;
    private String message;
    SnEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
