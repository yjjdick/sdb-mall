package io.sdb.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.sdb.serializer.Date2LongSerializer;
import lombok.Data;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: quendi
 * Date: 2018/6/14
 */
@Data
public class NewsVO {
    Long id;
    String title;
    Integer head;
    String img;
    String content;
    @JsonSerialize(using = Date2LongSerializer.class)
    Date createDate;
    @JsonSerialize(using = Date2LongSerializer.class)
    Date updateDate;
    Integer enable;
}
