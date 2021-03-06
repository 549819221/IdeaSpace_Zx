package com.server.ticket.service;


import com.server.ticket.entity.*;
import net.lingala.zip4j.exception.ZipException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * (Basis)表服务接口
 * @author wanghb
 * @since 2020-07-10 14:43:45
 */
public interface BasisService  {
    Object dataUpload(UploadDataInfo uploadDataInfo, String code) throws IOException, ZipException;

    Object getToken(User user, HttpServletRequest request);

    UploadDataResult updateStatus(String packageSerialParam);
    Boolean uploadFtp(PackageSerialInfo packageSerialInfo)throws Exception;

    UploadDataResult reUploadFtp(String serial);
}