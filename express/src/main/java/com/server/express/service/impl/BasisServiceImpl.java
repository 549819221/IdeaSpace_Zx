package com.server.express.service.impl;

import com.alibaba.fastjson.JSON;
import com.server.express.dao.PackageSerialDao;
import com.server.express.entity.*;
import com.server.express.service.BasisService;
import com.server.express.service.PackageSerialService;
import com.server.express.task.ScheduledTasks;
import com.server.express.util.*;
import io.swagger.annotations.ApiOperation;
import net.lingala.zip4j.exception.ZipException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
     * @description  上传数据
     * @return 返回结果
     * @date 20/07/10 15:00
     * @author wanghb
     * @edit
     * @param uploadDataInfo
     * @param uploadUrl
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Object dataUpload(UploadDataInfo uploadDataInfo, String uploadUrl) throws IOException, ZipException {

        String serial = uploadDataInfo.getSerial();
        String encryptData = uploadDataInfo.getEncryptData();
        String accountNo = uploadDataInfo.getAccountNo();
        String signature = uploadDataInfo.getSignature();
        String token = uploadDataInfo.getToken();

        if(PowerUtil.isNull( token )){
            return new UploadDataResult( ParamEnum.resultCode.paramError.getCode(),  ParamEnum.resultCode.paramError.getName(),"token字段不能为空.");
        }
        if(!JwtUtil.verify( token ) ){
            return new UploadDataResult( ParamEnum.resultCode.tokenExpired.getCode(),  ParamEnum.resultCode.tokenExpired.getName(),"");
        }
        if(PowerUtil.isNull( serial )){
            return new UploadDataResult( ParamEnum.resultCode.paramError.getCode(),  ParamEnum.resultCode.paramError.getName(),"serial(流水号)字段不能为空.");
        }
        int count = packageSerialDao.countBySerial(serial);
        if (count > 0){
            return new UploadDataResult( ParamEnum.resultCode.paramError.getCode(),  ParamEnum.resultCode.paramError.getName(), new StringBuilder().append( "此 " ).append( serial ).append( " serial(流水号) 已存在。" ).toString() );
        }
        if(PowerUtil.isNull( encryptData )){
            return new UploadDataResult( ParamEnum.resultCode.paramError.getCode(),  ParamEnum.resultCode.paramError.getName(),"encryptData(主数据)字段不能为空.");
        }
        if(PowerUtil.isNull( accountNo )){
            return new UploadDataResult( ParamEnum.resultCode.paramError.getCode(),  ParamEnum.resultCode.paramError.getName(),"accountNo(快递方账号)字段不能为空.");
        }
        if(PowerUtil.isNull( signature )){
            return new UploadDataResult( ParamEnum.resultCode.paramError.getCode(),  ParamEnum.resultCode.paramError.getName(),"signature(报文签名)字段不能为空.");
        }

        PackageSerialInfo packageSerialInfo = new PackageSerialInfo();
        packageSerialInfo.setSerial(serial);
        packageSerialInfo.setUploadTime(new Date() );
        packageSerialInfo.setResult(ParamEnum.resultStatus.status0.getCode());
        packageSerialInfo.setEvent(ParamEnum.eventStatus.status0.getCode());
        packageSerialInfo.setFtpStatus(ParamEnum.ftpStatus.status0.getCode());
        packageSerialInfo.setSyncFtpStatus( ParamEnum.syncFtpStatus.status0.getCode() );
        packageSerialInfo.setFastdfsStatus( ParamEnum.fastdfsStatus.status0.getCode() );
        packageSerialInfo.setFtpPath( new StringBuilder( ftpUploadPath ).append( uploadUrl ).toString() );
        Boolean isSuccess = null;
        if(ParamEnum.properties.dev.getCode().equals( active ) || ParamEnum.properties.pro.getCode().equals( active )){
            try {
                FastDFSClient fastDFSClient = new FastDFSClient(fdfsConfPath);
                String fastDFSPath = fastDFSClient.uploadFile(JSON.toJSONString(uploadDataInfo).getBytes());
                if (PowerUtil.isNotNull( fastDFSPath )) {
                    byte[] data = fastDFSClient.download(fastDFSPath);
                    if (data == null) {
                        return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "fastDFS文件上传失败。");
                    }
                    packageSerialInfo.setFileSize( data.length );
                    packageSerialInfo.setResult(ParamEnum.resultStatus.status1.getCode());
                    packageSerialInfo.setFastdfsId(fastDFSPath);
                    isSuccess = true;
                }else{
                    isSuccess = false;
                }
            } catch (Exception e) {
                packageSerialInfo.setResult(ParamEnum.resultStatus.status2.getCode());
                logger.error( ExceptionUtil.getOutputStream( e ) );
                return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "fastDFS文件上传异常。",ExceptionUtil.getOutputStream( e ));
            }
        }else  if(ParamEnum.properties.test.getCode().equals( active )){
            //这个是直接请求接口的数据传输
            String url = "";
            if(ParamEnum.uploadUrl.dataUpload.getCode().equals( uploadUrl )){
                url = dataUploadUrl;
            }else if(ParamEnum.uploadUrl.expressStaffDataUpload.getCode().equals( uploadUrl )){
                url = expressStaffDataUploadUrl;
            }
            Map<String, Object> object = HttpUtil.post( url, uploadDataInfo );
            if(PowerUtil.isNull( object )){
                return new UploadDataResult( ParamEnum.resultCode.error.getCode(), "调用接口返回为空。",PowerUtil.getString( object ));
            }else{
                String code = PowerUtil.getString( object.get("code") );
                isSuccess = ParamEnum.resultCode.success.getCode().equals( code );
                if(!isSuccess){
                    return object;
                }
            }
        }
        if(isSuccess){
            packageSerialDao.save( packageSerialInfo );
            return new UploadDataResult( ParamEnum.resultCode.success.getCode(),  ParamEnum.resultCode.success.getName(),"");
        }else {
            return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "上传失败","");
        }
    }

    /**
     * @description  获取token
     * @return 返回结果
     * @date 20/07/10 15:00
     * @author wanghb
     * @edit
     * @param user
     * @param request
     */
    @Override
    public Object getToken(User user, HttpServletRequest request) {
        String account = user.getAccount();
        String password = user.getPassword();
        if(ParamEnum.properties.dev.getCode().equals( active )){
            String token = JwtUtil.sign(user.getAccount(),user.getPassword());
            return new TokenResult(ParamEnum.resultCode.success.getCode(),ParamEnum.resultCode.success.getName(),token);
        }
        if("".equals( account ) || "".equals( password )){
            return new TokenResult(ParamEnum.resultCode.error.getCode(),"用户名或密码不能为空。","");
        }
        if (ScheduledTasks.accountData != null && ScheduledTasks.accountData.size() != 0) {
            if(password.equals( PowerUtil.getString( ScheduledTasks.accountData.get( account) ) )){
                String token = JwtUtil.sign(user.getAccount(),user.getPassword());
                return new TokenResult(ParamEnum.resultCode.success.getCode(),ParamEnum.resultCode.success.getName(),token);
            }else {
                return new TokenResult(ParamEnum.resultCode.error.getCode(),"用户名或密码错误。","");
            }
        } else {
            return new TokenResult(ParamEnum.resultCode.error.getCode(),"用户数据录入为空,请联系管理员。","");
        }
    }

    public static void main(String[] args) {
        System.out.println(JwtUtil.sign("admin","123456"));
    }

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
                return new UploadDataResult( ParamEnum.resultCode.error.getCode(), "同步ftp异常。异常信息:"+ExceptionUtil.getOutputStream( e ) );
            }
            if (isSuccess) {
                packageSerialInfo.setSyncFtpStatus( ParamEnum.syncFtpStatus.status1.getCode() );
                packageSerialDao.save( packageSerialInfo );
            }
            return new UploadDataResult( ParamEnum.resultCode.success.getCode(),  ParamEnum.resultCode.success.getName());
        }
    }
}
