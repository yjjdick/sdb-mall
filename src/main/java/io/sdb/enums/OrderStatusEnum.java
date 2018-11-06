package io.sdb.enums;

import lombok.Getter;

/**
 * Created by yjjdick
 * 2017-06-11 17:12
 */
@Getter
public enum OrderStatusEnum implements IEnum {
    NEW(0, "待发货"),
    SHIPPING(1, "已发货"),
    FINISHED(2, "已完成"),
    CANCEL(3, "已取消"),
    ;

    private Integer code;

    private String message;

    OrderStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
