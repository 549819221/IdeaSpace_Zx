package com.server.ftpsync.task;

import com.server.ftpsync.dao.PackageSerialDao;
import com.server.ftpsync.entity.PackageSerialInfo;
import com.server.ftpsync.service.BasisService;
import com.server.ftpsync.util.DateUtil;
import com.server.ftpsync.util.ExceptionUtil;
import com.server.ftpsync.util.FTPUtil;
import com.server.ftpsync.util.ParamEnum;
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


    /**
     * @description  每10分钟执行的定时任务
     * @date  20/07/16 10:26
     * @author  wanghb
     * @edit
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public synchronized void syncFtp() throws Exception {
        logger.info( "开始同步FTP==>" + DateUtil.toString( new Date() ,DateUtil.DATE_LONG) );
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
        logger.info( new StringBuilder("成功同步条数==>" ).append( successCount ).toString() );
    }


}