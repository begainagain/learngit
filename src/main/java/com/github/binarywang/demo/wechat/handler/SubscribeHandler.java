package com.github.binarywang.demo.wechat.handler;

import com.github.binarywang.demo.wechat.dao.BookRepository;
import com.github.binarywang.demo.wechat.dao.ChannelRepository;
import com.github.binarywang.demo.wechat.dao.TourismRepository;
import com.github.binarywang.demo.wechat.dao.Wshys_userRepository;
import com.github.binarywang.demo.wechat.entity.Channel;
import com.github.binarywang.demo.wechat.entity.Tourism;
import com.github.binarywang.demo.wechat.entity.Wshys_user;
import com.github.binarywang.demo.wechat.pic.ImageAndQRcode;
import com.github.binarywang.demo.wechat.pic.ImgUtils;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class SubscribeHandler extends AbstractHandler {
    @Autowired
    private ChannelRepository channelRepository;

  @Autowired
  private Wshys_userRepository wshys_userRepository;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private TourismRepository tourismRepository;


  @Override
  public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                  Map<String, Object> context, WxMpService weixinService,
                                  WxSessionManager sessionManager) throws WxErrorException {



      // 获取微信用户基本信息
      WxMpUser userWxInfo = weixinService.getUserService().userInfo(wxMessage.getFromUser(), null);
      WxMpXmlOutMessage responseResult = null;
      try {
          responseResult = handleSpecial(wxMessage,weixinService);
      } catch (Exception e) {
          this.logger.error(e.getMessage(), e);
      }
      if (responseResult != null) {
          return responseResult;
      }
      System.out.println(userWxInfo);

//      int expire_seconds = 604800;
      String openid = userWxInfo.getOpenId();
      String sex = userWxInfo.getSexDesc();
      String nickname = userWxInfo.getNickname();
      String headimgurl = userWxInfo.getHeadImgUrl();
      Wshys_user user = wshys_userRepository.findByOpenid(openid);
      String country = userWxInfo.getCountry();
      String province = userWxInfo.getProvince();
      String city = userWxInfo.getCity();
      String language = userWxInfo.getLanguage();
      String scene = openid;
      Wshys_user result = new Wshys_user();
      if(user == null){
          result.setOpenid(openid);
          result.setSex(sex);
          result.setCountry(country);
          result.setProvince(province);
          result.setCity(city);
          result.setLanguage(language);
          result.setNickname(nickname);
          result.setHeadimgurl(headimgurl);
          result.setCreatetime(new Date());
          wshys_userRepository.save(result);
      }
//      User us = wshys_userRepository.findByOpenid(openid);
//      Tourism tour = tourismRepository.findByUserid(us.getId());
//      if(tour!=null) {
//          String s = wshys_userRepository.findOpenidByID(tour.getTourismcommendid());
//          int tourismcommendid = wshys_userRepository.findIdByOpenid(s);
//          List<Tourism> users = tourismRepository.findByTourismcommendid(tourismcommendid);
//          SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//          int cards = bookRepository.findBookNumber("MAX");
//          if (us.getSubscribe().equals("false") && users.size() <= people && wshys_userRepository.findAllComplete().size() < cards) {
//              //正式服务器模板id: TLowLA4phokX0UK73z5JzJrfkyRVhpnvYhKS4CkHU8E
//              // 测试服务器模板id:TLowLA4phokX0UK73z5JzJrfkyRVhpnvYhKS4CkHU8E
//              //发送模板消息
//              WxMpTemplateMessage temp = WxMpTemplateMessage.builder()
//                      .toUser(s)
//                      .templateId("TLowLA4phokX0UK73z5JzJrfkyRVhpnvYhKS4CkHU8E")
//                      .url("")
//                      .build();
//              if (users.size() < people) {
//                  temp.addData(new WxMpTemplateData("keyword1", userWxInfo.getNickname() + "\n任务目标:" + people + "人\n" + "已完成:" + users.size() + "人\n" + "你还差" + (people - users.size()) + "位小伙伴的支持，就可以免费领价值3280元双人6日云南游VIP卡啦。快快喊上亲朋好友来为你呐喊助威吧！", "#1A1A1A"));
//              } else {
//                  temp.addData(new WxMpTemplateData("keyword1", userWxInfo.getNickname() + "\n任务目标:" + people + "人\n" + "已完成:" + users.size() + "人\n" + "你的任务目标已经达成，赶快找客服领取旅游卡吧。", "#1A1A1A"));
//              }
//              temp.addData(new WxMpTemplateData("keyword2", df.format(new Date()), "#1A1A1A"));
//              temp.addData(new WxMpTemplateData("first", "你有一位新的朋友支持你啦！", "#C1272D"));//红色
//              weixinService.getTemplateMsgService().sendTemplateMsg(temp);
//          }
//      }
      //更新用户关注信息
      wshys_userRepository.updateSubscribe("true",userWxInfo.getOpenId());
//
      //关注回复1
      WxMpKefuMessage message1 = WxMpKefuMessage
              .TEXT()
              .toUser(userWxInfo.getOpenId())
              .content("你好，欢迎关注无声或有声!")
              .build();
      weixinService.getKefuService().sendKefuMessage(message1);

//      //关注回复1
//      WxMpKefuMessage message2 = WxMpKefuMessage
//              .TEXT()
//              .toUser(userWxInfo.getOpenId())
//              .content("点击公众号下方菜单栏中“抽大奖”\n"
//                      +"参与免费抽取iPhone XS Max活动")
//              .build();
//      weixinService.getKefuService().sendKefuMessage(message2);
//
//      //关注回复2
//      WxMpKefuMessage message2 = WxMpKefuMessage
//              .TEXT()
//              .toUser(userWxInfo.getOpenId())
//              .content("hi，"+nickname+"，人人交易网最新福利——“梦回云南，体验之旅”，免费领价值3280元双人6日游VIP卡。点击“梦回云南”菜单，获取专属的海报后邀请朋友支持，即可「免费包邮」领取哦~~\n" +
//                      "\n" +
//                      "点击了解详情→<a href='https://mp.weixin.qq.com/s/RVzOMSsNPB8koR2LeLjp3w'>6日5晚云南双人游免费送！ </a>\n" +
//                      "\n" +
//                      "活动时间：2018年8月16日—8月24日\n")
//              .build();
//      weixinService.getKefuService().sendKefuMessage(message2);
//
//      //判断用户是否是通过活动二维码进入
//      if(wxMessage.getTicket()!=null&&wxMessage.getEventKey().length()>=28) {
//
//          //生成用户带参二维码
//          WxMpQrCodeTicket ticket = weixinService.getQrcodeService().qrCodeCreateTmpTicket(scene, expire_seconds);
//          File fromFile = weixinService.getQrcodeService().qrCodePicture(ticket);
//          //移动文件
//          File toFile =new  File(url_final+"pic/QRcode/"+openid+".png");
//          fromFile.renameTo(toFile);
//
//          //下载用户头像和调整图片尺寸
//          ImgUtils imgUtils = new ImgUtils();
//          imgUtils.downloadImgURL(userWxInfo.getHeadImgUrl(),userWxInfo.getNickname());
//          try {
//              File file1=new File(url_final+"pic/QRcode/"+userWxInfo.getOpenId()+".png");
//              File file2=new File(url_final+"pic/HeadImg/"+userWxInfo.getNickname()+".png");
//              imgUtils.resizeImage(file1, new File(url_final+"pic/QRcode/"+userWxInfo.getOpenId()+".png"), 144,144);
//              imgUtils.resizeImage(file2, new File(url_final+"pic/HeadImg/"+userWxInfo.getNickname()+".png"), 90,90);
//          } catch (Exception e) {
//              // TODO Auto-generated catch block
//              e.printStackTrace();
//          }
//          //将用户头像裁剪成圆形
//          imgUtils.circleHeadImg(userWxInfo.getNickname());
//
//          //制作带用户头像和带参二维码的海报
//          ImageAndQRcode add = new ImageAndQRcode();
//          try {
//              add.addImageQRcode( userWxInfo.getNickname(),url_final+"pic/img.png",url_final+"pic/HeadImg/"+nickname+".png",
//                      url_final+"pic/QRcode/"+openid+".png",url_final+"pic/"+userWxInfo.getOpenId()+".png",340,840,5,15);
//          } catch (Exception e) {
//              // TODO Auto-generated catch block
//              e.printStackTrace();
//          }
//          // 添加临时图片素材
//          File file = new File(url_final+"pic/"+userWxInfo.getOpenId()+".png");
//          String mediaType = "image";
//          WxMediaUploadResult res = weixinService.getMaterialService().mediaUpload(mediaType, file);
//          wshys_userRepository.updateMediaId(res.getMediaId(),userWxInfo.getOpenId());
//          wshys_userRepository.updatePosterTime(new Date(),userWxInfo.getOpenId());
//
//          System.out.println(res.getMediaId());
//          System.out.println(fromFile);
////    System.out.println(toFile);
//          System.out.println(userWxInfo);
//
//          //带参二维码渠道回复1:
//          WxMpKefuMessage message3 = WxMpKefuMessage
//                  .TEXT()
//                  .toUser(wxMessage.getFromUser())
//                  .content("云南游VIP卡待领取通知：\n" +
//                          "\n" +
//                          "您有一张价值3280元的云南游VIP卡尚未免费领取。\n" +
//                          "\n" +
//                          "下方是您的专属海报，邀请"+people+"个好友扫码关注支持（重复关注无效）后，即可免费领取旅游卡，包邮到家~！\n" +
//                          "\n" +
//                          "PS：50张送完即止，先到先得哦")
//                  .build();
//          weixinService.getKefuService().sendKefuMessage(message3);
//
//          //带参二维码渠道回复2(发送微信海报)
//          WxMpKefuMessage message4 = WxMpKefuMessage
//                  .IMAGE()
//                  .toUser(wxMessage.getFromUser())
//                  .mediaId(res.getMediaId())
//                  .build();
//          weixinService.getKefuService().sendKefuMessage(message4);
//      }
//
      this.logger.info("新关注用户 OPENID: " + wxMessage.getFromUser());
//      //关注回复2，微信海报
//      try {
//          return null;
////          return new ImageBuilder().build(res.getMediaId(), wxMessage, weixinService);
//      } catch (Exception e) {
//          this.logger.error(e.getMessage(), e);
//      }
      return null;
  }

  /**
   * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
   */
  private WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage,WxMpService wxMpService) throws Exception {
      //TODO
//          //判断用户是否是通过推荐进入并关注公众号
          if (wxMessage.getTicket() != null) {
              //获取用户信息
              WxMpUser userWxInfo = wxMpService.getUserService().userInfo(wxMessage.getFromUser(), null);
              String commendid = wxMessage.getEventKey().substring(8);
              String openid = wxMessage.getFromUser();
              String nickname = userWxInfo.getNickname();
              String sex = userWxInfo.getSexDesc();
              String headimgurl = userWxInfo.getHeadImgUrl();
              String country = userWxInfo.getCountry();
              String province = userWxInfo.getProvince();
              String city = userWxInfo.getCity();
              String language = userWxInfo.getLanguage();
              System.out.println(openid + commendid);
//
              if (commendid.length() < 28) {
                  Channel channel = channelRepository.findByOpenid(openid);
                  if (null == channel) {
                      Channel chan = new Channel();
                      chan.setChannel(commendid);
                      chan.setOpenid(openid);
                      chan.setRecent_time(new Date());
                      channelRepository.save(chan);
                  } else {
                      channelRepository.updateChannel(commendid, openid);
                      channelRepository.updateRecent_Time(new Date(), openid);
                  }
              }
          }
// else {
//                  User result = wshys_userRepository.findByOpenid(openid);
//                  if (result == null) {
//                      int cards = bookRepository.findBookNumber("MAX");
//                      int people = bookRepository.findPeopleNumber("MAX");
//                      User user = new User();
//                      user.setOpenid(openid);
//                      user.setSex(sex);
//                      user.setCountry(country);
//                      user.setProvince(province);
//                      user.setCity(city);
//                      user.setLanguage(language);
//                      user.setNickname(nickname);
//                      user.setCreatetime(new Date());
//                      user.setHeadimgurl(headimgurl);
//                      user.setSubscribe("true");
//                      wshys_userRepository.save(user);
//}
          return null;
      }
  }
