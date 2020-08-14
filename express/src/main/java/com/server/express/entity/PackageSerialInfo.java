package com.server.express.entity;
import java.io.Serializable;
import javax.persistence.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
* 包流水信息( PackageSerialEntity )实体抽象类
* @author wanghb
* @since 2020-8-14 11:25:52
*/
@Entity
@Table(name = "package_serial", schema = "express")
@EntityListeners(AuditingEntityListener.class)
public class PackageSerialInfo extends PackageSerialInfoAbstract implements Serializable {
	public PackageSerialInfo(){
		super();
	}

}