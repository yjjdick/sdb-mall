package io.sdb.dao;

import com.jfinal.kit.Kv;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.activerecord.*;
import com.zaxxer.hikari.util.FastList;
import io.sdb.common.entity.Filter;
import io.sdb.common.entity.Order;
import io.sdb.common.utils.GenericsUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Dao - 基类
 * 
 * 
 */
public class BaseDao<M extends Model<M>> {

	/** "ID"属性名称 */
	public static final String ID = "id";

	/** "创建日期"属性名称 */
	public static final String CREATE_DATE = "create_date";

	/** 实体类类型 */
	private Class<M> modelClass;
	
	protected M modelManager;
	
	public Class<M> getModelClass() {
		return modelClass;
	}

	public void setModelClass(Class<M> modelClass) {
		this.modelClass = modelClass;
	}
	
	/**
	 * 构造方法
	 */
	@SuppressWarnings("unchecked")
	public BaseDao(Class<M> entityClass) {
		this.setModelClass(GenericsUtils.getSuperClassGenricType(entityClass));
		try {
			modelManager = modelClass.newInstance();
		} catch (InstantiationException e) {
			LogKit.error("instance model fail!" + e);
		} catch (IllegalAccessException e) {
			LogKit.error("instance model fail!" + e);
		}
	}
	
	public String getTableName() {
		Table table = TableMapping.me().getTable(getModelClass());
		return table.getName();
	}

	public String[] getPrimaryKeys() {
		Table table = TableMapping.me().getTable(getModelClass());
		table.getPrimaryKey();
		return table.getPrimaryKey();
	}
	
	/**
	 * 查找实体对象
	 * 
	 * @param id
	 *            ID
	 * @return 实体对象，若不存在则返回null
	 */
	public M findById(Object ...id) {
		if (id == null) {
			return null;
		}
		return modelManager.findById(id);
	}

	public M findFirstByModel(M model){
		Record record = model.toRecord();
		SqlPara sqlPara = Db.getSqlPara("common.findByModel", Kv.by("record", record.getColumns()).set("tableName", this.getTableName()));
		return this.modelManager.findFirst(sqlPara);
	}

	public List<M> findByModel(M model){
		Record record = model.toRecord();
		SqlPara sqlPara = Db.getSqlPara("common.findByModel", Kv.by("record", record.getColumns()).set("tableName", this.getTableName()));
		return this.modelManager.find(sqlPara);
	}

	public List<M> findByFilter(Filter filter){
		List<Filter> filters = new ArrayList<>();
		filters.add(filter);
		return this.findByFilters(filters);
	}

	public List<M> findByFilters(List<Filter> filters){
		SqlPara sqlPara = Db.getSqlPara("common.findList", Kv.by("filters", filters).set("tableName", this.getTableName()));
		List<M> modelList = this.modelManager.find(sqlPara);
		return modelList;
	}

	public boolean deleteByModel(M model){
		Record record = model.toRecord();
		SqlPara sqlPara = Db.getSqlPara("common.deleteByModel", Kv.by("record", record.getColumns()).set("tableName", this.getTableName()));
		return Db.update(sqlPara) > 0;
	}

	public boolean deleteById(Object ...id){
		return this.modelManager.deleteById(id);
	}

	public M getDao(){
		return modelManager;
	}

	public List<M> findAll() {
		return findList(null, null, null, null);
	}


	/**
	 * 查找实体对象集合
	 *
	 * @param first
	 *            起始记录
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 实体对象集合
	 */
	public List<M> findList(Integer first, Integer count, List<Filter> filters, List<Order> orders) {
		String sql = "SELECT * FROM `" + getTableName() + "` WHERE 1 = 1 ";
		return findList(sql, first, count, filters, orders);
	}

	/**
	 * 查找实体对象集合
	 *
	 * @param sql
	 *            查询条件
	 * @param first
	 *            起始记录
	 * @param count
	 *            数量
	 * @param filters
	 *            筛选
	 * @param orders
	 *            排序
	 * @return 实体对象集合
	 */
	protected List<M> findList(String sql, Integer first, Integer count, List<Filter> filters, List<Order> orders) {
		Assert.notNull(sql, "baseDao findList sql cannot null");

		String sqlFilters = getFilters(filters);
		sql += sqlFilters;

		String sqlOrders = getOrders(orders);
		sql += sqlOrders;

		if (first != null && count != null) {
			sql += " LIMIT " + first + ", " + count;
		}
		return modelManager.find(sql);
	}

