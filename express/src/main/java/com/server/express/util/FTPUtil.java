package com.server.express.util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.StringTokenizer;

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

    /** 本地字符编码 */
    private static String LOCAL_CHARSET = "GBK";

    /**
     * @description  验证登录
     * @return  返回结果
     * @date  20/08/17 10:58
     * @author  wanghb
     * @edit
     */
    public  boolean login() {
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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
    public  boolean uploadFile(String path,File file) throws IOException{
        try {
            if(!login()){
                return false;
            }
            //设置上传文件的类型为二进制类型
            ftp.setFileType( FTP.BINARY_FILE_TYPE);
            String[] paths = path.split("/");
            for (int i = 0; i < paths.length; i++){
                String pathTemp = paths[i];
                System.out.println(ftp.printWorkingDirectory());
                ftp.makeDirectory(pathTemp);
                ftp.changeWorkingDirectory(pathTemp);
            }
            String fileName = new String(file.getName().getBytes("UTF-8"), FTP.DEFAULT_CONTROL_ENCODING );
            return ftp.storeFile(fileName, new FileInputStream( file ));
        } finally {
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
                        String fileName = new String(file.getName().getBytes(LOCAL_CHARSET),FTP.DEFAULT_CONTROL_ENCODING);
                        File localFile = new File(localpath + "/" + file.getName());
                        bufferRead = new BufferedOutputStream(new FileOutputStream(localFile));
                        ftp.retrieveFile(fileName, bufferRead);
                        bufferRead.flush();
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
     * @description  向FTP服务器下载文件
     * @param  fpath  文件路径
     * @return  返回结果
     * @date  20/08/17 17:01
     * @author  wanghb
     * @edit
     */
    public boolean getDateList(String fpath) throws IOException{
        if(!login()){
            return false;
        }
        boolean flag = false;

        if(fpath.startsWith("/") && fpath.endsWith("/")){
            try {
                BufferedOutputStream bufferRead = null;
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                //切换到当前目录
                ftp.changeWorkingDirectory(fpath);
                ftp.enterLocalActiveMode();
                FTPFile [] ftpFiles = ftp.listFiles();
                for (FTPFile file : ftpFiles) {
                    if (file.isFile()) {
                        String fileName = new String(file.getName().getBytes(LOCAL_CHARSET),FTP.DEFAULT_CONTROL_ENCODING);
                        System.out.println(fileName);
                        File localFile = File.createTempFile("pattern",".zip");
                        //File localFile = new File("C:\\Users\\Administrator\\Desktop\\" + file.getName());
                        bufferRead = new BufferedOutputStream(new FileOutputStream(localFile));
                        ftp.retrieveFile(fileName, bufferRead);
                        bufferRead.flush();
                        System.out.println( FileEncryptUtil.getPackageSerialInfo( localFile ) );
                    }
                }
                ftp.logout();
                flag = true;

            } catch (ZipException e) {
                e.printStackTrace();
            } finally{
                closeClient();
            }
        }
        return flag;
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

}
