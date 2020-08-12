package com.server.express.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

import javax.persistence.*;
import java.io.Serializable;

/**
 * (EventEntityAbstract)实体抽象类
 *
 * @author wanghb
 * @since 2020-08-12 17:55:46
 */
@MappedSuperclass
@ApiModel(description= "(EventEntityAbstract)实体抽象类")
public class EventInfoAbstract implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "obj_id")
    @ApiModelProperty(value="主键，包流水号",name="bjId",required=false)
    private String bjId;

    @Basic
    @Column(name = "event")
    @ApiModelProperty(value="事件情况：0.尚未通知事件；1.已通知事件未完成事件；2.已通知已完成事件",name="vent",required=false)
    private String vent;


    public String getBjId() {
        return bjId;
    }

    public void setBjId(String bjId) {
        this.bjId = bjId;
    }

    public String getVent() {
        return vent;
    }

    public void setVent(String vent) {
        this.vent = vent;
    }

}