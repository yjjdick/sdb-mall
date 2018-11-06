package io.sdb.service.impl;

import io.sdb.common.utils.R;
import io.sdb.dao.ProductCategoryDao;
import io.sdb.model.Goods;
import io.sdb.model.ProductCategory;
import io.sdb.model.Specification;
import io.sdb.service.GoodsService;
import io.sdb.service.ProductCategoryService;
import io.sdb.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryServiceImpl extends BaseServiceImpl<ProductCategoryDao, ProductCategory> implements ProductCategoryService {

    @Autowired
    private SpecificationService specificationService;

    @Autowired
    private GoodsService goodsService;

    @Override
    public List<ProductCategory> queryListOrder() {
        return this.dao.queryListOrder();
    }

    @Override
    public List<ProductCategory> queryListByParentId(Long parentId) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setParentId(parentId);
        return this.dao.findByModel(productCategory);
    }

    @Override
    public R delete(Long productCategoryId) {
        //判断是否有子类目
        List<ProductCategory> productCategoryList = queryListByParentId(productCategoryId);
        if(productCategoryList!=null && productCategoryList.size() > 0) {
            return R.error("请先删除子类目");
        }
        //判断是否有规格
        List<Specification> specificationList = specificationService.queryListByCategoryId(productCategoryId);
        if(specificationList!=null && specificationList.size()>0){
            return R.error("请先删除规格");
        }

        //判断是否有商品
        Goods goods = new Goods();
        goods.setProductCategoryId(productCategoryId);
        List<Goods> goodsList = goodsService.findByModel(goods);
        if(goodsList!=null && goodsList.size()>0){
            return R.error("请删除商品");
        }
        boolean isOk = this.dao.deleteById(productCategoryId);
        if(isOk){
            return R.ok();
        }else {
            return R.error("删除类目失败");
        }
    }

    @Override
    public R save(ProductCategory productCategory) {
        productCategory.save();
        return R.ok();
    }
}
