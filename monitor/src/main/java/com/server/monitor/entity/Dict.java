package com.server.monitor.entity;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "T_SERVER_DICT")
public class Dict implements Serializable {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar2(32) default sys_guid()")
    @ApiModelProperty(value = "字典数据主键")
    private String ObjId;

    @Column(nullable = false,length = 50)
    @ApiModelProperty(value = "字典数据代码")
    private String code;

    @Column(nullable = false,length = 50)
    @ApiModelProperty(value = "字典数据类型")
    private String type;

    @Column(length = 32)
    @ApiModelProperty(value = "字典数据父级ID")
    private String pid;

    @Column(length = 50,nullable = false)
    @ApiModelProperty(value = "字典数据值")
    private String value;

    @Column(length = 50)
    @ApiModelProperty(value = "字典值注释")
    private String note;

    @Column(length = 5)
    @ApiModelProperty(value = "排序")
    private String priority;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.REFRESH)
    @JoinColumn(name = "pid",updatable = false,insertable = false,foreignKey = @ForeignKey(name = "none"))
    private List<Dict> children;




}
