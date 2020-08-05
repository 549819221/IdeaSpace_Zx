package com.server.monitor.service.impl;

import com.server.monitor.entity.parent.Monitor;
import com.server.monitor.service.BasisService;
import com.server.monitor.util.*;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.management.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * (Basis)表服务实现类
 *
 * @author wanghb
 * @since 2020-07-10 14:43:46
 */
@Service("basisService")
@PropertySource({"classpath:application.properties"})
public class BasisServiceImpl implements BasisService {


    private static Logger logger = Logger.getLogger( DynamicQuartzServiceImpl.class );

    @Value("${basisUrL}")
    public String basisUrL;
    //public final static String basisUrL = "http://zyxlgx.top:9090/monitor-center/";

    @Value("${server.port}")
    public String serverPort;

    /**
     * 当前机器的nodeId
     */
    public static String nodeId = "";
    /**
     * 获取本节点的监控任务
     */
    public final static String allNodeUrL = "node/remote/monitor/all";
    /**
     * 注册监控节点
     */
    public final static String registerUrL = "node/remote/register";
    /**
     * 监控保存
     */
    public final static String logSaveUrL = "node/remote/log/note";


    /**
     * @description  节点注册
     * @return 返回结果
     * @date 20/07/10 15:00
     * @author wanghb
     * @edit
     */
    @Override
    public Map<String, Object> register() {
        Map<String, Object> result = new HashMap<>();
        try {
            StringBuffer url = new StringBuffer( basisUrL ).append( registerUrL );
            Map<String, Object> params = new HashMap<>();
            String name = "服务监控";
            String ip = HttpUtil.getHostIp();
            String port = serverPort;
            String objId = NodeRegisterTool.getObjId( ip + "|" + port );

            params.put( "objId", objId );
            params.put( "name", name );
            params.put( "ip", ip );
            params.put( "port", port );
            /**
             * 3001  为已注册
             */
            result = HttpUtil.post( url.toString(), params );
            //String errcode = PowerUtil.getString( result.get( "errcode" ) );
            nodeId = objId;
        } catch (Exception e) {
            logger.error( new StringBuilder( "节点注册异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
        }
        return result;
    }

    /**
     * @param objId
     * @return 返回结果
     * @description 获取所有的服务节点
     * @date 20/07/10 15:30
     * @author wanghb
     * @edit
     */
    @Override
    public List<Map<String, Object>> getAllNode(String objId) {
        StringBuffer url = new StringBuffer( basisUrL ).append( allNodeUrL );
        Map<String, Object> params = new HashMap<>();
        params.put( "objId", objId );
        Map<String, Object> result = null;
        try {
            result = HttpUtil.get( url.toString(), params );
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(PowerUtil.isNull( result )){
            return new ArrayList<>();
        }
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get( "data" );
        return list;
    }


}