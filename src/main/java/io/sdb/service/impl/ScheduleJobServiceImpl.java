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

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import io.sdb.common.annotation.JFinalTx;
import io.sdb.common.utils.Constant;
import io.sdb.common.utils.PageUtils;
import io.sdb.common.utils.Query;
import io.sdb.common.entity.Filter;
import io.sdb.dao.ScheduleJobDao;
import io.sdb.model.ScheduleJob;
import io.sdb.service.ScheduleJobService;
import io.sdb.job.utils.ScheduleUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service("scheduleJobService")
public class ScheduleJobServiceImpl extends BaseServiceImpl<ScheduleJobDao, ScheduleJob> implements ScheduleJobService {
	@Autowired
    private Scheduler scheduler;

    @Autowired
    ActiveRecordPlugin activeRecordPlugin;

	/**
	 * 项目启动时，初始化定时器
	 */
	@PostConstruct
	public void init(){
		List<ScheduleJob> scheduleJobList = this.findAll();
		for(ScheduleJob scheduleJob : scheduleJobList){
			CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getJobId());
            //如果不存在，则创建
            if(cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
            }else {
                ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
            }
		}
	}

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String beanName = (String)params.get("beanName");

        List<Filter> filters = new ArrayList<>();
        if(!StringUtils.isBlank(beanName)) {
            Filter filter = new Filter();
            filter.setProperty("bean_name");
            filter.setOperator(Filter.Operator.like);
            filter.setValue(beanName);
            filters.add(filter);
        }

        Query<ScheduleJob> query = new Query<ScheduleJob>(params);
        Page<ScheduleJob> pr = this.paginate(query.getCurrPage(), query.getLimit(), filters, query.getOrder());

		return new PageUtils(pr);
	}


	@Override
	@JFinalTx
	public void save(ScheduleJob scheduleJob) {
		scheduleJob.setCreateTime(new Date());
		scheduleJob.setStatus(Constant.ScheduleStatus.NORMAL.getValue());
		scheduleJob.save();
        
        ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
    }
	
	@Override
	@JFinalTx
	public boolean update(ScheduleJob scheduleJob) {
        ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
        return scheduleJob.update();
    }

	@Override
	@JFinalTx
    public void deleteBatch(Long[] jobIds) {
    	for(Long jobId : jobIds){
    		ScheduleUtils.deleteScheduleJob(scheduler, jobId);
    	}
    	
    	//删除数据
        this.dao.deleteBatch(jobIds);
	}

	@Override
    public int updateBatch(Long[] jobIds, int status){
    	Map<String, Object> map = new HashMap<>();
    	map.put("list", jobIds);
    	map.put("status", status);
        List<ScheduleJob> scheduleJobList = new ArrayList<>();
        for (Long jobId : jobIds) {
            ScheduleJob scheduleJob = new ScheduleJob();
            scheduleJob.setJobId(jobId);
            scheduleJob.setStatus(status);
            scheduleJobList.add(scheduleJob);
        }

        Db.batchUpdate(scheduleJobList, scheduleJobList.size());
    	return 1;
    }
    
	@Override
	@JFinalTx
    public void run(Long[] jobIds) {
    	for(Long jobId : jobIds){
    		ScheduleUtils.run(scheduler, this.findById(jobId));
    	}
    }

	@Override
	@JFinalTx
    public void pause(Long[] jobIds) {
        for(Long jobId : jobIds){
    		ScheduleUtils.pauseJob(scheduler, jobId);
    	}
        
    	updateBatch(jobIds, Constant.ScheduleStatus.PAUSE.getValue());
    }

	@Override
	@JFinalTx
    public void resume(Long[] jobIds) {
    	for(Long jobId : jobIds){
    		ScheduleUtils.resumeJob(scheduler, jobId);
    	}

    	updateBatch(jobIds, Constant.ScheduleStatus.NORMAL.getValue());
    }
    
}
