package io.sdb.controller;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.LogKit;
import io.sdb.common.entity.kuaidi100.NoticeRequest;
import io.sdb.common.entity.kuaidi100.NoticeResponse;
import io.sdb.common.entity.kuaidi100.Result;
import io.sdb.model.Logistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@Slf4j
@RequestMapping("/sys/logistics")
public class SysLogisticsController {

    @RequestMapping("notify")
    public NoticeResponse kuaidi100Notify(@RequestParam String param){
        NoticeResponse resp = new NoticeResponse();
        resp.setResult(false);
        resp.setReturnCode("500");
        resp.setMessage("保存失败");
        try {
            NoticeRequest nReq = JSON.parseObject(param,
                    NoticeRequest.class);

            Result result = nReq.getLastResult();
            // 处理快递结果
            Logistics logistics = Logistics.dao.findById(result.getNu());
            resp.setResult(true);
            resp.setReturnCode("200");
            resp.setMessage("提交成功");
            if(logistics != null){
                logistics.setUpdateDate(new Date());
                logistics.setOrderState(Integer.parseInt(result.getState()));
                logistics.setData(JSON.toJSONString(result.getData()));
                logistics.update();
                return resp;
            }else {
                logistics = new Logistics();
                logistics.setUpdateDate(new Date());
                logistics.setOrderState(Integer.parseInt(result.getState()));
                logistics.setData(JSON.toJSONString(result.getData()));
                logistics.setTrackingNo(result.getNu());
                logistics.save();
                return resp;
            }

            //response.getWriter().print(JsonKit.toJson(resp)); //这里必须返回，否则认为失败，过30分钟又会重复推送。
        } catch (Exception e) {
            e.printStackTrace();
            log.error("kuaidi100Notify 保存失败 error = ", e);
            resp.setMessage("保存失败");
            return resp;
            //response.getWriter().print(JsonKit.toJson(resp));//保存失败，服务端等30分钟会重复推送。
        }


    }
}
