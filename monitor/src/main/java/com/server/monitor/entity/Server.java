package com.server.monitor.entity;

import com.server.monitor.entity.parent.Monitor;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name="T_SERVER_INFO")
@DynamicUpdate
@DynamicInsert
public class Server implements Serializable {

    //状态代码
    private static final String STATUS_DICT_CODE="2001";
    //所属地区代码
    private static final String AREA_DICT_CODE="2002";
    //所属操作系统代码
    private static final String SYSTEM_DICT_CODE="2003";
    //所属操作系统版本代码
    private static final String SYSTEM_VERSION_DICT_CODE="2004";

    //服务器正常
    public static final String SERVER_OK="1";
    //服务器异常
    public static final String SERVER_ERROR="9";

    public static final String DELETE_STATE ="0";
    public static final String LIVE_STATE ="1";


    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "obj_id",columnDefinition = "varchar2(32) default sys_guid()")
    @NotNull(groups = updateValidator.class,message = "objId不能为空")
    @ApiModelProperty(value = "主键(自动生成)")
    private String objId;

    @ApiModelProperty(value="服务器名称(不超过50个字)")
    @Column(name="name",nullable = false,columnDefinition = "varchar2(50)")
    @NotNull(groups = {updateValidator.class,addValidator.class},message = "name不能为空")
    @Size(max = 50,groups = {updateValidator.class,addValidator.class},message = "长度不超过50")
    private String name;

    @ApiModelProperty(value="服务器ip")
    @Column(name="ip",nullable = false,columnDefinition = "varchar2(50)")
    @NotNull(groups = {updateValidator.class,addValidator.class},message = "ip不能为空")
    @Size(max = 50,groups = {updateValidator.class,addValidator.class},message = "长度不超过50")
    @Pattern(regexp = "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))",
            groups = {updateValidator.class,addValidator.class},message = "ip校验错误")
    private String ip;


    @Column(columnDefinition = "number(10) default 0")
    @ApiModelProperty(value = "ssh端口号")
    private Integer sshPort;

    @Column(length = 50)
    @ApiModelProperty(value = "ssh用户名")
    private String username;

    @Column(length = 50)
    @ApiModelProperty(value = "ssh密码")
    private String password;

    @Column(length = 2)
    private String status;



    @ApiModelProperty(value="服务器地区")
    @Column(length = 20)
    private String area;

    @ApiModelProperty(value="操作系统")
    @Column(length = 20)
    private String system;

    @ApiModelProperty(value="操作系统版本")
    @Column(length = 20)
    private String systemVersion;

    @ApiModelProperty(value="服务器描述(不超过50个字)")
    @Column(name="description",columnDefinition = "varchar2(50)")
    //@Size(max = 50,groups = {updateValidator.class,addValidator.class},message = "长度不超过50")
    private String description;

    @Column(columnDefinition = "varchar2(2) default 1")
    private String state;



    /**
     * 非数据库字段
     * ---------------------------------------------------------------------------
     */


    @OneToOne(targetEntity = Dict.class,fetch = FetchType.EAGER,cascade = CascadeType.REFRESH)
    @JoinColumnsOrFormulas(value=
            {@JoinColumnOrFormula(column=@JoinColumn(name="area",referencedColumnName="value",updatable = false,insertable = false,foreignKey = @ForeignKey(name = "none"))),
                    @JoinColumnOrFormula(formula=@JoinFormula(value= Server.AREA_DICT_CODE, referencedColumnName = "code"))
            })
    private Dict areaDetail;

    @OneToOne(targetEntity = Dict.class,fetch = FetchType.EAGER,cascade = CascadeType.REFRESH)
    @JoinColumnsOrFormulas(value=
            {@JoinColumnOrFormula(column=@JoinColumn(name="system",referencedColumnName="value",updatable = false,insertable = false,foreignKey = @ForeignKey(name = "none"))),
                    @JoinColumnOrFormula(formula=@JoinFormula(value= Server.SYSTEM_DICT_CODE, referencedColumnName = "code"))
            })
    private Dict systemDetail;

    @OneToOne(targetEntity = Dict.class,fetch = FetchType.EAGER,cascade = CascadeType.REFRESH)
    @JoinColumnsOrFormulas(value=
            {@JoinColumnOrFormula(column=@JoinColumn(name="systemVersion",referencedColumnName="value",updatable = false,insertable = false,foreignKey = @ForeignKey(name = "none"))),
                    @JoinColumnOrFormula(formula=@JoinFormula(value= Server.SYSTEM_VERSION_DICT_CODE, referencedColumnName = "code"))
            })
    private Dict systemVersionDetail;

    @OneToOne(targetEntity = Dict.class,fetch = FetchType.EAGER,cascade = CascadeType.REFRESH)
    @JoinColumnsOrFormulas(value=
            {@JoinColumnOrFormula(column=@JoinColumn(name="status",referencedColumnName="value",updatable = false,insertable = false,foreignKey = @ForeignKey(name = "none"))),
                    @JoinColumnOrFormula(formula=@JoinFormula(value= Server.STATUS_DICT_CODE, referencedColumnName = "code"))
            })
    private Dict statusDetail;


    public Server() {
    }
    public interface updateValidator{}
    public interface addValidator{}


}
