package com.server.monitor.entity.parent;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Created by Intellij IDEA.
 * User:  zyx
 * Date:  2020/6/8
 */
@Getter
@Setter
@MappedSuperclass
public class SimpleEntityParent {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar2(32) default sys_guid()")
    private String objId;

}
