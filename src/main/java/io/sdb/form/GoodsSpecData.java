package io.sdb.form;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GoodsSpecData {
    private String sn;
    private BigDecimal cost;
    private BigDecimal price;
    private Integer stock;
    private Boolean enable;
    private String specIds;
}
