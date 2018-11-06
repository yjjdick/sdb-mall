package io.sdb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by yjjdick
 * 2017-07-03 01:31
 */
@Data
@Component
@ConfigurationProperties(prefix = "kuaidi100")
public class Kuaidi100Config {

    private String notifyUrl;

    private String kuaidi100Key;

    private String kuaidi100PollUrl;

}
