package io.sdb.form;

import lombok.Data;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: quendi
 * Date: 2018/6/4
 */
@Data
public class InvoiceInfo {
    private Integer type;
    private String title;
    private String taxNumber;
    private String companyAddress;
    private String telephone;
    private String bankName;
    private String bankAccount;
}
