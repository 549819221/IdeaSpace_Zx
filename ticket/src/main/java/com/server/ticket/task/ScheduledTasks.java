package com.server.ticket.task;
import com.alibaba.fastjson.JSON;
import com.server.ticket.dao.PackageSerialDao;
import com.server.ticket.entity.PackageSerialInfo;
import com.server.ticket.entity.UploadDataInfo;
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
    public void syncFtp() throws Exception {
        logger.info( "开始同步FTP==>" + DateUtil.toString( new Date() ,DateUtil.DATE_LONG) );
        List<PackageSerialInfo> packageSerialInfos = packageSerialDao.getBySyncFtpStatus( ParamEnum.syncFtpStatus.status0.getCode() );
        for (PackageSerialInfo packageSerialInfo : packageSerialInfos) {

            FastDFSClient fastDFSClient = new FastDFSClient(fdfsConfPath);
            String fastdfsId = PowerUtil.getString( packageSerialInfo.getFastdfsId() );
            byte[] data = fastDFSClient.download(fastdfsId);
            UploadDataInfo uploadDataInfo = JSON.parseObject(data, UploadDataInfo.class);
            String serial = uploadDataInfo.getSerial();
            String zipPrefix = new StringBuilder(serial).append( "_" ).toString();
            File tempZip =  FileEncryptUtil.encryptStreamZip( JSON.toJSONString(uploadDataInfo),zipPrefix,zipEncode);
            String ftpUploadPath = packageSerialInfo.getFtpPath();
            //文件上传
            Boolean isSuccess = fTPUtil.uploadFile( ftpUploadPath, tempZip );
            tempZip.delete();
            if (isSuccess) {
                packageSerialInfo.setSyncFtpStatus( ParamEnum.syncFtpStatus.status1.getCode() );
                packageSerialDao.save( packageSerialInfo );
            }
        }

    }

}