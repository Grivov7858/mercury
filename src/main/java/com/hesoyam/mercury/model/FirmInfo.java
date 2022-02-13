package com.hesoyam.mercury.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public final class FirmInfo implements Serializable {
    private String name;
    private String bulstat;
    private String DDS;
    private String address;
    private String MOL;
    private String mobileNumber;
    private String IBAN;
    private String BIC;
    private String bank;

    @Override
    public String toString() {
        return "FirmInfo{" +
                "name='" + name + '\'' +
                ", bulstat='" + bulstat + '\'' +
                ", DDS='" + DDS + '\'' +
                ", address='" + address + '\'' +
                ", MOL='" + MOL + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", IBAN='" + IBAN + '\'' +
                ", BIC='" + BIC + '\'' +
                ", bank='" + bank + '\'' +
                '}';
    }
}
