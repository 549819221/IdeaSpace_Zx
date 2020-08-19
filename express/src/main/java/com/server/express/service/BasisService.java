package com.server.express.service;


import com.server.express.entity.*;
import net.lingala.zip4j.exception.ZipException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * (Basis)表服务接口
 * @author wanghb
 * @since 2020-07-10 14:43:45
 */
public interface BasisService  {
    Object dataUpload(UploadDataInfo uploadDataInfo) throws IOException, ZipException;
    TokenResult getToken(User user, HttpServletRequest request);

    UploadDataResult updateStatus(PackageSerialInfo packageSerialParam);
}