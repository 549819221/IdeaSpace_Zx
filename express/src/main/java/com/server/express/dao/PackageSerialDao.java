package com.server.express.dao;

import com.server.express.entity.PackageSerialInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * (packageSerial)表数据库访问层
 *
 * @author wanghb
 * @since 2020-08-12 17:55:44
 */
@Repository
public interface PackageSerialDao extends JpaRepository<PackageSerialInfo, String> {

    int countBySerial(String serial);
}