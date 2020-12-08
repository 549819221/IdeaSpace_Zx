package com.example.fastdfsdel.service;


import com.example.fastdfsdel.entity.PackageSerialInfo;

import java.util.List;

/**
* 包流水信息( PackageSerialService )服务类
* @author wanghb
* @since 2020-8-14 13:54:48
*/
public interface PackageSerialService  {

    void save(PackageSerialInfo packageSerialInfo);

    PackageSerialInfo view(String serial);

    void delete(String serial);

    void batchDelete(List<String> ids);

}
