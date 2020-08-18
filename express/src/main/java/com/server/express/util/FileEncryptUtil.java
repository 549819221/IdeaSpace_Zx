package com.server.express.util;

import io.swagger.annotations.ApiOperation;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileEncryptUtil {

    /**
     * @Description 将指定路径下的文件压缩至指定zip文件，并以指定密码加密,若密码为空，则不进行加密保护
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
     * @Description 将指定路径下的文件压缩至指定zip文件，并以指定密码加密,若密码为空，则不进行加密保护
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
            InputStream  inputStream = new FileInputStream(file);
            byte[] readBuff = new byte[4096];
            int readLen = -1;
            while ((readLen = inputStream.read(readBuff)) != -1) {
                outputStream.write(readBuff, 0, readLen);
            }
            outputStream.closeEntry();
            inputStream.close();
        }
        outputStream.finish();
        outputStream.close();
    }


    /**
     * @description  解析压缩文件
     * @param  tempFile  临时文件
     * @return  返回结果
     * @date  20/08/18 16:15
     * @author  wanghb
     * @edit
     */
    public static String getPackageSerialInfo(File tempFile) throws ZipException {
        long startTime = System.currentTimeMillis();
        ZipFile zipFile2 = new ZipFile(tempFile);
        //设置编码格式
        zipFile2.setFileNameCharset("GBK");
        if (!zipFile2.isValidZipFile()) {
            throw new ZipException("文件不合法或不存在");
        }
        //检查是否需要密码
        if (zipFile2.isEncrypted()) {
            zipFile2.setPassword("123456");
        }
        List<FileHeader> fileHeaderList = zipFile2.getFileHeaders();
        for (int i = 0; i < fileHeaderList.size(); i++) {
            FileHeader fileHeader = fileHeaderList.get(i);
            BufferedReader reader = new BufferedReader(new InputStreamReader(zipFile2.getInputStream(fileHeader)));
            String line;
            try {
                while ( ( line = reader.readLine() ) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("解压成功！");
        long endTime = System.currentTimeMillis();
        System.out.println("耗时：" + (endTime - startTime) + "ms");
        return "";

    }


}
