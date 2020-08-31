package com.server.ticket.service.impl;

import com.alibaba.fastjson.JSON;
import com.server.ticket.entity.*;
import com.server.ticket.service.BasisService;
import com.server.ticket.util.*;
import net.lingala.zip4j.exception.ZipException;
import org.apache.log4j.Logger;
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

    private final String ftpUploadPath = "/data";

    @Resource
    private FTPUtil fTPUtil;



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
        Boolean isSuccess;
        if(ParamEnum.properties.dev.getCode().equals( active )){
            String zipPrefix = new StringBuilder(serial).append( "_" ).toString();
            File tempZip =  FileEncryptUtil.encryptStreamZip(JSON.toJSONString(uploadDataInfo),zipPrefix,zipEncode);
            //文件上传
            isSuccess = fTPUtil.uploadFile( ftpUploadPath, tempZip );
            packageSerialInfo.setFtpPath( ftpUploadPath +"/"+ serial + ".zip" );
            tempZip.delete();
        }else{
            String url = new StringBuilder().append( projectUrl ).append( uploadUrl ).toString();
            Map<String, Object> object = HttpUtil.post( url, uploadDataInfo );
            if(PowerUtil.isNull( object )){
                return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  ParamEnum.resultCode.error.getName(),PowerUtil.getString( object ));
            }else{
                String code = PowerUtil.getString( object.get("code") );
                isSuccess = ParamEnum.resultCode.success.getCode().equals( code );
                if(!isSuccess){
                  return object;
                }
            }
            //FastDFS的上传方式  后边可能会用
            /*try {
                FastDFSClient fastDFSClient = new FastDFSClient("classpath:fdfs_client.conf");
                String fastDFSPath = fastDFSClient.uploadFile(JSON.toJSONString(uploadDataInfo).getBytes());
                if (PowerUtil.isNotNull( fastDFSPath )) {
                    packageSerialInfo.setResult(ParamEnum.resultStatus.status1.getCode());
                    packageSerialInfo.setFastdfsId(fastDFSPath);
                }
            } catch (Exception e) {
                packageSerialInfo.setResult(ParamEnum.resultStatus.status2.getCode());
                e.printStackTrace();
            }*/
        }
        if(isSuccess){
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
    public TokenResult getToken(User user, HttpServletRequest request) {
        String token = JwtUtil.sign(user.getAccount(),user.getPassword());
        return new TokenResult(ParamEnum.resultCode.success.getCode(),ParamEnum.resultCode.success.getName(),token);
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