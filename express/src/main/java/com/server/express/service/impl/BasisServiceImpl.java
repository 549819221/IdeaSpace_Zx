package com.server.express.service.impl;

import com.server.express.entity.UploadDataInfo;
import com.server.express.service.BasisService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

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

    @Value("${nodeIp}")
    public String nodeIp;


    /**
     * @description  上传数据
     * @return 返回结果
     * @date 20/07/10 15:00
     * @author wanghb
     * @edit
     * @param uploadDataInfo
     */
    @Override
    public void dataUpload(UploadDataInfo uploadDataInfo) {

    }

    /**
     * @description  节点注册
     * @return 返回结果
     * @date 20/07/10 15:00
     * @author wanghb
     * @edit
     */
    @Override
    public void getToken() {

    }



}