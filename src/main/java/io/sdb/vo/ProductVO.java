package io.sdb.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jfinal.plugin.activerecord.Model;
import io.sdb.serializer.Date2LongSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 购物车
 * Created by yjjdick
 * 2017-06-11 19:37
 */
@Data
public class ProductVO{
    String name;
    String caption;
    String image;
    String specificationValues;
    Integer stock;
    String sn;
    BigDecimal price;
    Integer enable;
    @JsonSerialize(using = Date2LongSerializer.class)
    Date createDate;
}
