package com.server.monitor.dynamicQuartz;

import com.server.monitor.entity.parent.Monitor;
import com.server.monitor.service.BasisService;
import com.server.monitor.service.DynamicQuartzService;
import com.server.monitor.service.impl.DynamicQuartzServiceImpl;
import com.server.monitor.util.MapUtil;
import javafx.scene.control.Tab;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@DisallowConcurrentExecution //定时任务
@Component
public class MonitorJob implements Job {
    private static Logger logger = Logger.getLogger( MonitorJob.class );
    @Resource
    DynamicQuartzService dynamicQuartzService;

    /**
     * @description  动态任务的逻辑处理方法
     * @param  arg0  当前任务的信息
     * @return  void
     * @date  20/07/14 15:35
     * @author  wanghb
     * @edit
     */
    @Override
    public void execute(JobExecutionContext arg0)  {
        String objId = arg0.getJobDetail().getKey().getGroup();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Monitor monitor = MapUtil.toBean( DynamicQuartzServiceImpl.monitorCache.get( objId ), Monitor.class );
        dynamicQuartzService.dealWithJob( DynamicQuartzServiceImpl.monitorCache.get( objId ) );
        logger.info(monitor.getName() + " 这个定时任务在执行! "+df.format(new Date())+" 时间间隔为:"+monitor.getExecuteInterval());
    }

}
