package com.server.fastdfstest.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SpringContextUtil implements ApplicationContextAware {


    /**
     * 上下文对象实例
     */
    private static ApplicationContext applicationContext;

    @Override
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 获取applicationContext
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean.
     * @param name
     * @return
     */
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }

    public static void main(String[] args) {
        String[] whb = new String[] {
                "APPROVAL_PROCESS",
                "JF_USE_LOG",
                "N_JSST_DATA",
                "OPERATOR_CONFIRM",
                "OPERATOR_PERSONNEL",
                "PROCESS_CONFIG",
                "PROCESS_CONFIG_DETAIL",
                "SIM_CARD_LIMIT",
                "SIM_CARD_RECORD",
                "SIM_CARD_RECORD_DETAIL",
                "T_APPSTORE_APPINFO",
                "T_AUTH_LEVEL",
                "T_AUTH_UPGRADE_APPLY",
                "T_CITY_MANAGER",
                "T_DICT_INFO",
                "T_ERROR_USER",
                "T_LOGIN_USER",
                "T_PEOPLE_USE_INFO",
                "T_RLBD_MONTH_CITY_USE",
                "T_RLBD_USE_COUNT",
                "T_SERVICE_DIC_INFO",
                "T_SUPER_AUTH_APPLY",
                "T_SYS_BLACK_LIST",
                "T_SYS_DEPT_INFO",
                "T_SYS_DEPT_INTERFACE",
                "T_SYS_MENU",
                "T_SYS_ROLE",
                "T_SYS_ROLE_DEPT",
                "T_SYS_ROLE_INTERFACE",
                "T_SYS_ROLE_USER",
                "T_SYS_TBSYNC_PARAMS",
                "T_SYS_USER_INFO",
                "T_SYS_USER_INTERFACE",
                "T_USE_COUNT_INFO",
                "T_USER_JF",
                "T_USER_JF_DICT",
                "T_USER_NOTICE",
                "T_USER_PUSHMSG",
                "T_USER_QUERY_HISTORY",
                "T_USER_ZHANGUO",
                "T_USER_ZHANGUO_PIC",
                "T_ZHCX_DATA_DBPARAM",
                "T_ZHCX_DATA_SJQ_WSPARAM",
                "T_ZHCX_DATA_WSPARAM",
                "T_ZHCX_FEATURES",
                "T_ZHCX_ROLE_FEATURE",
                "TJ_MONTH_USER_OVERUSE",
                "TJ_USE_DAY_CNT",
                "TJ_USE_MONTH",
                "TJ_USE_RLBD_CNT",
                "TJ_WEEK_USER_OVERUSE",
                "TJ_ZHANGUO_LASTMON",
                "Z_T_DICT_INFO"
        };
        String[] lhb = new String[] {
                "APPINFO",
                "APP_AUTH_DICT",
                "AREA_DICT",
                "AUDIT_DATA_DATE",
                "AUDIT_LOG",
                "CERT_INFO",
                "CONNECTIVITY_DATAPACKAGE",
                "DEFAULTPOLICE",
                "DEVICE_GROUP",
                "DEVICE_GROUP_TERMINAL",
                "DEVICE_POLICY",
                "DICT_INFO",
                "EXCEPTION_DATAPACKAGE",
                "EXCEPTION_DETAIL",
                "FEEDBACK_COLLECT_TOTAL",
                "FEEDBACK_INFO",
                "FEEDBACK_POLICY_RESULT",
                "FENCE_INFO",
                "FENCE_RULE_INFO",
                "FLOW_DATAPACKAGE",
                "FLOW_DETAIL",
                "HWINFO_DATAPACKAGE",
                "HWINFO_DETAIL",
                "HWLEGAL_DATAPACKAGE",
                "IMMEDIATE_RESULT",
                "INTEGRITYSTATUS_DATAPACKAGE",
                "LOGINERROR_DATAPACKAGE",
                "LOSTDEVICE",
                "LOST_DATAPACKAGE",
                "LOST_DEVICE_TCP",
                "MDMVERSION",
                "OPERATE_LOG",
                "POLICY_DATA_INFO",
                "POLICY_INFO",
                "POWER_DATAPACKAGE",
                "POWER_DETAIL",
                "PROTECTAPP",
                "PROTECTURL",
                "ROOTSTATUS_DATAPACKAGE",
                "RUNNINGTIME_DATAPACKAGE",
                "RUNNINGTIME_DETAIL",
                "R_BATCH_ROLE",
                "R_DEPT_ROLE",
                "R_USER_DEPT",
                "R_USER_ROLE",
                "SWINFO_DATAPACKAGE",
                "SWINFO_DETAIL",
                "SWLEGAL_DATAPACKAGE",
                "SWLEGAL_DETAIL",
                "TCP_POLICY_DATA",
                "TERMINAL_INFO",
                "TJ_DISHI_TERMINAL",
                "TRUSTED_DATAPACKAGE",
                "T_LOG_LOGIN",
                "T_SYS_DEPT",
                "T_SYS_ROLE",
                "ER_TERMINAL_INFO"
        };
        Set<String> whbSet = new HashSet<>();
        for (String s : whb) {
            whbSet.add(s  );
        }
        for (String s : lhb) {
            if(whbSet.contains( s )){
                System.out.println(s);
            }
        }
    }
}
