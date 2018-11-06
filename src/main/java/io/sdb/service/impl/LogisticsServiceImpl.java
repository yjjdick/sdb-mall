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

import com.alibaba.fastjson.JSON;
import io.sdb.common.entity.kuaidi100.TaskRequest;
import io.sdb.common.entity.kuaidi100.TaskResponse;
import io.sdb.common.utils.HttpRequest;
import io.sdb.config.Kuaidi100Config;
import io.sdb.dao.LogisticsDao;
import io.sdb.model.Logistics;
import io.sdb.service.LogisticsService;
import io.sdb.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;

@Service("sysLogisticsService")
@Slf4j
public class LogisticsServiceImpl extends BaseServiceImpl<LogisticsDao, Logistics> implements LogisticsService {

    @Autowired
    Kuaidi100Config kuaidi100Config;

    @Override
    public Boolean subscribe(String tradeNo, String code) {
        TaskRequest req = new TaskRequest();
        req.setCompany(code);
        req.setNumber(tradeNo);
        req.getParameters().put("callbackurl", kuaidi100Config.getNotifyUrl());
        req.setKey(kuaidi100Config.getKuaidi100Key());

        String kuaidi100PollUrl = kuaidi100Config.getKuaidi100PollUrl();

        HashMap<String, String> p = new HashMap<String, String>();
        p.put("schema", "json");
        p.put("param", JSON.toJSONString(req));
        try {
            String ret = HttpRequest.postData(kuaidi100PollUrl, p);
            TaskResponse resp = JSON.parseObject(ret, TaskResponse.class);
            if(resp.getResult() == true){
                log.info("订阅成功 订单号 = " + tradeNo);
                Logistics curLogistics = Logistics.dao.findById(tradeNo);
                if(curLogistics == null){
                    Logistics logistics = new Logistics();
                    logistics.setCheckState(1);
                    logistics.setTrackingNo(tradeNo);
                    logistics.save();
                }else {
                    curLogistics.setCheckState(1);
                    curLogistics.update();
                }

                return true;
            }else{
                log.info("订阅失败 订单号 = " + tradeNo);
                Logistics curLogistics = Logistics.dao.findById(tradeNo);
                if(curLogistics == null){
                    Logistics logistics = new Logistics();
                    logistics.setCheckState(2);
                    logistics.setTrackingNo(tradeNo);
                    logistics.save();
                }else {
                    curLogistics.setCheckState(2);
                    curLogistics.update();
                }
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
