package com.server.monitor.controller;

import com.server.monitor.entity.parent.Monitor;
import com.server.monitor.service.BasisService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * (Basis)表控制层
 * @author wanghb
 * @since 2020-07-10 14:43:44
 */
@RestController
@RequestMapping("/basis")
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class BasisController  {
    /**
     * 服务对象
     */
    @Resource
    private BasisService basisService;


    /**
     * @description  详情
     * @return  实体对象
     * @date  2020-07-10 14:43:44
     * @author  wanghb
     * @edit
     */
    @GetMapping("/register")
    @ResponseBody
    public Map<String, Object> register(){
        Map<String, Object> temp = basisService.register();
        return temp;
    }

    /**
     * @description  获取所有节点
     * @return  实体对象
     * @date  2020-07-10 14:43:44
     * @author  wanghb
     * @edit
     */
    @GetMapping("/getAllNode")
    @ResponseBody
    public Object getAllNode(){
        List<Map<String, Object>> temp = basisService.getAllNode("b71dc8548e88c235b37b8f30b68ffadd");
        return temp;
    }

}