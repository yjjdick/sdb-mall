package io.sdb.controller;

import io.sdb.common.utils.JsonUtils;
import io.sdb.common.utils.R;
import io.sdb.enums.ResultEnum;
import io.sdb.model.User;
import io.sdb.common.annotation.Login;
import io.sdb.common.annotation.LoginUser;
import io.sdb.form.MaLoginForm;
import io.sdb.service.UserService;
import io.sdb.common.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import me.chanjar.weixin.common.exception.WxErrorException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RestController
@Slf4j
@RequestMapping("/wechat/user")
public class WxMaUserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private WxMaService wxService;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    /**
     * 登陆接口
     */
    @PostMapping("/login")
    public R login(HttpServletRequest request, @RequestBody MaLoginForm maLoginForm) {

        if (StringUtils.isBlank(maLoginForm.getCode())) {
            return R.error("empty jscode");
        }

        try {
            WxMaJscode2SessionResult session = this.wxService.getUserService().getSessionInfo(maLoginForm.getCode());

            String sessionKey = session.getSessionKey();
            String openId = session.getOpenid();

            // 用户信息校验
            if (!this.wxService.getUserService().checkUserInfo(sessionKey, maLoginForm.getRawData(), maLoginForm.getSignature())) {
                return R.error("user check failed");
            }

            User user = new User();
            user.setMaOpenId(openId);

            User dbUser = userService.findFirstByModel(user);
            if (dbUser == null) {
                // 解密用户信息
                WxMaUserInfo userInfo = this.wxService.getUserService().getUserInfo(sessionKey, maLoginForm.getEncryptedData(), maLoginForm.getIv());
                dbUser = userService.addMaUser(userInfo);
                if (dbUser.getUserId() == null) {
                    return R.error(ResultEnum.MA_LOGIN_ERROR);
                }
            }

            //生成token
            String token = jwtUtils.generateToken(dbUser.getUserId());

            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            map.put("expire", jwtUtils.getExpire());

            return R.ok(map);
        } catch (WxErrorException e) {
            this.logger.error(e.getMessage(), e);
            return R.error(e.toString());
        }
    }

    @Login
    @GetMapping("userInfo")
    public R userInfo(@LoginUser User user) {
        return R.ok().put("user", user);
    }

    /**
     * <pre>
     * 获取用户信息接口
     * </pre>
     */
    @GetMapping("/info")
    public String info(String sessionKey, String signature, String rawData, String encryptedData, String iv) {
        // 用户信息校验
        if (!this.wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return "user check failed";
        }

        // 解密用户信息
        WxMaUserInfo userInfo = this.wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);

        return JsonUtils.toJson(userInfo);
    }

    /**
     * <pre>
     * 获取用户绑定手机号信息
     * </pre>
     */
    @GetMapping("/phone")
    public String phone(String sessionKey, String signature, String rawData, String encryptedData, String iv) {
        // 用户信息校验
        if (!this.wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return "user check failed";
        }

        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = this.wxService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);

        return JsonUtils.toJson(phoneNoInfo);
    }
}
