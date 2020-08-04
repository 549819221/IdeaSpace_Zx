package com.server.monitor.entity;

import com.server.monitor.entity.parent.Monitor;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "T_MONITOR_SERVER")
@DiscriminatorValue("SERVER")
@DynamicUpdate
@DynamicInsert
public class ServerMonitor extends Monitor {
    @Column(columnDefinition = "number(1) default 0")
    @ApiModelProperty(value = "是否开启ping (0:否 1:是)")
    private Integer ping;

    @Column(columnDefinition = "number(1) default 0")
    @ApiModelProperty(value = "是否开启telnet (0:否 1:是)")
    private Integer telnet;

    @Column(columnDefinition = "number(10) default 23")
    @ApiModelProperty(value = "telnet端口号")
    private Integer telnetPort;

}

