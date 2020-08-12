package com.server.express.service.impl;

import com.server.express.dao.EventDao;
import com.server.express.entity.EventInfo;
import com.server.express.service.EventService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * (Event)表服务实现类
 *
 * @author wanghb
 * @since 2020-08-12 17:55:46
 */
@Service("eventService")
public class EventServiceImpl  implements EventService {
    @Resource
    private final EventDao eventDao;

    public EventServiceImpl(EventDao eventDao) {
        this.eventDao = eventDao;
    }



    /**
     * @param eventInfo 实体
     * @return 无返回值
     * @description 保存
     * @date 2020-08-12 17:55:46
     * @author wanghb
     * @edit
     */
    @Override
    public void save(EventInfo eventInfo) {
        eventDao.save( eventInfo );
    }


    /**
     * @param id 主键id
     * @return 实体对象
     * @description 详情
     * @date 2020-08-12 17:55:46
     * @author wanghb
     * @edit
     */
    @Override
    public EventInfo view(String id) {
        Optional<EventInfo> eventEntity = eventDao.findById( id );
        return eventEntity.isPresent() ? eventEntity.get() : null;
    }


    /**
     * @param id 主键id
     * @return 实体对象
     * @description 删除
     * @date 2020-08-12 17:55:46
     * @author wanghb
     * @edit
     */
    @Override
    public void delete(String id) {
        eventDao.deleteById( id );
    }

    /**
     * @param ids 主键ids
     * @return 实体对象
     * @description 批量删除
     * @date 2020-07-08 16:03:22
     * @author wanghb
     * @edit
     */
    @Override
    @Transactional
    public void batchDelete(List<String> ids) {
        eventDao.batchDelete( ids );
    }

}