package com.server.express.controller;

import com.server.express.entity.*;
import com.server.express.service.BasisService;
import com.server.express.util.ExceptionUtil;
import com.server.express.util.ParamEnum;
import org.apache.log4j.Logger;
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
public class BasisController  {
    private static Logger logger = Logger.getLogger( BasisController.class );
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
    public Object dataUpload(@RequestBody UploadDataInfo uploadDataInfo) {
        Object obj = null;
        try {
            obj = basisService.dataUpload(uploadDataInfo);
        } catch (Exception e) {
            logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( e.getMessage() ).toString() );
        }
        return obj;
    }


    /**
     * @description  上传数据
     * @return  实体对象
     * @date  2020-07-10 14:43:44
     * @author  wanghb
     * @edit
     */
    @PostMapping("/expressStaffUpload")
    @ResponseBody
    public Object expressStaffUpload(@RequestBody ExpressStaff expressStaff) {
        Object obj = null;
        try {
            obj = basisService.expressStaffUpload(expressStaff);
        } catch (Exception e) {
            logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( e.getMessage() ).toString() );
        }
        return obj;
    }

    /**
     * @description  令牌获取
     * @return  实体对象
     * @date  2020-07-10 14:43:44
     * @author  wanghb
     * @edit
     */
    @PostMapping("/updateStatus")
    @ResponseBody
    public UploadDataResult updateStatus(@RequestBody PackageSerialInfo packageSerialParam){
        try {
            return basisService.updateStatus(packageSerialParam);
        } catch (Exception e) {
            logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( e.getMessage() ).toString() );
        }
    }
}