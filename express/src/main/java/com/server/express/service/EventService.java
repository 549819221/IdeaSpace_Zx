package com.server.express.service;

import com.server.express.entity.EventInfo;

import java.util.List;

/**
 * (Event)表服务接口
 *
 * @author wanghb
 * @since 2020-08-12 17:55:45
 */
public interface EventService  {

    void save(EventInfo eventInfo);

    EventInfo view(String id);

    void delete(String id);

    void batchDelete(List<String> ids);

}