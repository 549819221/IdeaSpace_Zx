package com.server.express.dao;

import com.server.express.entity.EventInfo;
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
 * (Event)表数据库访问层
 *
 * @author wanghb
 * @since 2020-08-12 17:55:44
 */
@Repository
public interface EventDao extends JpaRepository<EventInfo, String> {

}