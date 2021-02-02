package com.server.fastdfstest.task;

import com.alibaba.fastjson.JSON;
import com.server.fastdfstest.dao.PackageSerialDao;
import com.server.fastdfstest.entity.PackageSerialBakInfo;
import com.server.fastdfstest.entity.PackageSerialInfo;
import com.server.fastdfstest.entity.UploadDataResult;
import com.server.fastdfstest.service.BasisService;
import com.server.fastdfstest.util.*;
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
import java.util.*;

@Component
@EnableScheduling
public class ScheduledTasks {
    Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    public static Map<String, Object> accountData = new HashMap<>();


    @Value("${spring.profiles.active}")
    private String active;
    @Autowired
    private PackageSerialDao packageSerialDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Resource
    private FTPUtil fTPUtil;
    @Resource
    private BasisService basisService;

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
            if(ParamEnum.properties.dev.getCode().equals( active )){
                Map<String, Object> temp = new HashMap<>();
                temp.put( "admin","123456" );
                accountData = temp;
            }else {
                Map<String, Object> object = (Map<String, Object>) HttpUtil.get( getUserDataUrl, new HashMap<>() ).get( "data" );
                accountData = object;
            }
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
    public synchronized void delDfst(){
        if (false) {
            return;
        }
        logger.info( "========================>开始执行删除数据" );
        String sql = "select * from package_serial_bak where sync_ftp_status = 0  limit 0 ,1000";
        List<PackageSerialBakInfo> packageSerialBakInfos = jdbcTemplate.query(sql,new BeanPropertyRowMapper( PackageSerialBakInfo.class));
        List<PackageSerialBakInfo> successList = new ArrayList<>();
        try {

            for (int i = 0; i < packageSerialBakInfos.size(); i++) {
                FastDFSClient fastDFSClient = new FastDFSClient(fdfsConfPath);
                PackageSerialBakInfo packageSerialBakInfo = packageSerialBakInfos.get(i);
                String fastdfsId = packageSerialBakInfo.getFastdfsId();
                Boolean isSuccess = fastDFSClient.deleteFile( fastdfsId );
                if (isSuccess) {
                    successList.add( packageSerialBakInfo );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info( "此次查询数据====>"+packageSerialBakInfos.size() );
        logger.info( "删除成功的数据====>"+successList.size() );
        int[] delList = namedParameterJdbcTemplate.batchUpdate( "delete from package_serial_bak where serial = :serial ", JdbcTemplateUtil.ListBeanPropSource( packageSerialBakInfos ) );
        logger.info( "此次成功删除数据====>"+delList.length );

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
