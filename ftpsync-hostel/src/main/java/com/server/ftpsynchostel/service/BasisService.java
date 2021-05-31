package com.server.ftpsynchostel.service;


import com.alibaba.fastjson.JSON;
import com.server.ftpsynchostel.UploadDataSm2Info;
import com.server.ftpsynchostel.entity.PackageSerialLgInfo;
import com.server.ftpsynchostel.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * (Basis)表服务接口
 * @author wanghb
 * @since 2020-07-10 14:43:45
 */
@Service("basisService")
@PropertySource({"classpath:application.properties"})
public class BasisService  {

    private static Logger logger = Logger.getLogger( BasisService.class );


    @Value("${zip.encode}")
    private  String zipEncode;

    @Value("${fdfsConfPath}")
    private  String fdfsConfPath;

    @Resource
    private FTPUtil fTPUtil;



    /**
     * @description 从fastDfs同步到ftp
     * @param  PackageSerialLgInfo
     * @return  返回结果
     * @date  20/09/16 10:08
     * @author  wanghb
     * @edit
     */
    public Boolean uploadFtp(PackageSerialLgInfo PackageSerialLgInfo) throws Exception {
        FastDFSClient fastDFSClient = new FastDFSClient(fdfsConfPath);
        String fastdfsId = PowerUtil.getString( PackageSerialLgInfo.getFastdfsId() );
        byte[] data = fastDFSClient.download(fastdfsId);
        if (data == null) {
            logger.error( new StringBuilder( "这个流水号,从fstdfs读取为空,流水号:" ).append( PackageSerialLgInfo.getSerial() ).append( ".fastdfsId为" ).append( fastdfsId ).toString() );
            return false;
        }
        UploadDataSm2Info UploadDataSm2Info = JSON.parseObject(data, UploadDataSm2Info.class);
        String serial = UploadDataSm2Info.getSerial();
        String zipPrefix = new StringBuilder(serial).append( "_" ).toString();
        File tempZip =  FileEncryptUtil.encryptStreamZip( JSON.toJSONString(UploadDataSm2Info),zipPrefix,zipEncode);
        String ftpUploadPath = PackageSerialLgInfo.getFtpPath();
        //文件上传
        Boolean isSuccess = fTPUtil.uploadFile( ftpUploadPath, tempZip );
        tempZip.delete();
        return isSuccess;
    }


    /**
     * @description 从fastDfs同步到ftp
     * @param  PackageSerialLgInfos
     * @return  返回结果
     * @date  20/09/16 10:08
     * @author  wanghb
     * @edit
     */
    private static Object ftpLock = new Object();
    public Boolean uploadFtp(List<PackageSerialLgInfo> PackageSerialLgInfos,String ftpUploadPath) throws Exception {
        logger.info( "=================>进入上传方法");
        List<String> jsons = new ArrayList<>();
        for (PackageSerialLgInfo PackageSerialLgInfo : PackageSerialLgInfos) {
            String fastdfsId = PowerUtil.getString( PackageSerialLgInfo.getFastdfsId() );
            byte[] data = null;
            synchronized (this){
                FastDFSClient fastDFSClient = new FastDFSClient(fdfsConfPath);
                data = fastDFSClient.download(fastdfsId);
            }
            if (data == null) {
                PackageSerialLgInfo.setSyncFtpStatus( ParamEnum.syncFtpStatus.status2.getCode() );
                logger.error( new StringBuilder( "这个流水号,从fstdfs读取为空,流水号:" ).append( PackageSerialLgInfo.getSerial() ).append( ".fastdfsId为" ).append( fastdfsId ).toString() );
            }else {
                PackageSerialLgInfo.setSyncFtpStatus( ParamEnum.syncFtpStatus.status1.getCode() );
                UploadDataSm2Info uploadDataSm2Info = JSON.parseObject(data, UploadDataSm2Info.class);
                jsons.add( JSON.toJSONString(uploadDataSm2Info) );
            }
        }
        String serial = UUID.randomUUID().toString().replaceAll( "-","" );
        String zipPrefix = "files-"+new StringBuilder(serial).append( "_" ).toString();
        File tempZip =  FileEncryptUtil.encryptStreamZip(  jsons,zipPrefix,zipEncode);
        //String ftpUploadPath = "/expressData/expressStaffDataUpload";
        //文件上传
        logger.info( "=================>数据完毕开始上传");
        Boolean isSuccess = null;
        synchronized (ftpLock) {
            isSuccess = fTPUtil.uploadFile( ftpUploadPath, tempZip );
        }
        logger.info( "=================>ftp上传成功");
        tempZip.delete();
        return isSuccess;
    }

}
