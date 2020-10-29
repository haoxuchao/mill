package com.cn.android.bean;

public class GoodsBean {

    /**
     * imgs : http://192.168.0.121:8000/upload/c5005829611f493fa453d97ae11691fb.jpg,http://192.168.0.121:8000/upload/5e4ae980db114da085e7802e540573c1.jpghttp://192.168.0.121:8000/upload/5e4ae980db114da085e7802e540573c1.jpg,http://192.168.0.121:8000/upload/ee0024cafbc34a4e803ca2db625a4730.jpghttp://192.168.0.121:8000/upload/ee0024cafbc34a4e803ca2db625a4730.jpg
     * img : http://192.168.0.121:8000/upload/c5005829611f493fa453d97ae11691fb.jpg
     * head_img : http://192.168.0.137:8000/upload/e128b475a3f74e5fb1587c9db3ceab8e.jpg
     * nickname : ffgghsdf
     * id : B16F7B876F1456B270547BB42DB7938B
     * title : 给摸嗖嗖嗖
     * eb : 127
     */

    private String imgs;
    private String img;
    private String head_img;
    private String nickname;
    private String id;
    private String title;
    private double eb;

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getEb() {
        return eb;
    }

    public void setEb(double eb) {
        this.eb = eb;
    }
}
