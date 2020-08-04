package com.server.monitor.entity;

import com.server.monitor.entity.parent.Monitor;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "T_MONITOR_APPLICATION")
@DiscriminatorValue("APPLICATION")
@DynamicUpdate
@DynamicInsert
public class ApplicationMonitor extends Monitor {
    @Column(length = 50)
    @ApiModelProperty(value = "应用进程名")
    //@NotNull(groups = {updateView.class,addView.class},message = "progress不能为空！")
    //@Size(max = 50,message = "长度不超过50")
    private String progress;

    @Column(precision = 10)
    //@Min(value = 0,groups = {updateView.class,addView.class}, message = "端口号不正确!")
    //@Max(value = 65535,groups = {updateView.class,addView.class},message = "端口号不正确!")
    @ApiModelProperty(value = "是否开启端口监控(0:否，1：是)")
    private Integer app;

    @Column(precision = 10)
    //@Min(value = 0,groups = {updateView.class,addView.class}, message = "端口号不正确!")
    //@Max(value = 65535,groups = {updateView.class,addView.class},message = "端口号不正确!")
    @ApiModelProperty(value = "端口号")
    private Integer appPort;

    //@Min(value = 0,groups = {updateView.class,addView.class}, message = "数值错误！")
    //@Max(value = 1,groups = {updateView.class,addView.class},message = "数值错误！")
    @Column(precision = 10)
    @ApiModelProperty(value = "telnet端口号")
    private Integer telnetPort;

    @Column(columnDefinition = "number(1) default 0")
    @ApiModelProperty(value = "是否开启telnet(0:否，1：是)")
    private Integer telnet;

    @Column(columnDefinition = "number(10) default 2")
    @ApiModelProperty(value = "telnet超时时间(s)")
    private Integer telnetTimeout;

    @Column(columnDefinition = "number(1) default 0")
    @ApiModelProperty(value = "是否开启http (0:否，1：是)")
    private Integer http;

    @Column
    @ApiModelProperty(value = "http请求地址")
    private String url;

    @Column(columnDefinition = "number(10) default 2")
    @ApiModelProperty(value = "http超时时间(s)")
    private Integer httpTimeout;

    @Column(length = 50)
    @ApiModelProperty(value = "正确返回结果")
    private String httpResult;

}
