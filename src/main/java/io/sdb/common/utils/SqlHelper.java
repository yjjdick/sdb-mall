package io.sdb.common.utils;

import io.sdb.common.entity.Filter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.*;

import java.util.List;

public class SqlHelper {

    public String test() {
        return " and #para(abc)";
    }

    /**
     * 转换为Predicate
     *
     * @param filters
     *            筛选
     * @return Predicate
     */
    public String getFilters(List<Filter> filters) {
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
                    sql +=" AND "+ property + " > " + value;
                    break;
                case lt:
                    sql +=" AND "+ property + " < " +  value;
                    break;
                case ge:
                    sql +=" AND "+ property + " >= " + value;
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
}
