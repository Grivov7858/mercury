package com.hesoyam.mercury.model.invoice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class InvoiceInfo implements Serializable {
    private String invoiceNum;
    private String schoolNum;
    private List<Product> products;
}
