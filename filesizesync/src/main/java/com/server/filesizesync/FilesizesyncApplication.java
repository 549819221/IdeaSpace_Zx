package com.server.filesizesync;

import com.server.filesizesync.task.ScheduledTasks;
import com.server.filesizesync.util.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilesizesyncApplication {
    public static void main(String[] args) {
        SpringApplication.run( FilesizesyncApplication.class, args );
        SpringContextUtil.getBean( ScheduledTasks.class ).syncFileSiz();
    }

}
