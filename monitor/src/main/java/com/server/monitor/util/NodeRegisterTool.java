package com.server.monitor.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NodeRegisterTool {
    public static String getObjId(String source) throws NoSuchAlgorithmException {
        return md532(source);
    }

    private static String md532(String source) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] sourceBytes = source.getBytes( StandardCharsets.UTF_8);
        byte[] resultBytes = messageDigest.digest(sourceBytes);

        StringBuilder builder = new StringBuilder();
        for (byte b : resultBytes) {
            int val = b & 0xff;
            if (val < 16) {
                builder.append("0");
            }
            builder.append(Integer.toHexString(val));
        }
        return builder.toString();
    }
}