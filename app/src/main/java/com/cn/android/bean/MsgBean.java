package com.cn.android.bean;

public class MsgBean {

    /**
     * id : 5
     * type : 1
     * title : 账号到期
     * contentImg :
     * usreid : 012967
     * ctime :
     * status : 1
     */

    private String id;
    private int type;
    private String title;
    private String contentImg;
    private String usreid;
    private String ctime;
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentImg() {
        return contentImg;
    }

    public void setContentImg(String contentImg) {
        this.contentImg = contentImg;
    }

    public String getUsreid() {
        return usreid;
    }

    public void setUsreid(String usreid) {
        this.usreid = usreid;
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
