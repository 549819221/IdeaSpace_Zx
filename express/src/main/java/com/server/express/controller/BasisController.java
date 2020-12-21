package com.server.express.controller;

import com.server.express.entity.*;
import com.server.express.service.BasisService;
import com.server.express.util.DateUtil;
import com.server.express.util.ExceptionUtil;
import com.server.express.util.FastDFSClient;
import com.server.express.util.ParamEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * (Basis)表控制层
 * @author wanghb
 * @since 2020-07-10 14:43:44
 */
@RestController
@Api(value = "快递数据相关控制类",  tags = "快递数据相关控制类")
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
    @ApiOperation(value = "获取token", notes = "获取token")
    public Object getToken(@RequestBody User user, HttpServletRequest request) {
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
    @ApiOperation(value = "上传快递数据", notes = "上传快递数据")
    public Object dataUpload(@RequestBody UploadDataInfo uploadDataInfo) {
        System.out.println( "=================>快递数据上传开始:"+ DateUtil.toString( new Date(),DateUtil.DATE_LONG ) );
        Object obj = null;
        try {
            obj = basisService.dataUpload(uploadDataInfo,ParamEnum.uploadUrl.dataUpload.getCode());
        }catch (Exception e) {
            System.out.println( "=================>快递数据上传结束:"+ DateUtil.toString( new Date(),DateUtil.DATE_LONG ) );
            if(e.getMessage().indexOf( "PRIMARY" ) > -1){
                return new UploadDataResult( ParamEnum.resultCode.paramError.getCode(),  ParamEnum.resultCode.paramError.getName(), new StringBuilder().append( "该 " ).append( uploadDataInfo.getSerial() ).append( " serial(流水号) 已存在。" ).toString() );
            }else{
                logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
                return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            }
        }
        System.out.println( "=================>快递数据上传结束:"+ DateUtil.toString( new Date(),DateUtil.DATE_LONG ) );
        return obj;
    }


    /**
     * @description  上传数据
     * @return  实体对象
     * @date  2020-07-10 14:43:44
     * @author  wanghb
     * @edit
     */
    @PostMapping("/expressStaffDataUpload")
    @ResponseBody
    @ApiOperation(value = "上传快递公司数据", notes = "上传快递公司数据")
    public Object expressStaffUpload(@RequestBody UploadDataInfo expressStaff) {
        System.out.println( "=================>快递网点数据上传开始:"+ DateUtil.toString( new Date(),DateUtil.DATE_LONG ) );
        Object obj = null;
        try {
            obj = basisService.dataUpload(expressStaff,ParamEnum.uploadUrl.expressStaffDataUpload.getCode());
        }catch (Exception e) {
            System.out.println( "=================>快递网点数据上传结束:"+ DateUtil.toString( new Date(),DateUtil.DATE_LONG ) );
            if(e.getMessage().indexOf( "PRIMARY" ) > -1){
                return new UploadDataResult( ParamEnum.resultCode.paramError.getCode(),  ParamEnum.resultCode.paramError.getName(), new StringBuilder().append( "该 " ).append( expressStaff.getSerial() ).append( " serial(流水号) 已存在。" ).toString() );
            }else{
                logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
                return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            }
        }
        System.out.println( "=================>快递网点数据上传结束:"+ DateUtil.toString( new Date(),DateUtil.DATE_LONG ) );
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
    public UploadDataResult updateStatus(@RequestBody PackageSerialInfo packageSerialParam){
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
