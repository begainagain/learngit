package com.github.binarywang.demo.wechat.controller;

import com.github.binarywang.demo.wechat.dao.AnalysisRepository;
import com.github.binarywang.demo.wechat.dao.Hot_articleRepository;
import com.github.binarywang.demo.wechat.dao.ShareRepository;
import com.github.binarywang.demo.wechat.dao.Wshys_userRepository;
import com.github.binarywang.demo.wechat.entity.Analysis;
import com.github.binarywang.demo.wechat.entity.Share;
import com.github.binarywang.demo.wechat.entity.Wshys_user;
import com.github.binarywang.demo.wechat.pic.EncodeImgZingLogo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.github.binarywang.demo.wechat.utils.HtmlUtils.url_path;
import static com.github.binarywang.demo.wechat.utils.ImageSpider.url_final;


@RestController
@RequestMapping("/user/portal")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private Wshys_userRepository wshys_userRepository;

    @Autowired
    private Hot_articleRepository hot_articleRepository;

    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private ShareRepository shareRepository;

    private String rrjy = "rrjywwspb";

    @GetMapping(value = "/getjson")
    public JSONObject CeShi() throws ParseException {
        Date date=new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,-5);//把日期往前减少一天.整数往后推,负数往前移动
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(calendar.getTime());
        Date currentTime_2 = formatter.parse(dateString);
        System.out.println(currentTime_2);
        Analysis analysis = analysisRepository.findByRefdate(currentTime_2);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("refdate", analysis.getRefdate().toString());
        jsonObject.put("newuser", analysis.getNewuser());
        jsonObject.put("canceluser", analysis.getCanceluser());
        jsonObject.put("netincrease", analysis.getNetincrease());
        return jsonObject;
    }

    /*
    *微信用户授权
     */
    @GetMapping("/authorize")
    public String authorize(@RequestParam("status") String status, HttpServletResponse response) throws Exception {
            //设置回调地址
            String REDIRECT_URI = url_path+"wshys-api/user/portal/register";
            String SCOPE = "snsapi_userinfo";
                String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(REDIRECT_URI, SCOPE, status);
                response.sendRedirect(redirectUrl);
            return null;
    }

    /*
    *微信用户信息录入
     */
    @GetMapping(value = "register")
    public String  userres(@RequestParam("code") String code ,@RequestParam("state") String state
                           ,HttpServletResponse response) throws WxErrorException, IOException {
        String lang = "zh_CN";
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        WxMpUser oauth2getUserInfo = new WxMpUser();
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            logger.error("【微信网页授权】{}", e);
        }
        oauth2getUserInfo = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, lang);
        System.out.println(oauth2getUserInfo);
        JSONObject json = JSONObject.fromObject(oauth2getUserInfo);
        String openid = json.getString("openId");
        if (state.equals("art")){
            response.sendRedirect(url_path + "fm/#/artcon"+"?userid=" + openid);//跳转页面
        }else if(state.equals("1")){
            response.sendRedirect(url_path +"fm/#/august"+"?userid=" + openid);//跳转页面
        }else if (state.equals("0")){
            response.sendRedirect(url_path + "fm/#/"+"?userid=" + openid);//跳转页面
        }
//        response.sendRedirect(state+"/#/home"+"?userid=" + openid);//跳转页面
        String sex = json.getString("sexDesc");
        String country = json.getString("country");
        String province = json.getString("province");
        String language = json.getString("language");
        String city = json.getString("city");
        String nickname = json.getString("nickname");
        String headimgurl = json.getString("headImgUrl");
        String subscribe = json.getString("subscribe");
        System.out.println(subscribe);
        Wshys_user result = wshys_userRepository.findByOpenid(openid);
        if(null == result){
            Wshys_user user = new Wshys_user();
            user.setSex(sex);
            user.setCountry(country);
            user.setProvince(province);
            user.setLanguage(language);
            user.setCity(city);
            user.setNickname(nickname);
            user.setHeadimgurl(headimgurl);
            user.setOpenid(openid);
            user.setCreatetime(new Date());
            user.setSubscribe("false");
            wshys_userRepository.save(user);
        }else {
            result.setSex(sex);
            result.setCountry(country);
            result.setProvince(province);
            result.setLanguage(language);
            result.setCity(city);
            result.setNickname(nickname);
            result.setHeadimgurl(headimgurl);
            result.setOpenid(openid);
            wshys_userRepository.save(result);
        }

