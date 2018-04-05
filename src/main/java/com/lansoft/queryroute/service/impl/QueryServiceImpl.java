package com.lansoft.queryroute.service.impl;

import com.lansoft.queryroute.model.DetailInfo;
import com.lansoft.queryroute.model.RouteInfo;
import com.lansoft.queryroute.service.QueryService;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component("queryRoute")
public class QueryServiceImpl implements QueryService {
    public static final String NEWLUYOU_GAIYAOADDRESS = "/wholecourseroute/jsp/intf/queryZhuanXianXinXiIntf.action?"; // 概要信息地址
    public static final String NEWLUYOU_XIANGXIADDRESS = "/wholecourseroute/jsp/intf/queryZhuanXianLuYouIntf.action?"; // 详细信息地址
    public static final String GAIYAOMOSHIID = "wr_000001"; // 概要信息模式ID
    public static final String XIANGXIMOSHIID = "wr_000002"; // 详细信息模式ID
    private static final Logger log = LoggerFactory.getLogger (QueryServiceImpl.class);
    private static double identifier;

    @Override
    public List<RouteInfo> queryRoute (int queryflag, String specId) {
        String url = genUrl (queryflag, specId);
        String resultXMLTxt = queryDataFromRemote (url);
        log.info (resultXMLTxt);
        List<RouteInfo> routeInfoList = null;
        List<DetailInfo> detailInfoList = null;

        try {
            routeInfoList = parseRouteInfo (DocumentHelper.parseText (resultXMLTxt));
            if (routeInfoList != null && routeInfoList.size () > 0) {
                for (RouteInfo routeInfo : routeInfoList) {
                    url = genQueryDetailInfoUrl (routeInfo.getStrRouteId (), routeInfo.getYewuLeixing ());
                    resultXMLTxt = queryDataFromRemote (url);
                    detailInfoList = parseDetailInfo(DocumentHelper.parseText (resultXMLTxt));
                    routeInfo.setDetailInfoList (detailInfoList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }

        return routeInfoList;
    }

    private ArrayList<RouteInfo> parseRouteInfo (Document document) throws Exception {
        ArrayList<RouteInfo> alist = new ArrayList<> ();
        RouteInfo result = null;
        Node node = document.selectSingleNode ("//result");
        int i = 1;
        Element root = (Element) node;
        Iterator iterator = root.elementIterator ();
        Element child1 = null;
        while (iterator.hasNext ()) {
            result = new RouteInfo ();
            child1 = (Element) iterator.next ();
            String str_name = "", str_value = "";
            str_name = child1.getName ();
            if (str_name.equals ("row")) {
                Iterator iterator2 = child1.elementIterator ();
                Element child2 = null;
                while (iterator2.hasNext ()) {
                    child2 = (Element) iterator2.next ();
                    String name = null, value = null;
                    name = child2.getName ();
                    value = child2.getText ();
                    if (name.equals ("row-index")) {
                        result.setRow_index (value);
                    } else if (name.equals ("zhuanxian_hao")) {
                        result.setStrSTLNumber (value);
                    } else if (name.equals ("yewu_leibie")) {
                        result.setStrOperType (value);
                    } else if (name.equals ("kehu_mingcheng")) {
                        result.setStrCustomerName (value);
                    } else if (name.equals ("gongdan_hao")) {
                        result.setGd (value);
                    } else if (name.equals ("gongcheng_xiangmu")) {
                        result.setStrProjectName (value);
                    } else if (name.equals ("aduan_dizhi")) {
                        result.setStrPointA (value);
                    } else if (name.equals ("zduan_dizhi")) {
                        result.setStrPointZ (value);
                    } else if (name.equals ("dianlu_bianhao")) {
                        result.setStrCircuitCode (value);
                    } else if (name.equals ("sulv")) {
                        result.setStrVelocity (value);
                    } else if (name.equals ("fadan_shijian")) {
                        result.setStrAssiStartDate (value);
                    } else if (name.equals ("shiji_wancheng_shijian")) {
                        result.setStrAssiFinishDate (value);
                    } else if (name.equals ("dianlu_daihao")) {
                        result.setStrCircuiteSymbol (value);
                    } else if (name.equals ("luyou")) {
                        result.setStrRoute (value);
                    } else if (name.equals ("luyou_id")) {// 旧库所有
                        result.setStrRouteId (value);
//					} else if (name.equals("zhuanxian_id")) {// 新库所有，setStrRouteId字段不会用时有数据，所以同一字段保存不同数据
//						result.setStrRouteId(value);
                    } else if (name.equals ("yewu_id")) {// 新库所有，setStrRouteId字段不会用时有数据，所以同一字段保存不同数据
                        result.setStrRouteId (value);
                    } else if (name.equals ("yewu_leixing")) {//1为专线2为中继
                        result.setYewuLeixing (value);
                    } else if (name.equals ("beizhu")) {// 2007-10-10新添加节点 zdl
                        result.setRouteRemark (value);
                    } else if (name.equals ("servicelevel")) {// 2008-06-04 新添加节点
                        // lotus
                        result.setServicelevel (value);
                    } else if (name.equals ("zbflg")) {// 2008-06-04 新添加节点 lotus
                        result.setZbflg (value);
                    } else if (name.equals ("zbtimes")) {// 2008-06-04 新添加节点
                        // lotus
                        result.setZbtimes (value);
                    } else if (name.equals ("zblevel")) {// 2008-06-04 新添加节点
                        // lotus
                        result.setZblevel (value);
                    } else if (name.equals ("cust_manager")) {// 2008-10-20 新添加节点
                        // lotus
                        result.setCust_manager (value);
                    } else if (name.equals ("cust_manager_tel")) {// 2008-10-20
                        // 新添加节点
                        // lotus
                        result.setCust_manager_tel (value);
                    } else if (name.equals ("user_ip")) {// 2008-10-20 新添加节点
                        // lotus
                        result.setUser_ip (value);
                    } else if (name.equals ("rd")) {// 2008-10-20 新添加节点 lotus
                        result.setRd (value);
                    } else if (name.equals ("rt")) {// 2008-10-20 新添加节点 lotus
                        result.setRt (value);
                    } else if (name.equals ("dianlu_weihu_danwei")) {// 2014-01-14
                        // 增加电路维护单位
                        // yangyupeng
                        result.setCirCuitCareCop (value);
                    } else if (name.equals ("kehu_bianma")) { // 2014-09-24
                        // 增加客户编码
                        // yangyupeng
                        result.setCustCode (value);
                    }
                }
                alist.add (result);
            }
        }
        return alist;
    }


    private String queryDataFromRemote (String strurl) {
        log.info ("url=" + strurl);
        StringBuffer request = new StringBuffer ();
        try {
            URL url = new URL (strurl);
            URLConnection con = url.openConnection ();
            con.setDoInput (true);
            con.setDoOutput (true);
            con.setReadTimeout (30 * 1000);
            OutputStream output = con.getOutputStream ();
            output.flush ();
            InputStream inputs = con.getInputStream ();

            boolean b = true;
            while (b) {
                int i;
                byte[] buffer = new byte[1024];
                try {
                    i = inputs.read (buffer);
                } catch (IOException e) {
                    log.error ("读取数据错误", e);
                    i = -1;
                }
                byte[] buffer1 = new byte[i];
                for (int j = 0; j < i; j++) {
                    buffer1[j] = buffer[j];
                }
                String aa = null;
                try {
                    aa = new String (buffer1, "gb2312");
                } catch (UnsupportedEncodingException ex1) {
                    log.error ("读取数据错误", ex1);
                }
                request.append (aa);

                if (!checkXMLEnd (aa, "</result>")) {
                    b = false;
                }
            }
            inputs.close ();
            output.close ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return request.toString ();
    }

    public boolean checkXMLEnd (String strXml, String endStr) {
        boolean b = true;
        if (strXml.endsWith (endStr) || strXml.endsWith (endStr + "\n")) {
            b = false;
        }
        return b;
    }

    private String genUrl (int queryflag, String specId) {
        StringBuffer sb = new StringBuffer ();
        sb.append ("http://132.77.64.134:7701").append (NEWLUYOU_GAIYAOADDRESS); // 发送地址
        sb.append ("zhuanXian.mode_id=").append (GAIYAOMOSHIID); // 模式ID
        sb.append ("&zhuanXian.flag_id=").append (identifier++); // 流水号
        sb.append ("&zhuanXian.jq_flag=1"); // 精确或模糊查询
        sb.append ("&zhuanXian.sort_mode=asc"); // 排序标识
        if (queryflag == 1) { // 专线号
            sb.append ("&zhuanXian.zhuanXianHao=" + URLEncoder.encode (specId));
        }
        return sb.toString ();
    }

    public String genQueryDetailInfoUrl (String routeId, String yewuLeixing) {
        String str = "";
        StringBuffer sb = new StringBuffer ();
        sb.append ("http://132.77.64.134:7701").append (NEWLUYOU_XIANGXIADDRESS);
        sb.append ("zhuanXian.mode_id=").append (XIANGXIMOSHIID); // 模式ID
        sb.append ("&id=").append (URLEncoder.encode (routeId)); // 专线号ID
        sb.append ("&zhuanXian.flag_id=").append (identifier++); // 流水号
        sb.append ("&yewu_leixing=").append (yewuLeixing);
        str = sb.toString ();
        return str;
    }

    private ArrayList<DetailInfo> parseDetailInfo (Document document) throws Exception {
        ArrayList<DetailInfo> alist = new ArrayList ();
        DetailInfo result = null;
        String count = "";
        Node node = document.selectSingleNode ("//result");
        Element root = (Element) node;
        Iterator iterator = root.elementIterator ();
        Element child1 = null;
        while (iterator.hasNext ()) {
            result = new DetailInfo ();
            child1 = (Element) iterator.next ();
            String str_name = null, str_value = null;
            str_name = child1.getName ();
            if (str_name.equals ("row")) {
                Iterator iterator2 = child1.elementIterator ();
                Element child2 = null;
                while (iterator2.hasNext ()) {
                    child2 = (Element) iterator2.next ();
                    String name = null, value = null;
                    name = child2.getName ();
                    value = child2.getText ();
                    if (name.equals ("row-index")) {
                        result.setRow_index (value);
                    } else if (name.equals ("quju_hao")) {
                        result.setSectionNumber (value);
                    } else if (name.equals ("shebeiju_hao")) {
                        result.setEquipAreaNum (value);
                    } else if (name.equals ("shebei_leixing")) {
                        result.setEquipType (value);
                    } else if (name.equals ("shebei_mingcheng")) {
                        result.setEquipName (value);
                    } else if (name.equals ("lianjie_weizhi")) {
                        result.setLinkPlace (value);
                    } else if (name.equals ("dianlu_biaoshi")) {
                        result.setElectrocircuitID (value);
                    } else if (name.equals ("mokuai_xuhao_mingcheng")) {
                        result.setModeNum (value);
                    } else if (name.equals ("shebeiju_mingcheng")) {
                        result.setEquipAreaName (value);
                    } else if (name.equals ("beizhu")) {
                        result.setRemark (value);
                    } else if (name.equals ("jifang_mingcheng")) { // 2015-09-23
                        // 增加机房名称
                        // yangyupeng
                        result.setCopRoomName (value);
                    }
                }
            }
            alist.add (result);
        }

        return alist;
    }
}
