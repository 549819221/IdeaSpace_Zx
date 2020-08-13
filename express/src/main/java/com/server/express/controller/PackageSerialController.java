package com.server.express.controller;

import com.server.express.entity.PackageSerialInfo;
import com.server.express.service.PackageSerialService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (PackageSerial)表控制层
 *
 * @author wanghb
 * @since 2020-08-12 17:55:45
 */
@RestController
@RequestMapping("/api/packageSerial")
public class PackageSerialController {
    /**
     * 服务对象
     */
    @Resource
    private PackageSerialService packageSerialService;


    /**
     * @param packageSerialInfo 实体
     * @return 无返回值
     * @description 保存
     * @date 2020-08-12 17:55:45
     * @author wanghb
     * @edit
     */
    @PostMapping("/save")
    @ResponseBody
    public void save(@RequestBody PackageSerialInfo packageSerialInfo) {
        //packageSerialService.save( packageSerialInfo );
    }


    /**
     * @param id 主键id
     * @return 实体对象
     * @description 详情
     * @date 2020-08-12 17:55:45
     * @author wanghb
     * @edit
     */
    @GetMapping("/view")
    @ResponseBody
    public PackageSerialInfo view(@RequestParam(name = "id", required = true) String id) {
        PackageSerialInfo packageSerialInfo = packageSerialService.view( id );
        return null;
    }


    /**
     * @param id 主键id
     * @return 实体对象
     * @description 删除
     * @date 2020-08-12 17:55:45
     * @author wanghb
     * @edit
     */
    @GetMapping("/delete")
    @ResponseBody
    public void delete(@RequestParam(name = "id", required = true) String id) {
        //packageSerialService.delete( id );
    }

    /**
     * @param ids 主键id
     * @return 实体对象
     * @description 批量删除
     * @date 2020-07-08 16:03:21
     * @author wanghb
     * @edit
     */
    @PostMapping("/batchDelete")
    @ResponseBody
    public void batchDelete(@RequestBody List<String> ids) {
        //packageSerialService.batchDelete( ids );
    }
}