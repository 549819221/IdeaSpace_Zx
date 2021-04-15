package com.server.ftpsynchostel;

import com.server.ftpsynchostel.task.ScheduledTasks;
import com.server.ftpsynchostel.util.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FtpsyncHostelApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run( FtpsyncHostelApplication.class, args );
        System.out.println("=================================>启动成功<=================================");
    }

}
