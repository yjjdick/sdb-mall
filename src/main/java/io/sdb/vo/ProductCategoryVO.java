package io.sdb.vo;

import io.sdb.common.entity.Node;
import io.sdb.model.Area;
import io.sdb.model.ProductCategory;
import lombok.Data;

@Data
public class ProductCategoryVO extends Node<ProductCategory> {
    Long value;
    String label;
    String treePath;

    public ProductCategoryVO(ProductCategory productCategory) {
        super(productCategory.getId(), productCategory.getParentId());
        this.value = productCategory.getId();
        this.label = productCategory.getName();
        this.treePath = productCategory.getTreePath();
    }
}
