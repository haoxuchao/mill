package com.cn.android.bean;

public class TradBean {

    /**
     * max_bi_num : 100
     * head_img : http://192.168.0.121:8000/upload/56fed3d14b8c4806b68e4f71fe0eb1e6.png
     * nickname : 121212
     * id : 5A42D6B7A54EA60A04355344B6588DAD
     * min_bi_num : 1
     * bi_one_money : 1
     */

    private int max_bi_num;
    private String head_img;
    private String nickname;
    private String id;
    private int min_bi_num;
    private double bi_one_money;

    public int getMax_bi_num() {
        return max_bi_num;
    }

    public void setMax_bi_num(int max_bi_num) {
        this.max_bi_num = max_bi_num;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
