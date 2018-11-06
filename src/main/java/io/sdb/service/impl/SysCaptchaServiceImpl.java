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

import com.google.code.kaptcha.Producer;
import io.sdb.common.exception.RRException;
import io.sdb.common.utils.DateUtils;
import io.sdb.dao.SysCaptchaDao;
import io.sdb.model.SysCaptcha;
import io.sdb.service.SysCaptchaService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Date;

/**
 * 验证码
 *
 * @author Mark sunlightcs@gmail.com
 * @since 2.0.0 2018-02-10
 */
@Service("sysCaptchaService")
public class SysCaptchaServiceImpl extends BaseServiceImpl<SysCaptchaDao, SysCaptcha> implements SysCaptchaService {
    @Autowired
    private Producer producer;

    @Override
    public BufferedImage getCaptcha(String uuid) {
        if(StringUtils.isBlank(uuid)){
            throw new RRException("uuid不能为空");
        }
        //生成文字验证码
        String code = producer.createText();

        SysCaptcha captcha = new SysCaptcha();
        captcha.setUuid(uuid);
        captcha.setCode(code);
        //5分钟后过期
        captcha.setExpireTime(DateUtils.addDateMinutes(new Date(), 5));
        captcha.save();

        return producer.createImage(code);
    }

    @Override
    public boolean validate(String uuid, String code) {
        SysCaptcha qSysCaptcha = new SysCaptcha();
        qSysCaptcha.setUuid(uuid);


        SysCaptcha captcha = this.dao.findFirstByModel(qSysCaptcha);
        if(captcha == null){
            return false;
        }

        //删除验证码
        captcha.delete();

        if(captcha.getCode().equalsIgnoreCase(code) && captcha.getExpireTime().getTime() >= System.currentTimeMillis()){
            return true;
        }

        return false;
    }
}
