package com.cn.android.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class TradingOrder implements MultiItemEntity {

    /**
     * id : 44CEB5DB907859284C8AB5BA35EBC9E8
     * type : 1
     * userid : 012967
     * minBiNum : 10
     * maxBiNum : 100
     * biOneMoney : 10
     * totalMoney : 0
     * ctime : 2020-10-27 14:16:23
     * title :
     * buyAccountid :
     * status : 1
     * isDelete : 1
     * payTime :
     * addTime :
     * pid :
     * ordercode :
     */

    private String id;
    private int type;
    private String userid;
    private String minBiNum;
    private String maxBiNum;
    private double biOneMoney;
    private double totalMoney;
    private String ctime;
    private String title;
    private String buyAccountid;
    private int status;
    private int isDelete;
    private String payTime;
    private String addTime;
    private String pid;
    private String ordercode;

    @Override
    public int getItemType() {
        return type;
    }

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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMinBiNum() {
        return minBiNum;
    }

    public void setMinBiNum(String minBiNum) {
        this.minBiNum = minBiNum;
    }

    public String getMaxBiNum() {
        return maxBiNum;
    }

    public void setMaxBiNum(String maxBiNum) {
        this.maxBiNum = maxBiNum;
    }

    public double  getBiOneMoney() {
        return biOneMoney;
    }

    public void setBiOneMoney(double biOneMoney) {
        this.biOneMoney = biOneMoney;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBuyAccountid() {
        return buyAccountid;
    }

    public void setBuyAccountid(String buyAccountid) {
        this.buyAccountid = buyAccountid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }
}
