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

import com.google.gson.Gson;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;
import io.sdb.common.annotation.JFinalTx;
import io.sdb.common.exception.RRException;
import io.sdb.common.utils.PageUtils;
import io.sdb.common.utils.Query;
import io.sdb.common.entity.Filter;
import io.sdb.dao.SysConfigDao;
import io.sdb.model.SysConfig;
import io.sdb.sys.redis.SysConfigRedis;
import io.sdb.service.SysConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("sysConfigService")
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfigDao, SysConfig> implements SysConfigService {

	@Autowired
	private SysConfigRedis sysConfigRedis;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String paramKey = (String)params.get("paramKey");

		List<Filter> filters = new ArrayList<>();
		if(!StringUtils.isBlank(paramKey)) {
			Filter filter = new Filter();
			filter.setProperty("param_key");
			filter.setOperator(Filter.Operator.like);
			filter.setValue(paramKey);
			filters.add(filter);
		}

		Filter filter = new Filter();
		filter.setProperty("status");
		filter.setOperator(Filter.Operator.eq);
		filter.setValue(1);
		filters.add(filter);

		Query<SysConfig> query = new Query<SysConfig>(params);
		Page<SysConfig> pr = this.paginate(query.getCurrPage(), query.getLimit(), filters, query.getOrder());

		return new PageUtils(pr);
	}
	
	@Override
	@JFinalTx
	public void save(SysConfig config) {
		config.save();
		sysConfigRedis.saveOrUpdate(config);
	}

	@Override
	@JFinalTx
	public boolean update(SysConfig config) {
		boolean updateSucc = config.update();
		sysConfigRedis.saveOrUpdate(config);
		return updateSucc;
	}

	@Override
	@JFinalTx
	public void updateValueByKey(String key, String value) {
		SqlPara sqlPara = Db.getSqlPara("sysConfig.updateValueByKey", Kv.by("paramValue", value).set("paramKey", key));
		Db.update(sqlPara);
		sysConfigRedis.delete(key);
	}

	@Override
	@JFinalTx
	public void deleteBatch(Long[] ids) {
		for(Long id : ids){
			SysConfig config = this.findById(id);
			sysConfigRedis.delete(config.getParamKey());
		}

		this.dao.deleteBatch(ids);
	}

	@Override
	public String getValue(String key) {
		SysConfig config = sysConfigRedis.get(key);
		if(config == null){
			config = new SysConfig();
			config.setParamKey(key);
			config = this.findFirstByModel(config);
			sysConfigRedis.saveOrUpdate(config);
		}

		return config == null ? null : config.getParamValue();
	}
	
	@Override
	public <T> T getConfigObject(String key, Class<T> clazz) {
		String value = getValue(key);
		if(StringUtils.isNotBlank(value)){
			return new Gson().fromJson(value, clazz);
		}

		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RRException("获取参数失败");
		}
	}
}
