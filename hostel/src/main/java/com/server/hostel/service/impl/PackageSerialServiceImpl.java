package com.server.hostel.service.impl;


import com.server.hostel.service.PackageSerialService;
import com.server.hostel.dao.PackageSerialDao;
import com.server.hostel.entity.PackageSerialInfo;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;
import org.apache.log4j.Logger;

/**
* 包流水信息( PackageSerialServiceImpl )服务实现类
* @author wanghb
* @since 2020-8-14 13:54:48
*/
@Service("packageSerialService")
public class PackageSerialServiceImpl  implements PackageSerialService {

    private static Logger logger = Logger.getLogger( PackageSerialServiceImpl.class );

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
    @Override
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
    @Override
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
    @Override
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
    @Override
    @Transactional
    public void batchDelete(List<String> ids) {
        packageSerialDao.batchDelete( ids );
    }

}
