package com.example.fastdfsdel.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExceptionUtil {

    /**
     * catch中获取异常信息
     *
     * @param e Exception类型
     * @return String类型
     * @author wanghb
     * @date 2019-02-27
     */
    public static String getOutputStream(Exception e)
    {
        e.printStackTrace();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(baos));
        return baos.toString();
    }
}
