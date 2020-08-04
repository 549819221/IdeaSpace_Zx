package com.server.monitor.service.impl;

import ch.ethz.ssh2.Session;
import com.acrabsoft.utils.net.PingUtils;
import com.alibaba.fastjson.JSON;
import com.server.monitor.entity.*;
import com.server.monitor.entity.parent.Monitor;
import com.server.monitor.service.BasisService;
import com.server.monitor.service.DynamicQuartzService;
import com.server.monitor.util.*;
import com.server.monitor.util.entity.DiskInfo;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("dynamicQuartz")
@PropertySource({"classpath:application.properties"})
public class DynamicQuartzServiceImpl implements DynamicQuartzService {
    private static Logger logger = Logger.getLogger( DynamicQuartzServiceImpl.class );
    public static Map<String, Map<String, Object>> monitorCache = new HashMap<>();

    @Value("${basisUrL}")
    public String basisUrL;

    @Autowired
    private Scheduler scheduler;
    @Resource
    private BasisService basisService;

    /**
     * @return 结果
     * @description 测试定时任务的接口
     * @date 20/07/14 10:12
     * @author wanghb
     * @edit
     */
    @Override
    public Map<String, Object> quartz() {
        logger.info( "quartz方法开始执行================>" + DateUtils.formatDate( new Date(), "yyyy-MM-dd HH:mm:ss" ) );
        if (PowerUtil.isNull( BasisServiceImpl.nodeId )) {
            logger.error( "当前机器的monitorObjId为空" );
            return null;
        }
        List<Map<String, Object>> monitorsList = basisService.getAllNode( BasisServiceImpl.nodeId );
        //如果是首次执行 也就是项目启动时会执行
        boolean isFirst = ScheduledTasks.referenceTime == null;
        Long referenceTimeTemp = ScheduledTasks.referenceTime;
        for (int i = 0; i < monitorsList.size(); i++) {
            Map<String, Object> monitorTemp = monitorsList.get( i );
            Monitor monitor = MapUtil.toBean( monitorTemp, Monitor.class );
            Long createTime = monitor.getCreateTime().getTime();
            Long lastModifiedTime = monitor.getLastModifiedTime().getTime();
            String state = monitor.getRelate();
            boolean isDel = ParamEnum.Status.del.getCode().equals( state );
            String objId = monitor.getObjId();
            //间隔  秒
            Integer executeInterval = PowerUtil.getIntValue( monitor.getExecuteInterval() );
            TaskDo taskDo = new TaskDo( executeInterval, objId );
            /**********************处理的是首次执行开始**********************/
            if (isFirst && !isDel) {
                addJob( taskDo );
                monitorCache.put( objId, monitorTemp );
            }
            /**********************处理的是首次执行结束**********************/
            /*********************处理的是非首次执行开始*********************/
            if (!isFirst) {
                //节点删除的判断
                if (isDel) {
                    deleteJob( taskDo );
                } else {
                    monitorCache.put( objId, monitorTemp );
                    //节点创建的判断
                    if (createTime > referenceTimeTemp) {
                        addJob( taskDo );
                        //节点修改的判断
                    } else if (lastModifiedTime > referenceTimeTemp) {
                        updateJob( taskDo );
                    }
                }
            }
            /*********************处理的是非首次执行结束*********************/
            //取最晚的创建时间进行缓存
            if (ScheduledTasks.referenceTime == null || createTime > ScheduledTasks.referenceTime) {
                ScheduledTasks.referenceTime = createTime;
            }
            if (lastModifiedTime > ScheduledTasks.referenceTime) {
                ScheduledTasks.referenceTime = lastModifiedTime;
            }
        }

        return null;
    }

