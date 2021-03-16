package com.server.hostel;

import com.server.hostel.task.ScheduledTasks;
import com.server.hostel.util.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class HostelApplication {

    public static void main(String[] args) {
        SpringApplication.run( HostelApplication.class, args );
        SpringContextUtil.getBean( ScheduledTasks.class ).syncAccountData();
        //SpringContextUtil.getBean( ScheduledTasks.class ).syncPublicKey();
    }

}
