package io.sdb.service.impl;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import io.sdb.common.entity.Filter;
import io.sdb.dao.CartDao;
import io.sdb.dto.CartDTO;
import io.sdb.model.Cart;
import io.sdb.model.Product;
import io.sdb.common.utils.RecordUtils;
import io.sdb.service.CartService;
import io.sdb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl extends BaseServiceImpl<CartDao, Cart> implements CartService {

    @Autowired
    ProductService productService;

    @Override
    public Boolean saveOrUpdate(String userId, String productId, Integer quantity) {
        Cart qcart = new Cart();
        qcart.setProductId(productId);
        Cart cart = this.findFirstByModel(qcart);
        if (cart == null) {
            cart = new Cart();
            cart.setQuantity(quantity);
            cart.setProductId(productId);
            cart.setUserId(userId);
            cart.save();
        }else {
            cart.setQuantity(cart.getQuantity() + quantity);
            cart.setUpdateDate(new Date());
            cart.update();
        }
        return true;
    }

    @Override
    public Boolean remove(String userId, String cartIds) {
        String[] cartIdArr = cartIds.split(",");
        return this.deleteBatch(cartIdArr);
    }

    @Override
    public List<Product> getCartProduct(List<Long> cartItemIds) {
        Filter filter = new Filter();
        filter.setProperty("product_id");
        filter.setValue(cartItemIds);
        filter.setOperator(Filter.Operator.in);
        List<Cart> cartList = this.findByFilter(filter);

        List<String> productIds = cartList.stream().map(item -> {
            return item.getProductId();
        }).collect(Collectors.toList());
        filter = new Filter();
        filter.setProperty("product_id");
        filter.setValue(productIds);
        filter.setOperator(Filter.Operator.in);
        List<Product> productList = productService.findByFilter(filter);

        for (Cart cart : cartList) {
            for (Product product : productList) {
                if (cart.getProductId().equals(product.getSn())) {
                    product.setQuantity(cart.getQuantity());
                    break;
                }
            }
        }

        return productList;
    }

    @Override
    public List<CartDTO> listDetail(String userId) {
        SqlPara sqlPara =  Db.getSqlPara("cart.listDetail", Kv.by("userId", userId));
        List<Record> recordList = Db.find(sqlPara);
        List<CartDTO> cartDTOList = RecordUtils.converModel(recordList, CartDTO.class);
        return cartDTOList;
    }
}
