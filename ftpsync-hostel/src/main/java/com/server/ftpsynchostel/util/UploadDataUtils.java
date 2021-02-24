package com.server.ftpsynchostel.util;

import com.server.ftpsynchostel.entity.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UploadDataUtils {

    /**
     * @description  上报数据方法，对外公开
     * @param  expressInfoList  寄递数据
     * @return  上传结果
     * @date  20/07/31 17:25
     * @author  wanghb
     * @edit
     */
    public static final UploadDataResult uploadData(List<ExpressInfo> expressInfoList) throws IOException {

        byte[] encryptBytes = null;
        // 生成最终的数据包
        File data = packData(encryptBytes);
        String keyId = "";
        // 生成数据包信息，数据包信息将会在数据文件上传完成之后，同步上传消息队列中
        // 之后传输到公安网，最终在公安网通过数据包信息验证数据包完整性
        UploadDataInfo uploadDataInfo = packDataInfo(encryptBytes, keyId);
        // 上传加密后的文件到FastDFS
        uploadDataFile(data);
        // 发送数据文件信息到消息队列
        sendDataInfo(uploadDataInfo);
        // 保存信息数据到数据库，方便后续对账
        saveDataInfo(uploadDataInfo);
        return null;
    }

    /**
     * @description   保存信息数据到数据库，方便后续对账
     * @param  uploadDataInfo  上传信息
     * @date  20/07/31 17:21
     * @author  wanghb
     * @edit
     */
    private static void saveDataInfo(UploadDataInfo uploadDataInfo) {

    }

    /**
     * @description  发送数据文件信息到消息队列
     * @param  uploadDataInfo  上传信息
     * @return
     * @date  20/07/31 17:21
     * @author  wanghb
     * @edit
     */
    private static void sendDataInfo(UploadDataInfo uploadDataInfo) {

    }

    /**
     * @description  上传加密后的文件到FastDFS
     * @param  data  文件
     * @return
     * @date  20/07/31 17:20
     * @author  wanghb
     * @edit
     */
    private static void uploadDataFile(File data) {

    }

    /**
     * @description  生成数据包信息，数据包信息将会在数据文件上传完成之后，同步上传消息队列中
     * 之后传输到公安网，最终在公安网通过数据包信息验证数据包完整性
     * @param  encryptBytes  加密后的字节流
     * @param  keyId
     * @return  上传信息
     * @date  20/07/31 17:19
     * @author  wanghb
     * @edit
     */
    private static UploadDataInfo packDataInfo(byte[] encryptBytes, String keyId) {
        return null;
    }

    /**
     * @description  生成最终的数据包
     * @param  encryptBytes  加密后数据
     * @return  数据包
     * @date  20/07/31 17:18
     * @author  wanghb
     * @edit
     */
    private static File packData(byte[] encryptBytes) throws IOException {
        String  fileType = "txt";
        File resultFile = File.createTempFile( UUID.randomUUID().toString(), '.' + fileType, Files.createTempDirectory("tempFile").toFile());
        resultFile.deleteOnExit();
        FileUtils.copyToFile(new ByteArrayInputStream(encryptBytes), resultFile);
        return resultFile;
    }


    /**
     * @description  加密方法
     * @param  sSrc  sSrc  内容
     * @param  encodingFormat  编码
     * @param  sKey  加密用的key
     * @return
     * @date  20/08/11 10:42
     * @author  wanghb
     * @edit
     */
    public static String encrypt(String sSrc, String encodingFormat, String sKey) throws GeneralSecurityException, UnsupportedEncodingException {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes(encodingFormat);
        byte[] ivp = raw;
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivp);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(encodingFormat));
        return Base64.encodeBase64String(encrypted);//此处使用BASE64做转码。
    }


    public static void main(String[] args) throws IOException {
        List<ExpressInfo> expressInfoList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ExpressInfo expressInfo = new ExpressInfo();
            expressInfo.setSender(new Sender());
            expressInfo.setReceiver(new Receiver());
            expressInfo.setExpressStaff(new ExpressStaff());
            expressInfo.setType(0);
            expressInfo.setExpressType(0);
            expressInfo.setExpressTime(new Date());
            expressInfo.setExpressNo("");
            expressInfo.setExpressGoodsName("");
            expressInfo.setExpressInvoicePhoto("");
            expressInfo.setExpressBoxPhoto("");
            expressInfo.setExpressLon("");
            expressInfo.setExpressLat("");
            expressInfo.setPlaceCode("");
            expressInfo.setPlaceName("");
            expressInfo.setPlaceAddress("");
            expressInfo.setPlaceBusinessLicense("");
            expressInfo.setPlaceManager("");
            expressInfo.setPlaceManagerPhone("");
            expressInfo.setPlaceManagerIdcardNo("");
            expressInfo.setLegalPersonIdcardNo("");
            expressInfo.setPoliceAreaCode("");
            expressInfo.setPoliceAreaName("");
            expressInfo.setLegalPersonName("");
            expressInfo.setLegalPersonPhone("");
            expressInfoList.add(expressInfo);

        }
        System.out.println(UUID.randomUUID().toString().replace( "-","" ));
        //uploadData(expressInfoList);
    }
}
