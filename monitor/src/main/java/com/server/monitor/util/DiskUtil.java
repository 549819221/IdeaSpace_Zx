package com.server.monitor.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.server.monitor.entity.ApplicationMonitor;
import com.server.monitor.entity.DiskMonitor;
import com.server.monitor.entity.Server;
import com.server.monitor.entity.ServerMonitor;
import com.server.monitor.entity.parent.Monitor;
import com.server.monitor.service.impl.DynamicQuartzServiceImpl;
import com.server.monitor.util.entity.DiskInfo;
import javafx.util.Pair;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class DiskUtil {
    private static Logger logger = Logger.getLogger( DiskUtil.class);


    /**
     * 获取磁盘信息
     *
     * @return
     */
    public static Object executeCmd(Monitor monitor)throws Exception {
        Server server = monitor.getServerDetail();
        String host = server.getIp();
        String loginName = server.getUsername();
        String passWord = server.getPassword();
        String systemType = server.getSystem();
        Integer sshPort = server.getSshPort();

        /*host = "192.168.10.72";
        loginName = "root";
        passWord = "root123";
        systemType = ParamEnum.systemType.Windows.getCode();
        */

        Connection conn = new Connection(host, sshPort);
        Session ssh = null;
        Map<String, Object> returnMap = new HashMap<>();
        try {
            conn.connect();
            boolean flag = conn.authenticateWithPassword(loginName, passWord);
            if (PowerUtil.isNull( flag ) || !flag) {
                logger.error("登陆服务器的账号或密码错误.");
                throw new Exception("登陆服务器的账号或密码错误.");
            } else {
                //logger.info("连接成功");
                ssh = conn.openSession();

                if(ParamEnum.MonitorEnum.MONITOR_SERVER.getName().equals( monitor.getType() )){
                    ServerMonitor serverMonitor = (ServerMonitor) monitor;

                }else if(ParamEnum.MonitorEnum.MONITOR_APPLICATION.getName().equals( monitor.getType() )){
                    ApplicationMonitor applicationMonitor =  (ApplicationMonitor) monitor;
                }else if(ParamEnum.MonitorEnum.MONITOR_DISK.getName().equals( monitor.getType() )){
                    DiskMonitor diskMonitor =  (DiskMonitor) monitor;
                    String partition = diskMonitor.getPartition();
                    if( ParamEnum.systemType.Windows.getCode().equals( systemType ) ){
                        String cmd = new StringBuilder("wmic LogicalDisk where \"Caption='").append( partition ).append( ":" ).append( "'\" get FreeSpace,Size /value" ).toString();
                        return getDiskByWindows( ssh ,cmd);
                    }else{
                        String cmd = "df -hl";
                        return getDiskByLinux( ssh ,cmd);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ssh != null) {
                    ssh.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return returnMap;
    }

    /**
     * @description  获取windwos磁盘数据
     * @param  ssh  Session
     * @return  返回结果
     * @date  20/07/27 10:14
     * @author  wanghb
     * @edit
     */
    public static  BigDecimal memoryUnit = new BigDecimal("1024");
    public static  BigDecimal hundred = new BigDecimal( 100 );
    public static Object getDiskByWindows(Session ssh ,String cmd) throws IOException{
        BigDecimal totalSpace = BigDecimal.ZERO;
        BigDecimal freeSpace = BigDecimal.ZERO;
        BigDecimal usedSpace = BigDecimal.ZERO;

        ssh.execCommand(cmd);
        InputStream stdout = new StreamGobbler(ssh.getStdout());
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(stdout));

            String len = null;
            while((len = br.readLine()) != null){
                logger.info("结果行======>"+len);
                if(len.startsWith("FreeSpace")){
                    String[] str = len.split("=");
                    freeSpace = PowerUtil.getBigDecimal( str[1] ).divide(memoryUnit).divide(memoryUnit).divide(memoryUnit,2,BigDecimal.ROUND_HALF_DOWN);
                }
                if(len.startsWith("Size")){
                    String[] str = len.split("=");
                    totalSpace = PowerUtil.getBigDecimal( str[1] ).divide(memoryUnit).divide(memoryUnit).divide(memoryUnit,2,BigDecimal.ROUND_HALF_DOWN);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(br != null){
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        usedSpace = totalSpace.subtract(freeSpace);
        //logger.info("总空间大小 : " + totalSpace + "G");
        //logger.info("剩余空间大小 : " + freeSpace + "G");
        //logger.info("已用空间大小 : " + usedSpace + "G");
        DiskInfo diskInfo = new DiskInfo(totalSpace,usedSpace,freeSpace);
        return  diskInfo;
    }

    /**
     * @description  执行linux命令
     * @param  ssh  ssh链接
     * @return  返回结果
     * @date  20/07/27 10:14
     * @author  wanghb
     * @edit
     */
    public static Object getDiskByLinux(Session ssh,String cmd){
        Map<String, Object> map = new HashMap();
        BigDecimal totalSpace = BigDecimal.ZERO;
        BigDecimal freeSpace = BigDecimal.ZERO;
        BigDecimal usedSpace = BigDecimal.ZERO;
        BufferedReader br = null;
        try {
            ssh.execCommand(cmd);
            InputStream stdout = new StreamGobbler(ssh.getStdout());
            br = new BufferedReader(new InputStreamReader(stdout));
            String len = null;
            String[] strArray = null;
            while ((len = br.readLine()) != null) {
                //logger.info("结果行======>"+len);
                int m = 0;
                strArray = len.split(" ");
                for (String tmp : strArray) {
                    if (tmp.trim().length() == 0) {
                        continue;
                    }
                    ++m;
                    if (tmp.indexOf("G") != -1) {
                        if (m == 3) {
                            if (!tmp.equals("") && !tmp.equals("0")) {
                                usedSpace = usedSpace.add(PowerUtil.getBigDecimal( tmp.substring(0,tmp.length() - 1) ).multiply(memoryUnit)) ;
                            }
                        }
                        if (m == 4) {
                            if (!tmp.equals("none") && !tmp.equals("0")) {
                                freeSpace = freeSpace.add(PowerUtil.getBigDecimal( tmp.substring(0,tmp.length() - 1) ).multiply(memoryUnit)) ;
                            }

                        }
                    }
                    if (tmp.indexOf("M") != -1) {
                        if (m == 3) {
                            if (!tmp.equals("") && !tmp.equals("0")) {
                                usedSpace = usedSpace.add(PowerUtil.getBigDecimal( tmp.substring(0,tmp.length() - 1) )) ;
                            }
                        }
                        if (m == 4) {
                            if (!tmp.equals("none") && !tmp.equals("0")) {
                                freeSpace = freeSpace.add(PowerUtil.getBigDecimal( tmp.substring(0,tmp.length() - 1) )) ;

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(br != null){
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        usedSpace = usedSpace.divide(memoryUnit,2,BigDecimal.ROUND_HALF_DOWN);
        freeSpace = freeSpace.divide(memoryUnit,2,BigDecimal.ROUND_HALF_DOWN);
        totalSpace = usedSpace.add( freeSpace );
        //logger.info("已用空间：" + totalSpace +"G");
        //logger.info("已用空间：" + usedSpace +"G");
        //logger.info("可用空间：" + freeSpace +"G");
        DiskInfo diskInfo = new DiskInfo(totalSpace,usedSpace,freeSpace);
        return  diskInfo;
    }
}
