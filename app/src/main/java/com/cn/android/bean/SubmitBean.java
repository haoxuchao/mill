package com.cn.android.bean;

import java.io.Serializable;
import java.util.List;

public class SubmitBean implements Serializable {

    /**
     * ordercode : 0011211938
     * total_num : 1
     * total_eb : 45
     * shopList : [{"img":"http://192.168.0.121:8000/upload/516cd24e7d2e4e36aa4e8d6bf82fc067.jpg","num":1,"eb":45}]
     * address : {"isDefault":1,"address":"fdfdfdsfsdfdsfdsfd","proCityArea":"sd","phone":"1232132432","name":"disjd","ctime":"2020-10-20 15:37:20","id":"A0A04DD13F3EF675DC0AE47D259353BB","userid":"012967","status":1}
     */

    private String ordercode;
    private int total_num;
    private double total_eb;
    private Address address;
    private List<ShopListBean> shopList;

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }

    public double getTotal_eb() {
        return total_eb;
    }

    public void setTotal_eb(int total_eb) {
        this.total_eb = total_eb;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<ShopListBean> getShopList() {
        return shopList;
    }

    public void setShopList(List<ShopListBean> shopList) {
        this.shopList = shopList;
    }


    public  class ShopListBean implements Serializable{
        /**
         * img : http://192.168.0.121:8000/upload/516cd24e7d2e4e36aa4e8d6bf82fc067.jpg
         * num : 1
         * eb : 45
         */

        private String img;
        private String name;
        private int num;
        private double eb;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public double getEb() {
            return eb;
        }

        public void setEb(double eb) {
            this.eb = eb;
        }
    }
}
