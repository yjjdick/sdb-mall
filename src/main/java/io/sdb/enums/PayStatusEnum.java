package io.sdb.enums;

import lombok.Getter;

/**
 * Created by yjjdick
 * 2017-06-11 17:16
 */
@Getter
public enum PayStatusEnum implements IEnum {

    WAIT(0, "等待支付"),
    SUCCESS(1, "支付成功"),
    REFUND(2, "退款"),

    ;

    private Integer code;

    private String message;

    PayStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
