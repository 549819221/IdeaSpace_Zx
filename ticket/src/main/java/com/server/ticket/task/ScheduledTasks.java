package com.server.ticket.task;
import com.alibaba.fastjson.JSON;
import com.server.ticket.dao.PackageSerialDao;
import com.server.ticket.entity.PackageSerialInfo;
import com.server.ticket.entity.UploadDataInfo;
import com.server.ticket.service.BasisService;
import com.server.ticket.util.*;
import lombok.SneakyThrows;
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

    @Autowired
    private PackageSerialDao packageSerialDao;
    @Autowired
    private BasisService basisService;

    @Resource
    private FTPUtil fTPUtil;

    @Value("${zip.encode}")
    private  String zipEncode;

    @Value("${getUserDataUrl}")
    private String getUserDataUrl;

    @Value("${fdfsConfPath}")
    private  String fdfsConfPath;
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
            Map<String, Object> object = (Map<String, Object>)HttpUtil.get( getUserDataUrl, new HashMap<>() ).get( "data" );
            accountData = object;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @description  每五分钟执行的定时任务
     * @date  20/07/16 10:26
     * @author  wanghb
     * @edit
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void syncFtp() {
        /*logger.info( "开始同步FTP==>" + DateUtil.toString( new Date() ,DateUtil.DATE_LONG) );
        List<PackageSerialInfo> packageSerialInfos = packageSerialDao.getBySyncFtpStatus( ParamEnum.syncFtpStatus.status0.getCode() );
        if (packageSerialInfos == null) {
            return;
        }
        logger.info( "获取数据条数==>" + packageSerialInfos.size() );
        int successCount = 0;
        for (int i = 0; i < packageSerialInfos.size(); i++) {
            PackageSerialInfo packageSerialInfo = packageSerialInfos.get( i );
            Boolean isSuccess = false;
            try {
                isSuccess = basisService.uploadFtp(packageSerialInfo);
            } catch (Exception e) {
                logger.error( "这个流水号同步异常==>" + packageSerialInfo.getSerial()+",异常信息:"+ ExceptionUtil.getOutputStream( e ) );
                e.printStackTrace();
            }
            if (isSuccess) {
                successCount++;
                packageSerialInfo.setSyncFtpStatus( ParamEnum.syncFtpStatus.status1.getCode() );
                packageSerialDao.save( packageSerialInfo );
            }
        }
        logger.info( new StringBuilder("成功同步条数==>" ).append( successCount ).toString() );*/
    }

}