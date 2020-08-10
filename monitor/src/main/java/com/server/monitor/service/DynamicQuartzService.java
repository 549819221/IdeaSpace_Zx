package com.server.monitor.service;

import com.server.monitor.entity.TaskDo;
import com.server.monitor.entity.parent.Monitor;
import org.quartz.SchedulerException;

import java.io.IOException;
import java.util.Map;

/**
 * 动态定时任务服务接口
 * @author wanghb
 * @since 2020-07-10 14:43:45
 */
public interface DynamicQuartzService {
    
    Map<String, Object> quartz();
    
    void pauseJob(TaskDo task);
    
    void resumeJob(TaskDo task);
    
    void deleteJob(TaskDo task);
    
    void runJobNow(TaskDo task);
    
    void updateJob(TaskDo task);

    void addJob(TaskDo task);

    boolean isExist(String s) throws SchedulerException;

    void dealWithJob(Map<String, Object> monitor);
}
