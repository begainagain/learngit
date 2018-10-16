package com.github.binarywang.demo.wechat.utils;

import com.github.binarywang.demo.wechat.dao.Hot_articleRepository;
import com.github.binarywang.demo.wechat.entity.ProxyIP;
import com.github.binarywang.demo.wechat.pic.ImgUtils;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.binarywang.demo.wechat.utils.HtmlUtils.url_path;


@Component
public class ImageSpider {


    @Autowired
    private Hot_articleRepository hot_articleRepository;


    public static final String url_final = "/mnt/";
//    public static final String url_final = "D:/Program Files/新建文件夹/";
//        public static final String url_final = "/mnt/";

    private static List<String> SRC = new ArrayList<>();
    // 地址
    private static final String URL = "http://www.tooopen.com/view/1439719.html";
    // 获取img标签正则
    private static final String IMGURL_REG = "<img(.*?)data-src=(.*?)[^>]*>.*?";
    // 获取src路径的正则
    private static final String IMGSRC_REG = "[a-zA-z]+://[^\\s]*";
    private static final String IMGSRC_REG_ONE = "h+(.*?)\"";
    private static final String IMGSRC_BACK = "background-image: url\\(.*?\\)";
    private static final String IMGSRC_BORDER = "-webkit-border-image: url\\(.*?\\)";
    // 获取正确的src路径正则
    private static final String IMGSRC_SRC = "data-src=\".*?\"";
    private static final String IMGSRC_RATIO = "data-ratio=\".*?\"";
    private static final String IMGSRC_RIGHT = "[-+]?[0-9]{0,2}\\.\\d*|0\\.\\d*[1-9]\\d*$|[0-9]";
    private static final String IMGSRC_NOSMALL = "data-w=\".*?\"";
    private static final String IMGSRC_WIDTH = "[1-9][0-9]{1,}";
    // 获取固定标签下的文字的正则
    private static final String WORD_HTML = "global_error_msg warn";
    private static final String Html_Word = "此帐号已被屏蔽, 内容无法查看";
    private static final String Html_Word2 = "此内容因违规无法查看";
    private static final String Html_Word3 = "此内容被投诉且经审核涉嫌侵权，无法查看。";
    //匹配文章作者
    private static final String Author_Tag ="<strong class=\"profile_nickname\">.*?</strong>";
    private static final String Wechat_Author = "[\\u4e00-\\u9fa5]{2,}";

    private static final PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();

    private static final CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).setConnectionManagerShared(true).build();

