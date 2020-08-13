package com.server.express.service;


import com.server.express.entity.TokenResult;
import com.server.express.entity.UploadDataInfo;
import com.server.express.entity.UploadDataResult;
import com.server.express.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * (Basis)表服务接口
 * @author wanghb
 * @since 2020-07-10 14:43:45
 */
public interface BasisService  {
    Object dataUpload(UploadDataInfo uploadDataInfo);
    TokenResult getToken(User user, HttpServletRequest request);

}