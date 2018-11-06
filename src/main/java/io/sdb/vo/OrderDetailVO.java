package io.sdb.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.sdb.common.utils.EnumUtil;
import io.sdb.enums.OrderStatusEnum;
import io.sdb.model.OrderDetail;
import io.sdb.serializer.Date2LongSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by yjjdick
 * 2017-06-11 18:30
 */
@Data
public class OrderDetailVO {
    String detailId;
    String orderId;
    String productId;
    String productName;
    String productModel;
    String productSpec;
    BigDecimal productPrice;
    Integer productQuantity;
    String productIcon;
    @JsonSerialize(using = Date2LongSerializer.class)
    Date createDate;
    @JsonSerialize(using = Date2LongSerializer.class)
    Date updateDate;
}
