package com.github.binarywang.demo.wechat.pic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static com.github.binarywang.demo.wechat.utils.ImageSpider.url_final;

public class EncodeImgZingLogo {
    /**
     * 二维码绘制logo
     * @param twodimensioncodeImg 二维码图片文件
     * @param logoImg logo图片文件
     * */
    public static BufferedImage encodeImgLogo(File twodimensioncodeImg, File logoImg){
        BufferedImage twodimensioncode = null;
        try{
            if(!twodimensioncodeImg.isFile() || !logoImg.isFile()){
                System.out.println("输入非图片");
                return null;
            }
            //读取二维码图片
            twodimensioncode = ImageIO.read(twodimensioncodeImg);
            //读取logo图片
            BufferedImage logo = ImageIO.read(logoImg);
            //获取画笔
            Graphics2D g = twodimensioncode.createGraphics();
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(0.8f));
            //设置二维码大小，太大，会覆盖二维码，此处20%
            int logoWidth = logo.getWidth(null) > twodimensioncode.getWidth()*2 /10 ? (twodimensioncode.getWidth()*2 /10) : logo.getWidth(null);
            int logoHeight = logo.getHeight(null) > twodimensioncode.getHeight()*2 /10 ? (twodimensioncode.getHeight()*2 /10) : logo.getHeight(null);
            //设置logo图片放置位置
            //中心
            int x = (twodimensioncode.getWidth() - logoWidth) / 2;
            int y = (twodimensioncode.getHeight() - logoHeight) / 2;
            //右下角，15为调整值
//			int x = twodimensioncode.getWidth()  - logoWidth-15;
//			int y = twodimensioncode.getHeight() - logoHeight-15;
            // 在图形和图像中实现混合和透明效果
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,1.0f));
            //开始合并绘制图片
            g.drawImage(logo, x, y, logoWidth, logoHeight, null);
            g.drawRoundRect(x, y, logoWidth, logoHeight, 15 ,15);
//            //logo边框大小
//            g.setStroke(new BasicStroke(2));
//            //logo边框颜色
//            g.drawRect(x, y, logoWidth, logoHeight);
            g.dispose();
            logo.flush();
            twodimensioncode.flush();
        }catch(Exception e){
            System.out.println("二维码绘制logo失败");
        }
        return twodimensioncode;
    }

    /**
     * 二维码输出到文件
     * @param twodimensioncodeImg 二维码图片文件
     * @param logoImg logo图片文件
     * @param format 图片格式
     * @param file 输出文件
     * */
    public void writeToFile(File twodimensioncodeImg,File logoImg,String format,File file){
        BufferedImage image = encodeImgLogo(twodimensioncodeImg, logoImg);
        try {
            ImageIO.write(image, format, file);
        } catch (IOException e) {
            System.out.println("二维码写入文件失败"+e.getMessage());
        }
    }
    /**
     * 二维码流式输出
     * @param twodimensioncodeImg 二维码图片文件
     * @param logoImg logo图片文件
     * @param format 图片格式
     * @param stream 输出流
     * */
    public  void writeToStream(File twodimensioncodeImg,File logoImg,String format,OutputStream stream){
        BufferedImage image = encodeImgLogo(twodimensioncodeImg, logoImg);
        try {
            ImageIO.write(image, format, stream);
        } catch (IOException e) {
            System.out.println("二维码写入流失败"+e.getMessage());
        }
    }

    /**
     * @Title: 构造图片
     * @Description: 生成水印并返回java.awt.image.BufferedImage
     * @param file 源文件(图片)
//     * @param waterFile 水印文件(图片)
//     * @param x 距离右下角的X偏移量
//     * @param y 距离右下角的Y偏移量
//     * @param alpha 透明度, 选择值从0.0~1.0: 完全透明~完全不透明
     * @return BufferedImage
     * @throws IOException
     */

    public BufferedImage water(File file,File logFile) throws IOException {
        ImgUtils imgUtils = new ImgUtils();
        // 获取二维码图
        BufferedImage buffImg = ImageIO.read(file);
        buffImg = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_sRGB), null).filter(buffImg, null);
        // 获取Log层图
        BufferedImage logImg = ImageIO.read(logFile);
        // 创建Graphics2D对象，用在底图对象上绘图
        Graphics2D g2d = buffImg.createGraphics();
        //设置颜色和画笔粗细
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(0.8f));
        g2d.setFont(new Font("微软雅黑", Font.BOLD, 29));
        //设置二维码大小，太大，会覆盖二维码，此处20%
        int logoWidth = logImg.getWidth(null) > buffImg.getWidth()*2 /10 ? (buffImg.getWidth()*2 /10) : logImg.getWidth(null);
        int logoHeight = logImg.getHeight(null) > buffImg.getHeight()*2 /10 ? (buffImg.getHeight()*2 /10) : logImg.getHeight(null);
        try {
            File file1=new File(url_final+"log.png");
            imgUtils.resizeImage(file1, new File(url_final+"log.png"), logoWidth, logoHeight);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //设置logo图片放置位置
        //中心
        int m = (buffImg.getWidth() - logoWidth) / 2;
        int n = (buffImg.getHeight() - logoHeight) / 2;
        //获取二维码层图宽度和高度
        int waterImgWidth =logImg.getWidth();// 获取层图的宽度
        int waterImgHeight =logImg.getHeight();// 获取层图的高度
        // 在图形和图像中实现混合和透明效果
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,1.0f));
        // 绘制二维码
        g2d.drawImage(logImg,m, n, logoWidth, logoHeight,null);
        g2d.dispose();// 释放图形上下文使用的系统资源
        return buffImg;
    }

//    public BufferedImage toGray(BufferedImage srcImg){
//        return new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_sRGB), null).filter(srcImg, null);
//    }


    /**
     * 输出水印图片
     *
     * @param buffImg 图像加水印之后的BufferedImage对象
     * @param savePath 图像加水印之后的保存路径
     */

    public void generateWater(BufferedImage buffImg, String savePath) {
        int temp =savePath.lastIndexOf(".") + 1;
        try {
            ImageIO.write(buffImg, savePath.substring(temp), new File(savePath));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
