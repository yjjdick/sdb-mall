package io.sdb.enums;

import lombok.Getter;

/**
 * Created by yjjdick
 * 2017-06-11 17:12
 */
@Getter
public enum CampaignEnum implements IEnum {
    GROUPON(1, "团购"),
    ;

    private Integer code;

    private String message;

    CampaignEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
