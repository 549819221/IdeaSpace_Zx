package com.server.monitor.util;

import com.server.monitor.entity.ApplicationMonitor;
import com.server.monitor.entity.DiskMonitor;
import com.server.monitor.entity.ServerMonitor;
import com.server.monitor.entity.TaskDo;
import com.server.monitor.entity.parent.Monitor;
import com.server.monitor.service.BasisService;
import com.server.monitor.service.DynamicQuartzService;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
public class ScheduledTasks {
    @Resource
    private DynamicQuartzService dynamicQuartzService;
    /**
     * monitors 下标缓存  键:objId   值:下标
     */
    public static Map<String, Integer> monitorsIndexCache = new HashMap<>();
    /**
     * 任务数据缓存
     */
    public static List<Monitor> monitors = new ArrayList<>();
    /**
     * 参考时间
     */
    public static Long referenceTime = null;

    @Autowired
    private Scheduler scheduler;

    @Scheduled(cron = "0 */1 * * * ?")
    public void runfirst(){
        dynamicQuartzService.quartz();
    }

}