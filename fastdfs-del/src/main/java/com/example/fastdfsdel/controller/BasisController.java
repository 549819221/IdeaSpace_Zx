package com.example.fastdfsdel.controller;

import com.example.fastdfsdel.entity.PackageSerialInfo;
import com.example.fastdfsdel.entity.UploadDataInfo;
import com.example.fastdfsdel.entity.UploadDataResult;
import com.example.fastdfsdel.entity.User;
import com.example.fastdfsdel.service.BasisService;
import com.example.fastdfsdel.util.ExceptionUtil;
import com.example.fastdfsdel.util.ParamEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "快递数据相关控制类",  tags = "快递数据相关控制类")
@RequestMapping("/basis")
public class BasisController  {
    private static Logger logger = Logger.getLogger( BasisController.class );
    @Resource
    private BasisService basisService;


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
    public com.example.fastdfsdel.entity.UploadDataResult updateStatus(@RequestBody PackageSerialInfo packageSerialParam){
        try {
            return basisService.updateStatus(packageSerialParam);
        } catch (Exception e) {
            logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            return new com.example.fastdfsdel.entity.UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
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
    public com.example.fastdfsdel.entity.UploadDataResult reUploadFtp(@RequestParam(name = "serial",  required = true) String serial){
        try {
            return basisService.reUploadFtp(serial);
        } catch (Exception e) {
            logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
        }
    }
}
