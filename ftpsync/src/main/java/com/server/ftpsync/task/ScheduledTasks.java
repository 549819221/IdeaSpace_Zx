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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
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
    private static Object syncFtpLock = new Object();

    @Scheduled(cron = "0/1 * * * * ?")
    public void syncFtp() throws Exception {
        synchronized (syncFtpLock) {
            logger.info( "开始同步寄递数据FTP==>" + DateUtil.toString( new Date() ,DateUtil.DATE_LONG) );
            Date startDate = new Date();
            /*Integer count = packageSerialDao.countByFtpPathAndSyncFtpStatus(ParamEnum.uploadFapPath.dataUpload.getCode(),ParamEnum.syncFtpStatus.status0.getCode());
            if(count == 0){
                return;
            }*/
            Thread t0 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.dataUpload.getCode(),0);  } );
            Thread t1 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.dataUpload.getCode(),1 * Integer.parseInt( ftpFileSize ));  } );
            Thread t2 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.dataUpload.getCode(),2 * Integer.parseInt( ftpFileSize )); } );
            Thread t3 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.dataUpload.getCode(),3 * Integer.parseInt( ftpFileSize )); } );
            Thread t4 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.dataUpload.getCode(),4 * Integer.parseInt( ftpFileSize )); } );

            Thread t5 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.dataUpload.getCode(),5 * Integer.parseInt( ftpFileSize )); } );
            Thread t6 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.dataUpload.getCode(),6 * Integer.parseInt( ftpFileSize )); } );
            Thread t7 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.dataUpload.getCode(),7 * Integer.parseInt( ftpFileSize )); } );
            Thread t8 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.dataUpload.getCode(),8 * Integer.parseInt( ftpFileSize )); } );
            Thread t9 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.dataUpload.getCode(),9 * Integer.parseInt( ftpFileSize )); } );

            Thread t10 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.dataUpload.getCode(),10 * Integer.parseInt( ftpFileSize )); } );
            Thread t11 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.dataUpload.getCode(),11 * Integer.parseInt( ftpFileSize )); } );
            Thread t12 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.dataUpload.getCode(),12 * Integer.parseInt( ftpFileSize )); } );
            Thread t13 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.dataUpload.getCode(),13 * Integer.parseInt( ftpFileSize )); } );
            Thread t14 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.dataUpload.getCode(),14 * Integer.parseInt( ftpFileSize )); } );

            //Thread t15 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.dataUpload.getCode(),15 * Integer.parseInt( ftpFileSize )); } );
            //Thread t16 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.dataUpload.getCode(),16 * Integer.parseInt( ftpFileSize )); } );
            //Thread t17 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.dataUpload.getCode(),17 * Integer.parseInt( ftpFileSize )); } );
            //Thread t18 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.dataUpload.getCode(),18 * Integer.parseInt( ftpFileSize )); } );
            //Thread t19 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.dataUpload.getCode(),19 * Integer.parseInt( ftpFileSize )); } );

            t0.start();
            t1.start();
            t2.start();
            t3.start();
            t4.start();
            t5.start();
            t6.start();
            t7.start();
            t8.start();
            t9.start();

            t10.start();
            t11.start();
            t12.start();
            t13.start();
            t14.start();
            //t15.start();
            //t16.start();
            //t17.start();
            //t18.start();
            //t19.start();


            t0.join();
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
            t6.join();
            t7.join();
            t8.join();
            t9.join();

            t10.join();
            t11.join();
            t12.join();
            t13.join();
            t14.join();
            //t15.join();
            //t16.join();
            //t17.join();
            //t18.join();
            //t19.join();
            System.out.println("=================>寄递数据同步FTP共耗时"+((System.currentTimeMillis() - startDate.getTime()) / 1000)+"秒");
        }
    }

    /**
     * @description  每10分钟执行的定时任务
     * @date  20/07/16 10:26
     * @author  wanghb
     * @edit
     */
    private static Object syncStaffFtpLock = new Object();
    @Scheduled(cron = "0/1 * * * * ?")
    public void syncStaffFtp() throws Exception {
        synchronized (syncStaffFtpLock) {
            logger.info( "开始同步快递员数据FTP==>" + DateUtil.toString( new Date() ,DateUtil.DATE_LONG) );
            Date startDate = new Date();
            /*Integer count = packageSerialDao.countByFtpPathAndSyncFtpStatus(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),ParamEnum.syncFtpStatus.status0.getCode());
            if(count == 0){
                return;
            }*/
            Thread t0 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),0);  } );
            Thread t1 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),1 * Integer.parseInt( ftpFileSize ));  } );
            Thread t2 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),2 * Integer.parseInt( ftpFileSize )); } );
            Thread t3 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),3 * Integer.parseInt( ftpFileSize )); } );
            Thread t4 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),4 * Integer.parseInt( ftpFileSize )); } );

            Thread t5 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),5 * Integer.parseInt( ftpFileSize )); } );
            Thread t6 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),6 * Integer.parseInt( ftpFileSize )); } );
            Thread t7 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),7 * Integer.parseInt( ftpFileSize )); } );
            Thread t8 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),8 * Integer.parseInt( ftpFileSize )); } );
            Thread t9 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),9 * Integer.parseInt( ftpFileSize )); } );

            Thread t10 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),10 * Integer.parseInt( ftpFileSize )); } );
            Thread t11 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),11 * Integer.parseInt( ftpFileSize )); } );
            Thread t12 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),12 * Integer.parseInt( ftpFileSize )); } );
            Thread t13 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),13 * Integer.parseInt( ftpFileSize )); } );
            Thread t14 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),14 * Integer.parseInt( ftpFileSize )); } );

            //Thread t15 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),15 * Integer.parseInt( ftpFileSize )); } );
            //Thread t16 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),16 * Integer.parseInt( ftpFileSize )); } );
            //Thread t17 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),17 * Integer.parseInt( ftpFileSize )); } );
            //Thread t18 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),18 * Integer.parseInt( ftpFileSize )); } );
            //Thread t19 = new Thread( () -> {processWhile(ParamEnum.uploadFapPath.expressStaffDataUpload.getCode(),19 * Integer.parseInt( ftpFileSize )); } );


            t0.start();
            t1.start();
            t2.start();
            t3.start();
            t4.start();
            t5.start();
            t6.start();
            t7.start();
            t8.start();
            t9.start();

            t10.start();
            t11.start();
            t12.start();
            t13.start();
            t14.start();

            //t15.start();
            //t16.start();
            //t17.start();
            //t18.start();
            //t19.start();

            t0.join();
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();
            t6.join();
            t7.join();
            t8.join();
            t9.join();

            t10.join();
            t11.join();
            t12.join();
            t13.join();
            t14.join();
            //t15.join();
            //t16.join();
            //t17.join();
            //t18.join();
            //t19.join();
            System.out.println("=================>快递员数据同步FTP共耗时"+((System.currentTimeMillis() - startDate.getTime()) / 1000)+"秒");
        }
    }

    private Boolean processWhile(String path,Integer startIndex)  {
        String sql = "select * from package_serial where ftp_path = '"+path+"' and sync_ftp_status = '"+ParamEnum.syncFtpStatus.status0.getCode()+"' ";
        if(PowerUtil.isNotNull( startUploadTime )){
            sql += " and upload_time >= '"+startUploadTime+"'";
        }
        if(PowerUtil.isNotNull( endUploadTime )){
            sql += " and upload_time <= '"+endUploadTime+"'";
        }
        sql += " limit "+startIndex+", "+ftpFileSize;
        logger.info( "此次之行的SQL=====>"+sql );
        List<PackageSerialInfo> packageSerialInfos = jdbcTemplate.query(sql,new BeanPropertyRowMapper(PackageSerialInfo.class));
        //List<PackageSerialInfo> packageSerialInfos = packageSerialDao.getBySyncFtpStatus( ParamEnum.syncFtpStatus.status0.getCode() );
        logger.info( "往ftp这个地址====>"+path+",同步压缩包,查询的总条数==>" + packageSerialInfos.size() );
        if (packageSerialInfos == null || packageSerialInfos.size() == 0) {
            return false;
        }
        try {
            Boolean isSuccess = basisService.uploadFtp(packageSerialInfos,path);
        } catch (Exception e) {
            logger.error( "往ftp这个地址====>"+path+",同步压缩包异常,异常信息:"+ ExceptionUtil.getOutputStream( e ) );
        }
        Integer successCount = 0;
        Integer failCount = 0;
        Integer noCount = 0;
        try {
            for (PackageSerialInfo packageSerialInfo : packageSerialInfos) {
                if(ParamEnum.syncFtpStatus.status2.getCode().equals( packageSerialInfo.getSyncFtpStatus(  ) )){
                    failCount++;
                }else if(ParamEnum.syncFtpStatus.status0.getCode().equals( packageSerialInfo.getSyncFtpStatus(  ) )){
                    noCount++;
                }else{
                    successCount++;
                }
            }
            namedParameterJdbcTemplate.batchUpdate("update package_serial set sync_ftp_status = :syncFtpStatus where serial = :serial",JdbcTemplateUtil.ListBeanPropSource( packageSerialInfos ) );
        } catch (Exception e) {
            logger.error( "往ftp这个地址====>"+path+",更新异常,异常信息:"+ ExceptionUtil.getOutputStream( e ) );
            logger.error( "异常数据:====>"+ JSON.toJSONString( packageSerialInfos ) );
        }
        logger.info( "往ftp这个地址====>"+path+",同步压缩包,成功同步数据条数==>" + successCount + "失败条数==>"+failCount+ "未同步条数==>"+noCount );
        return true;
    }


}
