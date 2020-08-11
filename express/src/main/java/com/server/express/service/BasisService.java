package com.server.express.service;


import com.server.express.entity.UploadDataInfo;

/**
 * (Basis)表服务接口
 * @author wanghb
 * @since 2020-07-10 14:43:45
 */
public interface BasisService  {
    public void dataUpload(UploadDataInfo uploadDataInfo);
    public void getToken();

}