package com.server.fastdfstest.service;


import com.server.fastdfstest.dao.PackageSerialDao;
import com.server.fastdfstest.entity.PackageSerialInfo;
import com.server.fastdfstest.service.PackageSerialService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * 包流水信息( PackageSerialServiceImpl )服务实现类
 * @author wanghb
 * @since 2020-8-14 13:54:48
 */
@Service("packageSerialService")
public class PackageSerialService {

    private static Logger logger = Logger.getLogger( PackageSerialService.class );

    @Resource
    private PackageSerialDao packageSerialDao;


    /**
     * @description 保存
     * @param packageSerialInfo 实体
     * @return 无返回值
     * @date 2020-8-14 13:54:48
     * @author wanghb
     * @edit
     */
    public void save(PackageSerialInfo packageSerialInfo) {
        packageSerialDao.save( packageSerialInfo );
    }


    /**
     * @description 详情
     * @param serial 主键id
     * @return 实体对象
     * @date 2020-8-14 13:54:48
     * @author wanghb
     * @edit
     */
    public PackageSerialInfo view(String serial) {
        Optional<PackageSerialInfo> packageSerialInfo = packageSerialDao.findById( serial );
        return packageSerialInfo.isPresent() ? packageSerialInfo.get() : null;
    }

    /**
     * @description 删除
     * @param serial 主键id
     * @return 实体对象
     * @date 2020-8-14 13:54:48
     * @author wanghb
     * @edit
     */
    public void delete(String serial) {
        packageSerialDao.deleteById( serial );
    }

    /**
     * @description 批量删除
     * @param ids 主键ids
     * @return 实体对象
     * @date 2020-8-14 13:54:48
     * @author wanghb
     * @edit
     */
    @Transactional
    public void batchDelete(List<String> ids) {
        packageSerialDao.batchDelete( ids );
    }

}
