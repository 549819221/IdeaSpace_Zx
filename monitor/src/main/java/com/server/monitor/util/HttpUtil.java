package com.server.monitor.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.server.monitor.dynamicQuartz.MonitorJob;
import com.server.monitor.service.impl.BasisServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

/**
 * Httpclient工具类
 * @author zyq
 * @date 2019-04-22
 */
public class HttpUtil {
    private static Logger logger = Logger.getLogger( HttpUtil.class );
    private static final int BYTE_LEN = 102400; // 100KB
    private static final String CHARSET = "UTF-8";  // 编码格式

    /**
     * @description  get请求
     * @param  url  请求地址（get请求时参数自己组装到url上）
     * @param  params  url
     * @return  响应文本
     * @date  20/07/10 16:33
     * @author  wanghb
     * @edit
     */
    public static Map<String, Object> get(String url,Map<String, Object> params)throws IOException{
        if(params != null && params.size() > 0){
            url += "?"+getUrlParamsByMap( params );
        }
        logger.info("get请求路径===>"+url);
        logger.info("get请求参数===>"+JSON.toJSONString( params ));
        // 请求地址，以及参数设置
        HttpGet get = new HttpGet(url);
        // 执行请求，获取相应
        String result = getRespString( get);
        Map<String, Object> resultMaps = (Map) JSON.parse(result);
        logger.info("get返回结果===>"+resultMaps);
        return resultMaps;
    }
    /**
     * @description  get请求
     * @param  url  请求地址（get请求时参数自己组装到url上）
     * @param  params  url
     * @return  响应文本
     * @date  20/07/10 16:33
     * @author  wanghb
     * @edit
     */
    public static Map<String, Object> get(String url,Map<String, Object> params,Integer connectTimeout) throws IOException {
        if(params != null && params.size() > 0){
            url += "?"+getUrlParamsByMap( params );
        }
        //logger.info("get请求路径===>"+url);
        //logger.info("get请求参数===>"+JSON.toJSONString( params ));
        // 请求地址，以及参数设置
        HttpGet get = new HttpGet(url);
        if(connectTimeout != null){
            connectTimeout = connectTimeout * 1000;
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout).build();
            get.setConfig(requestConfig);
        }
        // 执行请求，获取相应
        String result = getRespString( get);
        Map<String, Object> resultMaps = (Map) JSON.parse(result);
        //logger.info("get返回结果===>"+resultMaps);
        return resultMaps;
    }

    /**
     * @description  post请求
     * @param  urlPath  url地址
     * @param  params  参数
     * @return  返回结果
     * @date  20/07/10 16:35
     * @author  wanghb
     * @edit
     */
    public static Map<String, Object> post(String urlPath,Object params) throws IOException {
        Boolean isPrintln = !urlPath.contains( BasisServiceImpl.logSaveUrL );
        if(isPrintln){
            logger.info("post请求路径===>"+urlPath);
            logger.info("post请求参数===>"+JSON.toJSONString( params ));
        }
        String Json = JSON.toJSONString( params );
        String result = "";
        BufferedReader reader = null;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("accept", "application/json");
            byte[] writebytes = Json.getBytes();
            conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
            OutputStream outwritestream = conn.getOutputStream();
            outwritestream.write(Json.getBytes());
            outwritestream.flush();
            outwritestream.close();
            if (conn.getResponseCode() == 200) {
                reader = new BufferedReader(new InputStreamReader( conn.getInputStream()));
                result = reader.readLine();
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Map<String, Object> resultMaps = (Map)JSON.parse(result);
        if(isPrintln) {
            logger.info( "post返回结果===>" + resultMaps );
        }
        return resultMaps;
    }

    /**
     * @description  将map转换成url
     * @param  map  参数
     * @return  返回结果
     * @date  20/07/10 16:33
     * @author  wanghb
     * @edit
     */
    public static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }

    /**
     * @description  获取响应信息（String）
     * @param  request
     * @return  返回结果
     * @date  20/07/10 16:34
     * @author  wanghb
     * @edit
     */
    public static String getRespString(HttpUriRequest request) throws IOException {
        // 获取响应流
        InputStream in = getRespInputStream(request);
        if (in == null){
            return "";
        }
        // 流转字符串
        StringBuffer sb = new StringBuffer();
        byte[]b = new byte[BYTE_LEN];
        int len = 0;
        while ((len = in.read(b)) != -1) {
            sb.append(new String(b, 0, len, CHARSET));
        }
        return sb.toString();
    }


    /**
     * @description  获取响应信息（InputStream）
     * @param  request
     * @return  返回结果
     * @date  20/07/10 16:34
     * @author  wanghb
     * @edit
     */
    public static InputStream getRespInputStream(HttpUriRequest request) throws IOException {
        // 获取响应对象
        HttpResponse response = null;
        response = HttpClients.createDefault().execute(request);
        if (response == null) {
            return null;
        }
        // 获取Entity对象
        HttpEntity entity = response.getEntity();
        // 获取响应信息流
        InputStream in = null;
        if (entity != null) {
            in =  entity.getContent();
        }
        return in;
    }

}