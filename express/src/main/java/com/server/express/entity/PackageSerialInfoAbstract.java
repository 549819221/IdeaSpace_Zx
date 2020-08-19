package com.server.express.entity;

import com.fasterxml.jackson.annotation.JsonView;
import javax.persistence.*;
import java.math.BigDecimal;

import java.io.Serializable;
import java.util.*;
import javax.persistence.OneToMany;

import com.server.express.util.JsonViewMark;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 包流水信息( PackageSerialInfoAbstract )实体抽象类
* @author wanghb
* @since 2020-8-19 11:38:40
*/
@MappedSuperclass
@ApiModel(description= "包流水信息( PackageSerialInfoAbstract )实体")
public class PackageSerialInfoAbstract implements Serializable {

    @Id
    @Column(name = "serial")
    @JsonView(JsonViewMark.SimpleView.class)
    @ApiModelProperty(value="主键，包流水号",name=" serial",required=false)
    private String serial;

    @Basic
    @Column(name = "upload_time")
    @JsonView(JsonViewMark.SimpleView.class)
    @ApiModelProperty(value="接收数据包时间",name=" uploadTime",required=false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone="GMT+8")
    private Date uploadTime;

    @Basic
    @Column(name = "result")
    @JsonView(JsonViewMark.SimpleView.class)
    @ApiModelProperty(value="上传结果：0.上传成功未入库；1.上传成功已入库；2.上传成功入库失败",name=" result",required=false)
    private String result;

    @Basic
    @Column(name = "event")
    @JsonView(JsonViewMark.SimpleView.class)
    @ApiModelProperty(value="事件情况：0.尚未通知事件；1.已通知事件未完成事件；2.已通知已完成事件",name=" event",required=false)
    private String event;

    @Basic
    @Column(name = "ftp_path")
    @JsonView(JsonViewMark.SimpleView.class)
    @ApiModelProperty(value="上传到FTP成功后FTP返回的路径，之后通过此路径删除FTP中保存的文件",name=" ftpPath",required=false)
    private String ftpPath;

    @Basic
    @Column(name = "ftp_status")
    @JsonView(JsonViewMark.SimpleView.class)
    @ApiModelProperty(value="0.FTP中文件未删除；1.FTP中文件已删除",name=" ftpStatus",required=false)
    private String ftpStatus;

    public String getSerial() {
    return serial;
    }

    public void setSerial(String serial) {
    this.serial = serial;
    }

    public Date getUploadTime() {
    return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
    this.uploadTime = uploadTime;
    }

    public String getResult() {
    return result;
    }

    public void setResult(String result) {
    this.result = result;
    }

    public String getEvent() {
    return event;
    }

    public void setEvent(String event) {
    this.event = event;
    }

    public String getFtpPath() {
    return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
    this.ftpPath = ftpPath;
    }

    public String getFtpStatus() {
    return ftpStatus;
    }

    public void setFtpStatus(String ftpStatus) {
    this.ftpStatus = ftpStatus;
    }


}
