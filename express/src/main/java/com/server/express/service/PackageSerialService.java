package com.server.express.service;

import com.server.express.entity.PackageSerialInfo;

import java.util.List;

/**
* 包流水信息( PackageSerialService )服务类
* @author wanghb
* @since 2020-8-14 11:25:52
*/
public interface PackageSerialService  {

    void save(PackageSerialInfo packageSerialInfo);

    PackageSerialInfo view(String serial);

    void delete(String serial);

    void batchDelete(List<String> ids);

}