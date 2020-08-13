package com.example.demo.entity;

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
public class EventInfo implements Serializable {
    @Id
    @Column(name = "obj_id")
    private String bjId;

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