package io.sdb.enums;

import lombok.Getter;

/**
 * Created by yjjdick
 * 2017-06-11 17:12
 */
@Getter
public enum GrouponStatusEnum implements IEnum {
    PENDING(0, "等待开团"),
    OPEN(1, "已开团"),
    CANCEL(2, "取消"),
    ;

    private Integer code;

    private String message;

    GrouponStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
