package io.sdb.service.impl;

import com.jfinal.plugin.activerecord.*;
import io.sdb.common.entity.Filter;
import io.sdb.common.entity.Order;
import io.sdb.common.utils.PageUtils;
import io.sdb.common.utils.Query;
import io.sdb.dao.*;
import io.sdb.model.Goods;
import io.sdb.service.BaseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseServiceImpl<T extends BaseDao<M>, M extends Model<M>> implements BaseService<M> {

    @Autowired
    public T dao;

    @Override
    public List<M> findAll() {
        return dao.findAll();
    }

    @Override
    public M findById(Object... id) {
        return dao.findById(id);
    }

    @Override
    public List<M> findByModel(M model) {
        return dao.findByModel(model);
    }

    @Override
    public M findFirstByModel(M model) {
        return dao.findFirstByModel(model);
    }

    @Override
    public List<M> find(SqlPara sqlPara) {
        return dao.find(sqlPara);
    }

    @Override
    public M findFirst(SqlPara sqlPara) {
        return dao.findFirst(sqlPara);
    }

    @Override
    public boolean deleteBatch(Object[]... ids) {
        return dao.deleteBatch(ids);
    }

    @Override
    public boolean deleteBatch(String[] columns, Object[]... ids) {
        return dao.deleteBatch(columns, ids);
    }

    @Override
    public boolean deleteByModel(M model) {
        return dao.deleteByModel(model);
    }

    @Override
    public Page<M> paginate(Integer pageNum, Integer pageSize, List<Filter> filters, Order order) {
        return this.dao.paginate(pageNum, pageSize, filters, order);
    }

    @Override
    public Page<M> paginate(Integer pageNum, Integer pageSize, List<Filter> filters) {
        return this.dao.paginate(pageNum, pageSize, filters);
    }

    @Override
    public Page<M> paginate(Integer pageNum, Integer pageSize) {
        return this.dao.paginate(pageNum, pageSize);
    }

    @Override
    public Page<M> paginate(Integer pageNum, Integer pageSize, List<Filter> filters, List<Order> orders) {
        return this.dao.paginate(pageNum, pageSize, filters, orders);
    }

    @Override
    public Page<M> paginate(Integer pageNum, Integer pageSize, Filter filter, List<Order> orders) {
        return this.dao.paginate(pageNum, pageSize, filter, orders);
    }

    @Override
    public Page<M> paginate(Integer pageNum, Integer pageSize, Filter filter, Order order) {
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        return this.dao.paginate(pageNum, pageSize, filter, orderList);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Query<M> query = new Query<>(params);
        Page<M> pr = this.paginate(query.getCurrPage(), query.getLimit());
        return new PageUtils(pr);
    }

    @Override
    public M insert(M model) {
        model.save();
        return model;
    }

    @Override
    public List<M> findByFilter(Filter filter) {
        return this.dao.findByFilter(filter);
    }

    @Override
    public List<M> findByFilters(List<Filter> filter) {
        return this.dao.findByFilters(filter);
    }

    @Override
    public boolean update(M model) {
        return model.update();
    }

    @Override
    public boolean delete(M model) {
        return model.delete();
    }
}
