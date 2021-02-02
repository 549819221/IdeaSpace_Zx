package com.server.fastdfstest.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtil {
    //String --> File
    public File toFile(String str) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());

        toFile( stream,new File( "" ) );
        return null;
    }

    //String --> InputStream
    public InputStream toInputStream(String str){
        ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
        return stream;
    }

    //InputStream --> String
    String toString(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null){
            buffer.append(line);
        }
        return buffer.toString();
    }

    //InputStream --> String
    String toString(OutputStream is) throws IOException {
        return  new String( new ByteArrayOutputStream(12).toByteArray(), StandardCharsets.UTF_8);

    }


    //File --> InputStream
    public InputStream toInputStream(File file) throws FileNotFoundException {
        InputStream in = new FileInputStream( file );
        return in;
    }


    //InputStream --> File
    public void toFile(InputStream ins,File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }
}
