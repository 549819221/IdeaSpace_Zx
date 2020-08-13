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


}

