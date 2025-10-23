package com.zhaoxinms.statistics.model;

import lombok.Data;

@Data
public class PayLogStatisticsModel {
    private String month;
    private String count;
    private String resourceName;
    private String payMethod;
    private String receivable;
    private String discount;
    private String payMoney;
    private String lateFeeMoney;
    private String changeMoney;
    private String prePayMoney;
    private String preSaveMoney;
     
    public void initToZero() {
        java.time.LocalDate now = java.time.LocalDate.now();
        this.month = now.getYear() + "-" + String.format("%02d", now.getMonthValue());
        this.count = "0";
        this.receivable = "0";
        this.discount = "0"; 
        this.payMoney = "0";
        this.lateFeeMoney = "0";
        this.changeMoney = "0";
        this.prePayMoney = "0";
        this.preSaveMoney = "0";
    }
}
