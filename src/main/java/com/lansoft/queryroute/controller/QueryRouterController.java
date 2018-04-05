package com.lansoft.queryroute.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lansoft.queryroute.model.RouteInfo;
import com.lansoft.queryroute.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QueryRouterController {
    @Autowired
    private QueryService queryService;

    @RequestMapping(value = "/route/{id}", method = RequestMethod.GET)
    public String getRoute (@PathVariable(value = "id") String id) {
        List<RouteInfo> list = queryService.queryRoute (1, id);
        String ret = "";
        if (list != null) {
            ret = JSON.toJSONString (list, SerializerFeature.PrettyFormat);
        }
        return ret;
    }
}
