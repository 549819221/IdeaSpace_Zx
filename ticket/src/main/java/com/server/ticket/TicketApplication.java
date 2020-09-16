package com.server.ticket;

import com.server.ticket.task.ScheduledTasks;
import com.server.ticket.util.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class TicketApplication {

    public static void main(String[] args) {
        SpringApplication.run( TicketApplication.class, args );
        SpringContextUtil.getBean( ScheduledTasks.class ).syncAccountData();
    }

}
