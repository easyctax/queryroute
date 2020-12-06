package com.lansoft.queryroute.model;

import java.util.List;

public class RouteInfo {
    private String row_index = ""; // 记录序号

    private String strSTLNumber = ""; // 专线号

    private String strOperType = ""; // 业务类别

    private String gd = ""; // 工单号

    private String strCustomerName = ""; // 客户名称

    private String strProjectName = ""; // 工程项目

    private String strPointA = ""; // A端地址

    private String strPointZ = ""; // Z端地址

    private String strCircuitCode = ""; // 电路编号

    private String strVelocity = ""; // 速率

    private String strAssiStartDate = ""; // 发单时间

    private String strAssiFinishDate = ""; // 实际完成时间

    private String strCircuiteSymbol = ""; // 电路代号

    private String strRoute = ""; // 路由

    private String strRouteId = ""; // 路由ID

    private List<DetailInfo> detailInfoList;//详细信息结果集

    private String queryModeFlag = "1";//新全程路由库或者旧全程路由库标识

    private int Id; //序号

    private String balkNo; //受理单号

    private String routeRemark = ""; //备注

    private String gdState = "";//工单状态

    private String strSTLNumberID = "";

    private String servicelevel;//SLA网络运行服务等级

    private String zbflg;//重保标志

    private String zbtimes;//每日重保时段

    private String zblevel;//重保等级

    private String cust_manager;//客户经理姓名

    private String cust_manager_tel;//客户经理电话

    private String user_ip;//

    private String rd;

    private String rt;

    private Integer currMonthCount = -1;

    private String cirCuitCareCop;

    private String custCode;//客户编码 2014-09-24 杨育鹏

    private String yewuLeixing;//业务类型， 1为专线2为中继

    private String jifeiLeixing;

    public String getRow_index () {
        return row_index;
    }

    public void setRow_index (String row_index) {
        this.row_index = row_index;
    }

    public String getStrSTLNumber () {
        return strSTLNumber;
    }

    public void setStrSTLNumber (String strSTLNumber) {
        this.strSTLNumber = strSTLNumber;
    }

    public String getStrOperType () {
        return strOperType;
    }

    public void setStrOperType (String strOperType) {
        this.strOperType = strOperType;
    }

    public String getGd () {
        return gd;
    }

    public void setGd (String gd) {
        this.gd = gd;
    }

    public String getStrCustomerName () {
        return strCustomerName;
    }

    public void setStrCustomerName (String strCustomerName) {
        this.strCustomerName = strCustomerName;
    }

    public String getStrProjectName () {
        return strProjectName;
    }

    public void setStrProjectName (String strProjectName) {
        this.strProjectName = strProjectName;
    }

    public String getStrPointA () {
        return strPointA;
    }

    public void setStrPointA (String strPointA) {
        this.strPointA = strPointA;
    }

    public String getStrPointZ () {
        return strPointZ;
    }

    public void setStrPointZ (String strPointZ) {
        this.strPointZ = strPointZ;
    }

    public String getStrCircuitCode () {
        return strCircuitCode;
    }

    public void setStrCircuitCode (String strCircuitCode) {
        this.strCircuitCode = strCircuitCode;
    }

    public String getStrVelocity () {
        return strVelocity;
    }

    public void setStrVelocity (String strVelocity) {
        this.strVelocity = strVelocity;
    }

    public String getStrAssiStartDate () {
        return strAssiStartDate;
    }

    public void setStrAssiStartDate (String strAssiStartDate) {
        this.strAssiStartDate = strAssiStartDate;
    }

    public String getStrAssiFinishDate () {
        return strAssiFinishDate;
    }

    public void setStrAssiFinishDate (String strAssiFinishDate) {
        this.strAssiFinishDate = strAssiFinishDate;
    }

    public String getStrCircuiteSymbol () {
        return strCircuiteSymbol;
    }

    public void setStrCircuiteSymbol (String strCircuiteSymbol) {
        this.strCircuiteSymbol = strCircuiteSymbol;
    }

    public String getStrRoute () {
        return strRoute;
    }

    public void setStrRoute (String strRoute) {
        this.strRoute = strRoute;
    }

    public String getStrRouteId () {
        return strRouteId;
    }

    public void setStrRouteId (String strRouteId) {
        this.strRouteId = strRouteId;
    }

    public List<DetailInfo> getDetailInfoList () {
        return detailInfoList;
    }

    public void setDetailInfoList (List<DetailInfo> detailInfoList) {
        this.detailInfoList = detailInfoList;
    }

    public String getQueryModeFlag () {
        return queryModeFlag;
    }

    public void setQueryModeFlag (String queryModeFlag) {
        this.queryModeFlag = queryModeFlag;
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

    public String getRouteRemark () {
        return routeRemark;
    }

    public void setRouteRemark (String routeRemark) {
        this.routeRemark = routeRemark;
    }

    public String getGdState () {
        return gdState;
    }

    public void setGdState (String gdState) {
        this.gdState = gdState;
    }

    public String getStrSTLNumberID () {
        return strSTLNumberID;
    }

    public void setStrSTLNumberID (String strSTLNumberID) {
        this.strSTLNumberID = strSTLNumberID;
    }

    public String getServicelevel () {
        return servicelevel;
    }

    public void setServicelevel (String servicelevel) {
        this.servicelevel = servicelevel;
    }

    public String getZbflg () {
        return zbflg;
    }

    public void setZbflg (String zbflg) {
        this.zbflg = zbflg;
    }

    public String getZbtimes () {
        return zbtimes;
    }

    public void setZbtimes (String zbtimes) {
        this.zbtimes = zbtimes;
    }

    public String getZblevel () {
        return zblevel;
    }

    public void setZblevel (String zblevel) {
        this.zblevel = zblevel;
    }

    public String getCust_manager () {
        return cust_manager;
    }

    public void setCust_manager (String cust_manager) {
        this.cust_manager = cust_manager;
    }

    public String getCust_manager_tel () {
        return cust_manager_tel;
    }

    public void setCust_manager_tel (String cust_manager_tel) {
        this.cust_manager_tel = cust_manager_tel;
    }

    public String getUser_ip () {
        return user_ip;
    }

    public void setUser_ip (String user_ip) {
        this.user_ip = user_ip;
    }

    public String getRd () {
        return rd;
    }

    public void setRd (String rd) {
        this.rd = rd;
    }

    public String getRt () {
        return rt;
    }

    public void setRt (String rt) {
        this.rt = rt;
    }

    public Integer getCurrMonthCount () {
        return currMonthCount;
    }

    public void setCurrMonthCount (Integer currMonthCount) {
        this.currMonthCount = currMonthCount;
    }

    public String getCirCuitCareCop () {
        return cirCuitCareCop;
    }

    public void setCirCuitCareCop (String cirCuitCareCop) {
        this.cirCuitCareCop = cirCuitCareCop;
    }

    public String getCustCode () {
        return custCode;
    }

    public void setCustCode (String custCode) {
        this.custCode = custCode;
    }

    public String getYewuLeixing () {
        return yewuLeixing;
    }

    public void setYewuLeixing (String yewuLeixing) {
        this.yewuLeixing = yewuLeixing;
    }
}