    /**
     * @param monitorTemp 任务内容
     * @return
     * @description 处理任务
     * @date 20/07/15 9:43
     * @author wanghb
     * @edit
     */
    @Override
    public Object dealWithJob(Map<String, Object> monitorTemp) {
        MonitorLog monitorLog = new MonitorLog();
        try {
            Monitor monitor = MapUtil.toBean( monitorTemp, Monitor.class );
            String objId = monitor.getObjId();
            monitorLog.setNodeId( BasisServiceImpl.nodeId  );
            monitorLog.setMonitorId( objId );
            if (ParamEnum.MonitorEnum.MONITOR_SERVER.getName().equals( monitor.getType() )) {
                ServerMonitor serverMonitor = MapUtil.toBean( monitorTemp, ServerMonitor.class );
                performMonitor( serverMonitor, monitorLog );
            } else if (ParamEnum.MonitorEnum.MONITOR_DISK.getName().equals( monitor.getType() )) {
                DiskMonitor diskMonitor = MapUtil.toBean( monitorTemp, DiskMonitor.class );
                performMonitor( diskMonitor, monitorLog );
            } else if (ParamEnum.MonitorEnum.MONITOR_APPLICATION.getName().equals( monitor.getType() )) {
                ApplicationMonitor applicationMonitor = MapUtil.toBean( monitorTemp, ApplicationMonitor.class );
                performMonitor( applicationMonitor, monitorLog );
                return null;
            }
        } catch (Exception e) {
            logger.error( new StringBuilder( "监控异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            monitorLog.setStatus( ParamEnum.yesOrNo.no.getCode().toString() );
            monitorLog.setResult( new StringBuilder( "监控接口异常。" ).toString() );
        } finally {
            try {
                return HttpUtil.post( new StringBuffer( basisUrL ).append( BasisServiceImpl.logSaveUrL ).toString(), monitorLog );
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * @param serverMonitor 服务器监控信息
     * @param monitorLog    监控日志
     * @description 执行服务器监控
     * @date 20/07/24 17:04
     * @author wanghb
     * @edit
     */
    private void performMonitor(ServerMonitor serverMonitor, MonitorLog monitorLog) throws Exception {
        StringBuilder result = new StringBuilder();
        String state = null;
        String ip = serverMonitor.getServerDetail().getIp();
        Integer telnetPort = serverMonitor.getTelnetPort();
        if (ParamEnum.yesOrNo.yes.getCode().equals( serverMonitor.getPing() )) {
            try {
                boolean pingState = PingUtils.ping( ip );
                if (!pingState) {
                    state = ParamEnum.yesOrNo.no.getCode().toString();
                    result.append( "ping异常,异常ip:" ).append( ip );
                }
            } catch (IOException e) {
                result.append( "监控异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) );
                state = ParamEnum.yesOrNo.no.getCode().toString();
            }
        }
        if (ParamEnum.yesOrNo.yes.getCode().equals( serverMonitor.getTelnet() )) {
            boolean telnetPortState = PingUtils.tcpPing( ip, telnetPort );
            if (!telnetPortState) {
                result.append( "  telnetPing异常,异常ip:" ).append( ip ).append( "异常端口:" ).append( telnetPort );
                state = ParamEnum.yesOrNo.no.getCode().toString();
            }
        }
        state = state == null ? ParamEnum.yesOrNo.yes.getCode().toString() : state;
        monitorLog.setStatus( state );
        monitorLog.setResult( result.toString() );
    }

    /**
     * @param diskMonitor 服务器监控信息
     * @param monitorLog  监控日志
     * @description 执行服务器监控
     * @date 20/07/24 17:04
     * @author wanghb
     * @edit
     */
    private void performMonitor(DiskMonitor diskMonitor, MonitorLog monitorLog) throws Exception {
        DiskInfo diskInfo = (DiskInfo) DiskUtil.executeCmd( diskMonitor );
        BigDecimal operateValue = PowerUtil.getBigDecimal( diskMonitor.getOperateValue() );
        String state = null;
        StringBuilder result = new StringBuilder();
        //剩余空间百分比
        if (ParamEnum.operate.percentage.getCode().equals( diskMonitor.getOperate() )) {
            if (operateValue.compareTo( diskInfo.getPercentage() ) > 0) {
                state = ParamEnum.yesOrNo.no.getCode().toString();
                result.append( "磁盘容量超过阈值 设定的阈值剩余值为:" ).append( operateValue ).append( "%,目前剩余:" ).append( diskInfo.getPercentage() ).append( "%" );
            }
            //剩余空间大小
        } else {
            if (operateValue.compareTo( diskInfo.getFreeSpace() ) > 0) {
                state = ParamEnum.yesOrNo.no.getCode().toString();
                result.append( "磁盘容量超过阈值 设定的阈值剩余值为:" ).append( operateValue ).append( ",目前剩余:" ).append( diskInfo.getFreeSpace() );
            }
        }
        state = state == null ? ParamEnum.yesOrNo.yes.getCode().toString() : state;
        monitorLog.setStatus( state );
        monitorLog.setResult( result.toString() );
    }

    /**
     * @param applicationMonitor 服务器监控信息
     * @param monitorLog         监控日志
     * @description 执行次应用监控
     * @date 20/07/24 17:04
     * @author wanghb
     * @edit
     */
    private void performMonitor(ApplicationMonitor applicationMonitor, MonitorLog monitorLog) throws Exception {
        StringBuilder result = new StringBuilder();
        String state = null;
        Integer http = applicationMonitor.getHttp();
        if (ParamEnum.yesOrNo.yes.getCode().equals( http )) {
            String url = applicationMonitor.getUrl();
            String httpResult = PowerUtil.getString( applicationMonitor.getHttpResult() ) ;
            Integer httpTimeout = applicationMonitor.getHttpTimeout();
            String httpStrTemp = null;
            try {
                httpStrTemp = PowerUtil.getString( JSON.toJSONString( HttpUtil.get( url, null, httpTimeout ) ) );
                if (!httpStrTemp.contains( httpResult )) {
                    state = ParamEnum.yesOrNo.no.getCode().toString();
                    result.append( "http接口返回结果不一致。" );
                }
            } catch (ConnectTimeoutException e) {
                state = ParamEnum.yesOrNo.no.getCode().toString();
                result.append( "http请求超时异常。" );
            } catch (Exception e) {
                logger.error( new StringBuilder("http访问异常异常,异常信息:").append( ExceptionUtil.getOutputStream( e ) ) );
                state = ParamEnum.yesOrNo.no.getCode().toString();
                result.append( "http访问异常异常。" );
            }
        }
        state = state == null ? ParamEnum.yesOrNo.yes.getCode().toString() : state;
        monitorLog.setStatus( state );
        monitorLog.setResult( result.toString() );

    }


    /**
     * @param objId 任务id
     * @return
     * @description 判断任务是否存在
     * @date 20/07/14 15:01
     * @author wanghb
     * @edit
     */
    @Override
    public boolean isExist(String objId) throws SchedulerException {
        List<String> objIds = scheduler.getTriggerGroupNames();
        for (int i = 0; i < objIds.size(); i++) {
            if (PowerUtil.getString( objId ).equals( objIds.get( i ) )) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param task 任务对象
     * @return
     * @description 添加任务
     * @date 20/07/14 9:30
     * @author wanghb
     * @edit
     */
    @Override
    public void addJob(TaskDo task) {
        try {
            if (!isExist( task.getObjId() )) {
                logger.info( "执行了新增任务" );
                // 创建jobDetail实例，绑定Job实现类
                // 指明job的名称，所在组的名称，以及绑定job类
                Class<? extends Job> jobClass = (Class<? extends Job>) (Class.forName( task.getBeanClass() ).newInstance().getClass());
                // 任务名称和组构成任务key
                JobDetail jobDetail = JobBuilder.newJob( jobClass ).withIdentity( task.getJobName(), task.getObjId() ).build();
                // 定义调度触发规则
                // 使用cornTrigger规则
                // 触发器key
                Trigger trigger = TriggerBuilder.newTrigger().withIdentity( task.getJobName(), task.getObjId() )
                        .startAt( DateBuilder.futureDate( 1, DateBuilder.IntervalUnit.SECOND ) )
                        .withSchedule( SimpleScheduleBuilder.repeatSecondlyForever( task.getExecuteInterval() ) ).startNow().build();
                // 把作业和触发器注册到任务调度中
                scheduler.scheduleJob( jobDetail, trigger );
                // 启动
                if (!scheduler.isShutdown()) {
                    scheduler.start();
                }
            } else {
                updateJob( task );
            }
        } catch (Exception e) {
            logger.error( new StringBuilder( "添加任务异常 :" ).append( ExceptionUtil.getOutputStream( e ) ) );
            e.printStackTrace();
        }
    }


    /**
     * @param task 任务对象
     * @return void
     * @description 暂停一个job
     * @date 20/07/14 9:31
     * @author wanghb
     * @edit
     */
    @Override
    public void pauseJob(TaskDo task) {
        logger.info( "执行了暂停任务" );
        JobKey jobKey = JobKey.jobKey( task.getJobName(), task.getObjId() );
        try {
            scheduler.pauseJob( jobKey );
        } catch (SchedulerException e) {
            logger.error( new StringBuilder( "暂停一个job异常 :" ).append( ExceptionUtil.getOutputStream( e ) ) );
        }
    }


    /**
     * @param task 任务对象
     * @return void
     * @description 恢复一个job
     * @date 20/07/14 9:31
     * @author wanghb
     * @edit
     */
    @Override
    public void resumeJob(TaskDo task) {
        logger.info( "执行了恢复任务" );
        JobKey jobKey = JobKey.jobKey( task.getJobName(), task.getObjId() );
        try {
            scheduler.resumeJob( jobKey );
        } catch (SchedulerException e) {
            logger.error( new StringBuilder( "恢复任务异常 :" ).append( ExceptionUtil.getOutputStream( e ) ) );

        }
    }


    /**
     * @param task 任务对象
     * @return void
     * @description 删除一个job
     * @date 20/07/14 9:31
     * @author wanghb
     * @edit
     */
    @Override
    public void deleteJob(TaskDo task) {
        logger.info( "执行了删除任务" );
        JobKey jobKey = JobKey.jobKey( task.getJobName(), task.getObjId() );
        try {
            scheduler.deleteJob( jobKey );
        } catch (SchedulerException e) {
            logger.error( new StringBuilder( "删除一个job异常 :" ).append( ExceptionUtil.getOutputStream( e ) ) );

        }
    }


    /**
     * @param task 任务对象
     * @return void
     * @description 立即触发job
     * @date 20/07/14 9:31
     * @author wanghb
     * @edit
     */
    @Override
    public void runJobNow(TaskDo task) {
        logger.info( "执行了立即触发任务" );
        JobKey jobKey = JobKey.jobKey( task.getJobName(), task.getObjId() );
        try {
            scheduler.triggerJob( jobKey );
        } catch (SchedulerException e) {
            logger.error( new StringBuilder( "立即触发job异常 :" ).append( ExceptionUtil.getOutputStream( e ) ) );
        }
    }


    /**
     * @param task 任务对象
     * @return void
     * @description 更新job
     * @date 20/07/14 9:31
     * @author wanghb
     * @edit
     */
    @Override
    public void updateJob(TaskDo task) {
        try {
            if (isExist( task.getObjId() )) {
                logger.info( "执行了更新任务" );
                TriggerKey triggerKey = TriggerKey.triggerKey( task.getJobName(), task.getObjId() );
                SimpleTrigger trigger = (SimpleTrigger) scheduler.getTrigger( triggerKey );
                SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.repeatSecondlyForever( task.getExecuteInterval() );
                trigger = trigger.getTriggerBuilder().withIdentity( triggerKey ).withSchedule( simpleScheduleBuilder ).build();
                scheduler.rescheduleJob( triggerKey, trigger );
            } else {
                addJob( task );
            }
        } catch (SchedulerException e) {
            logger.error( new StringBuilder( "更新job异常 :" ).append( ExceptionUtil.getOutputStream( e ) ) );
        }
    }
}
