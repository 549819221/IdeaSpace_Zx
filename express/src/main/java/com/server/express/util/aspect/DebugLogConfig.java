package com.server.express.util.aspect;

public class DebugLogConfig {

    public DebugLogConfig(Boolean isOpne,Boolean isOpneIn,Boolean isOpneOut,Boolean isOpneController,Boolean isOpneServe,String entityMark){
        this.isOpne = isOpne;
        this.isOpneIn = isOpneIn;
        this.isOpneOut = isOpneOut;
        this.isOpneController = isOpneController;
        this.isOpneServe = isOpneServe;
        this.entityMark = entityMark;
    }

    /**
     * 是否开启本地调试
     */
    public boolean isOpne;
    /**
     * 是否开启进入方法打印
     */
    public boolean isOpneIn;
    /**
     * 是否开启返回值打印
     */
    public boolean isOpneOut;
    /**
     * 是否开启控制层打印
     */
    public boolean isOpneController;
    /**
     * 是否开启服务层打印
     */
    public boolean isOpneServe;
    /**
     * 实体标识
     */
    public  String entityMark;


}