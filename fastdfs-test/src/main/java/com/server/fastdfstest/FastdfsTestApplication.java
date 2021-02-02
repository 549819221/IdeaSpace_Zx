package com.server.fastdfstest;

import com.server.fastdfstest.task.ScheduledTasks;
import com.server.fastdfstest.util.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FastdfsTestApplication {

    public static void main(String[] args) {
        SpringApplication.run( FastdfsTestApplication.class, args );
        SpringContextUtil.getBean( ScheduledTasks.class ).syncAccountData();
        SpringContextUtil.getBean( ScheduledTasks.class ).delDfst();
    }

}
