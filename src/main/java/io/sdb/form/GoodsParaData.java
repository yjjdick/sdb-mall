package io.sdb.form;

import lombok.Data;

import java.util.List;

@Data
public class GoodsParaData {
    private String name;
    private List<GoodsParaEntryData> entries;
}
