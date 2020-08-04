package com.server.monitor.dynamicQuartz;

import com.server.monitor.entity.TaskDo;
import com.server.monitor.entity.parent.Monitor;
import com.server.monitor.service.BasisService;
import com.server.monitor.service.DynamicQuartzService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;



@Component
@Order(value = 1)
public class ScheduleJobInitListener implements CommandLineRunner {


    @Override
    public void run(String... arg0) throws Exception {
        try {
            initSchedule();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @description  这里获取任务信息数据
     * @return  void
     * @date  20/07/14 9:29
     * @author  wanghb
     * @edit
     */
    public void initSchedule() {
        //这里可以对任务信息初始化
    }



    
}
