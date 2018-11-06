package io.sdb.service.impl;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import io.sdb.common.annotation.JFinalTx;
import io.sdb.common.exception.RRException;
import io.sdb.dao.ProductDao;
import io.sdb.dto.CartDTO;
import io.sdb.dto.ProductDTO;
import io.sdb.enums.ResultEnum;
import io.sdb.model.Goods;
import io.sdb.model.Product;
import io.sdb.common.utils.RecordUtils;
import io.sdb.service.GoodsService;
import io.sdb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl extends BaseServiceImpl<ProductDao, Product> implements ProductService {

    @Autowired
    GoodsService goodsService;

    @Override
    @JFinalTx
    public void decreaseStock(List<CartDTO> cartDTOList) {
        //TODO 后续需要优化，处理并发
        for (CartDTO cartDTO: cartDTOList) {
            Product productInfo = this.findById(cartDTO.getProductId());
            if (productInfo == null) {
                throw new RRException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            Integer result = productInfo.getStock() - cartDTO.getQuantity();
            if (result < 0) {
                throw new RRException(ResultEnum.PRODUCT_STOCK_ERROR);
            }

            productInfo.setStock(result);

            productInfo.update();
        }
    }

    @Override
    public void creaseStock(List<CartDTO> cartDTOList) {
        //TODO 后续需要优化，处理并发
        for (CartDTO cartDTO: cartDTOList) {
            Product productInfo = this.findById(cartDTO.getProductId());
            if (productInfo == null) {
                throw new RRException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            Integer result = productInfo.getStock() + cartDTO.getQuantity();
            productInfo.setStock(result);
            productInfo.update();
        }
    }

    @Override
    public Goods getGoods(String productId) {
        Product product = this.findById(productId);
        Goods goods = goodsService.findById(product.getGoodsSn());
        return goods;
    }

    @Override
    public List<ProductDTO> listDetailByProductIds(String productIds) {
        String[] productIdArr = productIds.split(",");
        SqlPara sqlPara = Db.getSqlPara("product.listDetailByProductIds", Kv.by("productIds", productIdArr));
        List<Record> recordList = Db.find(sqlPara);
        List<ProductDTO> productDTOList = RecordUtils.converModel(recordList, ProductDTO.class);

        return productDTOList;
    }

}
