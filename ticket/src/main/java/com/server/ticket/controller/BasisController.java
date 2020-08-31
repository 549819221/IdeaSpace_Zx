package com.server.ticket.controller;

import com.server.ticket.entity.*;
import com.server.ticket.service.BasisService;
import com.server.ticket.util.ExceptionUtil;
import com.server.ticket.util.ParamEnum;
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
@RequestMapping("/ticket")
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
     * @description 上传项目数据
     * @return  实体对象
     * @date  2020-07-10 14:43:44
     * @author  wanghb
     * @edit
     */
    @PostMapping("/dataUpload/project")
    @ResponseBody
    public Object project(@RequestBody UploadDataInfo uploadDataInfo) {
        Object obj = null;
        try {
            obj = basisService.dataUpload(uploadDataInfo,ParamEnum.uploadUrl.project.getCode());
        } catch (Exception e) {
            logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( e.getMessage() ).toString() );
        }
        return obj;
    }


    /**
     * @description 上传场次数据
     * @return  实体对象
     * @date  2020-07-10 14:43:44
     * @author  wanghb
     * @edit
     */
    @PostMapping("/dataUpload/perform")
    @ResponseBody
    public Object perform(@RequestBody UploadDataInfo expressStaff) {
        Object obj = null;
        try {
            obj = basisService.dataUpload(expressStaff,ParamEnum.uploadUrl.perform.getCode());
        } catch (Exception e) {
            logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( e.getMessage() ).toString() );
        }
        return obj;
    }


    /**
     * @description 上传售票数据
     * @return  实体对象
     * @date  2020-07-10 14:43:44
     * @author  wanghb
     * @edit
     */
    @PostMapping("/dataUpload/ticketSold")
    @ResponseBody
    public Object ticketSold(@RequestBody UploadDataInfo expressStaff) {
        Object obj = null;
        try {
            obj = basisService.dataUpload(expressStaff,ParamEnum.uploadUrl.ticketSold.getCode());
        } catch (Exception e) {
            logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( e.getMessage() ).toString() );
        }
        return obj;
    }


    /**
     * @description 上传验票数据
     * @return  实体对象
     * @date  2020-07-10 14:43:44
     * @author  wanghb
     * @edit
     */
    @PostMapping("/dataUpload/ticketChecked")
    @ResponseBody
    public Object ticketChecked(@RequestBody UploadDataInfo expressStaff) {
        Object obj = null;
        try {
            obj = basisService.dataUpload(expressStaff,ParamEnum.uploadUrl.ticketChecked.getCode());
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
    public UploadDataResult updateStatus(@RequestBody String packageSerialParam){
        try {
            return basisService.updateStatus(packageSerialParam);
        } catch (Exception e) {
            logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( e.getMessage() ).toString() );
        }
    }
}