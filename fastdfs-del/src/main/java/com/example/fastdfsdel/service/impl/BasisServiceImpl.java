package com.example.fastdfsdel.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.fastdfsdel.dao.PackageSerialDao;
import com.example.fastdfsdel.entity.*;
import com.example.fastdfsdel.service.BasisService;
import com.example.fastdfsdel.util.ParamEnum;
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
    private com.example.fastdfsdel.util.FTPUtil fTPUtil;

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
    public UploadDataResult updateStatus(com.example.fastdfsdel.entity.PackageSerialInfo packageSerialParam) {
        String serial = packageSerialParam.getSerial();
        String ftpStatus = packageSerialParam.getFtpStatus();
        String result = packageSerialParam.getResult();
        if(com.example.fastdfsdel.util.PowerUtil.isNull( serial )){
            return new com.example.fastdfsdel.entity.UploadDataResult( com.example.fastdfsdel.util.ParamEnum.resultCode.paramError.getCode(),  com.example.fastdfsdel.util.ParamEnum.resultCode.paramError.getName(),"serial(流水号)字段不能为空.");
        }
        Optional<com.example.fastdfsdel.entity.PackageSerialInfo> packageSerialInfoOptional = packageSerialDao.findById(serial);
        if (!packageSerialInfoOptional.isPresent()){
            return new com.example.fastdfsdel.entity.UploadDataResult( com.example.fastdfsdel.util.ParamEnum.resultCode.paramError.getCode(),  com.example.fastdfsdel.util.ParamEnum.resultCode.paramError.getName(), new StringBuilder().append( serial ).append( " 该serial(流水号) 不存在。" ).toString() );
        }else {
            com.example.fastdfsdel.entity.PackageSerialInfo packageSerialInfo = packageSerialInfoOptional.get();
            if (com.example.fastdfsdel.util.PowerUtil.isNotNull( ftpStatus )) {
                if (!com.example.fastdfsdel.util.ParamEnum.ftpStatus.status1.getCode().equals( result )) {
                    return new com.example.fastdfsdel.entity.UploadDataResult( com.example.fastdfsdel.util.ParamEnum.resultCode.paramError.getCode(),  com.example.fastdfsdel.util.ParamEnum.resultCode.paramError.getName(), new StringBuilder( "ftpStatus(ftp文件状态)字段不符合规范.所传参数: " ).append( result ).toString() );
                }
                packageSerialInfo.setFtpStatus( ftpStatus );
            }
            if (com.example.fastdfsdel.util.PowerUtil.isNotNull( result )) {
                if (!com.example.fastdfsdel.util.ParamEnum.resultStatus.status1.getCode().equals( result ) && !com.example.fastdfsdel.util.ParamEnum.resultStatus.status2.getCode().equals( result )) {
                    return new com.example.fastdfsdel.entity.UploadDataResult( com.example.fastdfsdel.util.ParamEnum.resultCode.paramError.getCode(),  com.example.fastdfsdel.util.ParamEnum.resultCode.paramError.getName(), new StringBuilder("result(上传结果)字段不符合规范.所传参数:" ).append( result ).toString() );
                }
                packageSerialInfo.setResult( result );
            }
            packageSerialDao.save( packageSerialInfo );
        }
        return new com.example.fastdfsdel.entity.UploadDataResult( com.example.fastdfsdel.util.ParamEnum.resultCode.success.getCode(),  "更新成功", "" );
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
    public Boolean uploadFtp(com.example.fastdfsdel.entity.PackageSerialInfo packageSerialInfo) throws Exception {
        com.example.fastdfsdel.util.FastDFSClient fastDFSClient = new com.example.fastdfsdel.util.FastDFSClient(fdfsConfPath);
        String fastdfsId = com.example.fastdfsdel.util.PowerUtil.getString( packageSerialInfo.getFastdfsId() );
        byte[] data = fastDFSClient.download(fastdfsId);
        if (data == null) {
            logger.error( new StringBuilder( "这个流水号,从fstdfs读取为空,流水号:" ).append( packageSerialInfo.getSerial() ).append( ".fastdfsId为" ).append( fastdfsId ).toString() );
            return false;
        }
        com.example.fastdfsdel.entity.UploadDataInfo uploadDataInfo = JSON.parseObject(data, UploadDataInfo.class);
        String serial = uploadDataInfo.getSerial();
        String zipPrefix = new StringBuilder(serial).append( "_" ).toString();
        File tempZip =  com.example.fastdfsdel.util.FileEncryptUtil.encryptStreamZip( JSON.toJSONString(uploadDataInfo),zipPrefix,zipEncode);
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
        Optional<com.example.fastdfsdel.entity.PackageSerialInfo> packageSerialInfoOptional = packageSerialDao.findById( serial );
        if (!packageSerialInfoOptional.isPresent()) {
            return new com.example.fastdfsdel.entity.UploadDataResult( com.example.fastdfsdel.util.ParamEnum.resultCode.error.getCode(), "该流水号不存在。" );
        } else {
            PackageSerialInfo packageSerialInfo = packageSerialInfoOptional.get();
            Boolean isSuccess = null;
            try {
                isSuccess = uploadFtp(packageSerialInfo);
            } catch (Exception e) {
                return new com.example.fastdfsdel.entity.UploadDataResult( com.example.fastdfsdel.util.ParamEnum.resultCode.error.getCode(), "同步ftp异常。异常信息:"+ com.example.fastdfsdel.util.ExceptionUtil.getOutputStream( e ) );
            }
            if (isSuccess) {
                packageSerialInfo.setSyncFtpStatus( com.example.fastdfsdel.util.ParamEnum.syncFtpStatus.status1.getCode() );
                packageSerialDao.save( packageSerialInfo );
            }
            return new UploadDataResult( com.example.fastdfsdel.util.ParamEnum.resultCode.success.getCode(),  ParamEnum.resultCode.success.getName());
        }
    }
}
