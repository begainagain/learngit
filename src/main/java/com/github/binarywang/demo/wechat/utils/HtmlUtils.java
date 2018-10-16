package com.github.binarywang.demo.wechat.utils;

import com.sun.jimi.core.util.P;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.binarywang.demo.wechat.utils.ImageSpider.downLoadFromUrl;
import static com.github.binarywang.demo.wechat.utils.ImageSpider.file_path;
import static com.github.binarywang.demo.wechat.utils.ImageSpider.getHTML;


public class HtmlUtils {

//    public static final String url_path = "https://www.rrjiaoyi.com/";
//    public static final String url_path = "D:/Program Files/新建文件夹/";
    public static final String url_path = "https://rrtest.rrjiaoyi.com/";
//    private static final String WECHAT="<span class=\"rich_media_meta rich_media_meta_nickname\" id=\"profileBt\">([\\s\\S]*?)</em>";

    public static final String video_path = "/mnt/video";

    private static final String QRCODE="<div class=\"qr_code_pc\">([\\s\\S]*?)</div>";

    private static final String CUE="<div class=\"rich_media_content \" id=\"js_content\">([\\s\\S]*?)</div>";

    private static final String VIDEO="<iframe class=\"video_iframe\".*?>([\\s\\S]*?)</iframe>";

    private static final String BACKGROUND="style=\"background-image:.*?\"";

    private static final String IMG_BACK="background-image: url\\(.*?\\)";

    private static final String IMG_BORDER="-webkit-border-image: url\\(.*?\\)";

    private static final String IMGSRC_REG = "[a-zA-z]+://[^\\s]*";

    private static final String IMG_VID = "vid=.*?\"";

    private static final String VA_VID = "([0-9a-zA-Z_]){10,}";

