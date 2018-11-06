package io.sdb.form;

import lombok.Data;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: quendi
 * Date: 2018/6/4
 */
@Data
public class OrderDetailForm {
    Integer pageNum;
    Integer pageSize;
    Integer payStatus;
    Integer orderStatus;
}
