package com.server.monitor.entity;

import com.server.monitor.entity.parent.Monitor;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.*;

@Setter
@Getter
@Entity
@Table(name = "T_MONITOR_DISK")
@DiscriminatorValue("DISK")
@DynamicUpdate
@DynamicInsert
public class DiskMonitor extends Monitor {

    @Column(length = 50)
    @ApiModelProperty(value = "监控分区(多个分区用;隔开)")
    //@NotNull(groups = {updateView.class,addView.class},message = "partition不能为空!")
    //@Size(max = 50)
    private String partition;

    @Column(columnDefinition = "number(1) default 1")
    @ApiModelProperty(value = "操作类型(1:剩余空间百分比 2：剩余空间大小)")
    //@NotNull(groups = {updateView.class,addView.class},message = "operate不能为空！")
    //@Max(value = 2)
    //@Min(value = 1)
    private Integer operate;

    @Column(columnDefinition = "decimal(10,4) default 20.0")
    @ApiModelProperty(value = "对应操作的数值")
    //@NotNull(groups = {updateView.class,addView.class},message = "value不能为空！")
    //@Digits(integer = 10,fraction = 4,message = "value不合法！")
    private String operateValue;






}
