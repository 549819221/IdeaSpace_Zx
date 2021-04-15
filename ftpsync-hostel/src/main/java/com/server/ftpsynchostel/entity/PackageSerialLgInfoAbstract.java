package com.server.ftpsynchostel.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.server.ftpsynchostel.util.JsonViewMark;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
* 旅馆包流水信息( PackageSerialLgInfoAbstract )实体抽象类
* @author wanghb
* @since 2020-8-19 11:38:40
*/
@MappedSuperclass
@Setter
@Getter
@ApiModel(description= "包流水信息( PackageSerialLgInfoAbstract )实体")
public class PackageSerialLgInfoAbstract implements Serializable {

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
    @ApiModelProperty(value="ftp目录",name=" ftpPath",required=false)
    private String ftpPath;

    @Basic
    @Column(name = "ftp_status")
    @JsonView(JsonViewMark.SimpleView.class)
    @ApiModelProperty(value="0.FTP中文件未删除；1.FTP中文件已删除",name=" ftpStatus",required=false)
    private String ftpStatus;

    @Basic
    @Column(name = "fastdfs_id")
    @JsonView(JsonViewMark.SimpleView.class)
    @ApiModelProperty(value="上传到FastDFS成功后FastDFS返回的ID，之后通过此ID删除FastDFS中保存的文件",name=" fastdfsId",required=false)
    private String fastdfsId;

    @Basic
    @Column(name = "fastdfs_status")
    @JsonView(JsonViewMark.SimpleView.class)
    @ApiModelProperty(value="0.FastDFS中文件未删除；1.FastDFS中文件已删除",name=" fastdfsStatus",required=false)
    private String fastdfsStatus;

    @Basic
    @Column(name = "sync_ftp_status")
    @JsonView(JsonViewMark.SimpleView.class)
    @ApiModelProperty(value="同步ftp状态  0.未同步；1.已同步",name=" fastdfsStatus",required=false)
    private String syncFtpStatus;

    @Basic
    @Column(name = "public_key")
    @ApiModelProperty(value="公钥",name="publicKey",required=false)
    private String publicKey;

    @Basic
    @Column(name = "file_size")
    @ApiModelProperty(value="文件大小(单位b)",name="fileSize",required=false)
    private String fileSize;

    @Basic
    @Column(name = "data_type")
    @ApiModelProperty(value="数据类型",name="dataType",required=false)
    private String dataType;



}
