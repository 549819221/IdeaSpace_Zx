package com.server.express.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * (PackageSerialEntity)实体类
 *
 * @author wanghb
 * @since 2020-08-13 10:05:37
 */
@Entity
@Table(name = "package_serial", schema = "express")
public class PackageSerialInfo implements Serializable {

    @Id
    @Column(name = "serial")
    //主键，包流水号
    private String serial;

    @Basic
    @Column(name = "upload_time")
    //接收数据包时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date uploadTime;

    @Basic
    @Column(name = "result")
    //上传结果：0.上传成功未入库；1.上传成功已入库；2.上传成功入库失败
    private String result;

    @Basic
    @Column(name = "event")
    //事件情况：0.尚未通知事件；1.已通知事件未完成事件；2.已通知已完成事件
    private String event;

    @Basic
    @Column(name = "fastdfs_id")
    //上传到FastDFS成功后FastDFS返回的ID，之后通过此ID删除FastDFS中保存的文件
    private String fastdfsId;

    @Basic
    @Column(name = "fastdfs_status")
    //0.FastDFS中文件未删除；1.FastDFS中文件已删除
    private String fastdfsStatus;


    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date ploadTime) {
        this.uploadTime = ploadTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String esult) {
        this.result = esult;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String vent) {
        this.event = vent;
    }

    public String getFastdfsId() {
        return fastdfsId;
    }

    public void setFastdfsId(String astdfsId) {
        this.fastdfsId = astdfsId;
    }

    public String getFastdfsStatus() {
        return fastdfsStatus;
    }

    public void setFastdfsStatus(String astdfsStatus) {
        this.fastdfsStatus = astdfsStatus;
    }
}