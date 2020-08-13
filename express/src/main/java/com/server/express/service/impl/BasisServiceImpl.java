package com.server.express.service.impl;

import com.server.express.entity.*;
import com.server.express.service.BasisService;
import com.server.express.util.HttpUtil;
import com.server.express.util.JwtUtil;
import com.server.express.util.ParamEnum;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

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

    private static String dataUploadUrl= "http://app4.zyxlgx.top:9090/delivery/dataUpload";
    @Value("${spring.profiles.active}")
    public String active;


    String testEncryptData = "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";
    /**
     * @description  上传数据
     * @return 返回结果
     * @date 20/07/10 15:00
     * @author wanghb
     * @edit
     * @param uploadDataInfo
     */
    @Override
    public Object dataUpload(UploadDataInfo uploadDataInfo) {
        String token = uploadDataInfo.getToken();
        if( JwtUtil.verify( token ) ){

        }
        Object object = null;
        uploadDataInfo.setEncryptData( testEncryptData);
        if(ParamEnum.properties.dev.getCode().equals( active )){
            try {
                object = HttpUtil.post( dataUploadUrl, uploadDataInfo );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{

        }
        return object;
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


    public static void main(String[] args) {
        System.out.println( JwtUtil.verify( "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXNzd29yZCI6IjEyMzQ1NiIsImV4cCI6MTU5NzIzMjMyNCwiYWNjb3VudCI6ImFkbWluIn0.diN8dbN7VITioIo7pyoQ3ZhpAZJ-uTMDgdNagoQuF3I" ) );
    }

}