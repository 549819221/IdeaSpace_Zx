package com.server.ftpsync;

import com.server.ftpsync.task.ScheduledTasks;
import com.server.ftpsync.util.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FtpsyncApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run( FtpsyncApplication.class, args );
        //SpringContextUtil.getBean( ScheduledTasks.class ).syncStaffFtp();
        //SpringContextUtil.getBean( ScheduledTasks.class ).syncFtp();
    }

}
