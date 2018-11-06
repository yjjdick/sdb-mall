package io.sdb.form;

import lombok.Data;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: quendi
 * Date: 2018/6/4
 */
@Data
public class CheckOutForm {
    private List<Long> cartItemIds;
    private List<ProductInfo> productInfos;
    private InvoiceInfo invoiceInfo;
    private ReceiveInfo receiveInfo;
    private boolean needInvoice;
    private String remark;
}
