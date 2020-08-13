package com.server.express.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

/**
 * (EventEntity)实体类
 *
 * @author wanghb
 * @since 2020-08-12 17:55:46
 */
@Entity
@Table(name = "event", schema = "express")
@EntityListeners(AuditingEntityListener.class)
public class EventInfo implements Serializable {
    //主键，包流水号
    @Id
    @Column(name = "obj_id")
    private String bjId;

    //事件情况：0.尚未通知事件；1.已通知事件未完成事件；2.已通知已完成事件
    @Basic
    @Column(name = "event")
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