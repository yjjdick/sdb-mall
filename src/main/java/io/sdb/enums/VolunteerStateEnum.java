package io.sdb.enums;

import lombok.Getter;

@Getter
public enum VolunteerStateEnum implements IEnum {
    PENDING(0,"VOLUNTEER_STATE_PENDING"),//待审核
    PASS(1,"VOLUNTEER_STATE_PASS"),//通过
    REJUECT(2,"VOLUNTEER_STATE_REJUECT"),//拒绝
    ;

    private Integer code;
    private String message;
    VolunteerStateEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
