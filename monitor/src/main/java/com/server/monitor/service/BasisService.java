package com.server.monitor.service;

import com.server.monitor.entity.parent.Monitor;

import java.util.List;
import java.util.Map;

/**
 * (Basis)表服务接口
 * @author wanghb
 * @since 2020-07-10 14:43:45
 */
public interface BasisService  {
    Map<String, Object> register();
    List<Map<String, Object>> getAllNode(String objId);



}