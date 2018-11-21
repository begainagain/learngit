package com.github.binarywang.demo.wechat.controller;

import com.github.binarywang.demo.wechat.dao.BookRepository;
import com.google.gson.JsonObject;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import static com.github.binarywang.demo.wechat.utils.ImageSpider.getLevelStr;

@RestController
@RequestMapping("/minipro")
public class MiniProController {

    @Autowired
    BookRepository bookRepository;

    @PostMapping("/DealFollow")
    public JSONObject DealFollow() throws IOException {
        int number = bookRepository.findTimes("DealFollow");


        String url = "http://api.rrjiaoyi.com/stock/httpServiceImpl/doStock";
        int a = number*9;
        if(number<3){
            number+=1;
        }else {
            number=0;
        }
        bookRepository.updatetimes(number,"DealFollow");
        String result1 = null;
        String result2 = null;
        String result3 = null;
        for (int i = 0; i < 3; i++) {
            String JSON = "{\n" +
                    "\t\"header\":{\n" +
                    "\t\t\t\t\t\t\t\"action\":\"S064\",\n" +
                    "\t\t\t\t\t\t\t\"code\":\"0\",\n" +
                    "\t\t\t\t\t\t\t\"devicetype\":\"0\",\n" +
                    "\t\t\t\t\t\t\t\"msgtype\":0,\n" +
                    "\t\t\t\t\t\t\t\"sendingtime\":\"2017-09-27 10:39:12.744\",\n" +
                    "\t\t\t\t\t\t\t\"version\":\"1.0.1\"\n" +
                    "\t\t\t\t\t\t},\n" +
                    "\t                       \"startIndex\":" + a + "\n" +
                    "}";
            //创建连接对象
            HttpClient httpClient = new HttpClient();
            //创建请求
            PostMethod method = new PostMethod(url);
            //设置请求头格式为json格式
            RequestEntity entity = new StringRequestEntity(JSON, "application/json", "UTF-8");
            //设置请求体信息
            method.setRequestEntity(entity);
            //设置请求头信息
            method.setRequestHeader("APPKEY", "C6C6E17AC153ED8DF8D61207");
            //创建连接
            try {
                httpClient.executeMethod(method);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //获取返回信息
            InputStream in = null;
            try {
                in = method.getResponseBodyAsStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStreamReader isr = new InputStreamReader(in, "UTF-8");
            char[] b = new char[4096];
            StringBuilder sb = new StringBuilder();
            for (int n; (n = isr.read(b)) != -1; ) {
                sb.append(new String(b, 0, n));
            }
            JSONObject json = JSONObject.fromObject(sb);
            String returnStr = sb.toString();


            int level = 0;
            //存放格式化的json字符串
            StringBuffer jsonForMatStr = new StringBuffer();
            for (int index = 0; index < returnStr.length(); index++)//将字符串中的字符逐个按行输出
            {
                //获取s中的每个字符
                char c = returnStr.charAt(index);
                //level大于0并且jsonForMatStr中的最后一个字符为\n,jsonForMatStr加入\t
                if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                    jsonForMatStr.append(getLevelStr(level));
                }
                switch (c) {
                    case '{':
                    case '[':
                        jsonForMatStr.append(c + "\n");
                        level++;
                        break;
                    case ',':
                        jsonForMatStr.append(c + "\n");
                        break;
                    case '}':
                    case ']':
                        jsonForMatStr.append("\n");
                        level--;
                        jsonForMatStr.append(getLevelStr(level));
                        jsonForMatStr.append(c);
                        break;
                    default:
                        jsonForMatStr.append(c);
                        break;
                }
            }
            System.out.println(jsonForMatStr);
            String jn = String.valueOf(jsonForMatStr);

            switch (i) {
                case 0:
                    result1 = jn.substring(jn.indexOf("[") + 1, jn.indexOf("]"));
                case 1:
                    result2 = jn.substring(jn.indexOf("[") + 1, jn.indexOf("]"));
                case 2:
                    result3 = jn.replace("[",  "[" + result1 + "," + result2 +"," );
            }
            System.out.println(result1 + "r111111111111111111");

            System.out.println(result2);
            a += 3;
        }
        JSONObject array = JSONObject.fromObject(result3);
//        System.out.println(array.getString("result"));
        return array;
    }

    @PostMapping("/LIST")
    public JSONObject List(@RequestHeader("api") String api) throws IOException {
        String action =null;
        String more = null;
        if (api.equals("month")) {
            action = "S042";
            more = "\t                    \"profitType\":\"\",\n" +
                    "\t                     \"style_id\":\"4\"\n";
        }else if(api.equals("continuity")){
            action = "S062";
            more = "\t                    \"type\":\"1\"";
        }else if(api.equals("total")){
            action = "S062";
            more = "\t                    \"type\":\"3\"";
        }else if(api.equals("steady")){
            action = "S043";
            more = "\t                    \"type\":\"2\"";
        }
        String url = "http://api.rrjiaoyi.com/stock/httpServiceImpl/doStock";
        int a = 0;
        String result1 = null;
        String result2 = null;
        String JSON = "{\n" +
                "\t\t\t\t\"header\":\n" +
                "\t\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\t\"action\":\""+action+"\",\n" +
                "\t\t\t\t\t\t\t\"code\":\"0\",\n" +
                "\t\t\t\t\t\t\t\"devicetype\":\"0\",\n" +
                "\t\t\t\t\t\t\t\"msgtype\":0,\n" +
                "\t\t\t\t\t\t\t\"sendingtime\":\"2016-09-27 10:39:12.744\",\n" +
                "\t\t\t\t\t\t\t\"version\":\"1.0.1\",\n" +
                "\t\t\t\t\t\t\t\"page\":{\"index\":\"1\",\"size\":\"10\"}\n" +
                "\t\t\t\t\t\t},\n" +
                more+
                "\t\t\t\t}\n";
        //创建连接对象
        HttpClient httpClient = new HttpClient();
        //创建请求
        PostMethod method = new PostMethod(url);
        //设置请求头格式为json格式
        RequestEntity entity = new StringRequestEntity(JSON, "application/json", "UTF-8");
        //设置请求体信息
        method.setRequestEntity(entity);
        //设置请求头信息
        method.setRequestHeader("APPKEY", "C6C6E17AC153ED8DF8D61207");
        //创建连接
        try {
            httpClient.executeMethod(method);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取返回信息
        InputStream in = null;
        try {
            in = method.getResponseBodyAsStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(in, "UTF-8");
        char[] b = new char[4096];
        StringBuilder sb = new StringBuilder();
        for (int n; (n = isr.read(b)) != -1; ) {
            sb.append(new String(b, 0, n));
        }
        JSONObject json = JSONObject.fromObject(sb);
        String returnStr = sb.toString();


        int level = 0;
        //存放格式化的json字符串
        StringBuffer jsonForMatStr = new StringBuffer();
        for (int index = 0; index < returnStr.length(); index++)//将字符串中的字符逐个按行输出
        {
            //获取s中的每个字符
            char c = returnStr.charAt(index);
            //level大于0并且jsonForMatStr中的最后一个字符为\n,jsonForMatStr加入\t
            if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
            }
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c + "\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c + "\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }
        System.out.println(jsonForMatStr);
        String jn = String.valueOf(jsonForMatStr);

//            switch (i) {
//                case 0:
        result1 = jn.substring(jn.indexOf("[") + 1, jn.indexOf("]"));
//                case 1:
//                    result2 = jn.replace("]", "," + result1 + "]");
//            }
        System.out.println(result1 + "r111111111111111111");

        System.out.println(result2);
        a += 3;

        JSONObject array = JSONObject.fromObject(jn);
        return array;
    }

}
