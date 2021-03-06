package com.server.ticket.controller;

import com.server.ticket.entity.*;
import com.server.ticket.service.BasisService;
import com.server.ticket.util.ExceptionUtil;
import com.server.ticket.util.ParamEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * (Basis)表控制层
 * @author wanghb
 * @since 2020-07-10 14:43:44
 */
@RestController
@RequestMapping("/ticket")
@Api(value = "票务数据相关控制类",  tags = "票务数据相关控制类")
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
    @ApiOperation(value = "令牌获取", notes = "令牌获取")
    public Object getToken(@RequestBody User user, HttpServletRequest request) throws IOException {

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
    @ApiOperation(value = "上传项目数据", notes = "上传项目数据")
    public Object project(@RequestBody UploadDataInfo uploadDataInfo) {
        Object obj = null;
        try {
            obj = basisService.dataUpload(uploadDataInfo,ParamEnum.uploadUrl.project.getCode());
        } catch (Exception e) {
            if(e.getMessage().indexOf( "PRIMARY" ) > -1){
                return new UploadDataResult( ParamEnum.resultCode.paramError.getCode(),  ParamEnum.resultCode.paramError.getName(), new StringBuilder().append( "该 " ).append( uploadDataInfo.getSerial() ).append( " serial(流水号) 已存在。" ).toString() );
            }else{
                logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
                return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            }
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
    @ApiOperation(value = "上传场次数据", notes = "上传场次数据")
    public Object perform(@RequestBody UploadDataInfo expressStaff) {
        Object obj = null;
        try {
            obj = basisService.dataUpload(expressStaff,ParamEnum.uploadUrl.perform.getCode());
        } catch (Exception e) {
            if(e.getMessage().indexOf( "PRIMARY" ) > -1){
                return new UploadDataResult( ParamEnum.resultCode.paramError.getCode(),  ParamEnum.resultCode.paramError.getName(), new StringBuilder().append( "该 " ).append( expressStaff.getSerial() ).append( " serial(流水号) 已存在。" ).toString() );
            }else{
                logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
                return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            }
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
    @ApiOperation(value = "上传售票数据", notes = "上传售票数据")
    public Object ticketSold(@RequestBody UploadDataInfo expressStaff) {
        Object obj = null;
        try {
            obj = basisService.dataUpload(expressStaff,ParamEnum.uploadUrl.ticketSold.getCode());
        } catch (Exception e) {
            if(e.getMessage().indexOf( "PRIMARY" ) > -1){
                return new UploadDataResult( ParamEnum.resultCode.paramError.getCode(),  ParamEnum.resultCode.paramError.getName(), new StringBuilder().append( "该 " ).append( expressStaff.getSerial() ).append( " serial(流水号) 已存在。" ).toString() );
            }else{
                logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
                return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            }
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
    @ApiOperation(value = "上传验票数据", notes = "上传验票数据")
    public Object ticketChecked(@RequestBody UploadDataInfo expressStaff) {
        Object obj = null;
        try {
            obj = basisService.dataUpload(expressStaff,ParamEnum.uploadUrl.ticketChecked.getCode());
        } catch (Exception e) {
            if(e.getMessage().indexOf( "PRIMARY" ) > -1){
                return new UploadDataResult( ParamEnum.resultCode.paramError.getCode(),  ParamEnum.resultCode.paramError.getName(), new StringBuilder().append( "该 " ).append( expressStaff.getSerial() ).append( " serial(流水号) 已存在。" ).toString() );
            }else{
                logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
                return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            }
        }
        return obj;
    }


    /**
     * @description  更新状态
     * @return  实体对象
     * @date  2020-07-10 14:43:44
     * @author  wanghb
     * @edit
     */
    @PostMapping("/updateStatus")
    @ResponseBody
    @ApiOperation(value = "更新状态", notes = "更新状态")
    public UploadDataResult updateStatus(@RequestBody String packageSerialParam){
        try {
            return basisService.updateStatus(packageSerialParam);
        } catch (Exception e) {
            logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
        }
    }

    /**
     * @description  重新打包接口
     * @return  实体对象
     * @date  2020-07-10 14:43:44
     * @author  wanghb
     * @edit
     */
    @GetMapping("/reUploadFtp")
    @ResponseBody
    @ApiOperation(value = "重新打包接口", notes = "重新打包接口")
    public UploadDataResult reUploadFtp(@RequestParam(name = "serial",  required = true) String serial){
        try {
            return basisService.reUploadFtp(serial);
        } catch (Exception e) {
            logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
        }
    }
}