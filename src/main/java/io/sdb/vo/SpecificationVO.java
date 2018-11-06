package io.sdb.vo;

import io.sdb.common.entity.Node;
import io.sdb.model.ProductCategory;
import io.sdb.model.Specification;
import lombok.Data;

@Data
public class SpecificationVO extends Node<Specification> {
    Long value;
    String label;

    public SpecificationVO(Specification specification) {
        super(specification.getId(), specification.getParentId());
        this.value = specification.getId();
        this.label = specification.getName();
    }
}
