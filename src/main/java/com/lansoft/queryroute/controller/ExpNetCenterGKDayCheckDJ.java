package com.lansoft.queryroute.controller;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.Mail;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

//超级中台日报-早报
public class ExpNetCenterGKDayCheckDJ {
	// 超时预约
	private static final String SQL_Z_X_1 = "SELECT GO.GROUP_NAME 分公司, A.C, A.C1 FROM (SELECT T.FIRST_DEPT_ID, COUNT(*) C, SUM(DECODE(SIGN(NVL(P.RESERVATION_USER_TIME, (NVL(T.BALK_AUDIT_TIME,SYSDATE) - T.ACCEPT_TIME) * 1440) - 1440), 1, 1, 0)) C1 FROM GKX_LAST_PAI_HIS T, PROD_PSTN_ADSL_ISDN P WHERE T.BALK_NO = P.BALK_NO AND T.ACCEPT_TIME < TRUNC(SYSDATE )  AND T.ACCEPT_TIME >= TRUNC(SYSDATE-1 )  AND T.CUST_TYPE_ID = 1 AND T.BALK_SUB_SORT_ID = 1001 AND T.ACCEPT_TIME >= TO_DATE('20180701', 'YYYYMMDD') GROUP BY T.FIRST_DEPT_ID) A, IFM_SYS_GROUP_GKX_ORDER GO WHERE GO.GROUP_ID = A.FIRST_DEPT_ID ORDER BY GO.GROUP_ORDER";
	private static final String SQL_Z_X_1_DJ = "SELECT GO.GKX_ABBREV 分公司, GO.CELIANG_ABBREV, NVL(A.C, 0), NVL(A.C1, 0) FROM (SELECT T.FIRST_DEPT_ID, T.PROCESS_GROUP_ID, COUNT(*) C, SUM(DECODE(SIGN(NVL(P.RESERVATION_USER_TIME, (NVL(T.BALK_AUDIT_TIME, SYSDATE) - T.ACCEPT_TIME) * 1440) - 1440), 1, 1, 0)) C1 FROM V_BEFORE_YESTADAY_BALK T, PROD_PSTN_ADSL_ISDN P WHERE T.受理单号 = P.BALK_NO GROUP BY T.FIRST_DEPT_ID, T.PROCESS_GROUP_ID) A, IFM_SYS_GROUP_CELIANG_ORDER GO WHERE GO.CELIANG_GROUP_ID = A.PROCESS_GROUP_ID(+) ORDER BY GO.GROUP_ORDER";
	private static final String SQL_Z_W_1 = "SELECT GO.GROUP_NAME 分公司, NVL(A.C, 0) C, NVL(A.C1, 0) C1 FROM (SELECT GC.GKX_GROUP_ID, COUNT(*) C, SUM(DECODE(SIGN(NVL(A.OPER_TIME, NVL(T1.RETURN_TIME, SYSDATE)) - T1.MOS_DEAL_DATE - 1), 1, 1, 0)) C1 FROM (SELECT T.WORK_ID, MIN(T.OPER_TIME) OPER_TIME FROM T_IOM_SHEET_RESERVATION T WHERE T.OPER_TIME >= TRUNC(SYSDATE - 1) GROUP BY T.WORK_ID) A, T_IOM_SHEET T1, IFM_SYS_GKX_CELIANG GC, IFM_SYS_CELIANG_DAIWEI CD, CFG_REPAIR_MAN CR WHERE GC.CELIANG_GROUP_ID = CD.CELIANG_GROUP_ID AND CD.DAIWEI_GROUP_ID = CR.CR_GROUP_ID AND T1.FETCH_LOGIN_NO = CR.CR_USERNAME AND T1.WORK_ID = A.WORK_ID(+) AND T1.WORK_TYPE = 0 AND T1.WORK_STATUS <> 4 AND T1.FLOW_NODE = '2003' AND T1.MOS_DEAL_DATE >= TRUNC(SYSDATE - 1) AND T1.MOS_DEAL_DATE < TRUNC(SYSDATE) GROUP BY GC.GKX_GROUP_ID) A, IFM_SYS_GROUP_GKX_ORDER GO WHERE GO.GROUP_ID = A.GKX_GROUP_ID(+) ORDER BY GO.GROUP_ORDER";
	private static final String SQL_Z_W_1_DJ = "SELECT GO.GKX_ABBREV 分公司, GO.CELIANG_ABBREV, NVL(A.C, 0) C, NVL(A.C1, 0) C1 FROM (SELECT GC.GKX_GROUP_ID, GC.CELIANG_GROUP_ID, COUNT(*) C, SUM(DECODE(SIGN(NVL(A.OPER_TIME, NVL(T1.RETURN_TIME, SYSDATE)) - T1.MOS_DEAL_DATE - 1), 1, 1, 0)) C1 FROM (SELECT T.WORK_ID, MIN(T.OPER_TIME) OPER_TIME FROM T_IOM_SHEET_RESERVATION T WHERE T.OPER_TIME >= TRUNC(SYSDATE - 2) GROUP BY T.WORK_ID) A, T_IOM_SHEET T1, IFM_SYS_GKX_CELIANG GC, IFM_SYS_CELIANG_DAIWEI CD, CFG_REPAIR_MAN CR WHERE GC.CELIANG_GROUP_ID = CD.CELIANG_GROUP_ID AND CD.DAIWEI_GROUP_ID = CR.CR_GROUP_ID AND T1.FETCH_LOGIN_NO = CR.CR_USERNAME AND T1.WORK_ID = A.WORK_ID(+) AND T1.WORK_TYPE = 0 AND T1.WORK_STATUS <> 4 AND T1.FLOW_NODE = '2003' AND T1.MOS_DEAL_DATE >= TRUNC(SYSDATE - 2) AND T1.MOS_DEAL_DATE < TRUNC(SYSDATE - 1) GROUP BY GC.GKX_GROUP_ID, GC.CELIANG_GROUP_ID) A, IFM_SYS_GROUP_CELIANG_ORDER GO WHERE GO.CELIANG_GROUP_ID = A.CELIANG_GROUP_ID(+) ORDER BY GO.GROUP_ORDER";
	// 预约时间修改率
	private static final String SQL_Z_X_7 = "SELECT G.GROUP_NAME, NVL(T2.NUM, 0) 昨日在途数, NVL(T1.NUM, 0) 昨日预约数 FROM (SELECT T.FIRST_DEPT_ID, COUNT(DISTINCT(R.BALK_NO)) NUM FROM BALK_BASIC T, BALK_RESERVATION_REMARK_APPLY R WHERE R.BALK_NO = T.BALK_NO AND T.BALK_SUB_SORT_ID = 1001 AND T.CUST_TYPE_ID = 1 AND R.OPER_TIME >= TRUNC(SYSDATE - 1) AND R.OPER_TIME < TRUNC(SYSDATE) AND T.ACCEPT_TIME >= TO_DATE('20180701', 'YYYYMMDD') GROUP BY T.FIRST_DEPT_ID) T1, (SELECT T.FIRST_DEPT_ID, COUNT(DISTINCT(R.BALK_NO)) NUM FROM BALK_BASIC T, BALK_DISPCH_RESERVATION_REMARK R WHERE R.BALK_NO = T.BALK_NO AND (T.BALK_AUDIT_TIME >= TRUNC(SYSDATE - 1) OR T.BALK_STATUS_ID < 5) AND R.OPER_TIME < TRUNC(SYSDATE) AND T.BALK_SUB_SORT_ID = 1001 AND T.CUST_TYPE_ID = 1 AND T.ACCEPT_TIME >= TO_DATE('20180701', 'YYYYMMDD') GROUP BY T.FIRST_DEPT_ID) T2, IFM_SYS_GROUP_GKX_ORDER G WHERE T1.FIRST_DEPT_ID(+) = G.GROUP_ID  AND T2.FIRST_DEPT_ID = G.GROUP_ID ORDER BY G.GROUP_ORDER";
	private static final String SQL_Z_X_7_DJ = "SELECT G.GKX_ABBREV, G.CELIANG_ABBREV, NVL(T2.NUM, 0) 昨日在途数, NVL(T1.NUM, 0) 昨日预约数 FROM (SELECT T.FIRST_DEPT_ID, T.PROCESS_GROUP_ID, COUNT(DISTINCT(R.BALK_NO)) NUM FROM BALK_BASIC T, BALK_RESERVATION_REMARK_APPLY R WHERE R.BALK_NO = T.BALK_NO AND T.BALK_SUB_SORT_ID = 1001 AND T.CUST_TYPE_ID = 1 AND R.OPER_TIME >= TRUNC(SYSDATE - 1) AND R.OPER_TIME < TRUNC(SYSDATE) AND T.ACCEPT_TIME >= TO_DATE('20180701', 'YYYYMMDD') GROUP BY T.FIRST_DEPT_ID, T.PROCESS_GROUP_ID) T1, (SELECT T.FIRST_DEPT_ID, T.PROCESS_GROUP_ID, COUNT(DISTINCT(R.BALK_NO)) NUM FROM BALK_BASIC T, BALK_DISPCH_RESERVATION_REMARK R WHERE R.OPER_TIME < TRUNC(SYSDATE) AND R.BALK_NO = T.BALK_NO AND (T.BALK_AUDIT_TIME >= TRUNC(SYSDATE - 1) OR T.BALK_STATUS_ID < 5) AND T.BALK_SUB_SORT_ID = 1001 AND T.CUST_TYPE_ID = 1 AND T.ACCEPT_TIME >= TO_DATE('20180701', 'YYYYMMDD') GROUP BY T.FIRST_DEPT_ID, T.PROCESS_GROUP_ID) T2, IFM_SYS_GROUP_CELIANG_ORDER G WHERE T1.PROCESS_GROUP_ID(+) = G.CELIANG_GROUP_ID AND T2.PROCESS_GROUP_ID(+) = G.CELIANG_GROUP_ID ORDER BY G.GROUP_ORDER";
	private static final String SQL_Z_W_7 = "SELECT G.GROUP_NAME, NVL(T2.NUM, 0) 昨日在途工单数, NVL(T1.NUM, 0) 昨日修改数 FROM (SELECT T.FILIALE_ID, COUNT(DISTINCT(R.BALK_NO)) NUM FROM T_PALM_IOM_SHEET_2003_TOTAL T, ZONGHE.BALK_RESERVATION_REMARK_APPLY@IFM_NEW R WHERE R.BALK_NO = T.WORK_ID AND T.WORK_TYPE = 0 AND T.FLOW_NODE = '2003' AND T.WORK_STATUS <= 2 AND R.OPER_TIME >= TRUNC(SYSDATE - 1) AND R.OPER_TIME < TRUNC(SYSDATE) GROUP BY T.FILIALE_ID) T1, (SELECT T.FILIALE_ID, COUNT(DISTINCT(R.WORK_ID)) NUM FROM T_PALM_IOM_SHEET_2003_TOTAL T, T_PALM_IOM_RESERVATION R WHERE R.WORK_ID = T.WORK_ID AND T.WORK_TYPE = 0 AND T.FLOW_NODE = '2003' AND T.WORK_STATUS <= 2 AND (T.RETURN_TIME > TRUNC(SYSDATE) OR T.WORK_STATUS < 2) AND R.OPERATION_TIME < TRUNC(SYSDATE) GROUP BY T.FILIALE_ID) T2, IFM_SYS_GROUP_GKX_ORDER G WHERE G.GROUP_ID = T1.FILIALE_ID(+) AND G.GROUP_ID = T2.FILIALE_ID ORDER BY G.GROUP_ORDER";
	private static final String SQL_Z_W_7_DJ = "SELECT G.GKX_ABBREV, G.CELIANG_ABBREV, NVL(T2.NUM, 0) 昨日在途工单数, NVL(T1.NUM, 0) 昨日修改数 FROM (SELECT T.FILIALE_ID, T.BUREAU_ID, COUNT(DISTINCT(R.BALK_NO)) NUM FROM T_PALM_IOM_SHEET_2003_TOTAL T, ZONGHE.BALK_RESERVATION_REMARK_APPLY@IFM_NEW R WHERE R.BALK_NO = T.WORK_ID AND T.WORK_TYPE = 0 AND T.FLOW_NODE = '2003' AND T.WORK_STATUS <= 2 AND R.OPER_TIME >= TRUNC(SYSDATE - 1) AND R.OPER_TIME < TRUNC(SYSDATE) GROUP BY T.FILIALE_ID, T.BUREAU_ID) T1, (SELECT T.FILIALE_ID, T.BUREAU_ID, COUNT(DISTINCT(R.WORK_ID)) NUM FROM T_PALM_IOM_SHEET_2003_TOTAL T, T_PALM_IOM_RESERVATION R WHERE R.WORK_ID = T.WORK_ID AND T.WORK_TYPE = 0 AND T.FLOW_NODE = '2003' AND T.WORK_STATUS <= 2 AND (T.RETURN_TIME > TRUNC(SYSDATE) OR T.WORK_STATUS < 2) AND R.OPERATION_TIME < TRUNC(SYSDATE) GROUP BY T.FILIALE_ID, T.BUREAU_ID) T2, ZONGHE.IFM_SYS_GROUP_CELIANG_ORDER@IFM_NEW G WHERE G.CELIANG_GROUP_ID = T1.BUREAU_ID(+) AND G.CELIANG_GROUP_ID = T2.BUREAU_ID(+) ORDER BY G.GROUP_ORDER";
	// 未按时交付率
	private static final String SQL_Z_X_6 = "SELECT GO.GROUP_NAME, NVL(A.C, 0) 昨天预约工单数, NVL(B.C, 0) 昨天预约未完成数 FROM (SELECT T.FIRST_DEPT_ID, COUNT(*) C FROM BALK_BASIC T, PROD_PSTN_ADSL_ISDN P WHERE T.BALK_NO = P.BALK_NO AND P.RESERVATION_TIME >= TRUNC(SYSDATE - 1) AND P.RESERVATION_TIME < TRUNC(SYSDATE) AND T.ACCEPT_TIME >= TO_DATE('20180701', 'YYYYMMDD') AND T.CUST_TYPE_ID = 1 AND T.BALK_SUB_SORT_ID = 1001 GROUP BY T.FIRST_DEPT_ID) A, (SELECT T.FIRST_DEPT_ID, COUNT(*) C FROM GKX_LAST_PAI T, PROD_PSTN_ADSL_ISDN P WHERE P.RESERVATION_TIME < TRUNC(SYSDATE) AND P.RESERVATION_TIME >= TRUNC(SYSDATE - 1) AND T.BALK_NO = P.BALK_NO AND T.CUST_TYPE_ID = 1 AND T.BALK_SUB_SORT_ID = 1001 GROUP BY T.FIRST_DEPT_ID) B, IFM_SYS_GROUP_GKX_ORDER GO WHERE A.FIRST_DEPT_ID(+) = GO.GROUP_ID AND B.FIRST_DEPT_ID(+) = GO.GROUP_ID ORDER BY GO.GROUP_ORDER";
	private static final String SQL_Z_X_6_DJ = "SELECT GO.GKX_ABBREV, GO.CELIANG_ABBREV, NVL(A.C, 0) 昨天预约工单数, NVL(A.C1, 0) 昨天预约未完成数 FROM (SELECT T.FIRST_DEPT_ID, T.PROCESS_GROUP_ID, COUNT(*) C, SUM(DECODE(T.BALK_STATUS_ID, 5, 0, 1)) C1 FROM BALK_BASIC T, PROD_PSTN_ADSL_ISDN P WHERE T.BALK_NO = P.BALK_NO AND P.RESERVATION_TIME >= TRUNC(SYSDATE - 1) AND P.RESERVATION_TIME < TRUNC(SYSDATE) AND T.ACCEPT_TIME >= TO_DATE('20180701', 'YYYYMMDD') AND T.CUST_TYPE_ID = 1 AND T.BALK_SUB_SORT_ID = 1001 GROUP BY T.FIRST_DEPT_ID, T.PROCESS_GROUP_ID) A, IFM_SYS_GROUP_CELIANG_ORDER GO WHERE A.PROCESS_GROUP_ID(+) = GO.CELIANG_GROUP_ID ORDER BY GO.GROUP_ORDER";
	private static final String SQL_Z_W_6 = "SELECT G.GROUP_NAME, NVL(T1.NUM, 0) 预约工单数, NVL(T2.NUM, 0) 未完成预约工单数 FROM (SELECT T.FILIALE_ID, COUNT(*) NUM FROM T_PALM_IOM_SHEET T WHERE T.SECOND_PRE_DATE >= TRUNC(SYSDATE - 1) AND T.SECOND_PRE_DATE < TRUNC(SYSDATE) AND T.WORK_STATUS <= 2 AND T.WORK_TYPE = 0 GROUP BY T.FILIALE_ID) T1, (SELECT GC.GKX_GROUP_ID, COUNT(*) NUM FROM T_PALM_IOM_SHEET T, ZONGHE.IFM_SYS_GKX_CELIANG@IFM_NEW GC, ZONGHE.CFG_REPAIR_MAN@IFM_NEW C WHERE GC.CELIANG_GROUP_ID = T.BUREAU_ID AND C.CR_USERNAME = T.FETCH_LOGIN_NO AND T.WORK_TYPE = 0 AND T.APPLY_DATE >= TO_DATE('20180701', 'yyyymmdd') AND T.SECOND_PRE_DATE < TRUNC(SYSDATE) AND T.SECOND_PRE_DATE >= TRUNC(SYSDATE - 1) AND T.WORK_STATUS < 2 GROUP BY GKX_GROUP_ID) T2, IFM_SYS_GROUP_GKX_ORDER G WHERE T1.FILIALE_ID(+) = G.GROUP_ID AND T2.GKX_GROUP_ID(+) = G.GROUP_ID ORDER BY G.GROUP_ORDER";
	private static final String SQL_Z_W_6_DJ = "SELECT G.GKX_ABBREV, G.CELIANG_ABBREV, NVL(T2.NUM, 0) 预约工单数, NVL(T2.NUM1, 0) 未完成预约工单数 FROM (SELECT GC.GKX_GROUP_ID, GC.CELIANG_GROUP_ID, COUNT(*) NUM, SUM(DECODE(T.WORK_STATUS, 0, 1, 1, 1, 0)) NUM1 FROM T_PALM_IOM_SHEET T, ZONGHE.IFM_SYS_GKX_CELIANG@IFM_NEW GC, ZONGHE.CFG_REPAIR_MAN@IFM_NEW C WHERE GC.CELIANG_GROUP_ID = T.BUREAU_ID AND C.CR_USERNAME = T.FETCH_LOGIN_NO AND T.WORK_TYPE = 0 AND T.APPLY_DATE >= TO_DATE('20180701', 'yyyymmdd') AND T.SECOND_PRE_DATE < TRUNC(SYSDATE) AND T.SECOND_PRE_DATE >= TRUNC(SYSDATE - 1) AND T.WORK_STATUS <= 2 GROUP BY GKX_GROUP_ID, GC.CELIANG_GROUP_ID) T2, ZONGHE.IFM_SYS_GROUP_CELIANG_ORDER@IFM_NEW G WHERE T2.CELIANG_GROUP_ID(+) = G.CELIANG_GROUP_ID ORDER BY G.GROUP_ORDER";
	// 超时预约明细
	private static final String SQL_Z_X_1_MX = "SELECT GO.GROUP_NAME 分公司, GC.CELIANG_ABBREV 分局, A.维护区域, A.姓名, A.受理单号, A.受理时间, A.预约时间 FROM (SELECT T.FIRST_DEPT_ID, T.PROCESS_GROUP_ID, T.受理单号, T.故障号码, TO_CHAR(T.ACCEPT_TIME, 'yyyy-mm-dd hh24:mi:ss') 受理时间, TO_CHAR(P.RESERVATION_TIME, 'yyyy-mm-dd hh24:mi:ss') 预约时间, T.维护区域, T.姓名, NVL(P.RESERVATION_USER_TIME, (NVL(T.BALK_AUDIT_TIME, SYSDATE) - T.ACCEPT_TIME) * 1440) 预约历时 FROM V_BEFORE_YESTADAY_BALK T, PROD_PSTN_ADSL_ISDN P WHERE T.受理单号 = P.BALK_NO) A, IFM_SYS_GROUP_GKX_ORDER GO, IFM_SYS_GKX_CELIANG GC WHERE A.预约历时 > 1440 AND GC.CELIANG_GROUP_ID = A.PROCESS_GROUP_ID AND GO.GROUP_ID = A.FIRST_DEPT_ID ORDER BY GO.GROUP_ORDER, A.受理单号";
	private static final String SQL_Z_W_1_MX = "SELECT GO.GROUP_NAME 分公司, A.分局, A.社区, A.姓名, A.订单号, A.工单号, A.接单时间, A.预约时间 FROM (SELECT GC.GKX_GROUP_ID, GC.CELIANG_ABBREV 分局, T1.EXEC_DEPT_NAME 社区, TO_CHAR(T1.ORDER_ID) 订单号, T1.WORK_ID 工单号, T1.PRODUCT_NUM 产品号码, A.OPER_TIME, CR.CR_NAME 姓名, TO_CHAR(T1.MOS_DEAL_DATE, 'yyyy-mm-dd hh24:mi:ss') 接单时间, TO_CHAR(T1.SECOND_PRE_DATE, 'yyyy-mm-dd hh24:mi:ss') 预约时间 FROM (SELECT T.WORK_ID, MIN(T.OPER_TIME) OPER_TIME FROM T_IOM_SHEET_RESERVATION T WHERE T.OPER_TIME >= TRUNC(SYSDATE - 2) GROUP BY T.WORK_ID) A, T_IOM_SHEET T1, IFM_SYS_GKX_CELIANG GC, IFM_SYS_CELIANG_DAIWEI CD, CFG_REPAIR_MAN CR WHERE GC.CELIANG_GROUP_ID = CD.CELIANG_GROUP_ID AND CD.DAIWEI_GROUP_ID = CR.CR_GROUP_ID AND T1.FETCH_LOGIN_NO = CR.CR_USERNAME AND NVL(A.OPER_TIME, NVL(T1.RETURN_TIME, SYSDATE)) - T1.MOS_DEAL_DATE > 1 AND T1.WORK_ID = A.WORK_ID(+) AND T1.WORK_STATUS <> 4 AND T1.WORK_TYPE = 0 AND T1.FLOW_NODE = '2003' AND T1.MOS_DEAL_DATE >= TRUNC(SYSDATE - 2) AND T1.MOS_DEAL_DATE < TRUNC(SYSDATE - 1)) A, IFM_SYS_GROUP_GKX_ORDER GO WHERE GO.GROUP_ID = A.GKX_GROUP_ID ORDER BY GO.GROUP_ORDER";
	// 未按时交付明细
	private static final String SQL_Z_X_2_MX = "SELECT GO.GROUP_NAME 分公司, GC.CELIANG_ABBREV 分局, A.维护区域, A.姓名, A.受理单号, A.受理时间, A.预约时间, A.故障截止时间 FROM (SELECT T.BALK_NO 受理单号, T.BALK_PHONE 故障号码, TO_CHAR(T.ACCEPT_TIME, 'yyyy-mm-dd hh24:mi:ss') 受理时间, TO_CHAR(P.RESERVATION_TIME, 'yyyy-mm-dd hh24:mi:ss') 预约时间, TO_CHAR(T.DEADLINE, 'yyyy-mm-dd hh24:mi:ss') 故障截止时间, T.PROCESS_GROUP_ID, R.CR_NAME 姓名, T.CURT_AREA_NAME 维护区域, T.FIRST_DEPT_ID FROM GKX_LAST_PAI T, PROD_PSTN_ADSL_ISDN P,CFG_REPAIR_MAN R WHERE P.RESERVATION_TIME < TRUNC(SYSDATE) AND P.RESERVATION_TIME >= TRUNC(SYSDATE - 1) AND T.BALK_NO = P.BALK_NO AND T.DW_USER_ID=R.CR_ID AND T.CUST_TYPE_ID = 1 AND T.BALK_SUB_SORT_ID = 1001) A, IFM_SYS_GKX_CELIANG GC, IFM_SYS_GROUP_GKX_ORDER GO WHERE A.FIRST_DEPT_ID = GO.GROUP_ID AND GC.CELIANG_GROUP_ID = A.PROCESS_GROUP_ID ORDER BY GO.GROUP_ORDER, A.受理时间 ";
	private static final String SQL_Z_W_2_MX = "SELECT GO.GROUP_NAME 分公司, A.分局, A.社区, A.姓名, A.订单号, A.工单号, TO_CHAR(A.APPLY_DATE, 'yyyy-mm-dd hh24:mi:ss') 受理日期, TO_CHAR(A.SECOND_PRE_DATE, 'yyyy-mm-dd hh24:mi:ss') 预约时间 FROM (SELECT GC.GKX_GROUP_ID, GC.CELIANG_ABBREV 分局, T.EXEC_DEPT_NAME 社区, TO_CHAR(T.ORDER_ID) 订单号, T.WORK_ID 工单号, T.PRODUCT_NUM 产品号码, T.APPLY_DATE, T.SECOND_PRE_DATE, C.CR_NAME 姓名 FROM T_PALM_IOM_SHEET T, ZONGHE.IFM_SYS_GKX_CELIANG@IFM_NEW GC, ZONGHE.CFG_REPAIR_MAN@IFM_NEW   C WHERE GC.CELIANG_GROUP_ID = T.BUREAU_ID AND T.APPLY_DATE >= TO_DATE('20180701', 'yyyymmdd') AND T.SECOND_PRE_DATE >= TRUNC(SYSDATE - 1) AND T.SECOND_PRE_DATE < TRUNC(SYSDATE) AND C.CR_USERNAME = T.FETCH_LOGIN_NO AND T.WORK_TYPE = 0 AND T.WORK_STATUS < 2) A, ZONGHE.IFM_SYS_GROUP_GKX_ORDER@IFM_NEW GO WHERE A.GKX_GROUP_ID = GO.GROUP_ID ORDER BY GO.GROUP_ORDER";
	// 预约时间修改明细
	private static final String SQL_Z_X_3_MX = "SELECT GO.GROUP_NAME 分公司, GC.CELIANG_ABBREV 分局, A.维护区域, A.姓名, A.受理单号, A.受理时间 FROM (SELECT DISTINCT(T.BALK_NO) 受理单号, T.BALK_PHONE 故障号码, TO_CHAR(T.ACCEPT_TIME, 'yyyy-mm-dd hh24:mi:ss') 受理时间, T.PROCESS_GROUP_ID, P.OPER_USER_NAME 姓名, T.CURT_AREA_NAME 维护区域, T.FIRST_DEPT_ID FROM GKX_LAST_PAI T, BALK_RESERVATION_REMARK_APPLY P WHERE T.BALK_NO = P.BALK_NO AND P.OPER_TIME >= TRUNC(SYSDATE - 1) AND P.OPER_TIME < TRUNC(SYSDATE) AND T.BALK_SUB_SORT_ID = 1001 AND T.CUST_TYPE_ID = 1 UNION SELECT DISTINCT(T.BALK_NO) 受理单号, T.BALK_PHONE 故障号码, TO_CHAR(T.ACCEPT_TIME, 'yyyy-mm-dd hh24:mi:ss') 受理时间, T.PROCESS_GROUP_ID, P.OPER_USER_NAME 姓名, T.CURT_AREA_NAME 维护区域, T.FIRST_DEPT_ID FROM GKX_LAST_PAI_HIS T, BALK_RESERVATION_REMARK_APPLY P WHERE T.BALK_NO = P.BALK_NO AND P.OPER_TIME >= TRUNC(SYSDATE - 1) AND P.OPER_TIME < TRUNC(SYSDATE) AND T.BALK_SUB_SORT_ID = 1001 AND T.CUST_TYPE_ID = 1) A, IFM_SYS_GKX_CELIANG GC, IFM_SYS_GROUP_GKX_ORDER GO WHERE A.FIRST_DEPT_ID = GO.GROUP_ID AND GC.CELIANG_GROUP_ID = A.PROCESS_GROUP_ID ORDER BY GO.GROUP_ORDER, A.受理时间, A.受理单号";
	private static final String SQL_Z_W_3_MX = "SELECT GO.GROUP_NAME 分公司, A.分局, A.社区, A.姓名, A.订单号, A.工单号, TO_CHAR(A.APPLY_DATE, 'yyyy-mm-dd hh24:mi:ss') 受理日期 FROM (SELECT DISTINCT (R.BALK_NO) 工单号, GC.GKX_GROUP_ID, GC.CELIANG_ABBREV 分局, T.EXEC_DEPT_NAME 社区, TO_CHAR(T.ORDER_ID) 订单号, T.PRODUCT_NUM 产品号码, T.APPLY_DATE, C.CR_NAME 姓名 FROM T_PALM_IOM_SHEET_2003_TOTAL T, ZONGHE.IFM_SYS_GKX_CELIANG@IFM_NEW GC, ZONGHE.CFG_REPAIR_MAN@IFM_NEW C, ZONGHE.BALK_RESERVATION_REMARK_APPLY@IFM_NEW R WHERE GC.CELIANG_GROUP_ID(+) = T.BUREAU_ID AND C.CR_USERNAME(+) = T.FETCH_LOGIN_NO AND R.BALK_NO = T.WORK_ID AND T.FILIALE_ID IS NOT NULL AND T.WORK_TYPE = 0 AND T.FLOW_NODE = '2003' AND T.WORK_STATUS <= 2 AND R.OPER_TIME >= TRUNC(SYSDATE - 1) AND R.OPER_TIME < TRUNC(SYSDATE)) A, ZONGHE.IFM_SYS_GROUP_GKX_ORDER@IFM_NEW GO WHERE A.GKX_GROUP_ID = GO.GROUP_ID ORDER BY GO.GROUP_ORDER, A.工单号";

