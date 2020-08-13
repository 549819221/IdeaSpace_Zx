package com.server.express.service;

import com.server.express.entity.PackageSerialInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (PackageSerial)表服务接口
 *
 * @author wanghb
 * @since 2020-08-12 17:55:45
 */
@Service("packageSerialService")
public interface PackageSerialService {

    void save(PackageSerialInfo packageSerialInfo);

    PackageSerialInfo view(String id);

    void delete(String id);

    void batchDelete(List<String> ids);

}