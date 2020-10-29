package com.cn.android.bean;

public class TradOrderDetail {

    /**
     * bi_num : 10
     * ordercode : 4903869384
     * max_bi_num : 90
     * nickname : 看看咯
     * ctime : 2020-10-29 13:55:48
     * total_money : 125
     * min_bi_num : 0
     * bi_one_money : 12.5
     * add_time : 2020-10-29 13:50:52
     * pay_time :
     */

    private int bi_num;
    private String ordercode;
    private int max_bi_num;
    private String nickname;
    private String ctime;
    private double total_money;
    private int min_bi_num;
    private double bi_one_money;
    private String add_time;
    private String pay_time;

    public int getBi_num() {
        return bi_num;
    }

    public void setBi_num(int bi_num) {
        this.bi_num = bi_num;
    }

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }

    public int getMax_bi_num() {
        return max_bi_num;
    }

    public void setMax_bi_num(int max_bi_num) {
        this.max_bi_num = max_bi_num;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public double getTotal_money() {
        return total_money;
    }

    public void setTotal_money(double total_money) {
        this.total_money = total_money;
    }

    public int getMin_bi_num() {
        return min_bi_num;
    }

    public void setMin_bi_num(int min_bi_num) {
        this.min_bi_num = min_bi_num;
    }

    public double getBi_one_money() {
        return bi_one_money;
    }

    public void setBi_one_money(double bi_one_money) {
        this.bi_one_money = bi_one_money;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }
}
