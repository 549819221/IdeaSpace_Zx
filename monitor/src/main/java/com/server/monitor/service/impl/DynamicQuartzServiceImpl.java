package com.server.monitor.service.impl;

import com.acrabsoft.utils.net.PingUtils;
import com.server.monitor.entity.*;
import com.server.monitor.entity.parent.Monitor;
import com.server.monitor.service.BasisService;
import com.server.monitor.service.DynamicQuartzService;
import com.server.monitor.util.*;
import com.server.monitor.util.entity.DiskInfo;
import io.swagger.annotations.ApiOperation;
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
import java.util.*;


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
     * @description 测试定时任务的接口
     * @return 结果
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
     * @description 处理任务
     * @param monitorTemp 任务内容
     * @return  处理结果
     * @date 20/07/15 9:43
     * @author wanghb
     * @edit
     */
    @Override
    public void dealWithJob(Map<String, Object> monitorTemp) {
        List<MonitorLog> monitorLogs = new ArrayList<>();
        Monitor monitor = MapUtil.toBean( monitorTemp, Monitor.class );
        if (ParamEnum.MonitorEnum.MONITOR_SERVER.getName().equals( monitor.getType() )) {
            ServerMonitor serverMonitor = MapUtil.toBean( monitorTemp, ServerMonitor.class );
            performMonitor( serverMonitor, monitorLogs );
        } else if (ParamEnum.MonitorEnum.MONITOR_DISK.getName().equals( monitor.getType() )) {
            DiskMonitor diskMonitor = MapUtil.toBean( monitorTemp, DiskMonitor.class );
            performMonitor( diskMonitor, monitorLogs );
        } else if (ParamEnum.MonitorEnum.MONITOR_APPLICATION.getName().equals( monitor.getType() )) {
            ApplicationMonitor applicationMonitor = MapUtil.toBean( monitorTemp, ApplicationMonitor.class );
            performMonitor( applicationMonitor, monitorLogs );
        }
        for (MonitorLog monitorLog : monitorLogs) {
            try {
                HttpUtil.post( new StringBuffer( basisUrL ).append( BasisServiceImpl.logSaveUrL ).toString(), monitorLog );
            } catch (Exception e) {
                logger.error( new StringBuilder( "日志发送异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
                e.printStackTrace();
            }
        }
    }

    /**
     * @description 执行服务器监控
     * @param serverMonitor 服务器监控信息
     * @param monitorLogs    监控日志
     * @date 20/07/24 17:04
     * @author wanghb
     * @edit
     */
    private void performMonitor(ServerMonitor serverMonitor, List<MonitorLog> monitorLogs)  {
        String ip = serverMonitor.getServerDetail().getIp();
        Integer telnetPort = serverMonitor.getTelnetPort();
        if (ParamEnum.yesOrNo.yes.getCode().equals( serverMonitor.getPing() )) {
            MonitorLog monitorLog = new MonitorLog();
            monitorLog.setNodeId( BasisServiceImpl.nodeId  );
            monitorLog.setMonitorId( serverMonitor.getObjId() );
            StringBuilder mas = new StringBuilder();
            StringBuilder result = new StringBuilder();
            String state = null;
            try {
                boolean pingState = PingUtils.ping( ip );
                if (!pingState) {
                    state = ParamEnum.yesOrNo.no.getCode().toString();
                    mas.append( "ping异常,异常ip:" ).append( ip ).append( "; " );
                }
            } catch (Exception e) {
                mas.append( "监控异常。" );
                result.append( "监控异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) );
                logger.error( new StringBuilder("监控异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e )));
                state = ParamEnum.yesOrNo.no.getCode().toString();
            }
            monitorLog.setStatus( state == null ? ParamEnum.yesOrNo.yes.getCode().toString() : state );
            monitorLog.setMsg( mas.toString() );
            monitorLog.setResult( result.toString() );
            monitorLogs.add(monitorLog);
        }
        if (ParamEnum.yesOrNo.yes.getCode().equals( serverMonitor.getTelnet() )) {
            MonitorLog monitorLog = getTelnetPingLog( serverMonitor.getObjId(), ip, telnetPort );
            monitorLogs.add(monitorLog);
        }
    }


    /**
     * @description 执行磁盘监控
     * @param diskMonitor 磁盘监控信息
     * @param monitorLogs  监控日志
     * @date 20/07/24 17:04
     * @author wanghb
     * @edit
     */
    private void performMonitor(DiskMonitor diskMonitor, List<MonitorLog> monitorLogs)  {
        MonitorLog monitorLog = new MonitorLog();
        monitorLog.setNodeId( BasisServiceImpl.nodeId  );
        monitorLog.setMonitorId( diskMonitor.getObjId() );
        StringBuilder mas = new StringBuilder();
        StringBuilder result = new StringBuilder();
        String state = null;
        try {
            DiskInfo diskInfo = (DiskInfo) DiskUtil.executeCmd( diskMonitor );
            BigDecimal operateValue = PowerUtil.getBigDecimal( diskMonitor.getOperateValue() );
            BigDecimal totalSpace = diskInfo.getTotalSpace();
            String partition = diskMonitor.getPartition();
            String systemType = diskMonitor.getServerDetail().getSystem();
            if ((BigDecimal.ZERO.compareTo( totalSpace ) == 0)) {
                mas.append( "此" ).append( systemType ).append( "服务器没有查到 " ).append( partition ).append( " 这个磁盘目录,请核对。" );
            }else{
                BigDecimal usedSpace = diskInfo.getUsedSpace();
                //剩余空间百分比
                if (ParamEnum.operate.percentage.getCode().equals( diskMonitor.getOperate() )) {
                    if (operateValue.compareTo( diskInfo.getPercentage() ) > 0) {
                        BigDecimal percentage = diskInfo.getPercentage();
                        state = ParamEnum.yesOrNo.no.getCode().toString();
                        mas.append( "磁盘容量低于阈值,设定的阈值为:" ).append( operateValue ).append( "%,目前剩余:" ).append( percentage ).append( "%,空间总量:" ).append( totalSpace ).append( "G,已使用:" ).append( usedSpace ).append( "G" );
                    }
                //剩余空间大小
                } else {
                    if (operateValue.compareTo( diskInfo.getFreeSpace() ) > 0) {
                        state = ParamEnum.yesOrNo.no.getCode().toString();
                        BigDecimal freeSpace = diskInfo.getFreeSpace();
                        mas.append( "磁盘容量低于阈值,设定的阈值为:" ).append( operateValue ).append( "G,目前剩余:" ).append( freeSpace ).append( "G,空间总量:" ).append( totalSpace ).append( "G,已使用:" ).append( usedSpace ).append( "G" );
                    }
                }
            }
            state = state == null ? ParamEnum.yesOrNo.yes.getCode().toString() : state;
            monitorLog.setStatus( state );
            monitorLog.setMsg( mas.toString() );
        } catch (Exception e) {
            e.printStackTrace();
            mas.append( "监控异常。");
            result.append( "监控异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) );
            logger.error( new StringBuilder("监控异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e )));
            state = ParamEnum.yesOrNo.no.getCode().toString();
        }
        monitorLog.setStatus( state == null ? ParamEnum.yesOrNo.yes.getCode().toString() : state );
        monitorLog.setMsg( mas.toString() );
        monitorLog.setResult( result.toString() );
        monitorLogs.add(monitorLog);
    }

    /**
     * @description 执行次应用监控
     * @param applicationMonitor 应用监控信息
     * @param monitorLogs 监控日志
     * @date 20/07/24 17:04
     * @author wanghb
     * @edit
     */
    private void performMonitor(ApplicationMonitor applicationMonitor, List<MonitorLog> monitorLogs) {
        Integer http = applicationMonitor.getHttp();
        Integer telnet = applicationMonitor.getTelnet();
        if (ParamEnum.yesOrNo.yes.getCode().equals( telnet )) {
            Integer telnetPort = applicationMonitor.getAppPort();
            String serverIp = applicationMonitor.getServerDetail().getIp();
            MonitorLog monitorLog = getTelnetPingLog( applicationMonitor.getObjId(), serverIp, telnetPort );
            monitorLogs.add(monitorLog);
        }
        if (ParamEnum.yesOrNo.yes.getCode().equals( http )) {
            MonitorLog monitorLog = new MonitorLog();
            monitorLog.setNodeId( BasisServiceImpl.nodeId  );
            monitorLog.setMonitorId( applicationMonitor.getObjId() );
            StringBuilder mas = new StringBuilder();
            StringBuilder result = new StringBuilder();
            String state = null;

            String url = applicationMonitor.getUrl();
            String httpResult = PowerUtil.getString( applicationMonitor.getHttpResult() ) ;
            Integer httpTimeout = applicationMonitor.getHttpTimeout();
            String httpStrTemp = null;

            try {
                httpStrTemp = PowerUtil.getString( HttpUtil.get( url, null, httpTimeout ));
                if (!httpStrTemp.contains( httpResult )) {
                    state = ParamEnum.yesOrNo.no.getCode().toString();
                    mas.append( "http接口返回结果不一致。" );
                    result.append( new StringBuilder( "http接口返回结果不一致,参考值:" ).append( httpResult ).append( ",实际返回值:" ).append( httpStrTemp ).toString()  );
                }
            } catch (ConnectTimeoutException e) {
                state = ParamEnum.yesOrNo.no.getCode().toString();
                mas.append( "http请求超时异常。");
                result.append( new StringBuilder( "http请求超时异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString());
            } catch (Exception e) {
                logger.error( new StringBuilder("http访问异常,异常信息:").append( ExceptionUtil.getOutputStream( e ) ) );
                state = ParamEnum.yesOrNo.no.getCode().toString();
                result.append( new StringBuilder( "http访问异常,访问地址:").append( url ).append(";异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString()  );
            }
            state = state == null ? ParamEnum.yesOrNo.yes.getCode().toString() : state;
            monitorLog.setStatus( state );
            monitorLog.setMsg( mas.toString() );
            monitorLog.setResult( result.toString() );
            monitorLogs.add(monitorLog);
        }
    }

    /**
     * @description  进行TelnetPing监控获取MonitorLog日志
     * @param  objId  节点id
     * @param  ip  监控ip
     * @param  telnetPort  监控端口
     * @return  返回结果
     * @date  20/08/10 9:52
     * @author  wanghb
     * @edit
     */
    public MonitorLog getTelnetPingLog(String objId, String ip, int telnetPort) {
        MonitorLog monitorLog = new MonitorLog();
        monitorLog.setNodeId( BasisServiceImpl.nodeId  );
        monitorLog.setMonitorId( objId );
        StringBuilder mas = new StringBuilder();
        StringBuilder result = new StringBuilder();
        String state = null;
        try {
            boolean telnetPortState = PingUtils.tcpPing( ip, telnetPort );
            if (!telnetPortState) {
                mas.append( "telnetPing异常,异常ip:" ).append( ip ).append( "异常端口:" ).append( telnetPort ).append( "; " );
                state = ParamEnum.yesOrNo.no.getCode().toString();
            }
        } catch (Exception e) {
            mas.append( "监控异常。"  );
            result.append( "监控异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) );
            logger.error( new StringBuilder("监控异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e )));
            state = ParamEnum.yesOrNo.no.getCode().toString();
        }
        monitorLog.setStatus( state == null ? ParamEnum.yesOrNo.yes.getCode().toString() : state );
        monitorLog.setMsg( mas.toString() );
        monitorLog.setResult( result.toString() );
        return monitorLog;
    }


    /**
     * @description 判断任务是否存在
     * @param objId 任务id
     * @return  是否存在该定时任务
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
