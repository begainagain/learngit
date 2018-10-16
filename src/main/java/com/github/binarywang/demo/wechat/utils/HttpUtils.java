package com.github.binarywang.demo.wechat.utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.log4j.Logger;


public class HttpUtils{
    private static Logger log = Logger.getLogger(HttpUtils.class);
    /**
     * 发送https请求，返回二维码图片
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param data 提交的数据
     * @param savePath 图片保存路径
     * @param fileName 图片名称
     * @param fileType 图片类型
     */
    public static void httpsRequestPicture(String requestUrl,String requestMethod,String data,String savePath,String fileName,String fileType){
        InputStream inputStream=null;
        try{
        URL url=new URL(requestUrl);
        HttpsURLConnection conn=(HttpsURLConnection)url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        //设置请求方式（GET/POST）
        conn.setRequestMethod(requestMethod);
        conn.connect();
        //当data不为null时向输出流写数据
        if(null!=data){
        //getOutputStream方法隐藏了connect()方法
        OutputStream outputStream=conn.getOutputStream();
        //注意编码格式
        outputStream.write(data.getBytes("UTF-8"));
        outputStream.close();
        }
        // 从输入流读取返回内容
        inputStream=conn.getInputStream();
        log.info("开始生成微信二维码...");
        WXPayUtils.inputStreamToMedia(inputStream,savePath,fileName,fileType);
        log.info("微信二维码生成成功!!!");
        conn.disconnect();
        }catch(Exception e){
        log.error("发送https请求失败，失败",e);
        }finally{
        //释放资源
        try{
        if(null!=inputStream){
        inputStream.close();
        }
        }catch(IOException e){
        log.error("释放资源失败，失败",e);
        }
        }
        }
        }