package com.cn.android.bean;

import java.io.Serializable;

public class Address implements Serializable {

    /**
     * id : A0A04DD13F3EF675DC0AE47D259353BB
     * name : disjd
     * phone : 1232132432
     * proCityArea : sd
     * address : fdfdfdsfsdfdsfdsfd
     * userid : 012967
     * ctime : 2020-10-20 15:37:20
     * status : 1
     * isDefault : 1
     */

    private String id;
    private String name;
    private String phone;
    private String proCityArea;
    private String address;
    private String userid;
    private String ctime;
    private int status;
    private int isDefault;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProCityArea() {
        return proCityArea;
    }

    public void setProCityArea(String proCityArea) {
        this.proCityArea = proCityArea;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
}
