package com.server.monitor.entity.parent;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Intellij IDEA.
 * User:  zyx
 * Date:  2020/6/4
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class FullEntityParent implements Serializable {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @ApiModelProperty(value = "主键")
    @Column(columnDefinition = "varchar2(32) default sys_guid()")
    private String objId;

    @CreatedDate
    @Column(updatable = false)
    private Date createTime;

    @CreatedBy
    @Column(name = "create_user_id",columnDefinition = "varchar2(32)")
    private String createUserId;

    @LastModifiedBy
    @Column(name = "last_modified_user_id",columnDefinition = "varchar2(32)")
    private String lastModifiedUserId;

    @LastModifiedDate
    @Column(name = "last_modified_time")
    private Date lastModifiedTime;

}