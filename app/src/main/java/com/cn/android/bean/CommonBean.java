package com.cn.android.bean;

public class CommonBean {

    /**
     * id : 1
     * customerImg :
     * openShopEb : 1
     * activationMoney : 1
     * tixianMinMoney : 10
     * tixianPercentum : 0.05
     */

    private String id;
    private String customerImg;
    private double openShopEb;
    private double activationMoney;
    private double tixianMinMoney;
    private double tixianPercentum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerImg() {
        return customerImg;
    }

    public void setCustomerImg(String customerImg) {
        this.customerImg = customerImg;
    }

    public double getOpenShopEb() {
        return openShopEb;
    }

    public void setOpenShopEb(int openShopEb) {
        this.openShopEb = openShopEb;
    }

    public double getActivationMoney() {
        return activationMoney;
    }

    public void setActivationMoney(double activationMoney) {
        this.activationMoney = activationMoney;
    }

    public double getTixianMinMoney() {
        return tixianMinMoney;
    }

    public void setTixianMinMoney(double tixianMinMoney) {
        this.tixianMinMoney = tixianMinMoney;
    }

    public double getTixianPercentum() {
        return tixianPercentum;
    }

    public void setTixianPercentum(double tixianPercentum) {
        this.tixianPercentum = tixianPercentum;
    }
}
