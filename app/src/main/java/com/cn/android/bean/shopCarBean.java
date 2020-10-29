package com.cn.android.bean;

public class shopCarBean {

    /**
     * img : http://192.168.0.121:8000/upload/f513af32f5994f5792ba0cc5a403ac88.png
     * shop_eb : 1
     * id : BB4F96559E9FF4DD04BE4772E1B65247
     * shop_name :
     全新，兰蔻极光水50ml
     兰蔻极光水中样50ml全新 全新
     * shop_num : 1
     */

    private String img;
    private int shop_eb;
    private String id;
    private String shop_name;
    private int shop_num;
    private boolean check;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getShop_eb() {
        return shop_eb;
    }

    public void setShop_eb(int shop_eb) {
        this.shop_eb = shop_eb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public int getShop_num() {
        return shop_num;
    }

    public void setShop_num(int shop_num) {
        this.shop_num = shop_num;
    }
}
