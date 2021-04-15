package com.server.ftpsynchostel.task;

import com.alibaba.fastjson.JSON;
import com.server.ftpsynchostel.entity.PackageSerialLgInfo;
import com.server.ftpsynchostel.service.BasisService;
import com.server.ftpsynchostel.util.*;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
public class ScheduledTasks {
    Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    public static Map<String, Object> accountData = new HashMap<>();

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
    private static Object syncLgxxFtpLock = new Object();
    @Scheduled(cron = "0/1 * * * * ?")
    public void syncLgxxFtp() throws Exception {
        synchronized (syncLgxxFtpLock) {
            logger.info( "开始同步旅馆数据FTP==>" + DateUtil.toString( new Date() ,DateUtil.DATE_LONG) );
            Date startDate = new Date();
            /*Integer count = PackageSerialLgDao.countByFtpPathAndSyncFtpStatus(ParamEnum.uploadUrl.lgxx.getCode(),ParamEnum.syncFtpStatus.status0.getCode());
            if(count == 0){
                return;
            }*/
            Thread t0 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),0);  } );
            Thread t1 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),1 * Integer.parseInt( ftpFileSize ));  } );
            Thread t2 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),2 * Integer.parseInt( ftpFileSize )); } );
            Thread t3 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),3 * Integer.parseInt( ftpFileSize )); } );
            Thread t4 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),4 * Integer.parseInt( ftpFileSize )); } );

            //Thread t5 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),5 * Integer.parseInt( ftpFileSize )); } );
            //Thread t6 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),6 * Integer.parseInt( ftpFileSize )); } );
            //Thread t7 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),7 * Integer.parseInt( ftpFileSize )); } );
            //Thread t8 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),8 * Integer.parseInt( ftpFileSize )); } );
            //Thread t9 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),9 * Integer.parseInt( ftpFileSize )); } );

            //Thread t10 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),10 * Integer.parseInt( ftpFileSize )); } );
            //Thread t11 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),11 * Integer.parseInt( ftpFileSize )); } );
            //Thread t12 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),12 * Integer.parseInt( ftpFileSize )); } );
            //Thread t13 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),13 * Inte ger.parseInt( ftpFileSize )); } );
            //Thread t14 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),14 * Integer.parseInt( ftpFileSize )); } );

            //Thread t15 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),15 * Integer.parseInt( ftpFileSize )); } );
            //Thread t16 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),16 * Integer.parseInt( ftpFileSize )); } );
            //Thread t17 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),17 * Integer.parseInt( ftpFileSize )); } );
            //Thread t18 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),18 * Integer.parseInt( ftpFileSize )); } );
            //Thread t19 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),19 * Integer.parseInt( ftpFileSize )); } );

            t0.start();
            t1.start();
            t2.start();
            t3.start();
            t4.start();
            //t5.start();
            //t6.start();
            //t7.start();
            //t8.start();
            //t9.start();

            //t10.start();
            //t11.start();
            //t12.start();
            //t13.start();
            //t14.start();
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
            //t5.join();
            //t6.join();
            //t7.join();
            //t8.join();
            //t9.join();

            //t10.join();
            //t11.join();
            //t12.join();
            //t13.join();
            //t14.join();
            //t15.join();
            //t16.join();
            //t17.join();
            //t18.join();
            //t19.join();
            System.out.println("=================>旅馆数据同步FTP共耗时"+((System.currentTimeMillis() - startDate.getTime()) / 1000)+"秒");
        }
    }

    /**
     * @description  每10分钟执行的定时任务
     * @date  20/07/16 10:26
     * @author  wanghb
     * @edit
     */
    private static Object syncLglkrzxxFtpLock = new Object();
    @Scheduled(cron = "0/1 * * * * ?")
    public void syncLglkrzxxFtp() throws Exception {
        synchronized (syncLglkrzxxFtpLock) {
            logger.info( "开始同步旅客入住数据FTP==>" + DateUtil.toString( new Date() ,DateUtil.DATE_LONG) );
            Date startDate = new Date();
            /*Integer count = PackageSerialLgDao.countByFtpPathAndSyncFtpStatus(ParamEnum.uploadUrl.lgxx.getCode(),ParamEnum.syncFtpStatus.status0.getCode());
            if(count == 0){
                return;
            }*/
            Thread t0 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lglkrzxx.getCode(),0);  } );
            Thread t1 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lglkrzxx.getCode(),1 * Integer.parseInt( ftpFileSize ));  } );
            Thread t2 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lglkrzxx.getCode(),2 * Integer.parseInt( ftpFileSize )); } );
            Thread t3 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lglkrzxx.getCode(),3 * Integer.parseInt( ftpFileSize )); } );
            Thread t4 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lglkrzxx.getCode(),4 * Integer.parseInt( ftpFileSize )); } );

            //Thread t5 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),5 * Integer.parseInt( ftpFileSize )); } );
            //Thread t6 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),6 * Integer.parseInt( ftpFileSize )); } );
            //Thread t7 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),7 * Integer.parseInt( ftpFileSize )); } );
            //Thread t8 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),8 * Integer.parseInt( ftpFileSize )); } );
            //Thread t9 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),9 * Integer.parseInt( ftpFileSize )); } );

            //Thread t10 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),10 * Integer.parseInt( ftpFileSize )); } );
            //Thread t11 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),11 * Integer.parseInt( ftpFileSize )); } );
            //Thread t12 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),12 * Integer.parseInt( ftpFileSize )); } );
            //Thread t13 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),13 * Integer.parseInt( ftpFileSize )); } );
            //Thread t14 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),14 * Integer.parseInt( ftpFileSize )); } );

            //Thread t15 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),15 * Integer.parseInt( ftpFileSize )); } );
            //Thread t16 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),16 * Integer.parseInt( ftpFileSize )); } );
            //Thread t17 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),17 * Integer.parseInt( ftpFileSize )); } );
            //Thread t18 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),18 * Integer.parseInt( ftpFileSize )); } );
            //Thread t19 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),19 * Integer.parseInt( ftpFileSize )); } );


            t0.start();
            t1.start();
            t2.start();
            t3.start();
            t4.start();

            //t5.start();
            //t6.start();
            //t7.start();
            //t8.start();
            //t9.start();

            //t10.start();
            //t11.start();
            //t12.start();
            //t13.start();
            //t14.start();

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
            //t5.join();
            //t6.join();
            //t7.join();
            //t8.join();
            //t9.join();

            //t10.join();
            //t11.join();
            //t12.join();
            //t13.join();
            //t14.join();
            //t15.join();
            //t16.join();
            //t17.join();
            //t18.join();
            //t19.join();
            System.out.println("=================>旅客入住数据同步FTP共耗时"+((System.currentTimeMillis() - startDate.getTime()) / 1000)+"秒");
        }
    }


    /**
     * @description  旅客退房数据
     * @date  20/07/16 10:26
     * @author  wanghb
     * @edit
     */
    private static Object syncLgLktfxxFtpLock = new Object();
    @Scheduled(cron = "0/1 * * * * ?")
    public void syncLktfxxFtp() throws Exception {
        synchronized (syncLgLktfxxFtpLock) {
            logger.info( "开始同步旅客退房数据FTP==>" + DateUtil.toString( new Date() ,DateUtil.DATE_LONG) );
            Date startDate = new Date();
            /*Integer count = PackageSerialLgDao.countByFtpPathAndSyncFtpStatus(ParamEnum.uploadUrl.lgxx.getCode(),ParamEnum.syncFtpStatus.status0.getCode());
            if(count == 0){
                return;
            }*/
            Thread t0 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lglktfxx.getCode(),0);  } );
            Thread t1 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lglktfxx.getCode(),1 * Integer.parseInt( ftpFileSize ));  } );
            Thread t2 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lglktfxx.getCode(),2 * Integer.parseInt( ftpFileSize )); } );
            Thread t3 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lglktfxx.getCode(),3 * Integer.parseInt( ftpFileSize )); } );
            Thread t4 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lglktfxx.getCode(),4 * Integer.parseInt( ftpFileSize )); } );

            //Thread t5 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),5 * Integer.parseInt( ftpFileSize )); } );
            //Thread t6 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),6 * Integer.parseInt( ftpFileSize )); } );
            //Thread t7 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),7 * Integer.parseInt( ftpFileSize )); } );
            //Thread t8 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),8 * Integer.parseInt( ftpFileSize )); } );
            //Thread t9 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),9 * Integer.parseInt( ftpFileSize )); } );

            //Thread t10 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),10 * Integer.parseInt( ftpFileSize )); } );
            //Thread t11 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),11 * Integer.parseInt( ftpFileSize )); } );
            //Thread t12 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),12 * Integer.parseInt( ftpFileSize )); } );
            //Thread t13 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),13 * Integer.parseInt( ftpFileSize )); } );
            //Thread t14 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),14 * Integer.parseInt( ftpFileSize )); } );

            //Thread t15 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),15 * Integer.parseInt( ftpFileSize )); } );
            //Thread t16 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),16 * Integer.parseInt( ftpFileSize )); } );
            //Thread t17 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),17 * Integer.parseInt( ftpFileSize )); } );
            //Thread t18 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),18 * Integer.parseInt( ftpFileSize )); } );
            //Thread t19 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),19 * Integer.parseInt( ftpFileSize )); } );


            t0.start();
            t1.start();
            t2.start();
            t3.start();
            t4.start();

            //t5.start();
            //t6.start();
            //t7.start();
            //t8.start();
            //t9.start();

            //t10.start();
            //t11.start();
            //t12.start();
            //t13.start();
            //t14.start();

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
            //t5.join();
            //t6.join();
            //t7.join();
            //t8.join();
            //t9.join();

            //t10.join();
            //t11.join();
            //t12.join();
            //t13.join();
            //t14.join();
            //t15.join();
            //t16.join();
            //t17.join();
            //t18.join();
            //t19.join();
            System.out.println("=================>旅客退房数据同步FTP共耗时"+((System.currentTimeMillis() - startDate.getTime()) / 1000)+"秒");
        }
    }

    /**
     * @description  旅客换房数据
     * @date  20/07/16 10:26
     * @author  wanghb
     * @edit
     */
    private static Object syncLkhfxxFtpLock = new Object();
    @Scheduled(cron = "0/1 * * * * ?")
    public void syncLkhfxxFtp() throws Exception {
        synchronized (syncLkhfxxFtpLock) {
            logger.info( "开始同步旅客换房数据FTP==>" + DateUtil.toString( new Date() ,DateUtil.DATE_LONG) );
            Date startDate = new Date();
            /*Integer count = PackageSerialLgDao.countByFtpPathAndSyncFtpStatus(ParamEnum.uploadUrl.lgxx.getCode(),ParamEnum.syncFtpStatus.status0.getCode());
            if(count == 0){
                return;
            }*/
            Thread t0 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lglkhfxx.getCode(),0);  } );
            Thread t1 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lglkhfxx.getCode(),1 * Integer.parseInt( ftpFileSize ));  } );
            Thread t2 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lglkhfxx.getCode(),2 * Integer.parseInt( ftpFileSize )); } );
            Thread t3 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lglkhfxx.getCode(),3 * Integer.parseInt( ftpFileSize )); } );
            Thread t4 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lglkhfxx.getCode(),4 * Integer.parseInt( ftpFileSize )); } );

            //Thread t5 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),5 * Integer.parseInt( ftpFileSize )); } );
            //Thread t6 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),6 * Integer.parseInt( ftpFileSize )); } );
            //Thread t7 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),7 * Integer.parseInt( ftpFileSize )); } );
            //Thread t8 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),8 * Integer.parseInt( ftpFileSize )); } );
            //Thread t9 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),9 * Integer.parseInt( ftpFileSize )); } );

            //Thread t10 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),10 * Integer.parseInt( ftpFileSize )); } );
            //Thread t11 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),11 * Integer.parseInt( ftpFileSize )); } );
            //Thread t12 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),12 * Integer.parseInt( ftpFileSize )); } );
            //Thread t13 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),13 * Integer.parseInt( ftpFileSize )); } );
            //Thread t14 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),14 * Integer.parseInt( ftpFileSize )); } );

            //Thread t15 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),15 * Integer.parseInt( ftpFileSize )); } );
            //Thread t16 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),16 * Integer.parseInt( ftpFileSize )); } );
            //Thread t17 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),17 * Integer.parseInt( ftpFileSize )); } );
            //Thread t18 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),18 * Integer.parseInt( ftpFileSize )); } );
            //Thread t19 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),19 * Integer.parseInt( ftpFileSize )); } );


            t0.start();
            t1.start();
            t2.start();
            t3.start();
            t4.start();

            //t5.start();
            //t6.start();
            //t7.start();
            //t8.start();
            //t9.start();

            //t10.start();
            //t11.start();
            //t12.start();
            //t13.start();
            //t14.start();

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
            //t5.join();
            //t6.join();
            //t7.join();
            //t8.join();
            //t9.join();

            //t10.join();
            //t11.join();
            //t12.join();
            //t13.join();
            //t14.join();
            //t15.join();
            //t16.join();
            //t17.join();
            //t18.join();
            //t19.join();
            System.out.println("=================>旅客换房数据同步FTP共耗时"+((System.currentTimeMillis() - startDate.getTime()) / 1000)+"秒");
        }
    }

    /**
     * @description  旅客换房数据
     * @date  20/07/16 10:26
     * @author  wanghb
     * @edit
     */
    private static Object syncLgcyryxxFtpLock = new Object();
    @Scheduled(cron = "0/1 * * * * ?")
    public void syncLgcyryxxFtp() throws Exception {
        synchronized (syncLgcyryxxFtpLock) {
            logger.info( "开始同步旅客换房数据FTP==>" + DateUtil.toString( new Date() ,DateUtil.DATE_LONG) );
            Date startDate = new Date();
            /*Integer count = PackageSerialLgDao.countByFtpPathAndSyncFtpStatus(ParamEnum.uploadUrl.lgxx.getCode(),ParamEnum.syncFtpStatus.status0.getCode());
            if(count == 0){
                return;
            }*/
            Thread t0 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgcyryxx.getCode(),0);  } );
            Thread t1 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgcyryxx.getCode(),1 * Integer.parseInt( ftpFileSize ));  } );
            Thread t2 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgcyryxx.getCode(),2 * Integer.parseInt( ftpFileSize )); } );
            Thread t3 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgcyryxx.getCode(),3 * Integer.parseInt( ftpFileSize )); } );
            Thread t4 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgcyryxx.getCode(),4 * Integer.parseInt( ftpFileSize )); } );

            //Thread t5 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),5 * Integer.parseInt( ftpFileSize )); } );
            //Thread t6 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),6 * Integer.parseInt( ftpFileSize )); } );
            //Thread t7 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),7 * Integer.parseInt( ftpFileSize )); } );
            //Thread t8 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),8 * Integer.parseInt( ftpFileSize )); } );
            //Thread t9 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),9 * Integer.parseInt( ftpFileSize )); } );

            //Thread t10 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),10 * Integer.parseInt( ftpFileSize )); } );
            //Thread t11 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),11 * Integer.parseInt( ftpFileSize )); } );
            //Thread t12 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),12 * Integer.parseInt( ftpFileSize )); } );
            //Thread t13 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),13 * Integer.parseInt( ftpFileSize )); } );
            //Thread t14 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),14 * Integer.parseInt( ftpFileSize )); } );

            //Thread t15 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),15 * Integer.parseInt( ftpFileSize )); } );
            //Thread t16 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),16 * Integer.parseInt( ftpFileSize )); } );
            //Thread t17 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),17 * Integer.parseInt( ftpFileSize )); } );
            //Thread t18 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),18 * Integer.parseInt( ftpFileSize )); } );
            //Thread t19 = new Thread( () -> {processWhile(ParamEnum.uploadUrl.lgxx.getCode(),19 * Integer.parseInt( ftpFileSize )); } );


            t0.start();
            t1.start();
            t2.start();
            t3.start();
            t4.start();

            //t5.start();
            //t6.start();
            //t7.start();
            //t8.start();
            //t9.start();

            //t10.start();
            //t11.start();
            //t12.start();
            //t13.start();
            //t14.start();

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
            //t5.join();
            //t6.join();
            //t7.join();
            //t8.join();
            //t9.join();

            //t10.join();
            //t11.join();
            //t12.join();
            //t13.join();
            //t14.join();
            //t15.join();
            //t16.join();
            //t17.join();
            //t18.join();
            //t19.join();
            System.out.println("=================>旅客换房数据同步FTP共耗时"+((System.currentTimeMillis() - startDate.getTime()) / 1000)+"秒");
        }
    }

    private Boolean processWhile(String path,Integer startIndex)  {
        String sql = "select * from package_serial_lg where ftp_path = '"+path+"' and sync_ftp_status = '"+ParamEnum.syncFtpStatus.status0.getCode()+"' ";
        if(PowerUtil.isNotNull( startUploadTime )){
            sql += " and upload_time >= '"+startUploadTime+"'";
        }
        if(PowerUtil.isNotNull( endUploadTime )){
            sql += " and upload_time <= '"+endUploadTime+"'";
        }
        sql += " limit "+startIndex+", "+ftpFileSize;
        logger.info( "此次之行的SQL=====>"+sql );
        List<PackageSerialLgInfo> PackageSerialLgInfos = jdbcTemplate.query(sql,new BeanPropertyRowMapper(PackageSerialLgInfo.class));
        //List<PackageSerialLgInfo> PackageSerialLgInfos = PackageSerialLgDao.getBySyncFtpStatus( ParamEnum.syncFtpStatus.status0.getCode() );
        logger.info( "往ftp这个地址====>"+path+",同步压缩包,查询的总条数==>" + PackageSerialLgInfos.size() );
        if (PackageSerialLgInfos == null || PackageSerialLgInfos.size() == 0) {
            return false;
        }
        try {
            Boolean isSuccess = basisService.uploadFtp(PackageSerialLgInfos,path);
        } catch (Exception e) {
            logger.error( "往ftp这个地址====>"+path+",同步压缩包异常,异常信息:"+ ExceptionUtil.getOutputStream( e ) );
        }
        Integer successCount = 0;
        Integer failCount = 0;
        Integer noCount = 0;
        try {
            for (PackageSerialLgInfo PackageSerialLgInfo : PackageSerialLgInfos) {
                if(ParamEnum.syncFtpStatus.status2.getCode().equals( PackageSerialLgInfo.getSyncFtpStatus(  ) )){
                    failCount++;
                }else if(ParamEnum.syncFtpStatus.status0.getCode().equals( PackageSerialLgInfo.getSyncFtpStatus(  ) )){
                    noCount++;
                }else{
                    successCount++;
                }
            }
            namedParameterJdbcTemplate.batchUpdate("update package_serial_lg set sync_ftp_status = :syncFtpStatus where serial = :serial",JdbcTemplateUtil.ListBeanPropSource( PackageSerialLgInfos ) );
        } catch (Exception e) {
            logger.error( "往ftp这个地址====>"+path+",更新异常,异常信息:"+ ExceptionUtil.getOutputStream( e ) );
            logger.error( "异常数据:====>"+ JSON.toJSONString( PackageSerialLgInfos ) );
        }
        logger.info( "往ftp这个地址====>"+path+",同步压缩包,成功同步数据条数==>" + successCount + "失败条数==>"+failCount+ "未同步条数==>"+noCount );
        return true;
    }


}
