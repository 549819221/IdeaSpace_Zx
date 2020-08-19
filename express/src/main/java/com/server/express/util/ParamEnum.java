package com.server.express.util;

/**
 * @Title: 对应数据字典的枚举类
 * @author wanghb
 * @date 2019-07-18
 */
public interface ParamEnum {
    /**
     * 配置环境
     * @author wanghb
     */
    enum properties {
        dev( "dev","本地环境"),
        pro( "pro","正式环境"),
        ;
        private String code;
        private String name;
        public String getCode() {
            return code;
        }
        public String getName() {
            return name;
        }
        properties(String code, String name) {
            this.code = code;
            this.name = name;
        }
        public static String getNameByCode(String code) {
            for (properties item : properties.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }
    }
    /**
     * 配置环境
     * @author wanghb
     */
    enum resultCode {
        success( "0","成功"),
        error( "-1","失败"),
        tokenExpired( "1003","token过期"),
        paramError( "400","参数异常"),
        ;
        private String code;
        private String name;
        public String getCode() {
            return code;
        }
        public String getName() {
            return name;
        }
        resultCode(String code, String name) {
            this.code = code;
            this.name = name;
        }
        public static String getNameByCode(String code) {
            for (resultCode item : resultCode.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }
    }

    /**
     * 上传结果
     * @author wanghb
     */
    enum resultStatus {
        status0( "0","上传成功未入库"),
        status1( "1","上传成功已入库"),
        status2( "2","上传成功入库失败"),
        ;
        private String code;
        private String name;
        public String getCode() {
            return code;
        }
        public String getName() {
            return name;
        }
        resultStatus(String code, String name) {
            this.code = code;
            this.name = name;
        }
        public static String getNameByCode(String code) {
            for (resultStatus item : resultStatus.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }
    }

    /**
     * 事件情况：0.尚未通知事件；1.已通知事件未完成事件；2.已通知已完成事件
     * @author wanghb
     */
    enum eventStatus {
        status0( "0","尚未通知事件"),
        status1( "1","已通知事件未完成事件"),
        status2( "2","已通知已完成事件"),
        ;
        private String code;
        private String name;
        public String getCode() {
            return code;
        }
        public String getName() {
            return name;
        }
        eventStatus(String code, String name) {
            this.code = code;
            this.name = name;
        }
        public static String getNameByCode(String code) {
            for (eventStatus item : eventStatus.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }
    }

    /**
     * 0.FTP中文件未删除；1.FTP中文件已删除
     * @author wanghb
     */
    enum ftpStatus {
        status0( "0","FTP中文件未删除"),
        status1( "1","FTP中文件已删除"),
        ;
        private String code;
        private String name;
        public String getCode() {
            return code;
        }
        public String getName() {
            return name;
        }
        ftpStatus(String code, String name) {
            this.code = code;
            this.name = name;
        }
        public static String getNameByCode(String code) {
            for (ftpStatus item : ftpStatus.values()) {
                if (item.getCode().equals(code)) {
                    return item.getName();
                }
            }
            return "";
        }
    }


}

