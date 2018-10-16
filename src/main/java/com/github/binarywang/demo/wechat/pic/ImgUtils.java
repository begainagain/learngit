package com.github.binarywang.demo.wechat.pic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import static com.github.binarywang.demo.wechat.utils.ImageSpider.url_final;

public class ImgUtils {

    /**
     * 裁剪图片方法
     * @param bufferedImage 图像源
     * @param startX 裁剪开始x坐标
     * @param startY 裁剪开始y坐标
     * @param endX 裁剪结束x坐标
     * @param endY 裁剪结束y坐标
     * @return
     */
    public static BufferedImage cropImage(BufferedImage bufferedImage, int startX, int startY, int endX, int endY) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        if (startX == -1) {
            startX = 0;
        }
        if (startY == -1) {
            startY = 0;
        }
        if (endX == -1) {
            endX = width - 1;
        }
        if (endY == -1) {
            endY = height - 1;
        }
        BufferedImage result = new BufferedImage(endX - startX, endY - startY, 4);
        for (int x = startX; x < endX; ++x) {
            for (int y = startY; y < endY; ++y) {
                int rgb = bufferedImage.getRGB(x, y);
                result.setRGB(x - startX, y - startY, rgb);
            }
        }
        return result;
    }

    public void downloadImgURL(String url,String nickname){
        try{
            URLConnection con = new URL(url).openConnection();
            con.setConnectTimeout(5000);
            InputStream is = con.getInputStream();
            byte[] bs = new byte[1024];
            int len;
            File sf=new File(url_final+"pic/HeadImg/"+nickname+".png");

            OutputStream os = new FileOutputStream(sf);
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            os.close();
            is.close();
        } catch (IOException e) {
        }
    }
    public void circleHeadImg(String nickname){
        try {
            BufferedImage bi1 = ImageIO.read(new File(url_final+"pic/HeadImg/"+nickname+".png"));
//            BufferedImage bi1 = ImageIO.read(new File("C:/Users/admin/坚果.png"));
            // 根据需要是否使用 BufferedImage.TYPE_INT_ARGB(透明色)
            BufferedImage bi2 = new BufferedImage(bi1.getWidth(), bi1.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, bi1.getWidth(), bi1.getHeight());
            Graphics2D g2 = bi2.createGraphics();
//            g2.setColor(Color.WHITE);
            g2.fill(new RoundRectangle2D.Float(0, 0, bi1.getWidth(), bi1.getWidth(), bi1.getHeight(), bi1.getHeight()));
//            g2.fill(new Rectangle(bi2.getWidth(), bi2.getHeight()));
//            g2.setComposite(AlphaComposite.SrcAtop);
            g2.setClip(shape);
            // 使用 setRenderingHint 设置抗锯齿
            g2.drawImage(bi1, 0, 0, null);
            g2.dispose();

            try {
                ImageIO.write(bi2, "jpg", new File(url_final+"pic/HeadImg/"+nickname+".png"));
//                ImageIO.write(bi2, "png", new File("C:/Users/admin/坚果.png"));

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    /**
     * 改变图片的大小到宽为size，然后高随着宽等比例变化
     * @throws IOException
     */
    public void resizeImage(File file1, File file2, int newWidth, int newHeight) throws IOException {
        try {
            BufferedImage src = ImageIO.read(file1); // 读入文件
            int width = src.getWidth(); // 得到源图宽
            int height = src.getHeight(); // 得到源图长

//        int newWidth = (int)(width * percent);
//        int newHeight = (int)(height * percent);
            Image image = src.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            ImageIO.write(tag, "png", file2);// 输出到文件流
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
