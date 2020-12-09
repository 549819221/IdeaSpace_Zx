package com.example.fastdfsdel.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.fastdfsdel.dao.PackageSerialDao;
import com.example.fastdfsdel.entity.*;
import com.example.fastdfsdel.service.BasisService;
import com.example.fastdfsdel.util.*;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.File;
import java.util.Optional;

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

    @Value("${spring.profiles.active}")
    private String active;

    @Value("${dataUploadUrl}")
    private String dataUploadUrl;
    @Value("${expressStaffDataUploadUrl}")
    private String expressStaffDataUploadUrl;

    @Value("${zip.encode}")
    private  String zipEncode;

    @Value("${fdfsConfPath}")
    private  String fdfsConfPath;

    private final String ftpUploadPath = "/expressData/";

    @Resource
    private FTPUtil fTPUtil;

    @Autowired
    private PackageSerialDao packageSerialDao;



    /**
     * @description  更新包流水信息
     * @param  packageSerialParam  实体
     * @return  返回结果
     * @date  20/08/19 15:06
     * @author  wanghb
     * @edit
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public UploadDataResult updateStatus(PackageSerialInfo packageSerialParam) {
        String serial = packageSerialParam.getSerial();
        String ftpStatus = packageSerialParam.getFtpStatus();
        String result = packageSerialParam.getResult();
        if(PowerUtil.isNull( serial )){
            return new UploadDataResult( ParamEnum.resultCode.paramError.getCode(),  ParamEnum.resultCode.paramError.getName(),"serial(流水号)字段不能为空.");
        }
        Optional<PackageSerialInfo> packageSerialInfoOptional = packageSerialDao.findById(serial);
        if (!packageSerialInfoOptional.isPresent()){
            return new UploadDataResult( ParamEnum.resultCode.paramError.getCode(),  ParamEnum.resultCode.paramError.getName(), new StringBuilder().append( serial ).append( " 该serial(流水号) 不存在。" ).toString() );
        }else {
            PackageSerialInfo packageSerialInfo = packageSerialInfoOptional.get();
            if (PowerUtil.isNotNull( ftpStatus )) {
                if (!ParamEnum.ftpStatus.status1.getCode().equals( ftpStatus )) {
                    return new UploadDataResult( ParamEnum.resultCode.paramError.getCode(),  ParamEnum.resultCode.paramError.getName(), new StringBuilder( "ftpStatus(ftp文件状态)字段不符合规范.所传参数: " ).append( result ).toString() );
                }
                packageSerialInfo.setFtpStatus( ftpStatus );
            }
            if (PowerUtil.isNotNull( result )) {
                if (!ParamEnum.resultStatus.status1.getCode().equals( result ) && !ParamEnum.resultStatus.status2.getCode().equals( result )) {
                    return new UploadDataResult( ParamEnum.resultCode.paramError.getCode(),  ParamEnum.resultCode.paramError.getName(), new StringBuilder("result(上传结果)字段不符合规范.所传参数:" ).append( result ).toString() );
                }
                packageSerialInfo.setResult( result );
            }
            packageSerialDao.save( packageSerialInfo );
        }
        return new UploadDataResult( ParamEnum.resultCode.success.getCode(),  "更新成功", "" );
    }


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
     * @description  重新上传ftp
     * @param  serial 流水号
     * @return  返回结果
     * @date  20/09/16 10:10
     * @author  wanghb
     * @edit
     */
    @Override
    public UploadDataResult reUploadFtp(String serial) {
        Optional<PackageSerialInfo> packageSerialInfoOptional = packageSerialDao.findById( serial );
        if (!packageSerialInfoOptional.isPresent()) {
            return new UploadDataResult( ParamEnum.resultCode.error.getCode(), "该流水号不存在。" );
        } else {
            PackageSerialInfo packageSerialInfo = packageSerialInfoOptional.get();
            Boolean isSuccess = null;
            try {
                isSuccess = uploadFtp(packageSerialInfo);
            } catch (Exception e) {
                return new UploadDataResult( ParamEnum.resultCode.error.getCode(), "同步ftp异常。异常信息:"+ ExceptionUtil.getOutputStream( e ) );
            }
            if (isSuccess) {
                packageSerialInfo.setSyncFtpStatus( ParamEnum.syncFtpStatus.status1.getCode() );
                packageSerialDao.save( packageSerialInfo );
            }
            return new UploadDataResult( ParamEnum.resultCode.success.getCode(),  ParamEnum.resultCode.success.getName());
        }
    }

    /**
     * @description  删除fastDfs
     * @param  serial
     * @return  返回结果
     * @date  2020-12-8 19:19
     * @author  wanghb
     * @edit
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public UploadDataResult delFastDfs(String serial) throws Exception {
        Optional<PackageSerialInfo> packageSerialInfoOptional = packageSerialDao.findById( serial );
        FastDFSClient fastDFSClient = new FastDFSClient(fdfsConfPath);
        if (!packageSerialInfoOptional.isPresent()) {
            return new UploadDataResult( ParamEnum.resultCode.error.getCode(), "该流水号不存在。" );
        } else {
            PackageSerialInfo packageSerialInfo = packageSerialInfoOptional.get();
            String fastdfsId = packageSerialInfo.getFastdfsId();
            Boolean isSuccess = null;
            try {
                isSuccess = fastDFSClient.deleteFile( fastdfsId );
            } catch (Exception e) {
                return new UploadDataResult( ParamEnum.resultCode.error.getCode(), "删除fastDfs异常。异常信息:"+ ExceptionUtil.getOutputStream( e ) );
            }
            if (isSuccess) {
                packageSerialInfo.setFastdfsStatus( ParamEnum.fastdfsStatus.status1.getCode() );
                packageSerialDao.save( packageSerialInfo );
            }
            return new UploadDataResult( ParamEnum.resultCode.success.getCode(),  ParamEnum.resultCode.success.getName());
        }
    }
}
