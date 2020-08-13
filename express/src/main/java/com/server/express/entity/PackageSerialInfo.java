package com.server.express.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    private String erial;

    @Basic
    @Column(name = "upload_time")
    //接收数据包时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date ploadTime;

    @Basic
    @Column(name = "result")
    //上传结果：0.上传成功未入库；1.上传成功已入库；2.上传成功入库失败
    private String esult;

    @Basic
    @Column(name = "event")
    //事件情况：0.尚未通知事件；1.已通知事件未完成事件；2.已通知已完成事件
    private String vent;

    @Basic
    @Column(name = "fastdfs_id")
    //上传到FastDFS成功后FastDFS返回的ID，之后通过此ID删除FastDFS中保存的文件
    private String astdfsId;

    @Basic
    @Column(name = "fastdfs_status")
    //0.FastDFS中文件未删除；1.FastDFS中文件已删除
    private String astdfsStatus;


    public String getErial() {
        return erial;
    }

    public void setErial(String erial) {
        this.erial = erial;
    }

    public Date getPloadTime() {
        return ploadTime;
    }

    public void setPloadTime(Date ploadTime) {
        this.ploadTime = ploadTime;
    }

    public String getEsult() {
        return esult;
    }

    public void setEsult(String esult) {
        this.esult = esult;
    }

    public String getVent() {
        return vent;
    }

    public void setVent(String vent) {
        this.vent = vent;
    }

    public String getAstdfsId() {
        return astdfsId;
    }

    public void setAstdfsId(String astdfsId) {
        this.astdfsId = astdfsId;
    }

    public String getAstdfsStatus() {
        return astdfsStatus;
    }

    public void setAstdfsStatus(String astdfsStatus) {
        this.astdfsStatus = astdfsStatus;
    }
}