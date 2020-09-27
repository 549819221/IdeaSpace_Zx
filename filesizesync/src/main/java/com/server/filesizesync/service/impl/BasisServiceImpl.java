package com.server.filesizesync.service.impl;

import com.alibaba.fastjson.JSON;
import com.server.filesizesync.entity.PackageSerialInfo;
import com.server.filesizesync.entity.UploadDataInfo;
import com.server.filesizesync.service.BasisService;
import com.server.filesizesync.util.FTPUtil;
import com.server.filesizesync.util.FastDFSClient;
import com.server.filesizesync.util.FileEncryptUtil;
import com.server.filesizesync.util.PowerUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

/**
 * (Basis)表服务实现类
 *
 * @author wanghb
 * @since 2020-07-10 14:43:46
 */
@Service("basisService")
@PropertySource({"classpath:application.properties"})
public class BasisServiceImpl implements BasisService {

    private static Logger logger = Logger.getLogger( BasisServiceImpl.class );


    @Value("${zip.encode}")
    private  String zipEncode;

    @Value("${fdfsConfPath}")
    private  String fdfsConfPath;

    @Resource
    private FTPUtil fTPUtil;



    /**
     * @description 从fastDfs同步到ftp
     * @param  packageSerialInfo
     * @return  返回结果
     * @date  20/09/16 10:08
     * @author  wanghb
     * @edit
     */
    @Override
    public Boolean syncFileSiz(PackageSerialInfo packageSerialInfo) throws Exception {
        FastDFSClient fastDFSClient = new FastDFSClient(fdfsConfPath);
        String fastdfsId = PowerUtil.getString( packageSerialInfo.getFastdfsId() );
        byte[] data = fastDFSClient.download(fastdfsId);
        if (data == null) {
            logger.error( new StringBuilder( "这个流水号,从fstdfs读取为空,流水号:" ).append( packageSerialInfo.getSerial() ).append( ".fastdfsId为" ).append( fastdfsId ).toString() );
            return false;
        }
        UploadDataInfo uploadDataInfo = JSON.parseObject(data, UploadDataInfo.class);
        String serial = uploadDataInfo.getSerial();
        String zipPrefix = new StringBuilder(serial).append( "_" ).toString();
        File tempZip =  FileEncryptUtil.encryptStreamZip( JSON.toJSONString(uploadDataInfo),zipPrefix,zipEncode);
        String ftpUploadPath = packageSerialInfo.getFtpPath();
        //文件上传
        Boolean isSuccess = fTPUtil.uploadFile( ftpUploadPath, tempZip );
        tempZip.delete();
        return isSuccess;
    }

}