package com.server.ticket.service.impl;

import com.alibaba.fastjson.JSON;
import com.server.ticket.dao.PackageSerialDao;
import com.server.ticket.entity.*;
import com.server.ticket.service.BasisService;
import com.server.ticket.task.ScheduledTasks;
import com.server.ticket.util.*;
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
import java.util.Date;
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


    @Value("${projectUrl}")
    private String projectUrl;

    @Value("${zip.encode}")
    private  String zipEncode;
    @Autowired
    private PackageSerialDao packageSerialDao;
    private final String ftpUploadPath = "/ticket";



    @Resource
    private FTPUtil fTPUtil;

    @Value("${fdfsConfPath}")
    private  String fdfsConfPath;

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
        if( !JwtUtil.verify( token ) ){
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
        packageSerialInfo.setFtpPath( new StringBuilder( uploadUrl.replaceAll( "/dataUpload","/ticketData" ) ).toString() );
        Boolean isSuccess = null;
        if(ParamEnum.properties.dev.getCode().equals( active ) || ParamEnum.properties.pro.getCode().equals( active )){
            try {
                FastDFSClient fastDFSClient = new FastDFSClient(fdfsConfPath );
                String fastDFSPath = fastDFSClient.uploadFile(JSON.toJSONString(uploadDataInfo).getBytes());
                if (PowerUtil.isNotNull( fastDFSPath )) {
                    packageSerialInfo.setResult(ParamEnum.resultStatus.status1.getCode());
                    packageSerialInfo.setFastdfsId(fastDFSPath);
                    isSuccess =true;
                }else{
                    isSuccess = false;
                }
            } catch (Exception e) {
                packageSerialInfo.setResult(ParamEnum.resultStatus.status2.getCode());
                logger.error( ExceptionUtil.getOutputStream( e ) );
                e.printStackTrace();
            }
        }else{
            String url = new StringBuilder().append( projectUrl ).append( uploadUrl ).toString();
            Map<String, Object> object = HttpUtil.post( url, uploadDataInfo );
            if(PowerUtil.isNull( object )){
                return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "调用接口返回为空。",PowerUtil.getString( object ));
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
            return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "上传失败。","");
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
    public UploadDataResult updateStatus(String packageSerialParam) {
        return new UploadDataResult( ParamEnum.resultCode.success.getCode(),  "更新成功", "" );
    }
}