//    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    private static List<ProxyIP> list = null;
    public static int num_ = 0;

    //保存爬取的网页
    private String dir = null;

    /**
     * 代理初始化
     *
     * @throws Exception
     */
    public static void proxyInit(String proxyText) throws Exception {
        list = new ArrayList<>();
        List<String> listIP = FileUtils.readLines(new File(proxyText));
        for (String str : listIP) {
            String ip = str.split(":")[0];
            int port = Integer.parseInt(str.split(":")[1]);
            ProxyIP proxyIp = new ProxyIP(ip, port);
            list.add(proxyIp);
        }
    }

    /**
     * 开始爬取
     *
     * @param webURL 要爬取的网址
     * @throws Exception 爬取失败
     */
    public void startCrawler(String webURL) throws Exception {

//        String path = dir + File.separator + webURL.substring(webURL.lastIndexOf("/")) + ".html";
//        File file = new File(path);

//        if (file.exists() && file.length() > 20_000)
//            return;
//        if (list == null) {
//            crawler(webURL, path, null, 0);
//            ImageSpide(webURL);
        httpRequest(webURL, null, 0);
//        } else {
//            int index = new Random().nextInt(6);
//            ProxyIP proxyIP=new ProxyIP(ProxyServer.proxyIP.get(0),ProxyServer.proxyPort.get(0));
//            crawler(webURL, path, list.get(index), index);
//            httpRequest(webURL,proxyIP,0);
//            ImageSpide(webURL);
//        }

    }

    // 获取html内容
    public static String getHTML(String srcUrl) throws Exception {
        URL url = new URL(srcUrl);
        URLConnection conn = url.openConnection();
        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String line = null;
        StringBuffer buffer = new StringBuffer();
        while ((line = br.readLine()) != null) {
            buffer.append(line);
            buffer.append("\n");
        }
        br.close();
        isr.close();
        is.close();
        return buffer.toString();
    }

    // 获取image url地址
    public boolean deleteWordHTML(String html, String url) {
        Matcher matcher = Pattern.compile(WORD_HTML).matcher(html);
        Matcher matcher1 = Pattern.compile(Html_Word).matcher(html);
        Matcher matcher2 = Pattern.compile(Html_Word2).matcher(html);
        Matcher matcher3 = Pattern.compile(Html_Word3).matcher(html);
        if (matcher.find()) {
            System.out.println("ssssssssssssssssss");
            String urlimage = url.substring(5);
            StringBuffer buffer = new StringBuffer();
            buffer.append("http");
            buffer.append(urlimage);
            String url1 = String.valueOf(buffer);
            System.out.println(url1);
            return true;
        } else if (matcher1.find() || matcher2.find() || matcher3.find()) {
            System.out.println("dddddddddddddddd");
            String urlimage = url.substring(5);
            StringBuffer buffer = new StringBuffer();
            buffer.append("http");
            buffer.append(urlimage);
            String url1 = String.valueOf(buffer);
            System.out.println(url1);
            return true;
        }
        return false;
    }

    // 获取image url地址
    public static List<String> getImageURL(String html) {
        Matcher matcher = Pattern.compile(IMGURL_REG).matcher(html);
        List<String> list = new ArrayList<>();
        while (matcher.find()){
            Matcher src = Pattern.compile(IMGSRC_SRC).matcher(matcher.group());
            while (src.find()) {
                list.add(src.group());
            }
        }
        Matcher matcher1 = Pattern.compile(IMGSRC_BACK).matcher(html);
        //背景图
        while (matcher1.find()) {
            list.add(matcher1.group());
        }
        Matcher matcher2 = Pattern.compile(IMGSRC_BORDER).matcher(html);
        //边框背景
        while (matcher2.find()) {
            String border = matcher2.group();
            if(border.contains("&quot;")) {
                border = border.replace("&quot;","");
            }
            list.add(border);
        }
//        System.out.println(list);
        return list;
    }


    // 获取image src地址
    public static List<String> getImageSrc(List<String> listUrl) {
        List<String> listSrc = new ArrayList<String>();
        for (String img : listUrl) {
            if (img.contains("p4.so.qhmsg.com")) {
                Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(img);
                while (matcher.find()) {
                    if (!matcher.group().contains("p4.so.qhmsg.com")) {
                        listSrc.add(matcher.group());
                        break;
                    }
                }
            } else if (img.contains("wx_fmt=")) {
                Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(img);
                if (matcher.find()) {
                    System.out.println(matcher.group());
                    if (matcher.group().endsWith("?\"") || matcher.group().endsWith("\"")) {
                        listSrc.add(matcher.group().substring(0,
                                matcher.group().length() - 1));
                    } else {
                        listSrc.add(matcher.group());
                    }
                }
            } else if (img.endsWith("640")) {
                Matcher matcher = Pattern.compile(IMGSRC_REG_ONE).matcher(img);
                if (matcher.find()) {
                    if (matcher.group().endsWith("\"")) {
                        listSrc.add(matcher.group().substring(0,
                                matcher.group().length() - 1));
                    } else {
                        listSrc.add(matcher.group());
                    }
                }
            } else {
                Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(img);
                if (matcher.find()) {
                    System.out.println(matcher.group());
                    if (matcher.group().endsWith("\"") || matcher.group().endsWith("?")) {
                        listSrc.add(matcher.group().substring(0,
                                matcher.group().length() - 1));
                    } else if (matcher.group().endsWith("?]")) {
                        listSrc.add(matcher.group().substring(0,
                                matcher.group().length() - 2));
                    } else {
                        listSrc.add(matcher.group());
                    }
                }
            }

        }
        return listSrc;
    }

    // 获取长宽比大于0.5的image src地址
    public static List<Integer> getRightImage(String html,List<String> listUrl) {
        int n = 0;
        String authors=null;
        Matcher author_tag = Pattern.compile(Author_Tag).matcher(html);
        if (author_tag.find()) {
            Matcher author = Pattern.compile(Wechat_Author).matcher(author_tag.group());
            if (author.find()) {
                authors=author.group();
                if(author.group().equals("冷笑话精选")||author.group().equals("湛江日报")||author.group().equals("夜听")||author.group().equals("央视新闻")||author.group().equals("央视新闻")||
                        author.group().equals("奔波儿灞与灞波儿奔")||author.group().equals("当时我就震惊了")||author.group().equals("扒姐来了")||author.group().equals("乌鸦电影")){
                    n=1;
                }else if(author.group().equals("蕊希")){
                    n=2;
                }
            }
        }
        Matcher img = Pattern.compile(IMGURL_REG).matcher(html);
        List<String> img_list = new ArrayList<>();
        while(img.find()){
            img_list.add(img.group());
        }
        System.out.println(img_list);
        Matcher matcher = Pattern.compile(IMGSRC_RATIO).matcher(img_list.toString());
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        System.out.println(list);
        System.out.println(list.size()+"list sizeeeeeeeeeeeeeeeeee");
        if (list.size() == 0) {
            return null;
        }
        List<Integer> math = new ArrayList<>();
        System.out.println(math.isEmpty());
        System.out.println(SRC+"SRC");
        System.out.println(list+"list");
        System.out.println(listUrl+"listUrl");
        for (int i = n; i < list.size(); i++) {
            System.out.println(i+"iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
            Matcher matcher1 = Pattern.compile(IMGSRC_NOSMALL).matcher(img_list.get(i));
            if (matcher1.find()) {
                Matcher width = Pattern.compile(IMGSRC_WIDTH).matcher(matcher1.group());//获取图片长度
                if (math.size() == 10) {
                    break;
                }
                int len=0;
                if (width.find()) {
                    System.out.println(width.group() + "ratoiiiiiiiiiiiiii");
                    Matcher matcher_ratio = Pattern.compile(IMGSRC_RIGHT).matcher(list.get(i));//获取图片长宽比
                    if (matcher_ratio.find()) {
                        if(authors!=null&&authors.equals("吴晓波频道")&&i==0){
                            continue;
                        }
                        if (Integer.valueOf(width.group()) >= 640) {
                            System.out.println(matcher1.group() + "1111111111111111111");
                            if (!SRC.contains(listUrl.get(i)) && Float.valueOf(matcher_ratio.group()) >= 0.5) {
                                if (Float.valueOf(matcher_ratio.group()) > 0.6 && Float.valueOf(matcher_ratio.group()) < 0.76) {
                                    System.out.println("777777777777777");
                                    math.add(i);
                                } else if (Float.valueOf(matcher_ratio.group()) <= 0.6) {
                                    System.out.println("666666666666666");
                                    math.add(i);
                                } else if (Float.valueOf(matcher_ratio.group()) > 0.76) {
                                    System.out.println("ggggggggggggggggggggggggggg");
                                    math.add(i);
                                }
                            } else if ((!SRC.contains(listUrl.get(i)) && Float.valueOf(matcher_ratio.group()) > 0.335&& Float.valueOf(matcher_ratio.group()) < 0.9)||(authors!=null&&authors.equals("夜听")&&authors.equals("夜叔")&&Float.valueOf(matcher_ratio.group()) > 0.335&& Float.valueOf(matcher_ratio.group()) < 0.9)) {
                                System.out.println("2333333333333333");
                                math.add(i);
                            }
                        } else if (Integer.valueOf(width.group()) < 640 && Integer.valueOf(width.group()) >= 440) {
                            System.out.println(width.group());
                            if (!SRC.contains(listUrl.get(i)) && Float.valueOf(matcher_ratio.group()) > 0.5) {
                                System.out.println(matcher_ratio.group());
                                if (Float.valueOf(matcher_ratio.group()) > 0.6 && Float.valueOf(matcher_ratio.group()) < 0.76) {
                                    math.add(i);
                                } else if (Float.valueOf(matcher_ratio.group()) > 0.5 && Float.valueOf(matcher_ratio.group()) <= 0.6) {
                                    math.add(i);
                                } else if (Float.valueOf(matcher_ratio.group()) > 0.76&& Float.valueOf(matcher_ratio.group()) < 0.9) {
                                    System.out.println("ggggggggggggggggggggggggggggggg");
                                    math.add(i);
                                }
                            } else if (Float.valueOf(matcher_ratio.group()) > 0.37&& Float.valueOf(matcher_ratio.group()) < 0.9) {
                                if (!SRC.contains(listUrl.get(i))) {
                                    math.add(i);
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println(math+"mathhhhhhhhhhhhhhhhhh");
        return math;
    }

    public static String file_path(String url1){
        String path=null;
        if (url1.contains("/mmbiz_")) {
//                            path = "D:/Program Files/新建文件夹/" + "Article-images/" + url1.substring(32, 82) + "." + url_handle(url1);
            path = url_final + "Article-images/" + url1.substring(32, 82) + "." + url_handle(url1);
        } else if(url1.contains("/mmbiz/")){
//                            path = "D:/Program Files/新建文件夹/" + "Article-images/" + url1.substring(28, 78) + "." + url_handle(url1);
            path = url_final + "Article-images/" + url1.substring(28, 78) + "." + url_handle(url1);
        }else if (url1.contains("/emoji_wx/") || url1.contains("/emoji_ios/")) {
            if (url1.endsWith("\"")) {
//                                path= "D:/Program Files/新建文件夹/"+"Article-images/" + url1.substring(url1.lastIndexOf("/")+1,url1.length());
                path = url_final + "Article-images/" + url1.substring(url1.lastIndexOf("/") + 1, url1.length());
                System.out.println(path + "表情");
            } else {
//                                path= "D:/Program Files/新建文件夹/"+"Article-images/" + url1.substring(url1.lastIndexOf("/")+1);
                path = url_final + "Article-images/" + url1.substring(url1.lastIndexOf("/") + 1);
                System.out.println(path + "表情第二种情况");
            }
        } else if (url1.contains("pgc-image") || url1.contains("p2.so.qhmsg.com")) {
            if (url1.endsWith("\"")) {
                path = url_final + "Article-images/" + url1.substring(url1.lastIndexOf("/") + 1, url1.length());
//                                path= "D:/Program Files/新建文件夹/"+"Article-images/" + url1.substring(url1.lastIndexOf("/")+1,url1.length());

                System.out.println(path + "表情");
            } else {
//                                path= "D:/Program Files/新建文件夹/"+"Article-images/" + url1.substring(url1.lastIndexOf("/")+1);
                path = url_final + "Article-images/" + url1.substring(url1.lastIndexOf("/") + 1);
                System.out.println("特殊图片");
            }
        } else if (url1.contains("_242_")) {
            if (url1.endsWith("\"")) {
//                                path= "D:/Program Files/新建文件夹/"+"Article-images/" + url1.substring(url1.lastIndexOf("/")+1,url1.length()-1);
                path = url_final + "Article-images/" + url1.substring(url1.lastIndexOf("/") + 1, url1.length());
            } else {
//                                path= "D:/Program Files/新建文件夹/"+"Article-images/" + url1.substring(url1.lastIndexOf("/")+1);
                path = url_final + "Article-images/" + url1.substring(url1.lastIndexOf("/") + 1);
                System.out.println(path + "表情第二种情况");
            }
        } else if (url1.contains("mmsns")) {
//                            path = "D:/Program Files/新建文件夹/" + "Article-images/" + url1.substring(28, 78) + "." + url_handle(url1);
            path = url_final + "Article-images/" + url1.substring(28, 78) + "." + url_handle(url1);
        } else if (!url1.contains("mmbiz")) {
//                            path = "D:/Program Files/新建文件夹/" + "Article-images/" + url1.substring(url1.lastIndexOf("/") + 1, url1.lastIndexOf("/") + 10) + "." + url_handle(url1);
            path = url_final + "Article-images/" + url1.substring(url1.lastIndexOf("/") + 1, url1.lastIndexOf("/") + 10) + "." + url_handle(url1);
        }else {
            path = url_final + "Article-images/" + url1.substring(url1.lastIndexOf("/")+1);
        }
        return path;
    }

    public static String url_handle(String url) {
        String url_one;
        if(url.contains("png")){
            url_one="png";
        }else if(url.contains("jpeg")){
            url_one="jpeg";
        }else if(url.contains("jpg")){
            url_one="jpg";
        }else if(url.contains("gif")){
            url_one="gif";
        }else if(url.contains("640?")){
            url_one="gif";
        }else{
            url_one="png";
        }
        return url_one;
    }

    /**
     * 发起http请求
     *
     * @throws Exception
     * @author chenmc
     * @date 2017年8月30日 下午2:36:09
     */
    public static Integer httpRequest(String urls, ProxyIP proxy, int index) throws Exception {
        CloseableHttpResponse response = null;
        HttpGet httpGet = null;
        int n = 0;
//        for (String url:urls)
        try {
            httpGet = new HttpGet(urls);
            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 " +
                    "(KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36");
            RequestConfig requestConfig = null;
            if (proxy == null) {
                requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(1000).build();
            } else {
                HttpHost httpHost = new HttpHost(proxy.getIp(), proxy.getPort());
                requestConfig = RequestConfig.custom().setProxy(httpHost).setConnectTimeout(5000).setSocketTimeout(1000).build();
//                Proxy proxy1 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("123.22.43.3", 8080));
//
//                HttpUrlConnection connection = (HttpUrlConnection)new URL("http://www.baidu.com/").openConnection(proxy1);
            }
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            System.out.println("111111111111111111111111");
            if (status == 200) {
                HttpEntity entity = response.getEntity();
//                    entity.writeTo(new FileOutputStream(path));
                String html = EntityUtils.toString(entity);
                System.out.println(html);
                System.out.println("下载成功！" + urls);
                long startTime = System.currentTimeMillis();
                String path = null;
                List<String> listUrl = getImageURL(html);
                System.out.println(listUrl);
                List<String> listSrc = getImageSrc(listUrl);
//                    List<Integer> integers = getRightImage(listUrl);
//                List<String> list_url = new ArrayList<>();
//                    if(integers!=null) {
//                        for (int i = 0; i < integers.size(); i++) {
//                            list_url.add(i, listSrc.get(integers.get(i)));
//                            System.out.println(listSrc.get(integers.get(i)));
//                        }
//                    }else {
//                list_url = listSrc;
//                    }
//                System.out.println(listSrc);

//                Proxy proxy1 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyServer.proxyIP.get(0), ProxyServer.proxyPort.get(0)));
                int i;
                for (i = 0; i < listSrc.size(); i++) {
                    String url1 = listSrc.get(i);
                    path = file_path(url1);

                    System.out.println(path);
                    //这里url写你想要下载图片的url
                    String uri = String.format(url1, String.format("%03d", i));
                    System.out.println(uri);
                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("referer", url1); //这是破解防盗链添加的参数
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5 * 1000);
                    InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
                    readInputStream(inStream, path);//得到图片的二进制数据，并保存
                    long endTime = System.currentTimeMillis();
                    System.out.println(path + "--耗时：" + (endTime - startTime) / 1000 + "s");
                    if(!path.endsWith("gif")) {
                        try {
                            MultiFormatReader formatReader = new MultiFormatReader();
                            // 二维码图片位置
                            File file = new File(path);
                            // 读取图片
                            BufferedImage image = ImageIO.read(file);
                            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
                            // 定义二维码的参数
                            HashMap hints = new HashMap();
                            // 设置编码字符集
                            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
                            // 处理读取结果
                            Result result = formatReader.decode(binaryBitmap, hints);
                            SRC.add(url1);
                            System.out.println(SRC + "SRRRRRRRRRRRRRRRRCCCCCCCCCCCCCCCC");
                            System.out.println("解析结果：" + result.toString());
                            System.out.println("二维码格式类型：" + result.getBarcodeFormat());
                            System.out.println("二维码文本内容：" + result.getText());
                        } catch (NotFoundException e) {
                            // TODO Auto-generatedcatch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println(i);
                num_ = 0;
            } else {
                if (list != null)
                    list.remove(index);
                throw new Exception("爬取到的网页非正常!");
            }
        } catch (Exception e) {
            System.err.println(e);
            System.err.println("下载失败！" + urls);
        } finally {
            if (httpGet != null)
                httpGet.clone();
            if (response != null)
                response.close();
        }
        return null;
    }

    /**
     * 获取代理ip链表
     *
     * @return
     */
    public List<ProxyIP> getList() {
        return list;
    }

    /**
     * 关闭爬取流
     */
    public void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图片
     *
     * @param inStream
     * @param path
     * @throws Exception
     * @author chenmc
     * @date 2017年8月30日 下午2:35:54
     */
    private static void readInputStream(InputStream inStream, String path) throws Exception {
        FileOutputStream fos = new FileOutputStream(new File(path));
        byte[] buffer = new byte[102400];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        inStream.close();
        fos.flush();
        fos.close();
    }

    public List<String> ImageSpide(String url) throws Exception {
//        CloseableHttpResponse response = null;
//        HttpGet httpGet = null;
//        try {
//            httpGet = new HttpGet(url);
//            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 " +
//                    "(KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36");
//            RequestConfig requestConfig = null;
//            if (proxy == null) {
//                requestConfig = RequestConfig.custom().setConnectTimeout(2000).setSocketTimeout(1000).build();
//            } else {
//                HttpHost httpHost = new HttpHost(proxy.getIp(), proxy.getPort());
//                requestConfig = RequestConfig.custom().setProxy(httpHost).setConnectTimeout(2000).setSocketTimeout(1000).build();
//            }
//            httpGet.setConfig(requestConfig);
//            response = httpClient.execute(httpGet);
//            int status = response.getStatusLine().getStatusCode();
//            if (status == 200) {
//                HttpEntity entity = response.getEntity();
//                entity.writeTo(new FileOutputStream(path));
//                System.out.println("下载成功！" + url);
//            } else {
//                if (list != null)
//                    list.remove(index);
//                throw new Exception("爬取到的网页非正常!");
//            }
//        } catch (Exception e) {
//            System.err.println(e);
//            System.err.println("下载失败！" + url);
//        } finally {
//            if (httpGet != null)
//                httpGet.clone();
//            if (response != null)
//                response.close();
//        }

        String html = getHTML(url);

        //通过正则匹配出每张图片的img标签
        Matcher img = Pattern.compile(IMGURL_REG).matcher(html);
        List<String> img_list = new ArrayList<>();
        while(img.find()){
            img_list.add(img.group());
        }
        System.out.println(img_list.size()+"img_list size");
        List<Float> ratio_list = new ArrayList<>();
        //通过正则匹配出每张图片的比例
        Matcher matcher = Pattern.compile(IMGSRC_RATIO).matcher(img_list.toString());
        while (matcher.find()){
            Matcher ratio = Pattern.compile(IMGSRC_RIGHT).matcher(matcher.group());
            while (ratio.find()) {
                ratio_list.add(Float.valueOf(ratio.group()));
            }
        }
        System.out.println(ratio_list.size()+"ratio size");
        //通过正则匹配出每张图片的格式
        List<String> form_list = new ArrayList<>();
        List<String> url_list = new ArrayList<>();
        Matcher src = Pattern.compile(IMGSRC_SRC).matcher(img_list.toString());
        while (src.find()){
            form_list.add(url_handle(src.group()));
            url_list.add(src.group());
        }


        List<String> listUrl = getImageURL(html);
        List<String> listSrc = getImageSrc(listUrl);
        List<Integer> integers = getRightImage(html,listSrc);
        System.out.println(integers+"integers");
        List<String> list_url = new ArrayList<>();
        if (integers != null) {
            int f=0;
            for (int i = 0; i < integers.size(); i++) {
                int n = integers.get(i);
                String form = form_list.get(n);
                System.out.println(ratio_list.get(n)+"sssssssssssss");
                if(f==0&&ratio_list.get(n)>0.8&&ratio_list.get(n)<=1.0) {
                    if(!form.equals("gif")){
                        String path = file_path(listSrc.get(n));
                        File newfile=new File(path);
                        BufferedImage bufferedimage=ImageIO.read(newfile);
                        int width = bufferedimage.getWidth();
                        int height = bufferedimage.getHeight();
                        float ratio = 500/655;
                        bufferedimage=ImgUtils.cropImage(bufferedimage,0,0,(int) (width),(int) (width*0.7));
                        String newPath = path.substring(0,path.lastIndexOf(".")-1)+"-1."+form;
                        ImageIO.write(bufferedimage, "jpg", new File(newPath));    //输出裁剪图片
                        list_url.add(newPath);
                        f++;
                    }else {
                        list_url.add(file_path(listSrc.get(n)));
                        f++;
                    }
                }else if(f==0&&ratio_list.get(n)>1.0){
                    if(!form.equals("gif")){
                        String path = file_path(listSrc.get(n));
                        System.out.println(listSrc.get(n));
                        System.out.println(path+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        File newfile=new File(path);
                        BufferedImage bufferedimage=ImageIO.read(newfile);
                        int width = bufferedimage.getWidth();
                        int height = bufferedimage.getHeight();
                        float ratio = 500/655;
                        bufferedimage=ImgUtils.cropImage(bufferedimage,0,(int)(20),(int) (width),(int) (width*0.7+20));
                        String newPath = path.substring(0,path.lastIndexOf(".")-1)+"-1."+form;
                        ImageIO.write(bufferedimage, "jpg", new File(newPath));    //输出裁剪图片
                        list_url.add(newPath);
                        f++;
                    }
                }else if(f==0&&ratio_list.get(n)<0.45){
                    if(!form.equals("gif")){
                        String path = file_path(listSrc.get(n));
                        File newfile=new File(path);
                        BufferedImage bufferedimage=ImageIO.read(newfile);
                        int width = bufferedimage.getWidth();
                        int height = bufferedimage.getHeight();
                        bufferedimage=ImgUtils.cropImage(bufferedimage,0,(int)(0),(int) (height/0.66),(int) (height));
                        String newPath = path.substring(0,path.lastIndexOf(".")-1)+"-1."+form;
                        ImageIO.write(bufferedimage, "jpg", new File(newPath));    //输出裁剪图片
                        list_url.add(newPath);
                        f++;
                    }
                }else {
                    list_url.add(file_path(listSrc.get(n)));
                    f++;
                }

                System.out.println(listSrc.get(n));
            }
            System.out.println(list_url);
        } else {
            list_url = listSrc;
        }
        System.out.println(list_url);

        /*
         * for(String img : listUrl){ System.out.println(img); }
         */
//        List<String> listSrc = getImageSrc(listUrl);

//        StorePicsUrl(listSrc,url);
        /*
         * for(String img : listSrc){ System.out.println(img); }
         */
//        Download(listSrc);
        return list_url;
    }

    public static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException
    {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(3000);
        conn.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        InputStream inputStream = conn.getInputStream();

        byte[] getData = readInputStream(inputStream);

        java.io.File saveDir = new java.io.File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        java.io.File file = new java.io.File(saveDir + java.io.File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    public static byte[] readInputStream(InputStream inputStream)
            throws IOException
    {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    public static void main(String[] args) throws Exception {





//        String url="https://mp.weixin.qq.com/mp/videoplayer?action=get_mp_video_play_url&__biz=MzI4MDU3Njk4MA==&mid=&idx=&vid=wxv_481601144360353794&uin=&key=&pass_ticket=&wxtoken=777&appmsg_token=&x5=0&f=json";
//        String html = getHTML(url);
//
//        Matcher src = Pattern.compile("[a-zA-z]+:.*?\"").matcher(html);
//        Matcher title = Pattern.compile("\"title\":\".*?\"").matcher(html);
//        if (src.find()) {
//            System.out.println("找到了");
//            System.out.println(src.group());
//        }
//        String title1 = null;
//        if (title.find()) {
//            title1=title.group().substring(9,title.group().length()-1);
//            System.out.println(title1);
//        }
//        String src1 = src.group().substring(0,src.group().length()-1).replace("\\","");
//        System.out.println(src1);
//
//        downLoadFromUrl(src1, title1 + ".mp4", "D:/Program Files/新建文件夹");

        Random random = new Random();

        for(int i =0;i<10;i++){
            int result = random.nextInt(900000)+100000;
            System.out.println(result);
        }

//        File newfile=new File("D:/Program Files/新建文件夹/Article-images/h0DBVXc9sJRhKKgXOdyKflheMYt7UYCBdZia14CxcV9KiagDHe.jpeg");
//        BufferedImage bufferedimage=ImageIO.read(newfile);
//        int width = bufferedimage.getWidth();
//        int height = bufferedimage.getHeight();
//        float ratio = 500/655;
//        bufferedimage=ImgUtils.cropImage(bufferedimage,0,0,(int) (width),(int) (width*0.6));
//
//        ImageIO.write(bufferedimage, "jpg", new File("D:/Program Files/新建文件夹/Article-images/h0DBVXc9sJRhKKgXOdyKflheMYt7UYCBdZia14CxcV9KiagDHe-1.jpeg"));    //输出裁剪图片

//        String url = "https://mp.weixin.qq.com/s?__biz=MzA5ODM5NTcwNQ==&mid=2650193159&idx=1&sn=26d94ea21d95f9b65fd25b65b27e5979&scene=0#wechat_redirect";
//
//        ImageSpider imageSpider = new ImageSpider();
//        imageSpider.startCrawler(url);
//        List<String> list= imageSpider.ImageSpide(url);
//        imageSpider.close();

//        String ur = "https://www.rrjiaoyi.com/fm";
//        String refer="https://www.rrjiaoyi.com/fm?v=1.04";
////        System.out.println(times);
//        ImageSpider.proxyInit("D:\\新建文件夹\\代理ip.txt");//代理ip文本路径
//        int n=0;
//        System.out.println(list.size());
//        for(int i=0;i<10000;i++){
//            if(n>=500)
//            System.out.println(i);
//            try {
//                int index = new Random().nextInt(list.size());
//
//                WebClient webClient = new WebClient(BrowserVersion.CHROME,list.get(index).getIp(),list.get(index).getPort());
//                webClient.getOptions().setJavaScriptEnabled(true);
//                webClient.getOptions().setCssEnabled(false);
//                webClient.setAjaxController(new NicelyResynchronizingAjaxController());
//                webClient.getOptions().setTimeout(3*1000);
//                webClient.getOptions().setThrowExceptionOnScriptError(false);
//                webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
//                HtmlPage rootPage= webClient.getPage(ur);
//                webClient.openWindow(new URL(ur),"ss");
//                String str = rootPage.getTitleText();
////                Thread.sleep(5*1000);
//                webClient.close();
//                n++;
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

//        StringBuffer sb = new StringBuffer();
//        try {
//            //构建一URL对象
//            URL url = new URL("https://www.rrjiaoyi.com/html/637173c3bb4814ce6fb5b1dc90a0a13.html");
//            //使用openStream得到一输入流并由此构造一个BufferedReader对象
//            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF8"));
//            String line;
//            //读取www资源
//            while ((line = in.readLine()) != null)
//            {
//                sb.append(line);
//            }
//            System.out.println(sb);
//            System.out.println(String.valueOf(sb).contains("<iframe")&&String.valueOf(sb).contains("</iframe>"));
//            in.close();
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }

////
////        String filename="ss";
////        Image image = Toolkit.getDefaultToolkit().getImage(filename);
////        File newfile=new File("C:\\Users\\admin\\Desktop\\yasuo.jpg");
////        BufferedImage bufferedimage=ImageIO.read(newfile);
////        int width = bufferedimage.getWidth();
////        int height = bufferedimage.getHeight();
////        //目标将图片裁剪成 宽240，高160
////        if (width > 240) {
////            /*开始x坐标              开始y坐标             结束x坐标                     结束y坐标*/
////            bufferedimage=ImgUtils.cropImage(bufferedimage,(int) (0),0,(int) (width),(int) (height));
//////            bufferedimage=ImgUtils.cropImage(bufferedimage,(int) ((width - 240) / 2),0,(int) (width - (width-240) / 2),(int) (height));
////            if (height > 160) {
////                bufferedimage=ImgUtils.cropImage(bufferedimage,0,(int) (0),width,(int) (width*0.6)
////                );
////            }
////        }else{
////            if (height > 160) {
////                bufferedimage=ImgUtils.cropImage(bufferedimage,0,(int) ((height - 160) / 2),(int) (width),(int) (height - (height - 160) / 2)
////                );
////            }
////        }
////        ImageIO.write(bufferedimage, "jpg", new File("C:\\Users\\admin\\Desktop\\caijian.jpg"));    //输出裁剪图片
//
//
//        for (int i = 0; i < list.size(); i++) {
//            String src=list.get(i);
//            System.out.println("src="+src);
//            if (i == 0) {
//                String path_one;
//                path_one = src;
//                System.out.println("path_one=================="+path_one);
//            } else if (i == 1) {
//                String path_two;
//                path_two=src;
//                System.out.println("path_two=================="+path_two);
//            } else if (i == 2) {
//                String path_three;
//                path_three=src;
//                System.out.println("path_three=================="+path_three);
//            }
//            System.out.println("更新完第" + i + "张!!!!!!!!!!!!!!!!");
//        }

//        Date d = new Date();
//        System.out.println(d.getHours());
//        try {
//
//            WebClient webClient = new WebClient();
//            webClient.getOptions().setJavaScriptEnabled(true);
//            webClient.getOptions().setCssEnabled(false);
//            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
//            webClient.getOptions().setTimeout(60);
//            webClient.getOptions().setThrowExceptionOnScriptError(false);
//            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
//            HtmlPage rootPage= webClient.getPage("https://www.rrjiaoyi.com/fm?v=1.03");
////            Thread.sleep(60*30);
//            webClient.close();
//        }
////        catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//        catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//       ImageSpider imageSpider= new ImageSpider();
//       imageSpider.startCrawler("https://mp.weixin.qq.com/s?__biz=MzA3NjU5ODcwNg==&mid=2448619537&idx=1&sn=8859c0de6c80a92b34b4d614380b93fa&scene=0#wechat_redirect");
//       imageSpider.close();
    }
}



//    }


