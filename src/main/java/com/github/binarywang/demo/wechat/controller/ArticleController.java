package com.github.binarywang.demo.wechat.controller;


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.binarywang.demo.wechat.dao.Hot_articleRepository;
import com.github.binarywang.demo.wechat.dao.Pc_habitRepository;
import com.github.binarywang.demo.wechat.dao.User_habitRepository;
import com.github.binarywang.demo.wechat.dao.Wshys_userRepository;
import com.github.binarywang.demo.wechat.entity.Hot_article;
import com.github.binarywang.demo.wechat.entity.Pc_habit;
import com.github.binarywang.demo.wechat.entity.ProxyIP;
import com.github.binarywang.demo.wechat.entity.User_habit;
import com.github.binarywang.demo.wechat.utils.DataUtils;
import com.github.binarywang.demo.wechat.utils.ImageSpider;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.binarywang.demo.wechat.utils.HtmlUtils.*;
import static com.github.binarywang.demo.wechat.utils.ImageSpider.num_;
import static com.github.binarywang.demo.wechat.utils.ImageSpider.url_final;

@RestController
@RequestMapping("/article")
public class ArticleController {


    @Autowired
    private Hot_articleRepository hot_articleRepository;

    @Autowired
    private Wshys_userRepository wshys_userRepository;

    @Autowired
    private User_habitRepository user_habitRepository;

    @Autowired
    private Pc_habitRepository pc_habitRepository;

    private static final String JSONS ="\\[.*?\\]";

    /*
     *通过网页中json数据存到数据库中
     */
    @PostMapping("/save")
    public Boolean savehotarticle(@RequestParam("json") String json) throws ParseException {
        Matcher jsons = Pattern.compile(JSONS).matcher(json);
        if(jsons.find()){
            json=jsons.group();
        }else {
            return false;
        }
        JSONArray jsonArray = JSONArray.fromObject(json);
        for (int i = 0; i < jsonArray.size(); i++) {
            Hot_article hot_article = new Hot_article();
            String title = jsonArray.getJSONObject(i).getString("title");
            Hot_article article = hot_articleRepository.findByTitle(title);
            if (article != null) {
                System.out.println("重复了！！！！！");
            } else {
                if (jsonArray.getJSONObject(i).containsKey("author")) {
                    String author = jsonArray.getJSONObject(i).getString("author");
                    if(author.equals("贝贝")||author.equals("佛佛佛")||author.equals("轻松筹")||author.equals("分众专享")||author.equals("美团福利社")||author.equals("妙法佛音")|| author.equals("刘素云")||
                            author.equals("缘分天空")||author.equals("天使爱美丽")||author.equals("CoCo都可")||author.equals("闲云谷")||author.equals("闹闹女巫店")|| author.equals("天天炫拍")||
                            author.equals("幸福西饼")||author.equals("萨克斯")||author.equals("点点星光")||author.contains("中国人寿")||author.equals("一起听吧") || author.equals("肯德基")||
                            author.equals("国网湖北电力")||author.equals("汉堡王中国")||author.equals("必胜客")||author.equals("工银信用卡微讯")||author.equals("音乐早餐") || author.contains("银行")||
                            author.equals("平安普惠")||author.equals("麦当劳")||author.equals("平安驿站")||author.equals("珠海交警") || author.equals("和教授")|| author.equals("招商蛇口邮轮母港")||
                            author.equals("每日音乐")||author.equals("最爱听情歌")|| author.contains("中国石化")|| author.equals("平安车险")|| author.contains("联通") || author.contains("中国太平")||
                            author.contains("家乐福中国")||author.contains("捡书姑娘")||author.contains("专车")|| author.contains("超市")|| author.contains("瑞幸咖啡")|| author.contains("小米商城")||
                            author.contains("沃尔玛")||author.contains("华润")|| author.contains("爱的家园")|| author.contains("经典流行好音乐")|| author.contains("感恩生活")|| author.contains("主耶稣")||
                            author.contains("家家悦")||author.contains("美宜佳")|| author.contains("农村信用社")|| author.contains("顺丰")||author.contains("梨乡莱阳")|| author.contains("移动")||
                            author.contains("飞机大战")||author.contains("花点时间")||author.contains("沃尔玛")||author.contains("浦发")||author.contains("鲜丰水果")){
                        continue;
                    }else {
                        hot_article.setAuthor(jsonArray.getJSONObject(i).getString("author"));
                    }
                } else {
                    hot_article.setAuthor("没有作者信息");
                }
                hot_article.setSummary(jsonArray.getJSONObject(i).getString("summary"));
                hot_article.setTitle(title);
                hot_article.setType(jsonArray.getJSONObject(i).getString("type"));
                hot_article.setUrl(jsonArray.getJSONObject(i).getString("url"));
                hot_article.setClickscount(jsonArray.getJSONObject(i).getInt("clicksCount"));
                hot_article.setLikecount(jsonArray.getJSONObject(i).getInt("likeCount"));
                hot_article.setOriginalflag(jsonArray.getJSONObject(i).getInt("originalFlag"));
                String string = jsonArray.getJSONObject(i).getString("publicTime");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                hot_article.setUpdatetime(sdf.parse(string));
                hot_article.setImage_one(null);
                hot_article.setImage_two(null);
                hot_article.setImage_three(null);
                System.out.println(hot_article);
                hot_articleRepository.save(hot_article);
            }
        }
        return true;
    }

