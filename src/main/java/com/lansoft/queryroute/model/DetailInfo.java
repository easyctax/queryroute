package com.lansoft.queryroute.model;

public class DetailInfo {
    private String row_index = ""; // 记录序号

    private String sectionNumber = ""; // 区局号

    private String equipAreaNum = ""; // 设备局号

    private String equipType = ""; // 设备类型

    private String equipName = ""; // 设备名称

    private String linkPlace = ""; // 连接位置

    private String electrocircuitID = ""; // 电路标识

    private int Id; // 序号

    private String balkNo; // 受理单号

    private int summaryId;// 路由概要信息ID

    private String stlNumber = ""; // 专线号

    private String modeNum = ""; // 模块序号

    private String equipAreaName = "";// 设备局名称

    private String remark = "";// 备注

    private String copRoomName;//机房名称 2015-09-23 杨育鹏

    public String getRow_index () {
        return row_index;
    }

    public void setRow_index (String row_index) {
        this.row_index = row_index;
    }

    public String getSectionNumber () {
        return sectionNumber;
    }

    public void setSectionNumber (String sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public String getEquipAreaNum () {
        return equipAreaNum;
    }

    public void setEquipAreaNum (String equipAreaNum) {
        this.equipAreaNum = equipAreaNum;
    }

    public String getEquipType () {
        return equipType;
    }

    public void setEquipType (String equipType) {
        this.equipType = equipType;
    }

    public String getEquipName () {
        return equipName;
    }

    public void setEquipName (String equipName) {
        this.equipName = equipName;
    }

    public String getLinkPlace () {
        return linkPlace;
    }

    public void setLinkPlace (String linkPlace) {
        this.linkPlace = linkPlace;
    }

    public String getElectrocircuitID () {
        return electrocircuitID;
    }

    public void setElectrocircuitID (String electrocircuitID) {
        this.electrocircuitID = electrocircuitID;
    }

    public int getId () {
        return Id;
    }

    public void setId (int id) {
        Id = id;
    }

    public String getBalkNo () {
        return balkNo;
    }

    public void setBalkNo (String balkNo) {
        this.balkNo = balkNo;
    }

    public int getSummaryId () {
        return summaryId;
    }

    public void setSummaryId (int summaryId) {
        this.summaryId = summaryId;
    }

    public String getStlNumber () {
        return stlNumber;
    }

    public void setStlNumber (String stlNumber) {
        this.stlNumber = stlNumber;
    }

    public String getModeNum () {
        return modeNum;
    }

    public void setModeNum (String modeNum) {
        this.modeNum = modeNum;
    }

    public String getEquipAreaName () {
        return equipAreaName;
    }

    public void setEquipAreaName (String equipAreaName) {
        this.equipAreaName = equipAreaName;
    }

    public String getRemark () {
        return remark;
    }

    public void setRemark (String remark) {
        this.remark = remark;
    }

    public String getCopRoomName () {
        return copRoomName;
    }

    public void setCopRoomName (String copRoomName) {
        this.copRoomName = copRoomName;
    }
}
