package com.server.monitor.entity;


import com.server.monitor.util.PowerUtil;

public class TaskDo {
    private String jobName = "监控任务";
    private Integer executeInterval;
    private String beanClass = "com.server.monitor.dynamicQuartz.MonitorJob";
    
    private String objId;

    public TaskDo(Integer executeInterval,String objId){
        this.executeInterval = executeInterval;
        this.objId = objId;
    }
    public String getJobName() {
        return jobName;
    }


    public Integer getExecuteInterval() {
        return executeInterval;
    }

    public void setExecuteInterval(Integer executeInterval) {
        this.executeInterval = executeInterval;
    }


    public String getBeanClass() {
        return beanClass;
    }

    public String getObjId() {
        return PowerUtil.getString( objId );
    }

    public void setJobGroup(String jobGroup) {
        this.objId = jobGroup;
    }
}
