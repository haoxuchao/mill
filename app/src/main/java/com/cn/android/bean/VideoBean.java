package com.cn.android.bean;

public class VideoBean {

    /**
     * is_focus : 2
     * praise_num : 0
     * head_img : http://192.168.0.137:8000/upload/e128b475a3f74e5fb1587c9db3ceab8e.jpg
     * nickname : ffgghsdf
     * shopid : B16F7B876F1456B270547BB42DB7938B
     * is_praise : 2
     * id : 82B82CED85D845465D37B8C99D3F09CE
     * userid : 012967
     * url : http://192.168.0.121:8000/upload/f523e75f908945fb94331b5b17099ef0.mp4
     */

    private int is_focus;
    private String praise_num;
    private String head_img;
    private String shop_name;
    private String nickname;
    private String shopid;
    private int is_praise;
    private String id;
    private String userid;
    private String url;

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public int getIs_focus() {
        return is_focus;
    }

    public void setIs_focus(int is_focus) {
        this.is_focus = is_focus;
    }

    public int getPraise_num() {
        return Integer.parseInt(praise_num);
    }

    public void setPraise_num(String praise_num) {
        this.praise_num = praise_num;
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

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public int getIs_praise() {
        return is_praise;
    }

    public void setIs_praise(int is_praise) {
        this.is_praise = is_praise;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
