package com.example.fastdfsdel.dao;

import com.example.fastdfsdel.entity.PackageSerialInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


/**
* 包流水信息( PackageSerialDao )Dao类
* @author wanghb
* @since 2020-8-14 13:54:48
*/
@Repository
public interface PackageSerialDao extends JpaRepository<PackageSerialInfo, String>, JpaSpecificationExecutor<PackageSerialInfo> {
    @Override
    Page<PackageSerialInfo> findAll(Specification<PackageSerialInfo> specification, Pageable pageable);

    @Transactional
    @Modifying
    @Query("delete  from PackageSerialInfo a where  a.serial in (:ids) ")
    void batchDelete(List<String> ids);

    int countBySerial(String serial);

    List<PackageSerialInfo> getBySyncFtpStatus(String code);
}
