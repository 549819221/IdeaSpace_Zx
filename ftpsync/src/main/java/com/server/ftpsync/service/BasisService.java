package com.server.ftpsync.service;


import com.server.ftpsync.entity.PackageSerialInfo;

/**
 * (Basis)表服务接口
 * @author wanghb
 * @since 2020-07-10 14:43:45
 */
public interface BasisService  {

    Boolean uploadFtp(PackageSerialInfo packageSerialInfo) throws Exception;

}