package io.sdb.form;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GoodsSpecGroupData {
    private String specificationName;
    private List<String> specificationEntries;
}