//        response.sendRedirect(state+"?userid=" + openid);
//        return "redirect:" + state + "?openid=" + openid;
        return null;
    }



    /*
    *用户分享功能以及分享的上下线绑定
    * shareid为分享链接的用户
    * openid为点击分享链接的用户
     */
    @PostMapping("/share/{shareuserid}")
    public Boolean invite(@RequestHeader("openid") String openid
            ,@PathVariable("shareuserid") String shareuserid){
            String userid = String.valueOf(wshys_userRepository.findIdByOpenid(shareuserid));//本人id  157
            String shareid = String.valueOf(wshys_userRepository.findIdByOpenid(openid));//点击分享链接的用户id  155
        if(userid.equals(shareid)){
            return false;
        }
            Share share = shareRepository.findByUserid(shareid);//通过shareid查询用户是否是第一次帮助其他用户并且绑定上线关系  1
            Share shared = shareRepository.findByUserid(userid);//通过userid查询用户是否是第一次参与活动并且绑定下线关系  1
            String t_shareid = shareRepository.findShareidByUserid(userid);//通过shareid找到点击用户分享链接的用户集体  157.155
            if (shareid.isEmpty() || shareid.equals("null")||userid.equals("null")) {
                return null;
            }
            if (null == shared) {//如果没有记录则进行第一次存储（下线关系）
                Share usershare = new Share();
                usershare.setUserid(userid);//设置用户表id为分享表被分享用户的shareid
                usershare.setShareid(shareid);//设置用户表id为分享表本人userid
                usershare.setSharetime(new Date());
                shareRepository.save(usershare);
            } else if (null == t_shareid || t_shareid.isEmpty()) {
                shareRepository.updateShareid(shareid, userid);
            } else {//如果有记录则在后面增加记录
                StringBuffer usershareid = new StringBuffer(t_shareid);//帮助过本人的用户id
                String sd = String.valueOf(usershareid);
                List a = shareRepository.checkid(userid, shareid, sd);
                if (a.size() == 0) {
                    String ushareid = String.valueOf(usershareid.append("," + shareid));
                    shareRepository.updateShareid(ushareid, userid);
                } else {
                    //重复的情况下给用户以某种提示
                    System.out.println("您重复了！");
                    return false;
                }
            }
            if (null == share) {//如果没有记录则进行上线绑定
                Share helpuser = new Share();
                helpuser.setUserid(shareid);//点击分享页面的用户id
                helpuser.setSharetime(new Date());//记录最新一次的更新
                helpuser.setHelpid(userid);//分享页面的用户id
                shareRepository.save(helpuser);
                return true;
            } else {
                String aa = shareRepository.findHelpidByUserid(shareid);
                List a = shareRepository.checkid(shareid, userid, aa);
                if (null == aa || aa.isEmpty()) {
                    shareRepository.updateHelpid(userid,shareid);
                    return true;
                } else if (a.size() == 0) {
                    StringBuffer userhelpid = new StringBuffer(aa);//帮助过用户的用户id
                    userhelpid.append("," + userid);//将用户id附加到帮助过用户的用户字段中
                    String ad = new String(userhelpid);
                    shareRepository.updateHelpid(ad, shareid);//更新帮助过用户的用户字段
                    return true;
                } else {
                    return false;
                }

            }
    }

    /*
    *展示用户上下线关系，用户帮助过的人和帮助过用户的人。
     */
    @GetMapping("/show")
    public JsonObject showmessage(@RequestHeader("openid") String openid){
        String userid=String.valueOf(wshys_userRepository.findIdByOpenid(openid));
        String shareid=shareRepository.findShareidByUserid(userid);//下线关系
        String helpid=shareRepository.findHelpidByUserid(userid);//上线关系
        String shelpid = String.valueOf(helpid);
        JsonArray array=new JsonArray();
        JsonArray array1=new JsonArray();
        JsonObject object= new JsonObject();
        if((null == shareid||shareid.isEmpty())&&(null==helpid||helpid.isEmpty())){
            return null;
        }
        if(null == shareid||shareid.isEmpty()) {
            System.out.println("空值哟");
        }else{
            String[] aa=shareid.split(",");
            for(String s:aa){
                Wshys_user a=wshys_userRepository.findById(Integer.valueOf(s));
                JsonObject lan1= new JsonObject();
                lan1.addProperty("nickname",a.getNickname());
                lan1.addProperty("headimgurl",a.getHeadimgurl());
                array1.add(lan1);
            }
            object.add("share",array1);
        }
        if(null==helpid||helpid.isEmpty()){
            System.out.println("空值");
        }
        else {
            String[] a=shelpid.split(",");
            for(String s:a){
                JsonObject lan = new JsonObject();
                Wshys_user user=wshys_userRepository.findById(Integer.valueOf(s));
                lan.addProperty("nickname",user.getNickname());
                lan.addProperty("headimgurl",user.getHeadimgurl());
                array.add(lan);
            }
            object.add("help",array);
        }
        return object;
    }

    /*
    *显示有多少人帮助过用户。
     */
    @GetMapping("/help")
    public Integer showhelptimes(@RequestHeader("openid") String openid){
//        if(!Token.equals("rrjyw")){
//            return null;
//        }
        Integer u = wshys_userRepository.findIdByOpenid(openid);
        if(null==u) {
        return null;
        }
        String userid = String.valueOf(wshys_userRepository.findIdByOpenid(openid));
        String shareid = shareRepository.findShareidByUserid(userid);
        if(null == shareid || shareid.isEmpty()){
            return null;
        }
        String[] help=shareid.split(",");
        System.out.println(help.length);
        return help.length;
    }

    /*
    *显示用户是否帮助过
     */
    @GetMapping("/whether-help/{shareuserid}")
    public Boolean showhelpmessage(@RequestHeader("openid") String openid,@PathVariable("shareuserid") String shareuserid){
        Wshys_user user = wshys_userRepository.findByOpenid(openid);
        if(null == user){
            System.out.println("openid或者shareid不存在或者缓存问题");
            return false;
        }
        String userid = String.valueOf(wshys_userRepository.findIdByOpenid(shareuserid));//本人id
        String ushareid = String.valueOf(wshys_userRepository.findIdByOpenid(openid));//点击用户分享网页的用户id
        System.out.println(wshys_userRepository.findIdByOpenid(shareuserid));
        Share share = shareRepository.findByUserid(ushareid);//通过userid查询用户是否是第一次参与活动
        Share share1 = shareRepository.findByUserid(userid);
        if(null == share || null == share1) {
            System.out.println("没帮助过");
            return false;//没有帮助过
        } else {
            String ss =share.getHelpid();
            System.out.println(ss+"......");
            if(null == share.getHelpid()||ss.isEmpty()){
                return false;
            }
            StringBuffer userhelpid = new StringBuffer(ss);//帮助过本人的用户id
            System.out.println(userhelpid);
            String sd = String.valueOf(userhelpid);
            List a =shareRepository.checkid(ushareid,userid,sd);
            System.out.println(a);
            System.out.println(a.size());
            if(a.size()==0){
                System.out.println("真的没帮助过");
                return false;//没有帮助过
            }else {
                //重复的情况下给用户以某种提示
                System.out.println("您重复了");
                return true;//帮助过
            }
        }
    }

    @PostMapping("/user-info")
    public JSONObject showUserInfo(@RequestHeader("openid") String openid){
        Wshys_user user = wshys_userRepository.findByOpenid(openid);
        if(null == user){
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nickname",user.getNickname());
        jsonObject.put("sex",user.getSex());
        jsonObject.put("country",user.getCountry());
        jsonObject.put("city",user.getCity());
        jsonObject.put("province",user.getProvince());
        jsonObject.put("language",user.getLanguage());
        jsonObject.put("headimgurl",user.getHeadimgurl());
        jsonObject.put("subscribe",user.getSubscribe());
        jsonObject.put("subscribe_time",user.getCreatetime());
        return jsonObject;
    }

    @PostMapping("/qrcode")
    public String Qrcode(@RequestHeader("number") int number,@RequestHeader("pass") String pass) throws WxErrorException{
        if(pass.equals("rrjywQRcode")) {
            for (int i = 1; i <= number; i++) {
                EncodeImgZingLogo encodeImgZingLogo = new EncodeImgZingLogo();
                //生成带参二维码
                int expire_seconds = 604800;
                String scene;
                if (i < 10) {
                    scene = "wshys_0" + i;
                } else {
                    scene = "wshys_" + i;
                }
                WxMpQrCodeTicket ticket = wxMpService.getQrcodeService().qrCodeCreateLastTicket(scene);
                File fromFile = wxMpService.getQrcodeService().qrCodePicture(ticket);

                File logImg = new File(url_final+"log.png");
                File sourcefile = new File(url_final+"pic/channelQRcode/" + scene + ".PNG");

                try {
                    BufferedImage buffImg = encodeImgZingLogo.water(fromFile, logImg);
                    encodeImgZingLogo.generateWater(buffImg, url_final+"pic/channelQRcode/" + scene + ".PNG");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 模板消息：活动状态变更通知
     * @param url       模板消息链接地址(可不填)
     * @param first     模板消息除标题最上方文字
     * @param keyword1  发起人:
     * @param keyword2  主题:
     * @param keyword3  时间:
     * @param keyword4  地址:
     * @return
     */
    @PostMapping("/sendMessage")
    public void sendMessage(@RequestParam("url") String url,@RequestParam("first") String first,
                            @RequestParam("keyword1") String keyword1,@RequestParam("keyword2") String keyword2,
                            @RequestParam("keyword3") String keyword3,@RequestParam("keyword4") String keyword4) throws WxErrorException {
        List<String> openid=wshys_userRepository.findBySubscribe("true");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        for(int i=0;i<openid.size();i++){
            //发送模板消息
            // 测试服务器模板id:7aFuZCMzxb7i7uPO1eZ9wa4ium5WGuhMrk5xx4hiOTc
            //正式服务器模板id: MUe8Sj2tE_VL7Y3rGN4SpUAG2NXZO81uqszIvZcQZJo
            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                    .toUser(openid.get(i))
                    .templateId("MUe8Sj2tE_VL7Y3rGN4SpUAG2NXZO81uqszIvZcQZJo")
                    .url(url)
                    .build();
            templateMessage.addData(new WxMpTemplateData("first", first, "#C1272D"));//红色
            templateMessage.addData(new WxMpTemplateData("keyword1", keyword1, "#1A1A1A"));
            templateMessage.addData(new WxMpTemplateData("keyword2", keyword2, "#1A1A1A"));
            templateMessage.addData(new WxMpTemplateData("keyword3", keyword3, "#1A1A1A"));
            templateMessage.addData(new WxMpTemplateData("keyword4", keyword4, "#1A1A1A"));
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        }
    }
}
