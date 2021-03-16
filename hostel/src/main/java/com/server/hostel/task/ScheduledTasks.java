package com.server.hostel.task;
import com.alibaba.fastjson.JSON;
import com.server.hostel.dao.PackageSerialDao;
import com.server.hostel.entity.PackageSerialInfo;
import com.server.hostel.entity.UploadDataInfo;
import com.server.hostel.service.impl.BasisService;
import com.server.hostel.util.*;
import lombok.SneakyThrows;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
public class ScheduledTasks {
    Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    public static Map<String, Object> accountData = new HashMap<>();
    public static String publicKey = "";


    @Value("${spring.profiles.active}")
    private String active;
    @Autowired
    private PackageSerialDao packageSerialDao;

    @Resource
    private FTPUtil fTPUtil;
    @Resource
    private BasisService basisService;

    @Value("${zip.encode}")
    private  String zipEncode;

    @Value("${fdfsConfPath}")
    private  String fdfsConfPath;
    @Value("${getUserDataUrl}")
    private String getUserDataUrl;



    /**
     * @description  每5分钟执行的定时任务
     * @date  20/07/16 10:26
     * @author  wanghb
     * @edit
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void syncAccountData()  {
        logger.info( "开始同步账号数据==>" + DateUtil.toString( new Date() ,DateUtil.DATE_LONG) );
        try {
            if(ParamEnum.properties.dev.getCode().equals( active )){
                Map<String, Object> temp = new HashMap<>();
                temp.put( "admin","123456" );
                accountData = temp;
            }else {
                Map<String, Object> object = (Map<String, Object>)HttpUtil.get( getUserDataUrl, new HashMap<>() ).get( "data" );
                accountData = object;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @description  每天23点执行
     * @date  20/07/16 10:26
     * @author  wanghb
     * @edit
     */
    @Scheduled(cron = "0 0 23 * * ?")
    public void syncPublicKey()  {
        try {
            basisService.syncPublicKey();
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @description  每10分钟执行的定时任务
     * @date  20/07/16 10:26
     * @author  wanghb
     * @edit
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void syncFtp() throws Exception {
        /*logger.info( "开始同步FTP==>" + DateUtil.toString( new Date() ,DateUtil.DATE_LONG) );
        List<PackageSerialInfo> packageSerialInfos = packageSerialDao.getBySyncFtpStatus( ParamEnum.syncFtpStatus.status0.getCode() );
        if (packageSerialInfos == null) {
            return;
        }
        for (PackageSerialInfo packageSerialInfo : packageSerialInfos) {
            Boolean isSuccess = basisService.uploadFtp(packageSerialInfo);
            if (isSuccess) {
                packageSerialInfo.setSyncFtpStatus( ParamEnum.syncFtpStatus.status1.getCode() );
                packageSerialDao.save( packageSerialInfo );
            }
        }*/
    }


}
