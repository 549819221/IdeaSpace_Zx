package com.server.ftpsynchostel.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.io.Serializable;

/**
* 包流水信息( PackageSerialInfo )实体抽象类
* @author wanghb
* @since 2020-8-14 13:54:48
*/
@Entity
@Table(name = "package_serial_lg")
@EntityListeners(AuditingEntityListener.class)
public class PackageSerialLgInfo extends PackageSerialLgInfoAbstract implements Serializable {
	public PackageSerialLgInfo(){
		super();
	}

}
