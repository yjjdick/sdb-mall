package io.sdb.form;

import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: quendi
 * Date: 2018/6/4
 */
@Data
public class GoodsListForm {
    Integer pageNum;
    Integer pageSize;
    Integer categoryId;
}