	/**
	 * 转换为Predicate
	 *
	 * @param filters
	 *            筛选
	 * @return Predicate
	 */
	private String getFilters(List<Filter> filters) {
		String sql = "";
		if (CollectionUtils.isEmpty(filters)) {
			return "";
		}
		for (Filter filter : filters) {
			if (filter == null) {
				continue;
			}
			String property = filter.getProperty();
			Filter.Operator operator = filter.getOperator();
			Object value = filter.getValue();
			Boolean ignoreCase = filter.getIgnoreCase();
			switch (operator) {
				case eq:
					if (value != null) {
						if (BooleanUtils.isTrue(ignoreCase) && value instanceof String) {
							sql +=" AND "+  property + " = " + ((String) value).toLowerCase();
						} else {
							sql +=" AND "+ property + " = " + value;
						}
					} else {
						sql +=" AND "+ property + " IS NULL ";
					}
					break;
				case ne:
					if (value != null) {
						if (BooleanUtils.isTrue(ignoreCase) && value instanceof String) {
							sql +=" AND "+ property + " != " + ((String) value).toLowerCase();
						} else {
							sql +=" AND "+ property + " != " + value;
						}
					} else {
						sql +=" AND "+ property + " IS NOT NULL ";
					}
					break;
				case gt:
					if (value instanceof Number) {
						sql +=" AND "+ property + " > " + (Number) value;
					}
					break;
				case lt:
					if (value instanceof Number) {
						sql +=" AND "+ property + " < " + (Number) value;
					}
					break;
				case ge:
					if (value instanceof Number) {
						sql +=" AND "+ property + " >= " + (Number) value;
					}
					break;
				case le:
					if (value instanceof Number) {
						sql +=" AND "+ property + " <= " + (Number) value;
					}
					break;
				case like:
					if (value instanceof String) {
						if (BooleanUtils.isTrue(ignoreCase)) {
							sql += " AND " + property + " LIKE '%" + ((String) value).toLowerCase() + "'";
						} else {
							sql += " AND " + property + " LIKE '%" + (String) value + "'";
						}
					}
					break;
				case in:
					sql +=" AND "+ property + " IN(" + value + ")";
					break;
				case isNull:
					sql +=" AND "+ property + " IS NULL";
					break;
				case isNotNull:
					sql +=" AND "+ property + " IS NOT NULL";
					break;
			}
		}
		return sql;
	}

	/**
	 * 转换为Order
	 *
	 * @param orders
	 *            排序
	 * @return Order
	 */
	private String getOrders(List<Order> orders) {
		String orderSql = "";
		if (!CollectionUtils.isEmpty(orders)) {
			orderSql = " ORDER BY ";
			for (Order order : orders) {
				String property = order.getProperty();
				Order.Direction direction = order.getDirection();
				switch (direction) {
					case asc:
						orderSql += property + " ASC, ";
						break;
					case desc:
						orderSql += property + " DESC,";
						break;
				}
			}
			orderSql = StringUtils.substring(orderSql, 0, orderSql.length() - 1);
		}
		return orderSql;
	}

	public Page<M> paginate(Integer pageNum, Integer pageSize, Filter filter, Order order) {
		List<Filter> filters = new ArrayList<>();
		filters.add(filter);
		List<Order> orders = new ArrayList<>();
		orders.add(order);
		Page<M> pr = this.paginate(pageNum, pageSize, filters, orders);
		return pr;
	}

	public Page<M> paginate(Integer pageNum, Integer pageSize) {
		List<Filter> filters = new ArrayList<>();
		List<Order> orders = new ArrayList<>();
		Page<M> pr = this.paginate(pageNum, pageSize, filters, orders);
		return pr;
	}

	public Page<M> paginate(Integer pageNum, Integer pageSize, List<Filter> filters, Order order) {
		List<Order> orders = new ArrayList<>();
		if (order != null) {
			orders.add(order);
		}
		Page<M> pr = this.paginate(pageNum, pageSize, filters, orders);
		return pr;
	}

	public Page<M> paginate(Integer pageNum, Integer pageSize, List<Filter> filters) {
		List<Order> orders = new ArrayList<>();
		Page<M> pr = this.paginate(pageNum, pageSize, filters, orders);
		return pr;
	}

	public Page<M> paginate(Integer pageNum, Integer pageSize, List<Filter> filters, List<Order> orders) {
		SqlPara sqlPara = Db.getSqlPara("common.findList", Kv.by("filters", filters).set("orders", orders).set("tableName", this.getTableName()));
		Page<M> pr = this.modelManager.paginate(pageNum, pageSize, sqlPara);
		return pr;
	}

	public Page<M> paginate(Integer pageNum, Integer pageSize, Filter filter, List<Order> orders) {
		List<Filter> filters = new ArrayList<>();
		filters.add(filter);
		Page<M> pr = this.paginate(pageNum, pageSize, filters, orders);
		return pr;
	}

	public boolean deleteBatch(Object[]... ids) {
		return this.deleteBatch(null, ids);
	}

	public boolean deleteBatch(String[] columns, Object[]... ids) {
		String[] primaryKeys = columns;
		if (columns == null) {
			primaryKeys = this.getPrimaryKeys();
		}

		SqlPara sqlPara = Db.getSqlPara("common.deleteBatch", Kv.by("ids", ids).set("primaryKeys", primaryKeys).set("tableName", this.getTableName()));
		return Db.update(sqlPara) > 0;
	}

	public M findFirst(SqlPara sqlPara) {
		return this.modelManager.findFirst(sqlPara);
	}

	public List<M> find(SqlPara sqlPara) {
		return this.modelManager.find(sqlPara);
	}

}