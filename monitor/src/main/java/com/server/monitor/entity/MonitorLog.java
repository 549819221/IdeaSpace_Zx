package com.server.monitor.entity;

import com.server.monitor.entity.parent.Monitor;
import com.server.monitor.entity.parent.SimpleEntityParent;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * Created by Intellij IDEA.
 * User:  zyx
 * Date:  2020/6/17
 */
@Getter
@Setter
@Entity
@Table(name = "T_LOG_INFO")
@DynamicUpdate
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class MonitorLog extends SimpleEntityParent {

    private final static String STATUS_DICT_CODE="4001";

    private final static String LOG_OK="1";
    private final static String LOG_ERROR="9";

    //NotNull
    //@NotEmpty
    @Column(length = 32)
    private String monitorId;

    @NotEmpty
    //@NotNull
    @Column(length = 32)
    private String nodeId;

    @CreatedDate
    @Column(updatable = false)
    private Date noteTime;

    //@NotNull
    @Column(length = 500)
    private String result;

    private String msg;

    @Column(length = 2)
    private String status;

    /**
     * 非数据库字段
     * -----------------------------------------------------------------------------------
     */

    @OneToOne(targetEntity = Dict.class,fetch = FetchType.EAGER,cascade = CascadeType.REFRESH)
    @JoinColumnsOrFormulas(value=
            {@JoinColumnOrFormula(column=@JoinColumn(name="status",referencedColumnName="value",updatable = false,insertable = false,foreignKey = @ForeignKey(name = "none"))),
                    @JoinColumnOrFormula(formula=@JoinFormula(value= MonitorLog.STATUS_DICT_CODE, referencedColumnName = "code"))
            })
    private Dict statusDetail;

    @OneToOne(targetEntity = MonitorNode.class,cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name = "nodeId",insertable = false,updatable = false,foreignKey = @ForeignKey(name = "none"))
    private MonitorNode nodeDetail;

    @OneToOne(targetEntity = Monitor.class,cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name = "monitorId",insertable = false,updatable = false,foreignKey = @ForeignKey(name = "none"))
    private Monitor monitorDetail;


}
