package com.server.monitor.util;


import com.server.monitor.entity.ApplicationMonitor;
import com.server.monitor.entity.DiskMonitor;
import com.server.monitor.entity.ServerMonitor;

/**
 * @Title: 对应数据字典的枚举类
 * @author wanghb
 * @date 2019-07-18
 */
public interface ParamEnum {
    /**
     * 字段类型
     * @author wanghb
     */
    enum Status {
        del( "0","删除"),
        exist( "1","正常"),
        ;
        private String code;
        private String name;
        public String getCode() {
            return code;
        }
        public String getName() {
            return name;
        }
        Status(String code, String name) {
            this.code = code;
            this.name = name;
        }
        public static String getNameByCode(String code) {
            for (systemType item : systemType.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }
    }
    /**
     * 是或否
     * @author wanghb
     */
    enum operate {
        percentage( 1,"剩余空间百分比"),
        specific( 2,"剩余空间大小"),
        ;
        private Integer code;
        private String name;
        public Integer getCode() {
            return code;
        }
        public String getName() {
            return name;
        }
        operate(Integer code, String name) {
            this.code = code;
            this.name = name;
        }
        public static String getNameByCode(String code) {
            for (systemType item : systemType.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }
    }

    /**
     * 是或否
     * @author wanghb
     */
    enum yesOrNo {
        no( 0,"否"),
        yes( 1,"是"),
        ;
        private Integer code;
        private String name;
        public Integer getCode() {
            return code;
        }
        public String getName() {
            return name;
        }
        yesOrNo(Integer code, String name) {
            this.code = code;
            this.name = name;
        }
        public static String getNameByCode(String code) {
            for (systemType item : systemType.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }
    }

    /**
     * 服务器类型
     * @author wanghb
     */
    enum systemType {
        Ubuntu( "Ubuntu","阿里云服务器"),
        Windows( "Windows","Windows服务器"),
        ;
        private String code;
        private String name;
        public String getCode() {
            return code;
        }
        public String getName() {
            return name;
        }
        systemType(String code, String name) {
            this.code = code;
            this.name = name;
        }
        public static String getNameByCode(String code) {
            for (systemType item : systemType.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }
    }

    /**
     * 字段类型  Monitor 的子类
     * @author wanghb
     */
    public enum MonitorEnum {
        MONITOR_SERVER("SERVER", ServerMonitor.class),
        MONITOR_APPLICATION("APPLICATION", ApplicationMonitor.class),
        MONITOR_DISK("DISK", DiskMonitor.class);
        private String name;
        private Class monitorClass;
        MonitorEnum(String name, Class monitorClass) {
            this.name = name;
            this.monitorClass = monitorClass;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Class getMonitorClass() {
            return monitorClass;
        }

        public void setMonitorClass(Class monitorClass) {
            this.monitorClass = monitorClass;
        }

        public static Class findMonitorClass(String name){
            String upperName=name.toUpperCase();
            for(MonitorEnum monitorEnum:values()){
                if(monitorEnum.getName().equals(upperName)){
                    return monitorEnum.getMonitorClass();
                }
            }
            return null;
        }
    }

}

