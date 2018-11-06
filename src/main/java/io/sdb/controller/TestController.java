package io.sdb.controller;

import com.jfinal.kit.PathKit;
import com.lly835.bestpay.model.PayResponse;
import io.sdb.common.utils.R;
import io.sdb.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    PayService payService;

    @GetMapping("/hello")
    public ModelAndView hello(ModelAndView modelAndView, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", "attachment; filename=test.doc");
        response.setContentType("application/octet-stream;charset=UTF-8");
        modelAndView.addObject("value", "test");
        modelAndView.setViewName("hello");
        return modelAndView;
    }

    @PostMapping("/pay")
    @ResponseBody
    public R pay() {
        PayResponse payResponse = payService.create(null);
        return R.ok().put("payResponse", payResponse);
    }

    @GetMapping("/print")
    @ResponseBody
    public String print() {
        String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
//            log.error("get server host Exception e:", e);
        }
        return host+"_123456";
    }

    /**
     * 微信异步通知
     * @param notifyData
     */
    @RequestMapping("/notify")
    public ModelAndView notify(@RequestBody String notifyData) {
        payService.notify(notifyData);

        //返回给微信处理结果
        return new ModelAndView("success");
    }

    public static void main(String[] args) {
        String baseTemplatePath = new StringBuilder(PathKit.getRootClassPath())
                .append("/")
//                .append(PathKit.getPackagePath(this))
//                .append("/tpl")
                .toString();
        log.info(System.getProperty("user.dir"));
    }
}