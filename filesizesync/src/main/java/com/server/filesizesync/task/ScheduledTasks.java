package com.server.filesizesync.task;
import com.server.filesizesync.dao.PackageSerialDao;
import com.server.filesizesync.entity.PackageSerialInfo;
import com.server.filesizesync.service.BasisService;
import com.server.filesizesync.util.*;
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


    @Value("${fdfsConfPath}")
    private  String fdfsConfPath;
    /**
     * @description  每10分钟执行的定时任务
     * @date  20/07/16 10:26
     * @author  wanghb
     * @edit
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public synchronized void syncFileSiz()  {
        logger.info( "开始同步反写文件大小==>" + DateUtil.toString( new Date() ,DateUtil.DATE_LONG) );
        //List<PackageSerialInfo> packageSerialInfos = packageSerialDao.getByFileSize( null );
        Boolean isWhile = true;
        while (isWhile){
            isWhile = processWhile();

        }
    }

    private Boolean processWhile() {
        List<PackageSerialInfo> packageSerialInfos = jdbcTemplate.query("select * from package_serial where file_size is null limit 0 , 100 ",new BeanPropertyRowMapper(PackageSerialInfo.class));
        logger.info( "获取数据条数==>" + packageSerialInfos.size() );
        if (packageSerialInfos == null || packageSerialInfos.size() == 0) {
            return false;
        }
        int successCount = 0;
        for (int i = 0; i < packageSerialInfos.size(); i++) {
            PackageSerialInfo packageSerialInfo = packageSerialInfos.get( i );
            try {
                FastDFSClient  fastDFSClient = new FastDFSClient(fdfsConfPath);
                String fastdfsId = PowerUtil.getString( packageSerialInfo.getFastdfsId() );
                byte[] data = fastDFSClient.download(fastdfsId);
                if (data == null) {
                    logger.error( new StringBuilder( "这个流水号,从fstdfs读取为空,流水号:" ).append( packageSerialInfo.getSerial() ).append( ".fastdfsId为" ).append( fastdfsId ).toString() );
                }else{
                    successCount++;
                    packageSerialInfo.setFileSize( data.length );
                    packageSerialDao.save( packageSerialInfo );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.info( new StringBuilder("成功同步条数==>" ).append( successCount ).toString() );
        return true;
    }


}