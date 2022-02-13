package com.hesoyam.mercury.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
public final class TradeFileInfo implements Serializable {
    private Integer fileNumber;
    private Date fileDate;
    private String productName;
    private Integer productQuantity;
    private Date manufactureDate;
    private String manufactureTime;
    private Date expiryDate;
    private String recipient;
    private String carNumber;
    private String deliveryTime;
}
