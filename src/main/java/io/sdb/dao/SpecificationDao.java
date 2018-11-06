package io.sdb.dao;

import io.sdb.model.ProductCategory;
import io.sdb.model.Specification;
import org.springframework.stereotype.Component;

@Component
public class SpecificationDao extends BaseDao<Specification> {
    public SpecificationDao() {
        super(Specification.class);
    }
}
