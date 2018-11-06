package io.sdb.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import io.sdb.config.AlipayConfig;
import io.sdb.service.OrderMasterService;
import io.sdb.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * APP登录授权
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:31
 */
@Controller
@Slf4j
@RequestMapping("/pay")
public class PayController {

    @Autowired
    PayService payService;

    @Autowired
    OrderMasterService orderService;

    @RequestMapping("/returnUrl")
    public void returnUrl(HttpServletResponse response, HttpServletRequest request) throws IOException, AlipayApiException {
        response.sendRedirect("https://www.baidu.com");
    }

    @RequestMapping("/refund/{no}")
    public void testTradeRefund(@PathVariable String no) throws Exception {
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(no); //与预授权转支付商户订单号相同，代表对该笔交易退款
        model.setRefundAmount("0.01");
        model.setRefundReason("预授权退款测试");
        model.setOutRequestNo("refund0000001");//标识一次退款请求，同一笔交易多次退款需要保证唯一，如部分退款则此参数必传。
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizModel(model);
        AlipayTradeRefundResponse response = client.execute(request);
        log.info("response: {}"+response.getBody());



//退款失败的"fund_change":"N"
//        {"alipay_trade_refund_response":{"code":"10000","msg":"Success","buyer_logon_id":"mqv***@sandbox.com","buyer_user_id":"2088102171383132","fund_change":"N","gmt_refund_pay":"2018-08-30 15:39:03","out_trade_no":"13c7b574-5f54-42da-8165-e9fc0602f74e","refund_fee":"0.01","send_back_fee":"0.00","trade_no":"2018083021001004130500297310"},"sign":"LgzBU1K7056OLY0AAvFAZBVcZuXXt7PTbZDms5nB5qieslSKiKbYc22Jw4As/GdCJJuH7NVEtBABRM9UBXtTJBCGGn7By/H+vZfkulv43E4ASHvLC8OjXiPrypUO4SDleBTB4WW5h3Vnwk8q/SJ+UXv0lb5xrib84Zspsbz//3rrhzqGJ2sr/40YZIoIuevq/OAmLAkbNuAyIiC4ZxkJijF+mD3PbITTdYs/5e4cVEwriZcdLWnptblucSUtjKwEZvwaAuXuB+CEbqaMEBnpPgye0yqvY9tNb6PmVxHX7sc8sL9rfgDjK09xIXMhU9tgh0R/cgPZsRXvSs/HjHiqlA=="}

//退款成功的"fund_change":"Y"
//        {"alipay_trade_refund_response":{"code":"10000","msg":"Success","buyer_logon_id":"mqv***@sandbox.com","buyer_user_id":"2088102171383132","fund_change":"Y","gmt_refund_pay":"2018-08-30 15:42:15","out_trade_no":"2936025a-73eb-4273-8b1c-dfa8d637ff70","refund_fee":"0.01","send_back_fee":"0.00","trade_no":"2018083021001004130500297311"},"sign":"Nr6WroDGtVfWVCvqSSY7Z2SPtV68UbN9eII9GSMIZ3D4xlMRd+NoEUPl8EB9h3z2T8rR7fUrjAAjfIfC5qamaW6LW9tGXI4d8GprgU0YGx+53ZqyXJIaz3LeSJ6+U7NsC3h62/jOmgow6qAqca/XHkjHXmDTeERC+D6nqJLY28E5EKbVZK2IvupCo4sJLih8nI6BS0H5IjVIsRztAjgosrqCHic32JUel4/uyouBfRGPp5pp+3fzbxdJ1yMbUeKjuipNFS9Bhdvdt3ngTJoOAF+o7vQn7DIsJZQn5zNNP3BCEeXZIiXEfF/yha2HBlAM/ush5yZBpjMhDLXYRGuJUQ=="}
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

    @RequestMapping("/alinotify")
    public void alinotify(HttpServletRequest request, HttpServletResponse response) {
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号
        String out_trade_no = "";
        String trade_no = "";
        String trade_status = "";

        try {
            out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
            //支付宝交易号
            log.info("alinotify out_trade_no = {}", out_trade_no);

            trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
            log.info("alinotify trade_no = {}", trade_no);

            //交易状态
            trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        //计算得出通知验证结果
        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
        boolean verify_result = false;
        try {
            verify_result = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        if(verify_result){//验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码

            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

            if(trade_status.equals("TRADE_FINISHED")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                //如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
            } else if (trade_status.equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //如果签约的是可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
            }

            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
//            out.clear();
            try {
                response.getWriter().print("success");	//请不要修改或删除
            } catch (IOException e) {
                e.printStackTrace();
            }

            //////////////////////////////////////////////////////////////////////////////////////////
        }else{//验证失败
            try {
                response.getWriter().print("fail");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("/test")
    public ModelAndView test() {
        return new ModelAndView("success");
    }

    @RequestMapping("/alipay")
    public void alipay(HttpServletResponse response) {
        // 商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = UUID.randomUUID().toString();
        // 订单名称，必填
        String subject = "test" + UUID.randomUUID().toString();
        System.out.println(subject);
        // 付款金额，必填
        String total_amount= "0.01";
        // 商品描述，可空
        String body = "test desc";
        // 超时时间 可空
        String timeout_express="";
        // 销售产品码 必填
        String product_code="QUICK_WAP_WAY";
        /**********************/
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        //调用RSA签名方式
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
        AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();

        // 封装请求支付信息
        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
        model.setOutTradeNo(out_trade_no);
        model.setSubject(subject);
        model.setTotalAmount(total_amount);
        model.setBody(body);
        model.setTimeoutExpress(timeout_express);
        model.setProductCode(product_code);
        alipay_request.setBizModel(model);
        // 设置异步通知地址
        alipay_request.setNotifyUrl(AlipayConfig.notify_url);
        // 设置同步地址
        alipay_request.setReturnUrl(AlipayConfig.return_url);

        // form表单生产
        String form = "";
        try {
            // 调用SDK生成表单
            form = client.pageExecute(alipay_request).getBody();
            response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
            response.getWriter().write(form);//直接将完整的表单html输出到页面
            response.getWriter().flush();
            response.getWriter().close();
        } catch (AlipayApiException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.error("Alipay ApiException>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", e);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Alipay IOException>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", e);
        }
    }
}
