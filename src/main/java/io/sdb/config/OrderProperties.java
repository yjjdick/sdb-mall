package io.sdb.config;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@ConfigurationProperties(prefix = "order")
@Component
@Data
public class OrderProperties {
    Boolean refund;
}
