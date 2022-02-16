package com.hesoyam.mercury.model.invoice;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class BuyerInfo implements Serializable {
    private String name;
    private String bulstat;
    private String DDS;
    private String address;
    private String MOL;
    private String IBAN;
    private String BIC;
}
