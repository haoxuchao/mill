package com.cn.android.bean;

import java.io.Serializable;
import java.util.List;

public class OrderBean implements Serializable {

    /**
     * userphne : 15646767679
     * address : 新疆维吾尔自治区	阿克苏地区	阿克苏市诗朗诵婆婆工农桥路虎
     * send_time :
     * shopList : [{"img":"http://192.168.0.121:8000/upload/516cd24e7d2e4e36aa4e8d6bf82fc067.jpg","shop_eb":45,"id":"260324F5BCDC396352D89EFBDFFC1EC8","shop_num":1}]
     * ordercode : 5219243211
     * ctime : 2020-10-20 18:30:08
     * shop_total_num : 1
     * shop_total_eb : 45
     * username : 陪老婆咯破
     * pay_time :
     * status : 1
     */

    private String userphne;
    private String address;
    private String send_time;
    private String ordercode;
    private String ctime;
    private int shop_total_num;
    private int shop_total_eb;
    private String username;
    private String pay_time;
    private int status;
    private List<ShopListBean> shopList;

    public String getUserphne() {
        return userphne;
    }

    public void setUserphne(String userphne) {
        this.userphne = userphne;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public int getShop_total_num() {
        return shop_total_num;
    }

    public void setShop_total_num(int shop_total_num) {
        this.shop_total_num = shop_total_num;
    }

    public int getShop_total_eb() {
        return shop_total_eb;
    }

    public void setShop_total_eb(int shop_total_eb) {
        this.shop_total_eb = shop_total_eb;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ShopListBean> getShopList() {
        return shopList;
    }

    public void setShopList(List<ShopListBean> shopList) {
        this.shopList = shopList;
    }

    public static class ShopListBean  implements Serializable{

        /**
         * img : http://192.168.0.121:8000/upload/dd6657558698447a9573863bce70b41a.jpg
         * shop_eb : 12
         * id : 57E5003692E5314E6AFD33F38D1C0CD4
         * shop_name : PK哦婆婆蓉蓉
         * shop_num : 2
         */

        private String img;
        private int shop_eb;
        private String id;
        private String shop_name;
        private int shop_num;

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
}
