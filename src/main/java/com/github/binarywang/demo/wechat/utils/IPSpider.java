package com.github.binarywang.demo.wechat.utils;

import com.sun.jimi.core.util.P;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPSpider {

        private static final String HtmlIp="((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))";

    private static final String IP_Port="<td.*?>\\b\\d{2,5}\\b\\b<.*?>";

    private static final String IPPort="\\b\\d{2,5}\\b\\b";

    private static final String response_S="[0-9].[0-9]|[0-9]";

    private static final String Speed_tr="<tr.*?class=(.*?)>([\\s\\S]*?)</tr>";

    private static final String kuaidaili="<td data-title=\"IP\">([\\s\\S]*?)</tr>";

    private static final String Speed_title="title=\"\\d\\.\\d{3,}秒\"";

    private static final String Speed_response="<td data-title=\"响应速度\">([\\s\\S]*?)</td>";

    private static final String Speed_IP = "\\d\\.\\d{3,}";


    private static CloseableHttpClient httpClient = HttpClients.createDefault();


    /**
     * 开始爬取
     *
     * @param webURL 要爬取的网址
     * @throws Exception 爬取失败
     */
    public void startCrawler(String webURL) throws Exception {

//        String path = dir + File.separator + webURL.substring(webURL.lastIndexOf("/")) + ".html";
//        File file = new File(path);
//
//        if (file.exists() && file.length() > 20_000)
//            return;
//        if (list == null) {
//            crawler(webURL, path, null, 0);
//            ImageSpide(webURL);
//        httpRequest(webURL,null,0);
//        } else {
//        int index = new Random().nextInt(6);
//        ProxyIP proxyIP=new ProxyIP(ProxyServer.proxyIP.get(0),ProxyServer.proxyPort.get(0));
//            crawler(webURL, path, list.get(index), index);
//            httpRequest(webURL,proxyIP,0);
//            ImageSpide(webURL);
//        }
        CloseableHttpResponse response = null;
        HttpGet httpGet = null;
        String url[]= webURL.split(",");
//        for (String url:urls)
        try {
            List<String> IP_All = new ArrayList<>();
            List<String> Port_All = new ArrayList<>();
            String html=null;
            for(String url_one:url) {
                for(int i=1;i<10;i++){
                    List<Integer> Sp=new ArrayList<>();
                    List<Float> Speed;
                    httpGet = new HttpGet(url_one+i);
                    httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 " +
                            "(KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36");
                    RequestConfig requestConfig = null;
                    requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(1000).build();
                    httpGet.setConfig(requestConfig);
                    response = httpClient.execute(httpGet);
                    int status = response.getStatusLine().getStatusCode();
                    if (status == 200) {
                        HttpEntity entity = response.getEntity();
    //                    entity.writeTo(new FileOutputStream(path));
    //                    System.out.println(EntityUtils.toString(entity));
                        html = EntityUtils.toString(entity);
    //                    System.out.println("下载成功！" + url_one);
                        List<String> Speed_tr= getSpeed_tr(html);
                        System.out.println(Speed_response);
                        List<String> title=getSpeed_title(Speed_tr);
                        List<Float> speed=getSpeed(title);
                        for(int m=0;m<speed.size();m++){
                            System.out.println(m);
                            if(speed.get(m)<=0.1){
                                Sp.add(m);
                            }
                        }
                        System.out.println(speed);
                        System.out.println(Sp.size()+"              Sp"+Sp);
                    }
                        List<String> IP = getHtmlIP(html);
                        List<String> IP_port = getIP_Port(html);
                        List<String> Port = getPort(IP_port);
                        for(int s=0;s<Sp.size();s++){
                            IP_All.add(IP.get(Sp.get(s)));
                            Port_All.add(Port.get(Sp.get(s)));
                        }
                        String file = "D:/新建文件夹/代理ip.txt";
                        createFile(IP_All,Port_All,file);
                        Thread.sleep(1000);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    // 获取html内容
    public String getHTML(String srcUrl) throws Exception {
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

    // 获取动态IP地址
    private static List<String> getHtmlIP(String html) {
        List<String> listIp = new ArrayList<>();
        Matcher matcher = Pattern.compile(HtmlIp).matcher(html);
        while (matcher.find()) {
            listIp.add(matcher.group());
        }
        return listIp;
    }

    //获取快代理的IP及端口等标签
    private static List<String> getkuaidailiHtmlIP(String html){
        List<String> listIp = new ArrayList<>();
        Matcher matcher = Pattern.compile(kuaidaili).matcher(html);
        while (matcher.find()) {
            listIp.add(matcher.group());
        }
        return listIp;
    }

    // 获取动态IP端口号地址
    private static List<String> getIP_Port(String html) {
        Matcher matcher = Pattern.compile(IP_Port).matcher(html);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    // 获取IP端口号地址
    private static List<String> getPort(List<String> listPort) {
        List<String> ListPort = new ArrayList<String>();
        for (String Port : listPort) {
            Matcher matcher = Pattern.compile(IPPort).matcher(Port);
            if (matcher.find()) {
                ListPort.add(matcher.group());
            }
        }
        return ListPort;
    }

    // 获取IP端口号地址
    private static List<String> getSpeed_tr(String html) {
        List<String> ListSpeed = new ArrayList<String>();
        Matcher matcher = Pattern.compile(Speed_tr).matcher(html);
        while (matcher.find()) {
            ListSpeed.add(matcher.group());
        }
        return ListSpeed;
    }

    // 获取IP端口号地址
    private static List<String> getSpeed_title(List<String> listSpeed) {
        List<String> ListPort = new ArrayList<String>();
        for (String Speed: listSpeed) {
            Matcher matcher = Pattern.compile(Speed_title).matcher(Speed);
            if (matcher.find()) {
                ListPort.add(matcher.group());
            }
        }
        return ListPort;
    }

    // 获取IP端口号地址
    private static List<String> getResponse_Speed(String html) {
        List<String> ListPort = new ArrayList<String>();
            Matcher matcher = Pattern.compile(Speed_response).matcher(html);
            while (matcher.find()) {
                ListPort.add(matcher.group());
        }
        return ListPort;
    }

    // 获取IP端口号地址
    private static List<Float> getSpeed(List<String> listSpeed) {
        List<Float> ListPort = new ArrayList<Float>();
        for (String Speed: listSpeed) {
            Matcher matcher = Pattern.compile(Speed_IP).matcher(Speed);
            if (matcher.find()) {
                ListPort.add(Float.valueOf(matcher.group()));
            }
        }
        return ListPort;
    }

    // 获取IP端口号地址
    private static List<String> getIP_Port(List<String> listSpeed) {
        List<String> ListPort = new ArrayList<String>();
        for (String Speed: listSpeed) {
            Matcher matcher = Pattern.compile(IPPort).matcher(Speed);
            if (matcher.find()) {
                ListPort.add(matcher.group());
            }
        }
        return ListPort;
    }

    private void createFile(List<String> IP,List<String> Port,String filepath) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(new File(filepath),true));
            for(int i=0;i<IP.size();i++){
                bw.write(IP.get(i)+":"+Port.get(i)+"\r\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

//    /**
//     * 发起http请求
//     *
//     * @author chenmc
//     * @date 2017年8月30日 下午2:36:09
//     * @throws Exception
//     */
//    public static Integer httpRequest(String urls, ProxyIP proxy, int index) throws Exception {
//        CloseableHttpResponse response = null;
//        HttpGet httpGet = null;
//        int n=0;
////        for (String url:urls)
//        try {
//            httpGet = new HttpGet(urls);
//            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 " +
//                    "(KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36");
//            RequestConfig requestConfig = null;
//            if (proxy == null) {
//                requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(1000).build();
//            } else {
//                HttpHost httpHost = new HttpHost(proxy.getIp(), proxy.getPort());
//                requestConfig = RequestConfig.custom().setProxy(httpHost).setConnectTimeout(5000).setSocketTimeout(1000).build();
//
////                    Proxy proxy1 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("123.22.43.3", 8080));
//
////                HttpUrlConnection connection = (HttpUrlConnection)new URL("http://www.baidu.com/").openConnection(proxy1);
//
//            }
//            httpGet.setConfig(requestConfig);
//            response = httpClient.execute(httpGet);
//            int status = response.getStatusLine().getStatusCode();
//
//            System.out.println("111111111111111111111111");
//            if (status == 200) {
//                HttpEntity entity = response.getEntity();
////                    entity.writeTo(new FileOutputStream(path));
////                    System.out.println(EntityUtils.toString(entity));
//                String html = EntityUtils.toString(entity);
//                System.out.println("下载成功！" + urls);
////                    long startTime = System.currentTimeMillis();
////                    String path;
//                List<String> IP = getHtmlIP(html);
//                List<String> IP_port = getIP_Port(html);
//                List<String> Port = getPort(IP_port);
//                System.out.println(IP+","+IP.size());
//                System.out.println(Port+","+Port.size());
////                    List<String> list_IP = getHtmlIP(html);
////                    System.out.println(list_IP);
////                    List<String> listSrc = getImageSrc(listUrl);
////                    List<Integer> integers = getRightImage(listUrl);
////                    List<String> list_url = new ArrayList<>();
//
////                    for(int i=0;i<integers.size();i++) {
////                        list_url.add(i,listSrc.get(integers.get(i)));
////                        System.out.println(listSrc.get(integers.get(i)));
////                    }
////                    getImageSrc(list_url);
////                    System.out.println(list_url.get(0));
////                    if(url_handle(urls).equals("no")){
////                        System.out.println("图片格式问题");
//////                        continue;
////                    }else {
//
//
////                    }
////                    Proxy proxy1 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ProxyServer.proxyIP.get(0), ProxyServer.proxyPort.get(0)));
////                    int i;
////                    System.out.println(list_url);
////                    for (i=0;i<list_url.size();i++) {
////                        String url1 = list_url.get(i);
////                        System.out.println(list_url);
////                        path = "D:/Program Files/新建文件夹/" + url1.substring(32,72) + "."+url_handle(url1);
//////                        path = "/data/wwwroot/www.rrjiaoyi.com/Article-images/" + url1.substring(32,72) + "."+url_handle(url1);
////                        System.out.println(path);
////                        //这里url写你想要下载图片的url
////                        String uri = String.format(url1, String.format("%03d",i));
////                        System.out.println(uri);
////                        URL url = new URL(uri);
////                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
////                        conn.setRequestProperty("referer", url1); //这是破解防盗链添加的参数
////                        conn.setRequestMethod("GET");
////                        conn.setConnectTimeout(5 * 1000);
////                        InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
////                        readInputStream(inStream, path);//得到图片的二进制数据，并保存
////                        long endTime = System.currentTimeMillis();
////                        System.out.println(path + "--耗时：" + (endTime - startTime) / 1000 + "s");
//////                        hot_articleRepository.updateTwo(path_two, url_one);
//
////                    }System.out.println(i);
////                    num_=i;
//            } else {
//                if (list != null)
//                    list.remove(index);
//                throw new Exception("爬取到的网页非正常!");
//            }
//        } catch (Exception e) {
//            System.err.println(e);
//            System.err.println("下载失败！" + urls);
//        } finally {
//            if (httpGet != null)
//                httpGet.clone();
//            if (response != null)
//                response.close();
//        }
//        return null;
//    }

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

//    /**
//     * 保存图片
//     *
//     * @author chenmc
//     * @date 2017年8月30日 下午2:35:54
//     * @param inStream
//     * @param path
//     * @throws Exception
//     */
//    public static void readInputStream(InputStream inStream, String path) throws Exception{
//        FileOutputStream fos = new FileOutputStream(new File(path));
//        byte[] buffer = new byte[102400];
//        int len = 0;
//        while( (len=inStream.read(buffer)) != -1 ){
//            fos.write(buffer, 0, len);
//        }
//        inStream.close();
//        fos.flush();
//        fos.close();
//    }

    public static void main(String[] args) throws Exception {
////        ImageSpider imageSpider = new ImageSpider();
//        String url = "https://mp.weixin.qq.com/s?__biz=MzU2ODI5ODMzNg==&mid=2247503103&idx=2&sn=94b7c1d0f48d38397eed0de4893c038d&scene=0#wechat_redirect";
////        try {
////            imageSpider.ImageSpide(url);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//        String html = getHTML(url);
//
//        List<String> listUrl = getImageURL(html);
//        List<Integer> integers = getRightImage(listUrl);
//
//        List<String> list_url = new ArrayList<>();
//
//        for (int i = 0; i < integers.size(); i++) {
//            list_url.add(i, listUrl.get(integers.get(i)));
//        }
////        System.out.println(list_url);
////
////        System.out.println(Double.valueOf("0.888888888")>0.5);
//        List<String> ss = getImageSrc(list_url);
//        String path_one = "http://www.rrjiaoyi.com/Article-images/" + ss.get(0).substring(32, 57) + "." + imageTest.url_handle(ss.get(0));
//        System.out.println(path_one);
//        System.out.println(ss);
        /*
         * for(String img : listUrl){ System.out.println(img); }
         */
//        List<String> listSrc = getImageSrc(listUrl);

//        StorePicsUrl(listSrc,url);
        /*
         * for(String img : listSrc){ System.out.println(img); }
         */
//        Download(listSrc);

//        String html = getHTML(url);
//
//        List<String> listUrl = getImageURL(html);
//        List<String> listSrc = getImageSrc(listUrl);
//        List<Integer> integers = getRightImage(listUrl);
//
//        List<String> list_url = new ArrayList<>();
//
//        for(int i=0;i<integers.size();i++) {
//            list_url.add(i,listSrc.get(integers.get(i)));
//            System.out.println(listSrc.get(integers.get(i)));
//        }
//        System.out.println(list_url);
//    }
        IPSpider ipSpider = new IPSpider();
        //  httpCrawler.proxyInit("E:\\IDECode\\StringUtils\\text\\代理ip.txt");//代理ip文本路径
        ipSpider.startCrawler("http://www.xicidaili.com/wn/,http://www.xicidaili.com/nt/,http://www.xicidaili.com/nn/,http://www.xicidaili.com/wt/");//要爬取的网址
//        ipSpider.startCrawler("https://www.kuaidaili.com/free/inha/");
        ipSpider.close();//关闭爬虫流
//        String html =imageSpider.getHTML("http://www.xicidaili.com/nn/");
//        List<String> IP = imageSpider.getHtmlIP(html);
//        List<String> IP_port = imageSpider.getIP_Port(html);
//        List<String> Port = imageSpider.getPort(IP_port);
//        System.out.println(IP);
//        System.out.println(Port);

    }

}
