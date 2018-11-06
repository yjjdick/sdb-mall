package io.sdb.dto;

import com.jfinal.plugin.activerecord.Model;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 购物车
 * Created by yjjdick
 * 2017-06-11 19:37
 */
@Data
public class ProductDTO {
    String name;
    String model;
    String goodsSn;
    String caption;
    String image;
    String specificationValues;
    Integer stock;
    String sn;
    BigDecimal price;
    Integer enable;
    Integer quantity;
    Date createDate;
}
