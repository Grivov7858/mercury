package com.hesoyam.mercury.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public final class ProductPrice implements Serializable {
    private String schoolNum;
    private String productName;
    private String price;

    @Override
    public String toString() {
        return "ProductPrice{" +
                "schoolNum='" + schoolNum + '\'' +
                ", productName='" + productName + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
