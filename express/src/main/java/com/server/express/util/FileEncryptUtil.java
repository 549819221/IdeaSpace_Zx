package com.server.express.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.unzip.UnzipUtil;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.net.ftp.FTP;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileEncryptUtil {
    /**
     * @Title: encrypt_zip
     * @Description:将指定路径下的文件压缩至指定zip文件，并以指定密码加密,若密码为空，则不进行加密保护
     * @param src_file 待压缩文件路径
     * @param dst_file zip路径+文件名
     * @param encode 加密密码
     * @return
     */
    public static void encryptZip(String src_file, String dst_file, String encode) {
        File file = new File(src_file);
        ZipParameters parameters = new ZipParameters();
        //压缩方式
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        //压缩级别
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        parameters.setEncryptFiles(true);
        //加密方式
        parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
        //设置密码
        parameters.setPassword(encode.toCharArray());
        try {
            ZipFile zipFile = new ZipFile(dst_file);
            zipFile.addFile(file, parameters);
        }catch (ZipException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Title: encryptStreamZip
     * @Description:将指定路径下的文件压缩至指定zip文件，并以指定密码加密,若密码为空，则不进行加密保护
     * @param tempFile 待压缩文件路径
     * @param zipFile zip路径+文件名
     * @param encode 加密密码
     * @return
     */
    public static void encryptStreamZip(File tempFile, File zipFile, String encode) throws IOException, ZipException {
        ArrayList filesToAdd = new ArrayList();
        filesToAdd.add(tempFile);

        ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(zipFile));

        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        // Set password
        parameters.setEncryptFiles(true);
        parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
        parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
        parameters.setPassword(encode);

        for (int i = 0; i < filesToAdd.size(); i++) {
            File file = (File)filesToAdd.get(i);
            outputStream.putNextEntry(file,parameters);

            if (file.isDirectory()) {
                outputStream.closeEntry();
                continue;
            }

            //InputStream  inputStream = new FileInputStream(file);
            byte[] readBuff = new byte[4096];
            int readLen = -1;
            /*while ((readLen = inputStream.read(readBuff)) != -1) {
                outputStream.write(readBuff, 0, readLen);
            }*/
            outputStream.closeEntry();
            //inputStream.close();
        }
        outputStream.finish();
        outputStream.close();
    }

    public static String getPackageSerialInfo(File tempFile) throws ZipException {
        ZipFile zipFile = new ZipFile(tempFile);
        if (zipFile.isEncrypted()) {
            zipFile.setPassword("123456");
        }
        File file = zipFile.getFile();
        String encoding = "UTF-8";
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }

    }



    public static void main(String[] args) throws IOException {
        /*Random random=new Random();//创建random实例对象，程序中会用，用于产生随机数
        JSONArray jsonArray=new JSONArray();//创建JSONArray对象
        File file = new File("C:\\Users\\Administrator\\Desktop\\Test.json");
        if(!file.exists())//判断文件是否存在，若不存在则新建
        {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream=new FileOutputStream(file);//实例化FileOutputStream
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream,"utf-8");//将字符流转换为字节流
        BufferedWriter bufferedWriter= new BufferedWriter(outputStreamWriter);//创建字符缓冲输出流对象

        for(int i=0;i<5;i++){
            JSONObject jsonObject=new JSONObject();//创建JSONObject对象
            jsonObject.put("Num",random.nextInt(100)+1);//产生1-100的随机数
            jsonObject.put("age",random.nextInt(8)+18);//产生18-25的随机数
            jsonObject.put("Goal",random.nextInt(41)+60);//产生60-100的随机数
            jsonArray.add(jsonObject);//将jsonObject对象旁如jsonarray数组中
        }
        String jsonString = jsonArray.toString();//将jsonarray数组转化为字符串
        bufferedWriter.write(jsonString);//将格式化的jsonarray字符串写入文件
        bufferedWriter.flush();//清空缓冲区，强制输出数据
        bufferedWriter.close();//关闭输出流*/

        //encryptZip("C:\\Users\\Administrator\\Desktop\\test.txt","C:\\Users\\Administrator\\Desktop\\test.zip","123456");
    }


}