    /*
     *热文推荐
     */
    @PostMapping("/recommend")
    public JsonObject showRecommend(@RequestHeader("openid") String openid) {
        int userid = wshys_userRepository.findIdByOpenid(openid);
        User_habit user_habit = user_habitRepository.findByUserid(userid);
        if (null == user_habit) {
            User_habit habit = new User_habit();
            habit.setUserid(userid);
            habit.setHistory_time(new Date());
            habit.setToday(new Date());
            habit.setHistory_number(0);
            habit.setAdver_number(0);
            user_habitRepository.save(habit);
        } else {
            Date usertime = user_habit.getHistory_time();
            if (DataUtils.belongDate(usertime, new Date(), 1)) {//历史观看日期与今天日期间隔在一天内则为False
                user_habitRepository.updatehistory_time(new Date(), userid);
                user_habitRepository.updateHistory_Number(0, userid);
            } else {
                int s = user_habit.getHistory_number();
                user_habitRepository.updateHistory_Number(s + 1, userid);
            }
        }
        int num = user_habitRepository.findNumberByUserid(userid);
        String[] strings = new String[]{"广告","时事,民生", "财富,企业", "幽默,乐活", "时尚,体娱", "情感,文摘"};

        JsonObject object = new JsonObject();
        Calendar calendar = Calendar.getInstance();  //得到日历
        calendar.setTime(new Date());//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -4);






        Date datetime = calendar.getTime();
        for (String S : strings) {

            JsonArray array = new JsonArray();
//            List<Hot_article> hot_article = hot_articleRepository.findByTypeAndNumber(S,datetime,num);
            int numb;
            if(S.equals("广告")){
                int ad_num = user_habitRepository.findAdNumber(userid);
                System.out.println(ad_num+"广告");
                List<Hot_article> adver = hot_articleRepository.findByTime("广告");
                List<Hot_article> advertisement = hot_articleRepository.advertisement("广告",ad_num);
                //广告位
                for (int m = 0; m < advertisement.size(); m++) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("summary", advertisement.get(m).getSummary());
                    jsonObject.addProperty("url", advertisement.get(m).getUrl());
                    jsonObject.addProperty("title", advertisement.get(m).getTitle());
                    jsonObject.addProperty("type","广告");
                    jsonObject.addProperty("author", advertisement.get(m).getAuthor());
                    jsonObject.addProperty("image_one", advertisement.get(m).getImage_one());
                    jsonObject.addProperty("html",advertisement.get(m).getHtml());
                    jsonObject.addProperty("num", 1);
                    array.add(jsonObject);
                }
                object.add("广告", array);
                if(ad_num>=adver.size()-1){
                    user_habitRepository.updateAdver_Number(0,userid);
                }else {
                    user_habitRepository.updateAdver_Number(ad_num+1,userid);
                }
                continue;
            }
            String[] SS = S.split(",");
            List<Hot_article> hot_article = hot_articleRepository.findForNow(SS[0],SS[1],datetime, num);
            for (int i = 0; i < hot_article.size(); i++) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("summary", hot_article.get(i).getSummary());
                jsonObject.addProperty("title", hot_article.get(i).getTitle());
                jsonObject.addProperty("url", hot_article.get(i).getUrl());
                jsonObject.addProperty("author", hot_article.get(i).getAuthor());
                if (hot_article.get(i).getImage_one() == null) {
                    numb = 0;
                } else {
                    jsonObject.addProperty("image_one", hot_article.get(i).getImage_one());
                    if (hot_article.get(i).getImage_two() == null) {
                        numb = 1;
                    } else {
                        jsonObject.addProperty("image_two", hot_article.get(i).getImage_two());
                        if (hot_article.get(i).getImage_three() == null) {
                            numb = 2;
                        } else {
                            numb = 3;
                            jsonObject.addProperty("image_three", hot_article.get(i).getImage_three());
                        }
                    }
                }
                jsonObject.addProperty("html",hot_article.get(i).getHtml());
                jsonObject.addProperty("num", numb);
                array.add(jsonObject);
            }
            if (hot_article.size() == 0) {
                user_habitRepository.updatetoday_time(new Date(), userid);
                user_habitRepository.updateHistory_Number(0, userid);
            } else {
                object.add(S, array);
            }
//        return jsonObject;
        }
        return object;
    }