	private int[] DUANJU_COUNT = new int[] { 14, 10, 16, 6, 10, 7, 6, 6, 6, 7, 6, 1, 1, 2, 1, 1 };

	public static void main(String[] args) {
		String mubanName = "E:\\autosendmail\\超级中台考核模板20181109.xls";
		try {
			Mail mail = new Mail("smtp.263xmail.com", "ifm@lansoft.com.cn", "IFM", "ifm@lansoft.com.cn", "lansoft123",
					"yeming@lansoft.com.cn,fengjuan2@chinaunicom.cn,tongmin6@chinaunicom.cn,lidan193@chinaunicom.cn,fjfly950123@163.com,2522232@qq.com,503886234@qq.com",
					"超级中台考核-最新", "超级中台考核-最新");

			ExpNetCenterGKDayCheckDJ exp = new ExpNetCenterGKDayCheckDJ();
			String fileName = exp.exp(mubanName);
			mail.addAttachfile(fileName);
			mail.send();
			System.out.println("finish");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String exp(String muban) throws Exception {
		Date date = new Date();
		SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd");
		String fileName = "E:\\autosendmail\\file\\超级中台考核(" + dateformat1.format(date) + ")含端局.xls";

		Workbook wb = Workbook.getWorkbook(new File(muban));
		File targetFile = new File(fileName);
		WritableWorkbook wwb = Workbook.createWorkbook(targetFile, wb);

		WritableFont writableFont = new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat[] wcfF = initCellFormat(writableFont);

		jxl.biff.DisplayFormat displayFormat = jxl.write.NumberFormats.PERCENT_FLOAT;
		WritableCellFormat[] wcfFPercent = initPercentCellFormat(writableFont, displayFormat);

		WritableCellFormat wcfFTitle = new jxl.write.WritableCellFormat(writableFont);
		wcfFTitle.setAlignment(Alignment.CENTRE);
		wcfFTitle.setBorder(Border.ALL, jxl.format.BorderLineStyle.THIN);
		wcfFTitle.setBackground(jxl.format.Colour.TAN);

		WritableCellFormat wcfFTitleXj = new jxl.write.WritableCellFormat(writableFont);
		wcfFTitleXj.setAlignment(Alignment.CENTRE);
		wcfFTitleXj.setBorder(Border.ALL, jxl.format.BorderLineStyle.THIN);
		wcfFTitleXj.setBackground(jxl.format.Colour.LIGHT_TURQUOISE);

		WritableCellFormat wcfFTitleZj = new jxl.write.WritableCellFormat(writableFont);
		wcfFTitleZj.setAlignment(Alignment.CENTRE);
		wcfFTitleZj.setBorder(Border.ALL, jxl.format.BorderLineStyle.THIN);
		wcfFTitleZj.setBackground(jxl.format.Colour.PALE_BLUE);

		WritableSheet wws = wwb.getSheet(0);
		WritableSheet wwsdjx = wwb.getSheet(1);// 端局修机
		WritableSheet wwsdjw = wwb.getSheet(2);// 端局装机
		int cols = wws.getColumns();
		Label labelT = null;
		labelT = new jxl.write.Label(1, 0, wws.getCell(1, 0).getContents(), wcfFTitle);
		wws.addCell(labelT);
		labelT = new jxl.write.Label(1, 2, wws.getCell(1, 2).getContents(), wcfFTitleXj);
		wws.addCell(labelT);
		for (int j = 1; j < cols; j++) {
			Cell c = wws.getCell(j, 3);
			labelT = new jxl.write.Label(j, 3, c.getContents(), wcfFTitleXj);
			wws.addCell(labelT);
		}
		labelT = new jxl.write.Label(1, 23, wws.getCell(1, 23).getContents(), wcfFTitleZj);
		wws.addCell(labelT);
		for (int j = 1; j < cols; j++) {
			Cell c = wws.getCell(j, 24);
			labelT = new jxl.write.Label(j, 24, c.getContents(), wcfFTitleZj);
			wws.addCell(labelT);
		}

		Label label = new jxl.write.Label(1, 1, "通报日期：" + dateformat1.format(date), wcfFTitle);
		wws.addCell(label);
		Label labeldjx = new jxl.write.Label(0, 1, "通报日期：" + dateformat1.format(date), wcfFTitle);
		wwsdjx.addCell(labeldjx);
		Label labeldjw = new jxl.write.Label(0, 1, "通报日期：" + dateformat1.format(date), wcfFTitle);
		wwsdjw.addCell(labeldjw);

		QueryResultDayCheck[] queryResultFgs = new QueryResultDayCheck[17];
		QueryResultDayCheck[] queryResultDj = new QueryResultDayCheck[116];
		Connection conn = getConnection();
		Connection connPalm = getPalmConnection();
		try {
			// 修机超时预约
			querydj1(conn, SQL_Z_X_1_DJ, queryResultFgs, queryResultDj, false, false, 1);
			writeSheet4(wws, wcfF, wcfFPercent, queryResultFgs, 1, 3, 0);
			writeSheet4(wwsdjx, wcfF, wcfFPercent, queryResultDj, 3, 5, 0);
			writeSheetMingxi(conn, wwb, SQL_Z_X_1_MX, 3);
			// 修机超时未预约率
			querydj1(conn, SQL_Z_X_7_DJ, queryResultFgs, queryResultDj, false, false, 2);
			writeSheet4(wws, wcfF, wcfFPercent, queryResultFgs, 4, 6, 0);
			writeSheet4(wwsdjx, wcfF, wcfFPercent, queryResultDj, 6, 8, 0);
			writeSheetMingxi(conn, wwb, SQL_Z_X_3_MX, 4);
			// 修机未交付率
			querydj1(conn, SQL_Z_X_6_DJ, queryResultFgs, queryResultDj, false, false, 3);
			writeSheet4(wws, wcfF, wcfFPercent, queryResultFgs, 7, 9, 0);
			writeSheet4(wwsdjx, wcfF, wcfFPercent, queryResultDj, 9, 11, 0);
			writeSheetMingxi(conn, wwb, SQL_Z_X_2_MX, 5);

			// 装机超时预约
			querydj1(conn, SQL_Z_W_1_DJ, queryResultFgs, queryResultDj, false, false, 1);
			writeSheet4(wws, wcfF, wcfFPercent, queryResultFgs, 1, 3, 21);
			writeSheet4(wwsdjw, wcfF, wcfFPercent, queryResultDj, 3, 5, 0);
			writeSheetMingxi(conn, wwb, SQL_Z_W_1_MX, 6);
			// 装机超时未预约率
			querydj1(connPalm, SQL_Z_W_7_DJ, queryResultFgs, queryResultDj, false, false, 2);
			writeSheet4(wws, wcfF, wcfFPercent, queryResultFgs, 4, 6, 21);
			writeSheet4(wwsdjw, wcfF, wcfFPercent, queryResultDj, 6, 8, 0);
			writeSheetMingxi(connPalm, wwb, SQL_Z_W_3_MX, 7);
			// 装机未交付率
			querydj1(connPalm, SQL_Z_W_6_DJ, queryResultFgs, queryResultDj, false, false, 3);
			writeSheet4(wws, wcfF, wcfFPercent, queryResultFgs, 7, 9, 21);
			writeSheet4(wwsdjw, wcfF, wcfFPercent, queryResultDj, 9, 11, 0);
			writeSheetMingxi(connPalm, wwb, SQL_Z_W_2_MX, 8);
		} finally {
			closeConnection(conn);
			closeConnection(connPalm);
		}
		wwb.write();
		wwb.close();
		wb.close();
		return fileName;
	}

	private WritableCellFormat[] initPercentCellFormat(WritableFont writableFont, jxl.biff.DisplayFormat displayFormat)
			throws WriteException {
		WritableCellFormat[] wcfFPercent = new WritableCellFormat[3];
		wcfFPercent[0] = new jxl.write.WritableCellFormat(writableFont, displayFormat);
		wcfFPercent[0].setAlignment(Alignment.CENTRE);
		wcfFPercent[0].setBorder(Border.ALL, jxl.format.BorderLineStyle.THIN);
		wcfFPercent[0].setBackground(jxl.format.Colour.GREEN);

		wcfFPercent[1] = new jxl.write.WritableCellFormat(writableFont, displayFormat);
		wcfFPercent[1].setAlignment(Alignment.CENTRE);
		wcfFPercent[1].setBorder(Border.ALL, jxl.format.BorderLineStyle.THIN);
		wcfFPercent[1].setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);

		wcfFPercent[2] = new jxl.write.WritableCellFormat(writableFont, displayFormat);
		wcfFPercent[2].setAlignment(Alignment.CENTRE);
		wcfFPercent[2].setBorder(Border.ALL, jxl.format.BorderLineStyle.THIN);
		wcfFPercent[2].setBackground(jxl.format.Colour.RED);
		return wcfFPercent;
	}

	private WritableCellFormat[] initCellFormat(WritableFont writableFont) throws WriteException {
		WritableCellFormat[] wcfF = new WritableCellFormat[4];
		wcfF[0] = new jxl.write.WritableCellFormat(writableFont);
		wcfF[0].setAlignment(Alignment.CENTRE);
		wcfF[0].setBorder(Border.ALL, jxl.format.BorderLineStyle.THIN);
		wcfF[0].setBackground(jxl.format.Colour.GREEN);

		wcfF[1] = new jxl.write.WritableCellFormat(writableFont);
		wcfF[1].setAlignment(Alignment.CENTRE);
		wcfF[1].setBorder(Border.ALL, jxl.format.BorderLineStyle.THIN);
		wcfF[1].setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);

		wcfF[2] = new jxl.write.WritableCellFormat(writableFont);
		wcfF[2].setAlignment(Alignment.CENTRE);
		wcfF[2].setBorder(Border.ALL, jxl.format.BorderLineStyle.THIN);
		wcfF[2].setBackground(jxl.format.Colour.RED);

		wcfF[3] = new jxl.write.WritableCellFormat(writableFont);
		wcfF[3].setAlignment(Alignment.CENTRE);
		wcfF[3].setBorder(Border.ALL, jxl.format.BorderLineStyle.THIN);
		wcfF[3].setBackground(jxl.format.Colour.WHITE);
		return wcfF;
	}

