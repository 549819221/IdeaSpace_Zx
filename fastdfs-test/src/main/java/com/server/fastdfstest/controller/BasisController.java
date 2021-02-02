package com.server.fastdfstest.controller;

import com.server.fastdfstest.entity.PackageSerialInfo;
import com.server.fastdfstest.entity.UploadDataInfo;
import com.server.fastdfstest.entity.UploadDataResult;
import com.server.fastdfstest.entity.User;
import com.server.fastdfstest.service.BasisService;
import com.server.fastdfstest.util.ExceptionUtil;
import com.server.fastdfstest.util.ParamEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
     * @description  上传快递数据
     * @return  实体对象
     * @date  2020-07-10 14:43:44
     * @author  wanghb
     * @edit
     *//**/
    @PostMapping("/dataUpload")
    @ResponseBody
    @ApiOperation(value = "上传快递数据", notes = "上传快递数据")
    public Object dataUpload(@RequestBody UploadDataInfo uploadDataInfo) {
        logger.info( new StringBuilder( "上传快递数据流水号=====================>" ).append( uploadDataInfo.getSerial() ).toString() );
        Date startDate = new Date();
        //System.out.println( "=================>快递数据上传开始:"+ DateUtil.toString( startDate,DateUtil.DATE_LONG ) );
        Object obj = null;
        try {
            obj = basisService.dataUpload(uploadDataInfo, ParamEnum.uploadUrl.dataUpload.getCode());
        }catch (Exception e) {
            Date endDate = new Date();
            //System.out.println( "=================>快递数据上传结束:"+ DateUtil.toString(endDate,DateUtil.DATE_LONG ) );
            System.out.println("=================>共耗时"+(endDate.getTime() - startDate.getTime())+"毫秒");
            if(e.getMessage().indexOf( "PRIMARY" ) > -1){
                return new UploadDataResult( ParamEnum.resultCode.paramError.getCode(),  ParamEnum.resultCode.paramError.getName(), new StringBuilder().append( "该 " ).append( uploadDataInfo.getSerial() ).append( " serial(流水号) 已存在。" ).toString() );
            }else{
                logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
                return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            }
        }
        Date endDate = new Date();
        //System.out.println( "=================>快递数据上传结束:"+ DateUtil.toString( endDate,DateUtil.DATE_LONG ) );
        System.out.println("=================>本次共耗时"+(endDate.getTime() - startDate.getTime())+"毫秒");
        return obj;
    }




    /**
     * @description  上传数据
     * @return  实体对象
     * @date  2020-07-10 14:43:44
     * @author  wanghb
     * @edit
     */
    @PostMapping("/dataUploadThread")
    @ResponseBody
    @ApiOperation(value = "上传快递公司数据", notes = "上传快递公司数据")
    public Object expressStaffUpload(@RequestBody UploadDataInfo expressStaff) {
        logger.info( new StringBuilder( "上传快递公司数据流水号=====================>" ).append( expressStaff.getSerial() ).toString() );
        Date startDate = new Date();
        //System.out.println( "=================>快递网点数据上传开始:"+ DateUtil.toString( startDate,DateUtil.DATE_LONG ) );
        Object obj = null;
        try {
            obj = basisService.dataUploadThread(expressStaff,ParamEnum.uploadUrl.expressStaffDataUpload.getCode());
        }catch (Exception e) {
            Date endDate = new Date();
            //System.out.println( "=================>快递网点数据上传结束:"+ DateUtil.toString( endDate,DateUtil.DATE_LONG ) );
            System.out.println("=================>共耗时"+(endDate.getTime() - startDate.getTime())+"毫秒");
            if(e.getMessage().indexOf( "PRIMARY" ) > -1){
                return new UploadDataResult( ParamEnum.resultCode.paramError.getCode(),  ParamEnum.resultCode.paramError.getName(), new StringBuilder().append( "该 " ).append( expressStaff.getSerial() ).append( " serial(流水号) 已存在。" ).toString() );
            }else{
                logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
                return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            }
        }
        Date endDate = new Date();
        //System.out.println( "=================>快递网点数据上传结束:"+ DateUtil.toString(endDate,DateUtil.DATE_LONG ) );
        System.out.println("=================>本次共耗时"+(endDate.getTime() - startDate.getTime())+"毫秒");
        return obj;
    }


    /**
     * @description  缓存数据处理
     * @return  实体对象
     * @date  2020-07-10 14:43:44
     * @author  wanghb
     * @edit
     */
    @GetMapping("/insert")
    @ResponseBody
    @ApiOperation(value = "缓存数据处理", notes = "缓存数据处理")
    public UploadDataResult insert(){
        try {
            return basisService.insert();
        } catch (Exception e) {
            logger.error( new StringBuilder( "程序异常,异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
            return new UploadDataResult( ParamEnum.resultCode.error.getCode(),  "程序异常", new StringBuilder("异常信息:" ).append( ExceptionUtil.getOutputStream( e ) ).toString() );
        }
    }


}

