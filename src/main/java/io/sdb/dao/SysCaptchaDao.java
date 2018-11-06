package io.sdb.dao;

import io.sdb.model.SysCaptcha;
import org.springframework.stereotype.Component;

@Component
public class SysCaptchaDao extends BaseDao<SysCaptcha> {
    public SysCaptchaDao() {
        super(SysCaptcha.class);
    }
}
