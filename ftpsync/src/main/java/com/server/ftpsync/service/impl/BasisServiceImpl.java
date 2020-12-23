package com.server.ftpsync.service.impl;

import com.alibaba.fastjson.JSON;
import com.server.ftpsync.dao.PackageSerialDao;
import com.server.ftpsync.entity.PackageSerialInfo;
import com.server.ftpsync.entity.TokenResult;
import com.server.ftpsync.entity.UploadDataInfo;
import com.server.ftpsync.entity.UploadDataResult;
import com.server.ftpsync.service.BasisService;
import com.server.ftpsync.util.*;
import net.lingala.zip4j.exception.ZipException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;

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
    public Boolean uploadFtp(PackageSerialInfo packageSerialInfo) throws Exception {
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


    /**
     * @description 从fastDfs同步到ftp
     * @param  packageSerialInfos
     * @return  返回结果
     * @date  20/09/16 10:08
     * @author  wanghb
     * @edit
     */
    @Override
    public Boolean uploadFtp(List<PackageSerialInfo> packageSerialInfos,String ftpUploadPath) throws Exception {
        logger.info( "=================>进入上传方法");
        List<String> jsons = new ArrayList<>();
        for (PackageSerialInfo packageSerialInfo : packageSerialInfos) {
            String fastdfsId = PowerUtil.getString( packageSerialInfo.getFastdfsId() );
            byte[] data = null;
            synchronized (this){
                FastDFSClient fastDFSClient = new FastDFSClient(fdfsConfPath);
                data = fastDFSClient.download(fastdfsId);
            }
            if (data == null) {
                packageSerialInfo.setSyncFtpStatus( ParamEnum.syncFtpStatus.status2.getCode() );
                logger.error( new StringBuilder( "这个流水号,从fstdfs读取为空,流水号:" ).append( packageSerialInfo.getSerial() ).append( ".fastdfsId为" ).append( fastdfsId ).toString() );
            }else {
                packageSerialInfo.setSyncFtpStatus( ParamEnum.syncFtpStatus.status1.getCode() );
                UploadDataInfo uploadDataInfo = JSON.parseObject(data, UploadDataInfo.class);
                jsons.add( JSON.toJSONString(uploadDataInfo) );
            }
        }
        String serial = UUID.randomUUID().toString().replaceAll( "-","" );
        String zipPrefix = "files-"+new StringBuilder(serial).append( "_" ).toString();
        File tempZip =  FileEncryptUtil.encryptStreamZip(  jsons,zipPrefix,zipEncode);
        //String ftpUploadPath = "/expressData/expressStaffDataUpload";
        //文件上传
        logger.info( "=================>数据完毕开始上传");
        Boolean isSuccess = fTPUtil.uploadFile( ftpUploadPath, tempZip );
        logger.info( "=================>ftp上传成功");
        tempZip.delete();
        return isSuccess;
    }

}
