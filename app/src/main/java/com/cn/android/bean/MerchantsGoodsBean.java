package com.cn.android.bean;

import java.io.Serializable;

public class MerchantsGoodsBean  implements Serializable {

    /**
     * id : 9282BC2ABD0E772FB045E67EEF1EBBA2
     * userid : 012967
     * name : null
     * img : http://192.168.0.121:8000/upload/516cd24e7d2e4e36aa4e8d6bf82fc067.jpg
     * imgs : http://192.168.0.121:8000/upload/516cd24e7d2e4e36aa4e8d6bf82fc067.jpg,http://192.168.0.121:8000/upload/6f867212fbd445afbe026768eed51038.jpg,http://192.168.0.121:8000/upload/b995ce5001f54a1e9aad9a7bbea8de73.jpg,http://192.168.0.121:8000/upload/096c2ccfd5c642febfeff7bd3884d561.jpg
     * title : 给克咯考虑咯啦咯啦咯啦咯考虑图他
     * eb : 45
     * num : 4
     * isVideo : 2
     * ctime : 2020-10-19 15:37:33
     * status : 1
     */

    private String id;
    private String userid;
    private String name;
    private String img;
    private String imgs;
    private String title;
    private double eb;
    private int num;
    private int isVideo;
    private String ctime;
    private int status;

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

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(int isVideo) {
        this.isVideo = isVideo;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
