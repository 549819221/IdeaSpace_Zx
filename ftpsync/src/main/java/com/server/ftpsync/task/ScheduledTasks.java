package com.server.ftpsync.task;

import com.alibaba.fastjson.JSON;
import com.server.ftpsync.dao.PackageSerialDao;
import com.server.ftpsync.entity.PackageSerialInfo;
import com.server.ftpsync.service.BasisService;
import com.server.ftpsync.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
    @Resource
    private BasisService basisService;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Value("${ftp.fileSize}")
    private String ftpFileSize;
    @Value("${start.uploadTime}")
    private String startUploadTime;
    @Value("${end.uploadTime}")
    private String endUploadTime;
    @Value("${ftp.paths}")
    private String ftpPaths;

    /**
     * @description  每10分钟执行的定时任务
     * @date  20/07/16 10:26
     * @author  wanghb
     * @edit
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public synchronized void syncFtp() throws Exception {
        logger.info( "开始同步FTP==>" + DateUtil.toString( new Date() ,DateUtil.DATE_LONG) );
        Boolean isWhile = true;
        while (isWhile){
            isWhile = processWhile();
        }
    }


    private Boolean processWhile()  {
        String[] paths = ftpPaths.split( "," );
        int count = 0;
        for (String path : paths) {
            String sql = "select * from package_serial where ftp_path = '"+path+"' and sync_ftp_status = '"+ParamEnum.syncFtpStatus.status0.getCode()+"' ";
            if(PowerUtil.isNotNull( startUploadTime )){
                sql += " and upload_time >= '"+startUploadTime+"'";
            }
            if(PowerUtil.isNotNull( endUploadTime )){
                sql += " and upload_time <= '"+endUploadTime+"'";
            }
            sql += " limit 0 , "+ftpFileSize;
            logger.info( "此次之行的SQL====>"+sql );
            List<PackageSerialInfo> packageSerialInfos = jdbcTemplate.query(sql,new BeanPropertyRowMapper(PackageSerialInfo.class));
            //List<PackageSerialInfo> packageSerialInfos = packageSerialDao.getBySyncFtpStatus( ParamEnum.syncFtpStatus.status0.getCode() );
            logger.info( "往ftp这个地址====>"+path+",同步压缩包,查询的总条数==>" + packageSerialInfos.size() );
            if (packageSerialInfos == null || packageSerialInfos.size() == 0) {
                count ++;
                continue;
            }
            try {
            Boolean isSuccess = basisService.uploadFtp(packageSerialInfos,path);
            } catch (Exception e) {
                logger.error( "往ftp这个地址====>"+path+",同步压缩包异常,异常信息:"+ ExceptionUtil.getOutputStream( e ) );
            }
            Integer successCount = 0;
            Integer failCount = 0;
            try {
                for (PackageSerialInfo packageSerialInfo : packageSerialInfos) {
                    if(ParamEnum.syncFtpStatus.status2.getCode().equals( packageSerialInfo.getSyncFtpStatus(  ) )){
                        failCount++;
                    }else{
                        successCount++;
                    }
                }
                packageSerialDao.saveAll( packageSerialInfos );
            } catch (Exception e) {
                logger.error( "往ftp这个地址====>"+path+",更新异常,异常信息:"+ ExceptionUtil.getOutputStream( e ) );
                logger.error( "异常数据:====>"+ JSON.toJSONString( packageSerialInfos ) );
            }
            logger.info( "往ftp这个地址====>"+path+",同步压缩包,成功同步数据条数==>" + successCount + "失败条数==>"+failCount );
        }
        if (count == paths.length) {
            return false;
        }else {
            return true;
        }
        /*int successCount = 0;
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
        }*/
    }


}
