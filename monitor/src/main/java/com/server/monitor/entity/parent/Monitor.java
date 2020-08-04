package com.server.monitor.entity.parent;

import com.server.monitor.entity.Dict;
import com.server.monitor.entity.MonitorNode;
import com.server.monitor.entity.Server;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "T_MONITOR_INFO")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type",columnDefinition = "varchar2(50)")
@DynamicUpdate
@DynamicInsert
public class Monitor extends FullEntityParent{

    //所属字典代码
    private static final String TYPE_DICT_CODE="1001";
    //所属状态
    private static final String STATUS_DICT_CODE="1002";

    //监控未启动
    public static final String MONITOR_READY="0";
    //监控正常
    public static final String MONITOR_OK="1";
    //监控异常
    public static final String MONITOR_ERROR="9";

    public static final String DELETE_STATE ="0";
    public static final String LIVE_STATE ="1";


    @Column(nullable = false,length = 32)
    @ApiModelProperty(value = "所属服务器ID")
    @NotNull(groups = {updateView.class,addView.class},message = "serverId不能为空！")
    private String serverId;

    @Column(nullable = false,length = 50)
    @ApiModelProperty(value = "监控任务名称")
    @NotNull(groups = {updateView.class,addView.class},message = "name不能为空！")
    @Size(max=50,message = "长度不超过50")
    private String name;

    @Column(length = 2)
    @ApiModelProperty(value = "任务状态")
    private String status;

    @Column(length = 32)
    @ApiModelProperty(value = "监控节点ID")
    private String nodeId;

    @Column(columnDefinition = "varchar2(2) default 1")
    @ApiModelProperty(value = "逻辑删除字段")
    private String state;

    @Column(insertable = false,updatable = false)
    @ApiModelProperty(value = "任务类型")
    private String type;

    @Column(precision = 10)
    @ApiModelProperty(value = "任务执行间隔时间")
    private Long executeInterval;

    private String relate;





    /**
     *
     * 分隔符
     * ----------------------------------------------
     * 下面是详情字段
     */

    @OneToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name = "nodeId",updatable = false,insertable = false,foreignKey = @ForeignKey(name = "none"))
    private MonitorNode nodeDetail;

    @OneToOne(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER)
    @JoinColumn(name = "serverId",updatable = false,insertable = false,foreignKey = @ForeignKey(name = "none"))
    private Server serverDetail;

    @OneToOne(targetEntity = Dict.class,fetch = FetchType.EAGER,cascade = CascadeType.REFRESH)
    @JoinColumnsOrFormulas(value=
            {@JoinColumnOrFormula(column=@JoinColumn(name="status",referencedColumnName="value",updatable = false,insertable = false,foreignKey = @ForeignKey(name = "none"))),
                    @JoinColumnOrFormula(formula=@JoinFormula(value= Monitor.STATUS_DICT_CODE, referencedColumnName = "code"))
            })
    private Dict statusDetail;


    @OneToOne(targetEntity = Dict.class,fetch = FetchType.EAGER,cascade = CascadeType.REFRESH)
    @JoinColumnsOrFormulas(value=
            {@JoinColumnOrFormula(column=@JoinColumn(name="type",referencedColumnName="value",updatable = false,insertable = false,foreignKey = @ForeignKey(name = "none"))),
                    @JoinColumnOrFormula(formula=@JoinFormula(value= Monitor.TYPE_DICT_CODE, referencedColumnName = "code"))
            })
    private Dict typeDetail;

    public interface queryView{};
    public interface addView{};
    public interface updateView{};

}
