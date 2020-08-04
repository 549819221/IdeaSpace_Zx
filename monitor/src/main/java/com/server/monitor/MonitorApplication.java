package com.server.monitor;

import com.server.monitor.service.BasisService;
import com.server.monitor.service.DynamicQuartzService;
import com.server.monitor.util.ScheduledTasks;
import com.server.monitor.util.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

@SpringBootApplication
public class MonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run( MonitorApplication.class, args );
        SpringContextUtil.getBean( BasisService.class ).register();
        SpringContextUtil.getBean( ScheduledTasks.class ).runfirst();
    }

}
