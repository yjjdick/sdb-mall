/**
 * Copyright 2018
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.sdb.service.impl;

import com.jfinal.plugin.activerecord.Page;
import io.sdb.common.utils.PageUtils;
import io.sdb.common.utils.Query;
import io.sdb.common.entity.Filter;
import io.sdb.common.entity.Order;
import io.sdb.dao.SysLogDao;
import io.sdb.model.SysLog;
import io.sdb.service.SysLogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("sysLogService")
public class SysLogServiceImpl extends BaseServiceImpl<SysLogDao, SysLog> implements SysLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String)params.get("key");

        List<Filter> filters = new ArrayList<>();
        if (!StringUtils.isBlank(key)) {
            Filter filter = new Filter();
            filter.setProperty("username");
            filter.setValue(key);
            filter.setOperator(Filter.Operator.like);
            filters.add(filter);
        }

        Order order = new Order();
        order.setProperty("create_date");
        order.setDirection(Order.Direction.desc);

        Query<SysLog> query = new Query<SysLog>(params);
        Page<SysLog> pr = this.paginate(query.getCurrPage(), query.getLimit(), filters, order);

        return new PageUtils(pr);
    }
}
