package io.sdb.enums;

import lombok.Getter;

@Getter
public enum GeneralEnum {
    TRUE(1,"true"),
    FALSE(0,"false"),
    ;

    private Integer code;
    private String message;
    GeneralEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
