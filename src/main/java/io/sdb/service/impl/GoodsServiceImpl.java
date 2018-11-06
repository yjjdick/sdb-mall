package io.sdb.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import io.sdb.common.annotation.JFinalTx;
import io.sdb.common.entity.Filter;
import io.sdb.common.entity.Order;
import io.sdb.common.utils.R;
import io.sdb.form.GoodsData;
import io.sdb.form.GoodsParaData;
import io.sdb.form.GoodsSpecData;
import io.sdb.common.utils.PageUtils;
import io.sdb.common.utils.Query;
import io.sdb.dao.GoodsDao;
import io.sdb.enums.SnEnum;
import io.sdb.model.Goods;
import io.sdb.model.Product;
import io.sdb.service.GoodsService;
import io.sdb.service.SnService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl extends BaseServiceImpl<GoodsDao, Goods> implements GoodsService {

    @Autowired
    SnService snService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String searchKey = (String)params.get("searchKey");
        Query<Goods> query = new Query<Goods>(params);
        List<Filter> filters = new ArrayList<>();
        if (!StringUtils.isBlank(searchKey)) {
            Filter filter = new Filter();
            filter.setProperty("name");
            filter.setValue(searchKey);
            filter.setOperator(Filter.Operator.like);
            filters.add(filter);

            filter = new Filter();
            filter.setProperty("caption");
            filter.setValue(searchKey);
            filter.setOperator(Filter.Operator.like);
            filter.setWhereOpt(Filter.WhereOpt.or);
            filters.add(filter);

            filter = new Filter();
            filter.setProperty("sn");
            filter.setValue(searchKey);
            filter.setOperator(Filter.Operator.like);
            filter.setWhereOpt(Filter.WhereOpt.or);
            filters.add(filter);
        }

        Order order = new Order();
        order.setProperty("create_date");
        order.setDirection(Order.Direction.desc);
        Page<Goods> pr = this.paginate(query.getCurrPage(), query.getLimit(), filters, order);

        return new PageUtils(pr);
    }

    @Override
    @JFinalTx
    public Goods saveUpdateGoods(GoodsData goods, List<GoodsParaData> paraList, List<GoodsSpecData> specList) {
        Goods newGoods = new Goods();
        newGoods.setName(goods.getName());
        newGoods.setModel(goods.getModel());
        newGoods.setCaption(goods.getCaption());
        newGoods.setImage(goods.getProductImageUrlArr().get(0));
        newGoods.setProductImages(StringUtils.join(goods.getProductImageUrlArr(), ','));
        newGoods.setIntroduction(StringUtils.join(goods.getIntroImageUrlArr(), ','));
        newGoods.setIsDelivery(goods.isDelivery());
        newGoods.setIsMarketable(goods.isMarketable());
        newGoods.setUnit(goods.getUnit());
        newGoods.setWeight(goods.getWeight());
        newGoods.setParameterValues(JSONArray.toJSONString(paraList));
        newGoods.setProductCategoryId(goods.getCategoryId().get(goods.getCategoryId().size() - 1));
        newGoods.setPrice(goods.getPrice());

        if(goods.isHasSpec()){
            newGoods.setSpecificationItems(JSONObject.toJSONString(goods.getSpecificationGroup()));
        }

        String goodsSn = null;
        if (goods.getSn() == null) {
            goodsSn = snService.generate(SnEnum.GOODS);
            newGoods.setSn(goodsSn);
            newGoods.save();
        } else {
            newGoods.setSn(goods.getSn());
            newGoods.update();
        }

        if (goods.isHasSpec()) {
            int defaultIdx = goods.getProductDefault();
            int curidx = 0;
            List<Product> products = new ArrayList<>();
            for (GoodsSpecData goodsSpecData : specList) {
                Product product = new Product();
                boolean defaultFlag = false;
                if (curidx == defaultIdx) {
                    defaultFlag = true;
                }
                product.setIsDefault(defaultFlag);
                product.setPrice(goodsSpecData.getPrice());
                product.setCost(goodsSpecData.getCost());
                product.setEnable(goodsSpecData.getEnable());
                product.setSpecificationValues(goodsSpecData.getSpecIds());
                if (goodsSn != null) {
                    product.setGoodsSn(goodsSn);
                }

                if (goodsSpecData.getSn() == null) {
                    String productSn = snService.generate(SnEnum.PRODUCT);
                    product.setSn(productSn);
                }else {
                    product.setSn(goodsSpecData.getSn());
                }

                product.setStock(goodsSpecData.getStock());
                products.add(product);
                curidx ++;
            }

            if (goods.getSn() != null) {
                Db.batchUpdate(products, products.size());
            } else {
                Db.batchSave(products, products.size());
            }

            newGoods.setProductList(products);
        }else {
            Product product = new Product();
            product.setIsDefault(true);
            product.setPrice(goods.getPrice());
            product.setCost(goods.getCost());
            product.setStock(goods.getStock());
            product.setEnable(true);
            if (goods.getSn() != null) {
                product.setGoodsSn(goods.getSn());
                String productSn = snService.generate(SnEnum.PRODUCT);
                product.setSn(productSn);
                product.save();
            } else {
                GoodsSpecData goodsSpecData = specList.get(0);
                String productSn = goodsSpecData.getSn();
                product.setSn(productSn);
                product.update();
            }
            newGoods.setProductList(new ArrayList<Product>(){
                {add(product);}
            });
        }

        return newGoods;
    }

    @Override
    @JFinalTx
    public R batchShelf(String[] ids) {
        if(ids == null){
            return R.error("请选择商品");
        }
        Filter filter = new Filter();
        filter.setProperty("sn");
        filter.setOperator(Filter.Operator.in);
        filter.setValue(ids);
        List<Goods> goodsList = this.dao.findByFilter(filter);
        if(goodsList == null){
            return R.error();
        }
        for(Goods good : goodsList){
            good.setIsMarketable(true);
        }
        Db.batchUpdate(goodsList,goodsList.size());
        return R.ok();
    }

    @Override
    @JFinalTx
    public R batchObtained(String[] ids) {
        if(ids == null){
            return R.error("请选择商品");
        }
        Filter filter = new Filter();
        filter.setProperty("sn");
        filter.setOperator(Filter.Operator.in);
        filter.setValue(ids);
        List<Goods> goodsList = this.dao.findByFilter(filter);
        if(goodsList == null){
            return R.error();
        }
        for(Goods good : goodsList){
            good.setIsMarketable(false);
        }
        Db.batchUpdate(goodsList,goodsList.size());
        return R.ok();
    }
}
