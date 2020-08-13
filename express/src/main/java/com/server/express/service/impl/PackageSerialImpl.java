package com.server.express.service.impl;

import com.server.express.dao.PackageSerialDao;
import com.server.express.entity.PackageSerialInfo;
import com.server.express.service.PackageSerialService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * (PackageSerial)表服务实现类
 *
 * @author wanghb
 * @since 2020-08-12 17:55:46
 */
@Repository
public class PackageSerialImpl implements PackageSerialService {
    @Resource
    private PackageSerialDao packageSerialDao;


    /**
     * @param packageSerialInfo 实体
     * @return 无返回值
     * @description 保存
     * @date 2020-08-12 17:55:46
     * @author wanghb
     * @edit
     */
    @Override
    public void save(PackageSerialInfo packageSerialInfo) {
        packageSerialDao.save( packageSerialInfo );
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
    public PackageSerialInfo view(String id) {
        Optional<PackageSerialInfo> packageSerialEntity = packageSerialDao.findById( id );
        return packageSerialEntity.isPresent() ? packageSerialEntity.get() : null;
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
        packageSerialDao.deleteById( id );
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
    }

}