package com.github.binarywang.demo.wechat.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class WXPayUtils {
    /**
     * 将输入流转换为图片
     * @param input 输入流
     * @param savePath 图片需要保存的路径
     * @param fileType 图片类型
     */
    public static void inputStreamToMedia(InputStream input, String savePath, String fileName, String fileType) throws Exception {
        String filePath = savePath + "/" + fileName + "." + fileType;
        File file = new File(filePath);
        FileOutputStream outputStream = new FileOutputStream(file);
        int length;
        byte[] data = new byte[1024];
        while ((length = input.read(data)) != -1) {
            outputStream.write(data, 0, length);
        }
        outputStream.flush();
        outputStream.close();
    }
}
