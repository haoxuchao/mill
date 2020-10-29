package com.cn.android.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MillBean implements MultiItemEntity {


    /**
     * produc_eb_num : 66
     * duihuan_eb_num : 650
     * etime :
     * name : 大型分红矿机
     * model : 大型
     * stime :
     * id : 18
     * time : 待释放EB不低于650
     * is_use : 1
     * category : 2
     * type : 2
     */

    private int produc_eb_num;
    private int duihuan_eb_num;
    private String etime;
    private String name;
    private String model;
    private String stime;
    private String id;
    private String time;
    private int is_use;
    private int category;
    private int type;

    @Override
    public int getItemType() {
        return type;
    }


    public int getProduc_eb_num() {
        return produc_eb_num;
    }

    public void setProduc_eb_num(int produc_eb_num) {
        this.produc_eb_num = produc_eb_num;
    }

    public int getDuihuan_eb_num() {
        return duihuan_eb_num;
    }

    public void setDuihuan_eb_num(int duihuan_eb_num) {
        this.duihuan_eb_num = duihuan_eb_num;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIs_use() {
        return is_use;
    }

    public void setIs_use(int is_use) {
        this.is_use = is_use;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