//    @PostMapping("/randomRecommend")
//    public void randomRecommend(){
//        JsonArray array = new JsonArray();
//        List<String> article = hot_articleRepository.findAllType();
//        System.out.println(article.size());
//        List<Article_class> classes=new LinkedList<>();
//        Random rand = new Random();
//        for(String s:article){
//            int randNumber = rand.nextInt(1000) + 1;
//            Article_class article_class = new Article_class();
//            article_class.setRandom(randNumber);
//            article_class.setArticle_class(s);
//            classes.add(article_class);
//        }
//        // 排序
//        Collections.sort(classes);
//        for(Article_class art:classes){
//            System.out.println("分类："+art.getArticle_class()+" 随机数："+art.getRandom());
//        }
//        JsonObject object =new JsonObject();
//        Calendar calendar = Calendar.getInstance();  //得到日历
//        calendar.setTime(new Date());//把当前时间赋给日历
//        calendar.add(Calendar.DAY_OF_MONTH, -5);
//        Date datetime = calendar.getTime();
//        for(int i=19;i<classes.size();i++){
//            List<Hot_article> hot_article = hot_articleRepository.findByTypeAndNumber(classes.get(i).getArticle_class(),datetime,num);
//            System.out.println(classes.get(i).getArticle_class());
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("title", hot_article.get(i).getTitle());
//            jsonObject.addProperty("summary", hot_article.get(i).getSummary());
//            jsonObject.addProperty("url", hot_article.get(i).getUrl());
//            jsonObject.addProperty("author", hot_article.get(i).getAuthor());
//            array.add(jsonObject);
//        }
//    }

    @PostMapping("/recommendByPC")
    public JsonObject showRecommendpc(@RequestHeader("pcid") String pcid) {
        Pc_habit pc_habit = pc_habitRepository.findByUserid(pcid);
        if (null == pc_habit) {
            Pc_habit habit = new Pc_habit();
            habit.setUserid(pcid);
            habit.setHistory_time(new Date());
            habit.setToday(new Date());
            habit.setHistory_number(0);
            pc_habitRepository.save(habit);
        } else {
            Date usertime = pc_habit.getHistory_time();
            if (DataUtils.belongDate(usertime, new Date(), 1)) {//历史观看日期与今天日期间隔在一天内则为False
                pc_habitRepository.updateHistory_Number(0, pcid);
                pc_habitRepository.updatehistory_time(new Date(), pcid);
            } else {
                int s = pc_habit.getHistory_number();
                pc_habitRepository.updateHistory_Number(s + 1, pcid);
            }
        }
        int num = pc_habitRepository.findNumberByUserid(pcid);
        String[] strings = new String[]{"时事,民生", "财富,企业", "幽默,乐活", "时尚,体娱", "情感,文摘"};
//        String[] strings = new String[]{"美食","健康"};

        JsonObject object = new JsonObject();
        Calendar calendar = Calendar.getInstance();  //得到日历
        calendar.setTime(new Date());//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -4);
        Date datetime = calendar.getTime();
        for (String S : strings) {
            JsonArray array = new JsonArray();
            String[] SS= S.split(",");
            System.out.println(SS[0]);
            //            List<Hot_article> hot_article = hot_articleRepository.findByTypeAndNumber(S,datetime,num);
            List<Hot_article> hot_article = hot_articleRepository.findNotVideo(SS[0],SS[1],datetime, num);

            int number=0;
            for (int i = 0; i < hot_article.size(); i++) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("title", hot_article.get(i).getTitle());
                jsonObject.addProperty("summary", hot_article.get(i).getSummary());
                jsonObject.addProperty("url", hot_article.get(i).getUrl());
                jsonObject.addProperty("author", hot_article.get(i).getAuthor());
                if (hot_article.get(i).getImage_one() == null) {
                    number = 0;
                } else {
                    jsonObject.addProperty("image_one", hot_article.get(i).getImage_one());
                    if (hot_article.get(i).getImage_two() == null) {
                        number = 1;
                    } else {
                        jsonObject.addProperty("image_two", hot_article.get(i).getImage_two());
                        if (hot_article.get(i).getImage_three() == null) {
                            number = 2;
                        } else {
                            number = 3;
                            jsonObject.addProperty("image_three", hot_article.get(i).getImage_three());
                        }
                    }
                }
                jsonObject.addProperty("html", hot_article.get(i).getHtml());
                jsonObject.addProperty("num", number);
                array.add(jsonObject);

            }
            if (hot_article.size() == 0) {
                pc_habitRepository.updateHistory_Number(0, pcid);
                pc_habitRepository.updatehistory_time(new Date(), pcid);
            } else {
                object.add(S, array);
            }
//        return jsonObject;
        }
        return object;
    }

    /*
     *通过类型搜索热文并返回json数据
     */
    @PostMapping("/bytype")
    public JsonObject findbyType(@RequestParam("type") String type) {
        List<Hot_article> articles = hot_articleRepository.findByTime(type);
        JsonArray array = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        int num;
        for (int i = 0; i < articles.size(); i++) {
            JsonObject object = new JsonObject();
            object.addProperty("title", articles.get(i).getTitle());
            object.addProperty("summary", articles.get(i).getSummary());
            object.addProperty("url", articles.get(i).getUrl());
            if (articles.get(i).getImage_one() == null) {
                num = 0;
            } else {
                object.addProperty("image_one", articles.get(i).getImage_one());
                if (articles.get(i).getImage_two() == null) {
                    num = 1;
                } else {
                    object.addProperty("image_two", articles.get(i).getImage_two());
                    if (articles.get(i).getImage_three() == null) {
                        num = 2;
                    } else {
                        object.addProperty("image_three", articles.get(i).getImage_three());
                        num = 3;
                    }
                }
            }
            object.addProperty("html",articles.get(i).getHtml());
            object.addProperty("num", num);
            array.add(object);
        }
        jsonObject.add("article_con", array);
        return jsonObject;
    }

    @PostMapping("/videoType")
    public void findVideo(){
        Calendar calendar = Calendar.getInstance();  //得到日历
        calendar.setTime(new Date());//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -4);
        Date datetime = calendar.getTime();
        List<Hot_article> video=hot_articleRepository.findForVideo(datetime);
        for(int i=0;i<video.size();i++){
            String html=video.get(i).getHtml();
            StringBuffer sb = new StringBuffer();
            try {
                //构建一URL对象
                URL url = new URL(html);
                //使用openStream得到一输入流并由此构造一个BufferedReader对象
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF8"));
                String line;
                //读取www资源
                while ((line = in.readLine()) != null)
                { sb.append(line); }
                if(String.valueOf(sb).contains("<iframe")&&String.valueOf(sb).contains("</iframe>")){
                    hot_articleRepository.updateOriginalflag(233,html);
                }
                in.close();
            } catch (Exception ex) {
                ex.printStackTrace(); }
        }
    }

    @PostMapping("/imageUrl")
    public void storeimage(@RequestHeader("method") String method, @RequestHeader("days") int n,@RequestHeader("num") int num) throws Exception {
        ImageSpider imageSpider = new ImageSpider();

        Calendar calendar = Calendar.getInstance();  //得到日历
        calendar.setTime(new Date());//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -n);
        Date datetime = calendar.getTime();
        List<String> url_all = new ArrayList<>();
        if (method.equals("all")) {
            url_all = hot_articleRepository.findUrl(datetime);
        } else if (method.equals("new")) {
            url_all = hot_articleRepository.findAllArticle();
        }
//
        for (String url_one : url_all) {
            Thread.sleep(5 * 1000); //设置暂停的时间 10 秒
            try {
                String urlimage;
                String url=url_one;
                if(url_one.startsWith("http:")) {
                    urlimage = url_one.substring(4);
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("https");
                    buffer.append(urlimage);
                    url = String.valueOf(buffer);
                    System.out.println(url+"urllllllllllllll");
                }
                imageSpider.startCrawler(url);//要爬取的网址
                System.out.println("要关闭这个网站链接了！！！！！");
                imageSpider.close();
                if (imageSpider.deleteWordHTML(imageSpider.getHTML(url), url)) {
                    hot_articleRepository.deleteURL(url_one);
                    System.out.println("删除成功,在执行阶段！！！！！！！！！！！！！！！！！！");
                    continue;
                }

                String html = imageSpider.getHTML(url);
                List<String> listUrl = ImageSpider.getImageURL(html);
                List<String> listSrc = ImageSpider.getImageSrc(listUrl);
                String newStr = replaceHtmlTag(html, "img", "data-src", listSrc, "\"");



//        saveHtml("C:/Users/admin/Desktop/photo/html.html",newStr);
                String neS = replaceHtmlVideo(newStr);
//        String final_html=neS.replace("</body>","-->\n" +
//                "\n" +
//                "    </body>");

//        Matcher matcherVideo = Pattern.compile(VIDEO).matcher(str);
//        List<String> ss = new ArrayList<>();
//        List<String> video = new ArrayList<>();
//        while (matcherVideo.find()) {
//            ss.add(matcherVideo.group());
//            System.out.println(ss);
//        }
//        for(int i=0;i<ss.size();i++){
//            Matcher matcher = Pattern.compile("[a-zA-z]+://[^\\s]*\"").matcher(ss.get(i));
////            System.out.println(matcher.group());
//            if (matcher.find()) {
//                String ssa  = matcher.group();
//                String src1 = ssa.substring(0,ssa.length()-1);
//                video.add(src1);
//            }
//        }
//        System.out.println(video);
//        String newStr1 = replaceHtmlTag(str, "iframe", "data-src", src, "\"");
//        System.out.println("       替换后为:"+newStr);
                String path_html = url_path +"html/" + url_one.substring(74,105) + "." + "html";
//                String path ="/data/wwwroot/www.rrjiaoyi.com/html/"+url_one.substring(74,105) + "." + "html";//无声或有声
                String path ="/data/wwwroot/rrtest.rrjiaoyi.com/html/"+url_one.substring(74,105) + "." + "html";//测试号
                saveHtml(path,neS);
                hot_articleRepository.updateHtml(path_html,url_one);
                

                int u = num_;
                List<String> list = imageSpider.ImageSpide(url);
                for (int i = 0; i < list.size(); i++) {
                    String src=list.get(i);
                    if (i == 0) {
                        String path_one;
                        path_one = url_path+"img/"+src.substring(src.lastIndexOf("/")+1);
//                        if(src.contains("/mmbiz/")){
////                            String path_one = "D:/Program Files/新建文件夹/" + src.substring(32,82) + "."+url_handle(src);
//                            path_one = url_path+"img/" + src.substring(28, 78) + "." + ImageSpider.url_handle(src);
//                        }else if(src.contains("mmbiz_")){
////                            System.out.println(startTag.get(m).substring(startTag.get(m).lastIndexOf("/")+1)+"2222222222222222222222222222");
////                            System.out.println(startTag.get(m)+"1111111111111111111111111111111111");
//                            path_one = url_path+"img/" + src.substring(32, 82) + "." + ImageSpider.url_handle(src);
//
////                            String path_one= "D:/Program Files/新建文件夹/" + src.substring(src.lastIndexOf("/")+1,src.lastIndexOf("/")+10) + "."+url_handle(src);
//                            System.out.println("path=================="+path_one);
//                        }else {
////                            System.out.println(startTag.get(m).substring(startTag.get(m).lastIndexOf("/")+1)+"2222222222222222222222222222");
////                            System.out.println(startTag.get(m)+"1111111111111111111111111111111111");
//                            path_one = url_path+"img/" + src.substring(src.lastIndexOf("/")+1,src.lastIndexOf("/")+10) + "." + ImageSpider.url_handle(src);
//
////                            String path_one= "D:/Program Files/新建文件夹/" + src.substring(src.lastIndexOf("/")+1,src.lastIndexOf("/")+10) + "."+url_handle(src);
//                            System.out.println("path=================="+path_one);
//                        }

//                        System.out.println("path_one=================="+path_one);
                        hot_articleRepository.updateOne(path_one, url_one);
                    } else if (i == 1) {
                        String path_two=list.get(i);
                        path_two = url_path+"img/"+src.substring(src.lastIndexOf("/")+1);
//                        if(src.contains("mmbiz/")){
//                            path_two = url_path +"img/" + src.substring(28, 78) + "." + ImageSpider.url_handle(src);
////                            String path_two = "D:/Program Files/新建文件夹/" + src.substring(32,82) + "."+url_handle(src);
//                        }else if(src.contains("mmbiz_")){
////                            System.out.println(startTag.get(m).substring(startTag.get(m).lastIndexOf("/")+1)+"2222222222222222222222222222");
////                            System.out.println(startTag.get(m)+"1111111111111111111111111111111111");
//                            path_two= url_path+"img/" + src.substring(32, 82) + "." + ImageSpider.url_handle(src);
//
////                            String path_one= "D:/Program Files/新建文件夹/" + src.substring(src.lastIndexOf("/")+1,src.lastIndexOf("/")+10) + "."+url_handle(src);
//                            System.out.println("path=================="+path_two);
//                        }else {
////                            System.out.println(startTag.get(m).substring(startTag.get(m).lastIndexOf("/")+1)+"2222222222222222222222222222");
////                            System.out.println(startTag.get(m)+"1111111111111111111111111111111111");
//                            path_two = url_path +"img/" + src.substring(src.lastIndexOf("/")+1,src.lastIndexOf("/")+10) + "." + ImageSpider.url_handle(src);
////                            String path_one= "D:/Program Files/新建文件夹/" + src.substring(src.lastIndexOf("/")+1,src.lastIndexOf("/")+10) + "."+url_handle(src);
//                        }
                        System.out.println("path_two=================="+path_two);
                        hot_articleRepository.updateTwo(path_two, url_one);
                    } else if (i == 2) {
                        String path_three=list.get(i);
                        path_three = url_path+"img/"+src.substring(src.lastIndexOf("/")+1);
//                        if(src.contains("/mmbiz_")){
//                            path_three= url_path+"img/" + src.substring(32, 82) + "." + ImageSpider.url_handle(src);
////                            String path_one= "D:/Program Files/新建文件夹/" + src.substring(src.lastIndexOf("/")+1,src.lastIndexOf("/")+10) + "."+url_handle(src);
//                            System.out.println("path=================="+path_three);
//                        }else if(src.contains("mmbiz/")){
//                            path_three = url_path +"img/" + src.substring(28, 78) + "." + ImageSpider.url_handle(src);
////                            String path_two = "D:/Program Files/新建文件夹/" + src.substring(32,82) + "."+url_handle(src);
//                        }else{
////                            System.out.println(startTag.get(m).substring(startTag.get(m).lastIndexOf("/")+1)+"2222222222222222222222222222");
////                            System.out.println(startTag.get(m)+"1111111111111111111111111111111111");
//                            path_three = url_path +"img/" + src.substring(src.lastIndexOf("/")+1,src.lastIndexOf("/")+10) + "." + ImageSpider.url_handle(src);
////                            String path_one= "D:/Program Files/新建文件夹/" + src.substring(src.lastIndexOf("/")+1,src.lastIndexOf("/")+10) + "."+url_handle(src);
//                        }
                        System.out.println("path_three=================="+path_three);
                        hot_articleRepository.updateThree(path_three, url_one);
                    }
                    System.out.println("更新完第" + i + "张!!!!!!!!!!!!!!!!");
                }
//        String url = "https://mp.weixin.qq.com/s?__biz=MzI2NDI3ODk0OQ==&mid=2247509272&idx=1&sn=529f8ccd909a5f61a73d7d404251bd4e&scene=0#wechat_redirect";
                System.out.println(list.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping("/delete")
    public void delete() {
        String filepath = url_final+"Article-images";//D盘下的file文件夹的目录
        System.out.println(filepath);
        File file = new File(filepath);//File类型可以是文件也可以是文件夹
        System.out.println(file.list().toString());
        File[] fileList = file.listFiles();//将该目录下的所有文件放置在一个File类型的数组中
        System.out.println(fileList.length);
        if(fileList.length>0) {
            for (int i = 0; i < fileList.length; i++) {
                System.out.println(fileList[i].getName().length());
                String filename = fileList[i].getName().substring(0,fileList[i].getName().lastIndexOf("."));
                if (filename.length() == 29||filename.length()==30) {//判断是否为文件
                    String path = url_final + "Article-images/" + fileList[i].getName();
                    File folder = new File(path);
                    folder.delete();
                    System.out.println(path+"已删除！！！！！！！！！");
                }
            }
        }
    }

}




