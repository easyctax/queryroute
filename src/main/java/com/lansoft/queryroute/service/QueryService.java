package com.lansoft.queryroute.service;

import com.lansoft.queryroute.model.RouteInfo;

import java.util.List;

public interface QueryService {
    List<RouteInfo> queryRoute(int queryflag, String specId);
}
