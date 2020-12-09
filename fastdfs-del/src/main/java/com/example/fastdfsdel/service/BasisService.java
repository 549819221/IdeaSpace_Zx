package com.example.fastdfsdel.service;


import com.example.fastdfsdel.entity.PackageSerialInfo;
import com.example.fastdfsdel.entity.UploadDataInfo;
import com.example.fastdfsdel.entity.UploadDataResult;
import com.example.fastdfsdel.entity.User;
import net.lingala.zip4j.exception.ZipException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * (Basis)表服务接口
 * @author wanghb
 * @since 2020-07-10 14:43:45
 */
public interface BasisService  {

    UploadDataResult updateStatus(com.example.fastdfsdel.entity.PackageSerialInfo packageSerialParam);

    Boolean uploadFtp(PackageSerialInfo packageSerialInfo) throws Exception;

    UploadDataResult reUploadFtp(String serial);

    UploadDataResult delFastDfs(String serial) throws Exception;
}
