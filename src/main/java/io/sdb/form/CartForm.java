package io.sdb.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: quendi
 * Date: 2018/6/4
 */
@Data
public class CartForm {
    String productId;
    Integer quantity;
    List<Long> cartItemIds;
}
