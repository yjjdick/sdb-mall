package io.sdb.service.impl;

import io.sdb.common.entity.Filter;
import io.sdb.common.utils.R;
import io.sdb.dao.SpecificationDao;
import io.sdb.model.Specification;
import io.sdb.service.SpecificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationServiceImpl extends BaseServiceImpl<SpecificationDao, Specification> implements SpecificationService {

    @Override
    public List<Specification> queryListByCategoryId(Long categoryId) {
        Specification specification = new Specification();
        specification.setCategoryId(categoryId);
        return this.dao.findByModel(specification);
    }

    @Override
    public List<Specification> queryListByOrder() {
        return this.dao.findAll();
    }

    @Override
    public R delete(Long id) {
         Filter filter = new Filter();
         filter.setProperty("parent_id");
         filter.setOperator(Filter.Operator.eq);
         filter.setValue(id);
         List<Specification> specificationList = this.dao.findByFilter(filter);
         if(specificationList!=null && specificationList.size()>0){
             return R.error("请先删除下级规格");
         }
         boolean bool = this.dao.deleteById(id);
         if(bool){
             return R.ok();
         }
         return R.error();
    }

    @Override
    public R save(Specification specification) {
        boolean bool =  specification.save();
        if(bool){
            return R.ok();
        }
        return R.error();
    }
}
