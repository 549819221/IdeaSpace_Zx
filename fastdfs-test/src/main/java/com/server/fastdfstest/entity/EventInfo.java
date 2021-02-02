package com.server.fastdfstest.entity;

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
    private String objId;

    //事件情况：0.尚未通知事件；1.已通知事件未完成事件；2.已通知已完成事件
    @Basic
    @Column(name = "event")
    private String event;


    public String getObjId() {
        return objId;
    }

    public void setObjId(String bjId) {
        this.objId = bjId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String vent) {
        this.event = vent;
    }
}