    /**
     * 替换指定标签的属性和值
     * @param str 需要处理的字符串
     * @param tag 标签名称
     * @param tagAttrib 要替换的标签属性值
     * @param startTag 新标签开始标记
     * @param endTag  新标签结束标记
     * @return
     * @author huweijun
     * @date 2016年7月13日 下午7:15:32
     */
    public static String replaceHtmlTag(String str, String tag, String tagAttrib, List<String> startTag, String endTag) throws InterruptedException {
        ImageSpider imageSpider = new ImageSpider();
        String strCue = null;
        String regxpForTag = "<\\s*" + tag + "\\s+([^>]*)\\s*" ;
        String regxpForTagAttrib = tagAttrib + "=\\s*\"([^\"]+)\"" ;
        Pattern patternForTag = Pattern.compile (regxpForTag,Pattern. CASE_INSENSITIVE );
        Pattern patternForAttrib = Pattern.compile (regxpForTagAttrib,Pattern. CASE_INSENSITIVE );


        Matcher matcherIMGBACK = Pattern.compile(IMG_BACK).matcher(str);
        Matcher matcherIMGBORDER = Pattern.compile(IMG_BORDER).matcher(str);
        while (matcherIMGBACK.find()) {
            Matcher matcherSRC = Pattern.compile(IMGSRC_REG).matcher(matcherIMGBACK.group());
            String url=null;
            String status;
            if(matcherSRC.find()) {
                url = matcherSRC.group();
            }
            String path=null;
            if (url != null) {
                path = file_path(url);
                path = url_path + "img/" + path.substring(path.indexOf("Article-images"));
            }
            str = str.replace(matcherIMGBACK.group(),"background-image: url(&quot;"+path+"&quot;)");
        }
        while (matcherIMGBORDER.find()) {
            Matcher matcherBORDER = Pattern.compile(IMGSRC_REG).matcher(matcherIMGBORDER.group());
            String url=null;
            if(matcherBORDER.find()) {
                url = matcherBORDER.group();
            }
            String path=null;
            if (url != null) {
                path = file_path(url);
                path = url_path + "img/" + path.substring(path.indexOf("Article-images"));
            }
            str = str.replace(matcherIMGBORDER.group(),"-webkit-border-image: url(&quot;"+path+"&quot;)");
        }

        Matcher matcherBack = Pattern.compile(BACKGROUND).matcher(str);
        if(matcherBack.find()){
            str = str.replace(matcherBack.group(),"style=\"\"");
        }
//        Matcher matcherWechat = Pattern.compile(WECHAT).matcher(str);
//        if (matcherWechat.find()) {
//            str = str.replace(matcherWechat.group(),"");
//            System.out.println(matcherWechat.group(1));
//        }
        Matcher matcherCue = Pattern.compile(CUE).matcher(str);
        if (matcherCue.find()) {
            strCue = matcherCue.group();
            StringBuffer string = new StringBuffer(strCue);
            if(str.contains("<iframe")){
                string.insert(strCue.length()-8,"<br><br><p style=\"text-align:center;\">尊重原创, 如有侵权请联系我们, 立即删除 \\n <!--");
            }else {
                string.insert(strCue.length()-8,"<br><br><p style=\"text-align:center;\">尊重原创, 如有侵权请联系我们, 立即删除");
            }
            str = str.replace(strCue,string);
        }
        Matcher matcherQR = Pattern.compile(QRCODE).matcher(str);
        if(matcherQR.find()){
            str = str.replace(matcherQR.group(),"");
        }
        Matcher matcherForTag = patternForTag.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result = matcherForTag.find();
        int m=0;
        while (result) {

            StringBuffer sbreplace = new StringBuffer( "<"+tag+" ");
            Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag.group(1));
            if (matcherForAttrib.find()&&m<startTag.size()) {
                System.out.println(matcherForAttrib.group());
                String attributeStr = matcherForAttrib.group(1);
//                matcherForAttrib.appendReplacement(sbreplace, startTag + attributeStr + endTag);
                String path=null;

                 if(startTag.get(m).contains("mmbiz/")){
                    path = url_path+"img/" + startTag.get(m).substring(28,78) + "."+ImageSpider.url_handle(startTag.get(m));
                }else if(startTag.get(m).contains("mmbiz_")) {
                     path = url_path + "img/" + startTag.get(m).substring(32, 82) + "." + ImageSpider.url_handle(startTag.get(m));
                 }else if (startTag.get(m).contains("/emoji_wx/") || startTag.get(m).contains("/emoji_ios/")) {
                     if (startTag.get(m).endsWith("\"")) {
//                                path= "D:/Program Files/新建文件夹/"+"Article-images/" + url1.substring(url1.lastIndexOf("/")+1,url1.length());
                         path = url_path + "img/" + startTag.get(m).substring(startTag.get(m).lastIndexOf("/") + 1, startTag.get(m).length());
                         System.out.println(path + "表情");
                     } else {
//                                path= "D:/Program Files/新建文件夹/"+"Article-images/" + url1.substring(url1.lastIndexOf("/")+1);
                         path = url_path + "img/" + startTag.get(m).substring(startTag.get(m).lastIndexOf("/") + 1);
                         System.out.println(path + "表情第二种情况");
                     }
                 } else {
                    System.out.println(startTag.get(m).substring(startTag.get(m).lastIndexOf("/")+1)+"2222222222222222222222222222");
                    System.out.println(startTag.get(m)+"1111111111111111111111111111111111");
                    path=url_path+"img/" +startTag.get(m).substring(startTag.get(m).lastIndexOf("/")+1);
                    if(!path.endsWith("png")||!path.endsWith("gif")||!path.endsWith("jpg")||!path.endsWith("jpeg")){
                        path= url_path+"img/" + startTag.get(m).substring(startTag.get(m).lastIndexOf("/")+1,startTag.get(m).lastIndexOf("/")+10) + "."+ImageSpider.url_handle(startTag.get(m));
                    }

                    System.out.println("path=================="+path);
                }
                matcherForAttrib.appendReplacement(sbreplace, "src=\"" + path + endTag);
                m+=1;
            }

            matcherForAttrib.appendTail(sbreplace);
            matcherForTag.appendReplacement(sb, sbreplace.toString());
            result = matcherForTag.find();
        }
        matcherForTag.appendTail(sb);
        return sb.toString();
    }

    public static String replaceHtmlVideo(String str) {

        Matcher matcherVideo = Pattern.compile(VIDEO).matcher(str);//找视频的iframe
        List<String> ss = new ArrayList<>();
        List<String> video = new ArrayList<>();
        while (matcherVideo.find()) {
            ss.add(matcherVideo.group());
            System.out.println(ss);
        }
        for(int i=0;i<ss.size();i++){
            Matcher matcher = Pattern.compile("[a-zA-z]+://[^\\s]*\"").matcher(ss.get(i));
            if (matcher.find()) {
                Matcher vid_img = Pattern.compile(IMG_VID).matcher(matcher.group());
                String src=null;
                if(vid_img.find()){
                    Matcher vid_value = Pattern.compile(VA_VID).matcher(vid_img.group());
                    if(vid_value.find()){

                        String url="https://mp.weixin.qq.com/mp/videoplayer?action=get_mp_video_play_url&__biz=MzI4MDU3Njk4MA==&mid=&idx=&vid="+vid_value.group()+"&uin=&key=&pass_ticket=&wxtoken=777&appmsg_token=&x5=0&f=json";
                        String html = null;
                        try {
                            html = getHTML(url);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        String title1 = null;
                        Matcher uri = Pattern.compile("[a-zA-z]+:.*?\"").matcher(html);
                        Matcher title = Pattern.compile("\"title\":\".*?\"").matcher(html);
                        if (uri.find()) {
                            System.out.println("找到了");
                            System.out.println(uri.group());
                            if (title.find()) {
                                title1=title.group().substring(9,title.group().length()-1);
                                System.out.println(title1);
                            }
                            String src1 = uri.group().substring(0,uri.group().length()-1).replace("\\","");
                            System.out.println(src1);
                            try {
                                downLoadFromUrl(src1, title1 + ".mp4", video_path);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            src=url_path+"video/"+title1+".mp4";
                        }else {
                            src = "https://v.qq.com/iframe/preview.html?vid="+vid_value.group();
                        }


                    }
                }
                video.add(src);
            }
        }
        for(int i=0;i<video.size();i++){
            String qq;
            if(video.get(i).startsWith(url_path)) {
                qq = "\t\t\t\t\t   <!-- 使用iframe嵌入第三方视频(例如:优酷视频、腾讯视频、土豆视频) -->\n" +
                        "\t\t\t\t\t<div style=\"margin: auto;\">\n" +
                        "\t\t\t\t\t    <video class=\"video_fill\" width=100% height=auto controls=\"controls\" src=\"" + video.get(i) + "\"></video>\n" +
                        "\t\t\t\t\t</div>";
            }else {
                qq = "\t\t\t\t\t   <!-- 使用iframe嵌入第三方视频(例如:优酷视频、腾讯视频、土豆视频) -->\n" +
                        "\t\t\t\t\t<div style=\"margin: auto;\">\n" +
                        "\t\t\t\t\t    <iframe width=100% height=auto  src=\"" + video.get(i) + "\" allowfullscreen frameborder=\"0\" class=\"video\"></iframe>\n" +
                        "\t\t\t\t\t</div>";
            }
            str = str.replace(ss.get(i), qq);
            System.out.println(qq);
        }

        System.out.println(video);
        return str;
    }

    public static String txt2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * Method: saveHtml
     * Description: save String to file
     * @param filepath
     * file path which need to be saved
     * @param str
     * string saved
     */
    public static void saveHtml(String filepath, String str){

        try {
            /*@SuppressWarnings("resource")
            FileWriter fw = new FileWriter(filepath);
            fw.write(str);
            fw.flush();*/
            OutputStreamWriter outs = new OutputStreamWriter(new FileOutputStream(filepath), "utf-8");
            outs.write(str);
//            System.out.print(str);
            outs.flush();
            outs.close();
        } catch (IOException e) {
            System.out.println("Error at save html...");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        String url = "https://mp.weixin.qq.com/s?__biz=MzIxMDIzODM1NA==&mid=2649457296&idx=1&sn=9acfefaefb9ea25e631d44fb2c2ac8db&scene=0#wechat_redirect";
        ImageSpider imageSpider = new ImageSpider();
        imageSpider.startCrawler(url);
        imageSpider.close();


        String html = getHTML(url);
        List<String> listUrl = ImageSpider.getImageURL(html);
        List<String> listSrc = ImageSpider.getImageSrc(listUrl);
        List<Integer> listRight = ImageSpider.getRightImage(html,listSrc);

        List<String> list = new ArrayList<>();
        System.out.println(listUrl+"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        if (listRight != null) {
            for(int i=0;i<listRight.size();i++){
                int m = listRight.get(i);
                list.add(listSrc.get(m));
                System.out.println(list);
            }
        }

        String newStr = replaceHtmlTag(html, "img", "data-src", listSrc, "\"");



//        saveHtml("C:/Users/admin/Desktop/photo/html.html",newStr);
        String neS = replaceHtmlVideo(newStr);
        saveHtml("C:/Users/admin/Desktop/photo/html.html",neS);

//        for (int i = 0; i < list.size(); i++) {
//            String src=list.get(i);
//            if (i == 0) {
//                String path_one;
//                if(src.contains("/mmbiz/")){
////                            String path_one = "D:/Program Files/新建文件夹/" + src.substring(32,82) + "."+url_handle(src);
//                    path_one = url_path+"Article-images/" + src.substring(28, 78) + "." + url_handle(src);
//                }else if(src.contains("/mmbiz_")){
////                            System.out.println(startTag.get(m).substring(startTag.get(m).lastIndexOf("/")+1)+"2222222222222222222222222222");
////                            System.out.println(startTag.get(m)+"1111111111111111111111111111111111");
//                    path_one = url_path+"Article-images/" + src.substring(32, 82) + "." + url_handle(src);
//
////                            String path_one= "D:/Program Files/新建文件夹/" + src.substring(src.lastIndexOf("/")+1,src.lastIndexOf("/")+10) + "."+url_handle(src);
//                    System.out.println("path=================="+path_one);
//                }else {
////                            System.out.println(startTag.get(m).substring(startTag.get(m).lastIndexOf("/")+1)+"2222222222222222222222222222");
////                            System.out.println(startTag.get(m)+"1111111111111111111111111111111111");
//                    path_one = url_path+"Article-images/" + src.substring(src.lastIndexOf("/")+1,src.lastIndexOf("/")+10) + "." + url_handle(src);
//
////                            String path_one= "D:/Program Files/新建文件夹/" + src.substring(src.lastIndexOf("/")+1,src.lastIndexOf("/")+10) + "."+url_handle(src);
//                    System.out.println("path=================="+path_one);
//                }
//
//                System.out.println("path_one=================="+path_one);
//            } else if (i == 1) {
//                String path_two;
//                if(src.contains("mmbiz/")){
//                    path_two = url_path +"Article-images/" + src.substring(28, 78) + "." + url_handle(src);
////                            String path_two = "D:/Program Files/新建文件夹/" + src.substring(32,82) + "."+url_handle(src);
//                }else if(src.contains("/mmbiz_")){
////                            System.out.println(startTag.get(m).substring(startTag.get(m).lastIndexOf("/")+1)+"2222222222222222222222222222");
////                            System.out.println(startTag.get(m)+"1111111111111111111111111111111111");
//                    path_two= url_path+"Article-images/" + src.substring(32, 82) + "." + url_handle(src);
//
////                            String path_one= "D:/Program Files/新建文件夹/" + src.substring(src.lastIndexOf("/")+1,src.lastIndexOf("/")+10) + "."+url_handle(src);
//                    System.out.println("path=================="+path_two);
//                }else {
////                            System.out.println(startTag.get(m).substring(startTag.get(m).lastIndexOf("/")+1)+"2222222222222222222222222222");
////                            System.out.println(startTag.get(m)+"1111111111111111111111111111111111");
//                    path_two = url_path +"Article-images/" + src.substring(src.lastIndexOf("/")+1,src.lastIndexOf("/")+10) + "." + ImageSpider.url_handle(src);
//
////                            String path_one= "D:/Program Files/新建文件夹/" + src.substring(src.lastIndexOf("/")+1,src.lastIndexOf("/")+10) + "."+url_handle(src);
//                }
//
//                System.out.println("path_two=================="+path_two);
//
//            } else if (i == 2) {
//                String path_three;
//                if(src.contains("mmbiz")){
////
//                    path_three = url_path +"Article-images/" + src.substring(32, 82) + "." + ImageSpider.url_handle(src);
////                            String path_two = "D:/Program Files/新建文件夹/" + src.substring(32,82) + "."+url_handle(src);
//                }else {
//                    path_three = url_path +"Article-images/" + src.substring(src.lastIndexOf("/")+1,src.lastIndexOf("/")+10) + "." + ImageSpider.url_handle(src);
//
////                            String path_one= "D:/Program Files/新建文件夹/" + src.substring(src.lastIndexOf("/")+1,src.lastIndexOf("/")+10) + "."+url_handle(src);
//                }
//                System.out.println("path_three=================="+path_three);
//            }
//            System.out.println("更新完第" + i + "张!!!!!!!!!!!!!!!!");
//        }
//        String fileName = "c:/regex.txt";
//        Map cidMap = new HashMap<String, String>();
//        List fckImages = new ArrayList<String>();
//        String str = ReadFromFile.readFile(fileName).toString();
//        String newStr = str;
//        Pattern pattern = Pattern
//                .compile("(?s)<img.*?(src=\"(.*?((common/FCKeditor.*?)|(cms/simpleDownload/?fileId=([0-9a-zA-Z]*))))\")");
//        /*
//         * (?s)//换行匹配开关，对应Pattern类中的字段DOTALL;
//         *
//         * <img.*?(src=\"(.*?((common/FCKeditor.*?)|(cms/simpleDownload /?fileId=([0-9a-zA-Z]*))))\")//整个img标签,group0
//         *
//         * .*?//表示非贪婪匹配，找到最近的就停止
//         *
//         * (src=\"(.*?((common/FCKeditor.*?)|(cms/simpleDownload /?fileId=([0-9a-zA-Z]*))))\")//整个src属性,group1
//         *
//         * (.*?((common/FCKeditor.*?)|(cms/simpleDownload \\?fileId=([0-9a-zA-Z]*))))//src属性的值，(包括url上下文，即应该根路径，eg:fms),group2
//         *
//         * ((common/FCKeditor.*?)|(cms/simpleDownload \\?fileId=([0-9a-zA-Z]*)))//src属性的值去掉url上下文，即 除去上下文的服务器web下相对路径，group3
//         *
//         * //group4和group5分别是用来匹配fck上传文件和从收件箱转发时原本就有的附件,group3的结果是在4和5之间二选一
//         *
//         * ([0-9a-zA-Z]*)//group6用来匹配group5中附件id
//         */
//        Matcher matcher = pattern.matcher(newStr);
//        while (matcher.find()) {
//            String img = matcher.group(0);
//            String src = matcher.group(1);
//            String url = matcher.group(2);
//            String g3_fckUrl = matcher.group(3);// 除去上下文的服务器web下相对路径
//            String fileId = matcher.group(6);// ([0-9a-zA-Z]*)
//            if (fileId != null) {
//                cidMap.put(fileId, "true");
//                String newUrl = "cid:" + fileId;
//                img = img.replace(url, newUrl);
//            } else {
//                String cid = UUID.randomUUID().toString();
//                String newUrl = "cid:" + cid;
//                img = img.replace(url, newUrl);
//                fckImages.add(g3_fckUrl);
//            }
//            System.out.println("group0--img tag is : ");
//            System.out.println(img);
//            System.out.println("group1--src attr is : ");
//            System.out.println("\t" + src);
//            System.out.println("group2--url is : ");
//            System.out.println("\t" + url);
//            System.out.println("group345--fckUrl or attUrl is : ");
//            System.out.println("\t" + g3_fckUrl);
//            System.out.println("group6--fileId is : ");
//            System.out.println("\t" + fileId);
//            System.out.println();
//        }
//        cidMap.put("fckImages", fckImages);
//        System.out.println(cidMap);
//        System.out.println(fckImages);


//        String str = " <img class=\"__bg_gif \" data-ratio=\"0.29469548133595286\" data-type=\"gif\" data-w=\"509\" width=\"auto\" data-src=\"https://mmbiz.qpic.cn/mmbiz_gif/6qiapmfprltavMiatibma9gXpWkL7oo7MGwD1uxKFsYzC6KelSsZoaCwUe3O8IjEz7G7eExHjibERsJ3FtK5MRODbA/640?wx_fmt=gif\" style=\"font-family: 微软雅黑;letter-spacing: 2px;text-align: justify;white-space: normal;color: rgb(89, 89, 89);font-size: 13px;background-color: rgb(255, 255, 255);box-sizing: border-box !important;word-wrap: break-word !important;visibility: visible !important;width: auto !important;\"  />";
//        /**
//         * 替换HTML标签
//         */
////        String subStr = str.replaceAll("<img(.*?)data-src=(.*?)[^>]*>.*?", "<2222222222>").replaceAll("<[^>]*>", "\n\t");
//        String src = "https://rrtest.rrjiaoyi.com/Article-images/6qiapmfprltavMiatibma9gXpWkL7oo7MGwD1uxKFsYzC6KelS.png";
//        String newStr = replaceHtmlTag(str, "img", "data-src", "src=\"http://junlenet.com/", "\"");
//        String subStr = str.replaceAll("<img(.*?)data-src=(.*?)[^>]*>.*?", "<img src=\""+src+"\">");
//        /**
//         * 打印替换后的字符串
//         */
//        System.out.println("打印替换后的字符串：" + subStr);

    }

}
