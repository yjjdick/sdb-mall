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
import io.sdb.dao.SysOssDao;
import io.sdb.model.SysOss;
import io.sdb.service.SysOssService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("sysOssService")
public class SysOssServiceImpl extends BaseServiceImpl<SysOssDao, SysOss> implements SysOssService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {

		Query<SysOss> query = new Query<SysOss>(params);
		Page<SysOss> pr = this.paginate(query.getCurrPage(), query.getLimit());

		return new PageUtils(pr);
	}
	
}
