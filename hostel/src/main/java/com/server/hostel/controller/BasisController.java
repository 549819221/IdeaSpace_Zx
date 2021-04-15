package com.server.hostel.controller;

import com.server.hostel.entity.*;
import com.server.hostel.entity.PackageSerialInfo;
import com.server.hostel.entity.UploadDataInfo;
import com.server.hostel.entity.UploadDataResult;
import com.server.hostel.entity.UploadDataSm2Info;
import com.server.hostel.service.impl.BasisService;
import com.server.hostel.util.DateUtil;
import com.server.hostel.util.ExceptionUtil;
import com.server.hostel.util.ParamEnum;
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
import java.util.Map;

/**
 * (Basis)表控制层
 * @author wanghb
 * @since 2020-07-10 14:43:44
 */
@RestController
@Api(value = "旅馆数据相关控制类",  tags = "旅馆数据相关控制类")
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
     * @description  获取
     * @return  实体对象
     * @date  2020-07-10 14:43:44
     * @author  wanghb
     * @edit
     */
    @PostMapping("/getPublicKey")
    @ResponseBody
    @ApiOperation(value = "获取公钥方法", notes = "获取公钥方法")
    public Object getPublicKey(@RequestBody Map<String, Object> params){
        Object returnObj = basisService.getPublicKey(params);
        return returnObj;
    }


    /**
     * @description  华住集团数据上传
     * @return  实体对象
     * @date  2020-07-10 14:43:44
     * @author  wanghb
     * @edit
     */
    @PostMapping("/dataUploadSm2")
    @ResponseBody
    @ApiOperation(value = "数据上传Sm2加密", notes = "数据上传Sm2加密")
    public Object dataUploadSm2(@RequestBody UploadDataSm2Info uploadDataSm2Info) {
        //logger.info( new StringBuilder( "旅馆数据流水号=====================>" ).append( uploadDataSm2Info.getSerial() ).toString() );
        Date startDate = new Date();
        Object obj = null;
        try {
            obj = basisService.dataUploadSm2(uploadDataSm2Info);
        }catch (Exception e) {
            Date endDate = new Date();
            System.out.println("=================>共耗时"+(endDate.getTime() - startDate.getTime())+"毫秒");
            if(e.getMessage().indexOf( "PRIMARY" ) > -1){
                return new UploadDataResult( ParamEnum.resultCode.paramError.getCode(),  ParamEnum.resultCode.paramError.getName(), new StringBuilder().append( "该 " ).append( uploadDataSm2Info.getSerial() ).append( " serial(流水号) 已存在。" ).toString() );
            }else{
                logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
                return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            }
        }
        Date endDate = new Date();
        System.out.println("=================>本次共耗时"+(endDate.getTime() - startDate.getTime())+"毫秒");
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
    public UploadDataResult updateStatus(@RequestBody PackageSerialLgInfo packageSerialParam){
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
