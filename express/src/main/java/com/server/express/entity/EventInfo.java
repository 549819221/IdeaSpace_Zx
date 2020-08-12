package com.server.express.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
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
public class EventInfo extends EventInfoAbstract implements Serializable {
    public EventInfo() {
        super();
    }
}