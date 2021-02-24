package com.server.hostel.util;
import com.alibaba.fastjson.JSON;
import com.server.hostel.entity.UploadDataInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class FastDFSClient {

    private static TrackerClient trackerClient = null;
    private TrackerServer trackerServer = null;
    private StorageServer storageServer = null;
    private StorageClient1 storageClient = null;


    public FastDFSClient(String conf) throws Exception {
        /*if (conf.contains("classpath:")) {
            conf = conf.replace("classpath:", this.getClass().getResource("/").getPath());
        }*/
        ClientGlobal.init(conf);
        if (trackerClient == null) {
            trackerClient = new TrackerClient();
        }
        trackerServer = trackerClient.getConnection();
        storageServer = null;
        storageClient = new StorageClient1(trackerServer, storageServer);
    }

    /**
     * @description  上传文件方法
     * @param fileName 文件全路径
     * @param extName 文件扩展名，不包含（.）
     * @param metas 文件扩展信息
     * @return  返回结果
     * @date  20/08/14 15:50
     * @author  wanghb
     * @edit
     */
    public String uploadFile(String fileName, String extName, NameValuePair[] metas) throws Exception {
        String result = storageClient.upload_file1(fileName, extName, metas);
        return result;
    }

    /**
     * @description  上传文件方法
     * @param fileName 文件全路径
     * @return  返回结果
     * @date  20/08/14 15:50
     * @author  wanghb
     * @edit
     */
    public String uploadFile(String fileName) throws Exception {
        return uploadFile(fileName, null, null);
    }

    /**
     * @description  上传文件方法
     * @param fileName 文件全路径
     * @param extName 文件扩展名，不包含（.）
     * @return  返回结果
     * @date  20/08/14 15:50
     * @author  wanghb
     * @edit
     */
    public String uploadFile(String fileName, String extName) throws Exception {
        return uploadFile(fileName, extName, null);
    }

    /**
     * @description  上传文件方法
     * @param  fileContent  文件内容，字节数组
     * @param  extName  文件扩展名
     * @param  metas  文件扩展信息
     * @return  返回结果
     * @date  20/08/14 15:50
     * @author  wanghb
     * @edit
     */
    public String uploadFile(byte[] fileContent, String extName, NameValuePair[] metas) throws Exception {
        String result = storageClient.upload_file1(fileContent, extName, metas);
        return result;
    }

    /**
     * @description  上传文件方法
     * @param fileContent 文件内容，字节数组
     * @return  返回结果
     * @date  20/08/14 15:50
     * @author  wanghb
     * @edit
     */
    public String uploadFile(byte[] fileContent) throws Exception {
        String path = uploadFile(fileContent, null, null);
        if (trackerServer != null) {
            trackerServer.close();
            trackerServer = null;

        }
        if (storageServer != null) {
            storageServer.close();
            storageServer = null;
        }
        return path;
    }

    /**
     * @description  上传文件方法
     * @param fileContent 文件内容，字节数组
     * @param  extName  文件扩展名
     * @return  返回结果
     * @date  20/08/14 15:50
     * @author  wanghb
     * @edit
     */
    public String uploadFile(byte[] fileContent, String extName) throws Exception {
        return uploadFile(fileContent, extName, null);
    }

    /**
     * @description  文件下载
     * @param fastDFSPath 文件地址
     * @param savePath 本地保存地址
     * @return  返回结果
     * @date  20/08/14 15:49
     * @author  wanghb
     * @edit
     */
    public  void download(String fastDFSPath,String savePath) throws Exception{
        byte[] bytes = storageClient.download_file1(fastDFSPath);
        IOUtils.write(bytes,new FileOutputStream(savePath));
    }
    /**
     * @description  文件下载
     * @param fastDFSPath 文件地址
     * @return  返回结果
     * @date  20/08/14 15:49
     * @author  wanghb
     * @edit
     */
    public  byte[] download(String fastDFSPath) throws Exception{
        byte[] bytes = storageClient.download_file1(fastDFSPath);
        return bytes;
    }

    /**
     * @description  文件删除
     * @param  fastDFSPath  fastDFS的文件的地址
     * @return  返回结果
     * @date  20/08/14 15:49
     * @author  wanghb
     * @edit
     */
    public Boolean deleteFile(String fastDFSPath){
        try {
            int i = storageClient.delete_file1(fastDFSPath);
            return i==0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @description  获取文件信息
     * @param  fastDFSPath  fastDFS的文件的地址
     * @return  返回结果
     * @date  20/08/14 15:49
     * @author  wanghb
     * @edit
     */
    public String getFileInfo(String fastDFSPath){
        try {
            FileInfo fileInfo = storageClient.get_file_info1(fastDFSPath);
            String sourceIpAddr = fileInfo.getSourceIpAddr();//文件IP地址
            long fileSize = fileInfo.getFileSize();//文件大小
            Date createTimestamp = fileInfo.getCreateTimestamp();//文件创建时间
            long crc32 = fileInfo.getCrc32();//错误码
            return fastDFSPath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void main(String[] args) {
        try {
           /* File file = new File("C:\\Users\\Administrator\\Desktop\\图片.png");
            InputStream inputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), inputStream);

            //获取当前文件的全路径加名称
            String originalFilename = multipartFile.getOriginalFilename();
            //获取当前上传文件的扩展名
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);*/
            //创建FastDFS的客户端
            //FastDFSClient fastDFSClient =new FastDFSClient("classpath:fdfs_client.conf");
            //获取上传数据的二进制字节码，以扩展名为extName的格式存在文件服务器，返回该文件在文件服务器的路径
            //String fastDFSPath = fastDFSClient.uploadFile(multipartFile.getBytes(), extName);
            //group1/M00/00/02/b-W4ZF82P7eACN6eAAHyc7Xu0aU9374580
            //byte[] download = fastDFSClient.download( "group1/M00/00/02/b-W4ZF82R3yABr1yAAAA0KyrcOA3279229" );
            //UploadDataInfo bean = JSON.parseObject(new String(download, "UTF-8"), UploadDataInfo.class);
            //System.out.println(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
