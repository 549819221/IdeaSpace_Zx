package com.server.filesizesync.service;


import com.server.filesizesync.entity.PackageSerialInfo;

/**
 * (Basis)表服务接口
 * @author wanghb
 * @since 2020-07-10 14:43:45
 */
public interface BasisService  {

    Boolean syncFileSiz(PackageSerialInfo packageSerialInfo) throws Exception;

}