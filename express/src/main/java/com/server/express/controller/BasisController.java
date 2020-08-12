package com.server.express.controller;

import com.server.express.entity.TokenResult;
import com.server.express.entity.UploadDataInfo;
import com.server.express.entity.User;
import com.server.express.service.BasisService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * (Basis)表控制层
 * @author wanghb
 * @since 2020-07-10 14:43:44
 */
@RestController
@RequestMapping("/basis")
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class BasisController  {

    @Resource
    private BasisService basisService;

    /**
     * @description  令牌获取
     * @return  实体对象
     * @date  2020-07-10 14:43:44
     * @author  wanghb
     * @edit
     */
    @PostMapping("/getToken")
    @ResponseBody
    public TokenResult getToken(@RequestBody User user, HttpServletRequest request){
        return basisService.getToken(user,request);
    }

    /**
     * @description  上传数据
     * @return  实体对象
     * @date  2020-07-10 14:43:44
     * @author  wanghb
     * @edit
     */
    @PostMapping("/dataUpload")
    @ResponseBody
    public Object dataUpload(@RequestBody UploadDataInfo uploadDataInfo){
        Object obj = basisService.dataUpload(uploadDataInfo);
        return obj;
    }

}