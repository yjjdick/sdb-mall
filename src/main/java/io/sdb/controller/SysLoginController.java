package io.sdb.controller;

import io.sdb.common.utils.R;
import io.sdb.enums.OrderStatusEnum;
import io.sdb.model.OrderMaster;
import io.sdb.model.SysUser;
import io.sdb.service.*;
import io.sdb.form.SysLoginForm;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 登录相关
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月10日 下午1:15:31
 */
@RestController
public class SysLoginController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	@Autowired
	private SysCaptchaService sysCaptchaService;

	@Autowired
	private OrderMasterService orderService;

	@Autowired
	private UserService userService;

	/**
	 * 验证码
	 */
	@GetMapping("captcha.jpg")
	public void captcha(HttpServletResponse response, String uuid)throws ServletException, IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		//获取图片验证码
		BufferedImage image = sysCaptchaService.getCaptcha(uuid);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
	}

	/**
	 * 验证码
	 */
	@GetMapping("/home/dash/info")
	public void dashInfo(){
		BigDecimal totalSale = orderService.getTodayTotalSale();
		Integer totalUsers = userService.getTotalUsers();
		Integer newUsers = userService.getYestodayNewUsers();
		List<OrderMaster> orderMasterList = orderService.getTodayOrders();
		Integer totalOrder = orderMasterList.size();
		Integer waitingOrder = 0;
		for (OrderMaster orderMaster:orderMasterList
			 ) {
			if (orderMaster.getOrderStatus() == OrderStatusEnum.NEW.getCode()) {
				waitingOrder++;
			}
		}

		R.ok().put("totalSale", totalSale)
				.put("newUsers", newUsers)
				.put("totalUsers", totalUsers)
				.put("waitingOrder", waitingOrder)
				.put("totalOrder", totalOrder);

	}

	/**
	 * 登录
	 */
	@PostMapping("/sys/login")
	public Map<String, Object> login(@RequestBody SysLoginForm form)throws IOException {
		boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
		if(!captcha){
			return R.error("验证码不正确");
		}

		//用户信息
		SysUser user = sysUserService.queryByUserName(form.getUsername());

		//账号不存在、密码错误
		if(user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
			return R.error("账号或密码不正确");
		}

		//账号锁定
		if(user.getStatus() == 0){
			return R.error("账号已被锁定,请联系管理员");
		}

		//生成token，并保存到数据库
		R r = sysUserTokenService.createToken(user.getUserId());
		return r;
	}


	/**
	 * 退出
	 */
	@PostMapping("/sys/logout")
	public R logout() {
		sysUserTokenService.logout(getUserId());
		return R.ok();
	}
	
}
