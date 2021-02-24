package com.server.hostel.entity;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
* 包流水信息( PackageSerialInfo )实体抽象类
* @author wanghb
* @since 2020-8-14 13:54:48
*/
@Entity
@Table(name = "package_serial_lg")
@EntityListeners(AuditingEntityListener.class)
public class PackageSerialInfo extends com.server.hostel.entity.PackageSerialLgInfoAbstract implements Serializable {
	public  PackageSerialInfo(){
		super();
	}

}