	private void writeSheetMingxi(Connection conn, WritableWorkbook wwb, String sql, int pageIndex) throws Exception {
		WritableSheet wws = wwb.getSheet(pageIndex);
		WritableFont writableFont = new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
		WritableCellFormat wcfF = new jxl.write.WritableCellFormat(writableFont);
		wcfF.setAlignment(Alignment.CENTRE);
		wcfF.setBorder(Border.ALL, jxl.format.BorderLineStyle.THIN);

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			int rowIndex = 1;
			int columnCount = 0;
			WritableCell labelC = null;
			while (rs.next()) {
				if (columnCount == 0) {
					ResultSetMetaData rsmd = rs.getMetaData();
					columnCount = rsmd.getColumnCount();
				}
				labelC = new jxl.write.Number(0, rowIndex, rowIndex, wcfF);
				wws.addCell(labelC);
				for (int index = 1; index <= columnCount; index++) {
					labelC = new jxl.write.Label(index, rowIndex, rs.getString(index), wcfF);
					wws.addCell(labelC);
				}
				rowIndex++;
			}
		} finally {
			closeResultSet(rs);
			closeStatement(ps);
		}

	}

	private void writeSheet(WritableSheet wws, WritableCellFormat[] wcfF, QueryResultDayCheck[] queryResult,
			int colIndex) throws WriteException, RowsExceededException {
		jxl.write.Number label = null;
		int rowIndex = 1;
		for (int index = 0; index < queryResult.length; index++) {
			rowIndex = index + 4;
			label = new jxl.write.Number(colIndex, rowIndex, queryResult[index].getValue(),
					wcfF[queryResult[index].getFlag()]);
			wws.addCell(label);
		}
	}

	private void writeSheet1(WritableSheet wws, WritableCellFormat[] wcfF, WritableCellFormat[] wcfFPercent,
			QueryResultDayCheck[] queryResult, int colIndex) throws WriteException, RowsExceededException {
		jxl.write.Number label = null;
		int rowIndex = 1;
		for (int index = 0; index < queryResult.length; index++) {
			rowIndex = index + 4;
			label = new jxl.write.Number(colIndex, rowIndex, queryResult[index].getCalValue(),
					wcfFPercent[queryResult[index].getFlag()]);
			wws.addCell(label);

			label = new jxl.write.Number(colIndex + 1, rowIndex, queryResult[index].getValue(), wcfF[3]);
			wws.addCell(label);
		}
	}

	private void writeSheet2(WritableSheet wws, WritableCellFormat[] wcfF, WritableCellFormat[] wcfFPercent,
			QueryResultDayCheck[] queryResult, int colIndex) throws WriteException, RowsExceededException {
		jxl.write.Number label = null;
		int rowIndex = 1;
		for (int index = 0; index < queryResult.length; index++) {
			rowIndex = index + 4;
			label = new jxl.write.Number(colIndex, rowIndex, queryResult[index].getCalValue(),
					wcfFPercent[queryResult[index].getFlag()]);
			wws.addCell(label);
		}
	}

	private void writeSheet4(WritableSheet wws, WritableCellFormat[] wcfF, WritableCellFormat[] wcfFPercent,
			QueryResultDayCheck[] queryResult, int colIndex, int rateColIdx, int startRow)
			throws WriteException, RowsExceededException {
		jxl.write.Number label = null;
		int rowIndex = 1;
		for (int index = 0; index < queryResult.length; index++) {
			if (queryResult[index] == null)
				continue;
			rowIndex = index + 4 + startRow;
			// 分子 值
			label = new jxl.write.Number(colIndex, rowIndex, queryResult[index].getValue1(), wcfF[1]);
			wws.addCell(label);
			// 分母值
			label = new jxl.write.Number(colIndex + 1, rowIndex, queryResult[index].getValue(), wcfF[1]);
			wws.addCell(label);
			// 率
			label = new jxl.write.Number(rateColIdx, rowIndex, queryResult[index].getCalValue(),
					wcfFPercent[queryResult[index].getFlag()]);
			wws.addCell(label);
		}
	}

	private void querydj1(Connection conn, String sql, QueryResultDayCheck[] queryResultsFgs,
			QueryResultDayCheck[] queryResultsDj, boolean cal, boolean asc, int type) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			int index = 0;
			int totalDj = 0;
			int totalDj1 = 0;
			int totalFgs = 0;
			int totalFgs1 = 0;
			int djIndex = 0;
			int fgsIndex = 0;
			while (rs.next()) {
				queryResultsDj[index] = new QueryResultDayCheck(rs.getString(2), rs.getInt(3), rs.getInt(4), cal);
				totalDj += queryResultsDj[index].getValue();
				totalDj1 += queryResultsDj[index].getValue1();
				totalFgs += queryResultsDj[index].getValue();
				totalFgs1 += queryResultsDj[index].getValue1();
				djIndex++;
				index++;

				if (djIndex == DUANJU_COUNT[fgsIndex]) {
					queryResultsFgs[fgsIndex] = new QueryResultDayCheck(rs.getString(1), totalDj, totalDj1, cal);
					if (DUANJU_COUNT[fgsIndex] > 1) {
						queryResultsDj[index++] = queryResultsFgs[fgsIndex];
					}
					fgsIndex++;
					djIndex = 0;
					totalDj = 0;
					totalDj1 = 0;
				}

			}
			queryResultsFgs[fgsIndex] = new QueryResultDayCheck("合计", totalFgs, totalFgs1, cal);
			queryResultsFgs[fgsIndex].setFlag(0);
		} finally {
			closeResultSet(rs);
			closeStatement(ps);
		}
		setFlag(queryResultsFgs, type);
		setFlag(queryResultsFgs, type);
		setFlag(queryResultsFgs, type);
	}

	// 95%（含）以上绿色，95%-90%（含）是黄色，90%以下红色
	private void setFlag(QueryResultDayCheck[] queryResult, int type) {
		int maxValue = 0;
		int minValue = 999999999;
		int count = queryResult.length;
		for (int index = 0; index < count; index++) {
			if (type == 1) {
				if (queryResult[index].getCalValue() >= 0.024) {
					queryResult[index].setFlag(2);
				} else if (queryResult[index].getCalValue() <= 0.015) {
					queryResult[index].setFlag(0);
				} else {
					queryResult[index].setFlag(1);
				}
			} else if (type == 2) {
				if (queryResult[index].getCalValue() >= 0.2) {
					queryResult[index].setFlag(2);
				} else if (queryResult[index].getCalValue() < 0.15) {
					queryResult[index].setFlag(0);
				} else {
					queryResult[index].setFlag(1);
				}
			} else if (type == 3) {
				if (queryResult[index].getCalValue() >= 0.19) {
					queryResult[index].setFlag(2);
				} else if (queryResult[index].getCalValue() < 0.1) {
					queryResult[index].setFlag(0);
				} else {
					queryResult[index].setFlag(1);
				}
			}
		}
	}

	private void setFlag(QueryResultDayCheck[] queryResult, int begin, int end) {
		int maxValue = 0;
		int minValue = 999999999;

		for (int index = begin; index <= end; index++) {
			if (queryResult[index].getValue() > maxValue) {
				maxValue = queryResult[index].getValue();
			}
			if (queryResult[index].getValue() < minValue) {
				minValue = queryResult[index].getValue();
			}
		}

		for (int index = begin; index <= end; index++) {
			if (queryResult[index].getValue() == maxValue) {
				queryResult[index].setFlag(2);
			}
			if (queryResult[index].getValue() == minValue) {
				queryResult[index].setFlag(0);
			}
		}

	}

	private void setFlag1(QueryResultDayCheck[] queryResult, int begin, int end, boolean asc) {
		float maxValue = 0f;
		float minValue = 999999999f;

		for (int index = begin; index <= end; index++) {
			if (queryResult[index].getCalValue() > maxValue) {
				maxValue = queryResult[index].getCalValue();
			}
			if (queryResult[index].getCalValue() < minValue) {
				minValue = queryResult[index].getCalValue();
			}
		}

		if (asc) {
			for (int index = begin; index <= end; index++) {
				if (Math.abs(queryResult[index].getCalValue() - minValue) < 0.0000001f) {
					queryResult[index].setFlag(0);
				}
				if (Math.abs(queryResult[index].getCalValue() - maxValue) < 0.0000001f) {
					queryResult[index].setFlag(2);
				}
			}
		} else {
			for (int index = begin; index <= end; index++) {
				if (Math.abs(queryResult[index].getCalValue() - maxValue) < 0.0000001f) {
					queryResult[index].setFlag(0);
				}
				if (Math.abs(queryResult[index].getCalValue() - minValue) < 0.0000001f) {
					queryResult[index].setFlag(2);
				}
			}
		}
	}

	private void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void closeStatement(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void executeSql(Connection conn, String sql) throws Exception {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			conn.commit();
		} finally {
			closeStatement(ps);
		}

	}

	private Connection getConnection() throws Exception {
		Connection conn = null;
		String driver = "oracle.jdbc.driver.OracleDriver";
		/**************** 生产正式数据库 *******************/
		String url = "jdbc:oracle:thin:@(description=(address_list= (address=(host=132.77.64.202) (protocol=tcp)(port=1522))(address=(host=132.77.64.203)(protocol=tcp) (port=1522)) (load_balance=yes)(failover=yes))(connect_data=(service_name=ifmdb)))";
		// String url = "jdbc:oracle:thin:@132.77.64.51:1523:IFMDB1";
		String user = "zonghe";
		String pwd = "zonghe2008";
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pwd);
		} catch (Exception e) {
			System.out.println("创建连接出现异常!");
			e.printStackTrace();
			conn = null;
			throw e;
		}

		return conn;
	}

	public Connection getPalmConnection() throws Exception {
		Connection conn = null;
		String driver = "oracle.jdbc.driver.OracleDriver";
		String url = "jdbc:oracle:thin:@(description=(address_list= (address=(host=132.77.64.202) (protocol=tcp)(port=1522))(address=(host=132.77.64.203)(protocol=tcp) (port=1522)) (load_balance=yes)(failover=yes))(connect_data=(service_name=palmom)))";
		// String url = "jdbc:oracle:thin:@132.77.64.51:1523:IFMDB1";
		String user = "palmom";
		String pwd = "palmom";
		do {
			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(url, user, pwd);
				conn.setAutoCommit(false);
			} catch (Exception e) {
				e.printStackTrace();
				conn = null;
				Thread.sleep(1000);
				// throw e;
			}
		} while (conn == null);
		return conn;
	}

}
