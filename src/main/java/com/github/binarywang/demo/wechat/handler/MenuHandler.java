package com.github.binarywang.demo.wechat.handler;

import com.github.binarywang.demo.wechat.dao.BookRepository;
import com.github.binarywang.demo.wechat.dao.AnalysisRepository;
import com.github.binarywang.demo.wechat.dao.Lucky_drawRepository;
import com.github.binarywang.demo.wechat.dao.Wshys_userRepository;
import com.github.binarywang.demo.wechat.entity.Analysis;
import com.github.binarywang.demo.wechat.entity.Lucky_draw;
import com.github.binarywang.demo.wechat.entity.Wshys_user;
import com.github.binarywang.demo.wechat.pic.ImageAndQRcode;
import com.github.binarywang.demo.wechat.pic.ImgUtils;
import com.github.binarywang.demo.wechat.utils.DataUtils;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpDataCubeService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpDataCubeServiceImpl;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeUserSummary;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import static me.chanjar.weixin.common.api.WxConsts.MenuButtonType;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MenuHandler extends AbstractHandler {

    @Autowired
    private WxMpService wxService;

    @Autowired
    private Wshys_userRepository wshys_userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AnalysisRepository inviteRepository;

    @Autowired
    private Lucky_drawRepository lucky_drawRepository;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {
        Wshys_user Wshys_user = wshys_userRepository.findByOpenid(wxMessage.getFromUser());//点击按钮用户的信息

//        int people = bookRepository.findTimes("MAX");
        // 获取微信用户基本信息
        WxMpUser userWxInfo = weixinService.getUserService().userInfo(wxMessage.getFromUser(), null);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//这个是你要转成后的时间的格式
        String sd = sdf.format(new Date());   // 时间戳转换成时间
        System.out.println(sd);
//    if(wxMessage.getEventKey().equals("NOW_ACT")){
//      //旅游卡上限
//      int cards = bookRepository.findBookNumber("MAX");
//      if(wshys_userRepository.findAllComplete().size()<cards) {
//        //点击回复1
//        WxMpKefuMessage message2 = WxMpKefuMessage
//                .TEXT()
//                .toUser(wxMessage.getFromUser())
//                .content("云南游VIP卡待领取通知：\n" +
//                        "\n" +
//                        "您有一张价值3280元的云南游VIP卡尚未免费领取。\n" +
//                        "\n" +
//                        "下方是您的专属海报，邀请"+people+"个好友扫码关注支持（重复关注无效）后，即可免费领取旅游卡，包邮到家~！\n" +
//                        "\n" +
//                        "PS：50张送完即止，先到先得哦")
//                .build();
//        wxService.getKefuService().sendKefuMessage(message2);
//        String time =String.valueOf(Wshys_user.getPostertime());
//        if(time.equals("null")|| null == Wshys_user.getPostertime() || DataUtils.belongDate(Wshys_user.getPostertime(),new Date(),3)) {//判断海报临时素材是否过期
//          String sex = userWxInfo.getSexDesc();
//          String openid = userWxInfo.getOpenId();
//          String nickname = userWxInfo.getNickname();
//          String headimgurl = userWxInfo.getHeadImgUrl();
//          String country = userWxInfo.getCountry();
//          String province = userWxInfo.getProvince();
//          String city = userWxInfo.getCity();
//          Wshys_user.setSex(sex);
//          Wshys_user.setNickname(nickname);
//          Wshys_user.setHeadimgurl(headimgurl);
//          Wshys_user.setCountry(country);
//          Wshys_user.setProvince(province);
//          Wshys_user.setCity(city);
//          wshys_userRepository.save(Wshys_user);
//          int expire_seconds = 604800;
//          String scene = openid;
//          //生成用户带参二维码
//          WxMpQrCodeTicket ticket = weixinService.getQrcodeService().qrCodeCreateTmpTicket(scene, expire_seconds);
//          File fromFile = weixinService.getQrcodeService().qrCodePicture(ticket);
//          //移动文件
//          File toFile =new  File(url_final+"pic/QRcode/"+Wshys_user.getOpenid()+".png");
//          fromFile.renameTo(toFile);
//
//          //下载用户头像和调整图片尺寸
//          ImgUtils imgUtils = new ImgUtils();
//          imgUtils.downloadImgURL(userWxInfo.getHeadImgUrl(),userWxInfo.getNickname());
//          try {
//            File file2=new File(url_final+"pic/HeadImg/"+userWxInfo.getNickname()+".png");
//            File file1=new File(url_final+"pic/QRcode/"+userWxInfo.getOpenId()+".png");
//            imgUtils.resizeImage(file1, new File(url_final+"pic/QRcode/"+userWxInfo.getOpenId()+".png"), 144,144);
//            imgUtils.resizeImage(file2, new File(url_final+"pic/HeadImg/"+userWxInfo.getNickname()+".png"), 90, 90);
//          } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//          }
//          //将用户头像裁剪成圆形
//          imgUtils.circleHeadImg(userWxInfo.getNickname());
//
//          //制作带用户头像和带参二维码的海报
//          ImageAndQRcode add = new ImageAndQRcode();
//          try {
//            add.addImageQRcode( userWxInfo.getNickname(),url_final+"pic/img.png",url_final+"pic/HeadImg/"+nickname+".png",
//                    url_final+"pic/QRcode/"+Wshys_user.getOpenid()+".png",url_final+"pic/"+userWxInfo.getOpenId()+".png",340,840,5,15);
//          } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//          }
//          // 过期之后重新添加临时素材并重新发送给用户
//          // 添加临时图片素材
//          File file = new File(url_final+"pic/"+userWxInfo.getOpenId()+".png");
//          String mediaType = "image";
//          WxMediaUploadResult res = weixinService.getMaterialService().mediaUpload(mediaType, file);
//          wshys_userRepository.updateMediaId(res.getMediaId(),userWxInfo.getOpenId());
//          wshys_userRepository.updatePosterTime(new Date(),Wshys_user.getOpenid());
//
//          //点击回复海报
//          WxMpKefuMessage message3 = WxMpKefuMessage
//                  .IMAGE()
//                  .toUser(wxMessage.getFromUser())
//                  .mediaId(res.getMediaId())
//                  .build();
//          wxService.getKefuService().sendKefuMessage(message3);
//        }else{
//          // 回复海报
//          WxMpKefuMessage message3 = WxMpKefuMessage
//                  .IMAGE()
//                  .toUser(wxMessage.getFromUser())
//                  .mediaId(Wshys_user.getMediaid())
//                  .build();
//          wxService.getKefuService().sendKefuMessage(message3);
//        }
//      }
//      else {
//        WxMpKefuMessage message = WxMpKefuMessage
//                .TEXT()
//                .toUser(wxMessage.getFromUser())
//                .content("很遗憾您错过了这次活动，不要灰心，再接再厉！！！\n" +
//                        "\n" +
//                        "免费活动不间断！！持续关注，更多大礼等着你~\n" +
//                        "\n" +
//                        "点击下方\"了解我们\"--“客服”↓↓↓\n" +
//                        "添加后有不定时神秘小礼物和现金奖励奉上哟~\n")
//                .build();
//        // 设置消息的内容等信息
//        wxService.getKefuService().sendKefuMessage(message);
//      }
//    }

    //点击抽奖事件
    if(wxMessage.getEventKey().equals("LUCKY_DRAW")){
        int times = bookRepository.findTimes("lucky_draw");
        Lucky_draw user = lucky_drawRepository.findByOpenid(wxMessage.getFromUser());
        int result;
        int user_number=0;
        if(user!=null){
            switch (times){
                case 1:
                    user_number=user.getRandom_num1();
                    break;
                case 2:
                    user_number=user.getRandom_num2();
                    break;
                case 3:
                    user_number=user.getRandom_num3();
                    break;
                case 4:
                    user_number=user.getRandom_num4();
                    break;
            }
        }
        System.out.println(user_number+"user_number");
        Random random = new Random();
        result = random.nextInt(90000000)+10000000;
        System.out.println(result+"result");
        if(user==null){
            Lucky_draw lucky_draw = new Lucky_draw();
            lucky_draw.setOpenid(wxMessage.getFromUser());
            lucky_draw.setTime(new Date());
            switch (times){
                case 1:
                    lucky_draw.setRandom_num1(result);
                    break;
                case 2:
                    lucky_draw.setRandom_num2(result);
                    break;
                case 3:
                    lucky_draw.setRandom_num3(result);
                    break;
                case 4:
                    lucky_draw.setRandom_num4(result);
                    break;
            }
            lucky_drawRepository.save(lucky_draw);
            System.out.println(result);
        }else if(user_number==0){
            lucky_drawRepository.upTime(new Date(),wxMessage.getFromUser());
            if(times==2){
                lucky_drawRepository.update2(result,wxMessage.getFromUser());
            }else if(times==3){
                lucky_drawRepository.update3(result,wxMessage.getFromUser());
            }else if(times==4){
                lucky_drawRepository.update4(result,wxMessage.getFromUser());
            }
        }else {
            if(times==1){
                result = user.getRandom_num1();
            }else if(times==2){
                result = user.getRandom_num2();
            }else if(times==3){
                result = user.getRandom_num3();
            }else{
                result = user.getRandom_num4();
            }
        }
        WxMpKefuMessage message3 = WxMpKefuMessage
              .TEXT()
              .toUser(wxMessage.getFromUser())
              .content("【抽奖&领奖】\n" +
                      "你的抽奖号码为："+String.valueOf(result)+"\n"+
                        "11月11日公布抽奖结果，届时将产生两位幸运儿，免费获得iPhone XS Max一台")
              .build();
      wxService.getKefuService().sendKefuMessage(message3);
    }
        //点击事件
        if(wxMessage.getEventKey().equals("THE_PRIZE")){
            WxMpKefuMessage message = WxMpKefuMessage
                    .TEXT()
                    .toUser(wxMessage.getFromUser())
                    .content("【抽奖结果】\n" +
                            "11月11日公布抽奖结果，届时将产生两位幸运儿，免费获得iPhone XS Max一台\n"+
                            "请耐心等待～")
                    .build();
            // 设置消息的内容等信息
            wxService.getKefuService().sendKefuMessage(message);
        }

        //点击事件
        if(wxMessage.getEventKey().equals("THE_LIST")){
            WxMpKefuMessage message = WxMpKefuMessage
                    .TEXT()
                    .toUser(wxMessage.getFromUser())
                    .content("【获奖结果及名单】\n" +
                            "敬请期待第一期获奖名单")
                    .build();
            // 设置消息的内容等信息
            wxService.getKefuService().sendKefuMessage(message);
        }

        //点击事件
        if(wxMessage.getEventKey().equals("CLICK_ZIYUAN")){
            WxMpKefuMessage message = WxMpKefuMessage
                    .TEXT()
                    .toUser(wxMessage.getFromUser())
                    .content("在线影院\n" +
                            "http://www.xiaozhi1.cn\n" +
                            "好事多磨，微信中无法打开\n" +
                            "请复制网址到其他浏览器（非微信中）打开")
                    .build();
            // 设置消息的内容等信息
            wxService.getKefuService().sendKefuMessage(message);
        }


//    String msg = String.format("type:%s, event:%s, key:%s",
//        wxMessage.getMsgType(), wxMessage.getEvent(),
//        wxMessage.getEventKey());
        if (MenuButtonType.VIEW.equals(wxMessage.getEvent())) {
            return null;
        }

        return null;
//    return WxMpXmlOutMessage.TEXT().content(msg)
//        .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
//        .build();
    }
}