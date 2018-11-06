package io.sdb.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.sdb.serializer.Date2LongSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class VolunteerInfoVO {
    String name;
    String idCard;
    @JsonSerialize(using = Date2LongSerializer.class)
    Date createDate;
    String id;
    Integer status;
}
