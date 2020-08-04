package com.server.monitor.controller;

import com.server.monitor.entity.TaskDo;
import com.server.monitor.service.BasisService;
import com.server.monitor.service.DynamicQuartzService;
import com.server.monitor.service.impl.DynamicQuartzServiceImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.Map;

/**
 * 动态定时任务控制层
 * @author wanghb
 * @since 2020-07-10 14:43:44
 */
@RestController
@RequestMapping("/dynamicQuartz")
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class DynamicQuartzController {

    /**
     * 服务对象
     */
    @Resource
    private DynamicQuartzService dynamicQuartzService;

    /**
     * @description  定时任务
     * @return  实体对象
     * @date  2020-07-10 14:43:44
     * @author  wanghb
     * @edit
     */
    @GetMapping("/quartz")
    @ResponseBody
    public Object quartz(){
        Map<String, Object> temp = dynamicQuartzService.quartz();
        return temp;
    }

    /**
     * @description  详情
     * @return  实体对象
     * @date  2020-07-10 14:43:44
     * @author  wanghb
     * @edit
     */
    @GetMapping("/carriedOut")
    @ResponseBody
    public Object carriedOut(){
        //服务
        //28F07CD81BE54D3EADCAFFFB7015C7A0
        //磁盘
        //F3CFE03EAB5F465F870111C694DF1CD9

        //dynamicQuartzService.dealWithJob( DynamicQuartzServiceImpl.monitorCache.get( "F3CFE03EAB5F465F870111C694DF1CD9" ) )
        return null;
    }



}
