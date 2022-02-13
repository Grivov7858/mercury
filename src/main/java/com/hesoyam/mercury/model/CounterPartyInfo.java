package com.hesoyam.mercury.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public final class CounterPartyInfo implements Serializable {
    private String schoolNum;
    private String schoolName;
    private String bulstat;
    private String address;
    private String MOL;
    private String carNumber;
    private String DDS;

    @Override
    public String toString() {
        return "CounterPartyInfo{" +
                "schoolNum='" + schoolNum + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", bulstat='" + bulstat + '\'' +
                ", address='" + address + '\'' +
                ", MOL='" + MOL + '\'' +
                ", plateNumber='" + carNumber + '\'' +
                ", DDS='" + DDS + '\'' +
                '}';
    }
}
