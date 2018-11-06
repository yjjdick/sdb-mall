package io.sdb.service;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;
import io.sdb.common.entity.Filter;
import io.sdb.common.entity.Order;
import io.sdb.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

public interface BaseService<M extends Model<M>> {

    List<M> findAll();

    M findById(Object ...id);

    List<M> findByModel(M model);

    M findFirstByModel(M model);

    List<M> find(SqlPara sqlPara);

    M findFirst(SqlPara sqlPara);

    boolean deleteBatch(Object[]... ids);

    boolean deleteBatch(String[] columns, Object[]... ids);

    boolean deleteByModel(M model);

    Page<M> paginate(Integer pageNum, Integer pageSize, List<Filter> filters, Order order);

    Page<M> paginate(Integer pageNum, Integer pageSize, List<Filter> filters);

    Page<M> paginate(Integer pageNum, Integer pageSize);

    Page<M> paginate(Integer pageNum, Integer pageSize, List<Filter> filters, List<Order> orders);

    Page<M> paginate(Integer pageNum, Integer pageSize, Filter filter, List<Order> orders);

    Page<M> paginate(Integer pageNum, Integer pageSize, Filter filter, Order order);

    PageUtils queryPage(Map<String, Object> params);

    M insert(M model);

    List<M> findByFilter(Filter filter);

    List<M> findByFilters(List<Filter> filter);

    boolean update(M model);

    boolean delete(M model);
}
