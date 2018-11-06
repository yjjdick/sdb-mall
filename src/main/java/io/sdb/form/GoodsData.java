package io.sdb.form;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GoodsData {
    private String sn;
    private String name;
    private String model;
    private String caption;
    private String unit;
    private boolean hasSpec;
    private boolean delivery;
    private boolean marketable;
    private Integer productDefault;
    private List<Long> categoryId;
    private float weight;
    private Integer stock;
    private BigDecimal price;
    private BigDecimal cost;
    private List<String> introImageUrlArr;
    private List<String> productImageUrlArr;
    private List<GoodsSpecGroupData> specificationGroup;

}
