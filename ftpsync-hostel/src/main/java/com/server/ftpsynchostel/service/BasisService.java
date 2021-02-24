package com.server.ftpsynchostel.service;


import com.server.ftpsynchostel.entity.PackageSerialInfo;

import java.util.List;

/**
 * (Basis)表服务接口
 * @author wanghb
 * @since 2020-07-10 14:43:45
 */
public interface BasisService  {

    Boolean uploadFtp(PackageSerialInfo packageSerialInfo) throws Exception;
    Boolean uploadFtp(List<PackageSerialInfo> packageSerialInfo,String ftpPath) throws Exception;

}
