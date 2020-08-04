package com.server.monitor.entity;

import com.server.monitor.entity.parent.FullEntityParent;
import com.server.monitor.entity.parent.Monitor;
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
import javax.validation.constraints.NotNull;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "T_MONITOR_NODE")
@DynamicUpdate
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class MonitorNode{

    //字典代码
    public static final String STATUS_DICT_CODE="3001";
    //节点正常
    public static final String NODE_OK="1";
    //节点异常
    public static final String NODE_ERROR="9";
    @Id
    @Column(length = 32)
    private String ObjId;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String ip;

    @Column(precision = 10)
    private Integer port;

    @Column(name = "last_heart_time")
    private Date lastHeartTime;

    @CreatedDate
    @Column(name = "register_time",updatable = false)
    private Date registerTime;

    @Column(length = 2)
    //@NotNull(message = "状态不能为空")
    private String status;


    /**
     * 非数据库字段
     * -------------------------------------------------------------------------
     */

    @OneToOne(targetEntity = Dict.class,fetch = FetchType.EAGER,cascade = CascadeType.REFRESH)
    @JoinColumnsOrFormulas(value=
            {@JoinColumnOrFormula(column=@JoinColumn(name="status",referencedColumnName="value",updatable = false,insertable = false,foreignKey = @ForeignKey(name = "none"))),
                    @JoinColumnOrFormula(formula=@JoinFormula(value= MonitorNode.STATUS_DICT_CODE, referencedColumnName = "code"))
            })
    private Dict statusDetail;


}
