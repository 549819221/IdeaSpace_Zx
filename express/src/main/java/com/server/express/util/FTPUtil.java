package com.server.express.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.server.express.entity.UploadDataInfo;
import io.swagger.annotations.ApiOperation;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.net.ftp.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
@Service("fTPUtil")
@PropertySource({"classpath:application.properties"})
public class FTPUtil {

    private  FTPClient ftp;

    @Value("${ftp.ip}")
    private String ftpIp;
    @Value("${ftp.port}")
    private  Integer ftpPort;
    @Value("${ftp.username}")
    private  String ftpUsername;
    @Value("${ftp.password}")
    private  String ftpPassword;
    @Value("${zip.encode}")
    private  String zipEncode;


    private static String SERVER_CHARSET = "ISO-8859-1";
    /** 本地字符编码 */
    private static String LOCAL_CHARSET = "GBK";

    /**
     * @description  验证登录
     * @return  返回结果
     * @date  20/08/17 10:58
     * @author  wanghb
     * @edit
     */
    public  boolean login() throws IOException {
        ftp = new FTPClient();
        ftp.connect(ftpIp, ftpPort);
        boolean isLogin = ftp.login( ftpUsername, ftpPassword );
        if(!isLogin){
            return false;
        }
        // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
        if (FTPReply.isPositiveCompletion(ftp.sendCommand("OPTS UTF8", "ON"))) {
            LOCAL_CHARSET = "UTF-8";
        }
        ftp.setControlEncoding(LOCAL_CHARSET);
        return true;
    }


    /**
     * @description  向FTP服务器上传文件
     * @param  path  需要上传的路径
     * @param  file  需要上传的文件
     * @return  返回结果
     * @date  20/08/17 16:56
     * @author  wanghb
     * @edit
     */
    public boolean uploadFile(String path,File file) throws IOException{
        FileInputStream fileInputStream = null;
        try {
            if(!login()){
                return false;
            }
            //设置上传文件的类型为二进制类型
            ftp.setFileType( FTP.BINARY_FILE_TYPE);
            String[] paths = path.split("/");
            for (int i = 0; i < paths.length; i++){
                String pathTemp = paths[i];
                ftp.makeDirectory(pathTemp);
                ftp.changeWorkingDirectory(pathTemp);
            }
            String fileName = new String(file.getName().getBytes(LOCAL_CHARSET), SERVER_CHARSET );
            fileInputStream = new FileInputStream( file );
            Boolean isSuccess = ftp.storeFile(fileName, fileInputStream);
            return isSuccess;
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            closeClient();
        }
    }


    /**
     * @description  向FTP服务器下载文件
     * @param  fpath  文件路径
     * @param  localpath  本地路径
     * @return  返回结果
     * @date  20/08/17 17:01
     * @author  wanghb
     * @edit
     */
    public boolean downloadFileList(String fpath,String localpath) throws IOException{
        if(!login()){
            return false;
        }
        boolean flag = false;
        BufferedOutputStream bufferRead = null;
        if(fpath.startsWith("/") && fpath.endsWith("/")){
            try {
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                //切换到当前目录
                ftp.changeWorkingDirectory(fpath);
                ftp.enterLocalActiveMode();
                FTPFile [] ftpFiles = ftp.listFiles();
                for (FTPFile file : ftpFiles) {
                    if (file.isFile()) {
                        String fileName = new String(file.getName().getBytes(LOCAL_CHARSET),SERVER_CHARSET);
                        File localFile = new File(localpath + "/" + file.getName());
                        FileOutputStream fileOutputStream = new FileOutputStream( localFile );
                        bufferRead = new BufferedOutputStream(fileOutputStream);
                        ftp.retrieveFile(fileName, bufferRead);
                        bufferRead.flush();
                        fileOutputStream.close();
                    }
                }
                ftp.logout();
                flag = true;
            } finally{
                closeClient();
            }
        }
        return flag;
    }

    /**
     * @description  获取数据
     * @param  fpath  文件路径
     * @return  返回结果
     * @date  20/08/17 17:01
     * @author  wanghb
     * @edit
     */
    public List<UploadDataInfo> getDateList(String fpath) throws IOException, ZipException {
        List<UploadDataInfo> uploadDataInfos = new ArrayList<>();
        if(!login()){
            return uploadDataInfos;
        }
        try {
            if(fpath.startsWith("/") && fpath.endsWith("/")){
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                //切换到当前目录
                ftp.changeWorkingDirectory(fpath);
                ftp.enterLocalActiveMode();
                FTPFile [] ftpFiles = ftp.listFiles();
                for (FTPFile file : ftpFiles) {
                    BufferedOutputStream bufferRead = null;
                    FileOutputStream fileOutputStream = null;
                    if (file.isFile()) {
                        String fileName = new String(file.getName().getBytes(LOCAL_CHARSET),SERVER_CHARSET);
                        File localFile = File.createTempFile("ftp",".zip");
                        localFile.deleteOnExit();
                        fileOutputStream = new FileOutputStream( localFile );
                        bufferRead = new BufferedOutputStream( fileOutputStream );
                        ftp.retrieveFile(fileName, bufferRead);
                        bufferRead.flush();
                        String packageSerialJson = FileEncryptUtil.getPackageSerialInfo( localFile,zipEncode );
                        uploadDataInfos.add( JSON.parseObject(packageSerialJson, UploadDataInfo.class) );
                        if (bufferRead != null) {
                            bufferRead.close();
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        localFile.delete();
                    }
                }
                ftp.logout();
            }
            return uploadDataInfos;
        }finally{
            closeClient();
        }

    }

    /**
     * @description  删除ftp文件夹下面的视频文件
     * @param  path  文件路径
     * @param  name  文件名
     * @return  返回结果
     * @date  20/08/18 18:11
     * @author  wanghb
     * @edit
     */
    public boolean deleteFile(String path,String name){
        boolean isAppend = false;
        try {
            if(!login()){
                return isAppend;
            }
            path = new String(path.getBytes(LOCAL_CHARSET),SERVER_CHARSET);
            ftp.changeWorkingDirectory(path);
            name = new String(name.getBytes(LOCAL_CHARSET),SERVER_CHARSET);
            ftp.dele(name);
            ftp.logout();
            isAppend = true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            closeClient();
        }
        return isAppend;
    }


    /**
     * @description  断开与远程服务器的连接
     * @return  返回结果
     * @date  20/08/17 11:32
     * @author  wanghb
     * @edit
     */
    public void closeClient(){
        if (ftp != null && ftp.isConnected()) {
            try {
                ftp.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        //文件删除
        //fTPUtil.deleteFile( "/data", "ftp7500632415927387638.zip" );
        //文件解析
        //List<UploadDataInfo> dateList = fTPUtil.getDateList( "/test/" );
        //文件下载
        //fTPUtil.downloadFileList("/test/","C:\\Users\\Administrator\\Desktop\\");
    }

}
