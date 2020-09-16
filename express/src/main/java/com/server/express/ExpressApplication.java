package com.server.express;

import com.server.express.task.ScheduledTasks;
import com.server.express.util.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
@EnableSwagger2
public class ExpressApplication {
    public static void main(String[] args) {

        SpringApplication.run( ExpressApplication.class, args );
        SpringContextUtil.getBean( ScheduledTasks.class ).syncAccountData();

    }

}
