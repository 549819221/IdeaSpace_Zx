package com.server.fastdfstest.dao;

import com.server.fastdfstest.entity.EventInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * (Event)表数据库访问层
 *
 * @author wanghb
 * @since 2020-08-12 17:55:44
 */
@Repository
public interface EventDao extends JpaRepository<EventInfo, String> {

}
