package com.github.binarywang.demo.wechat.handler;

import com.github.binarywang.demo.wechat.dao.BookRepository;
import com.github.binarywang.demo.wechat.dao.TourismRepository;
import com.github.binarywang.demo.wechat.dao.Wshys_userRepository;
import com.github.binarywang.demo.wechat.entity.Tourism;
import com.github.binarywang.demo.wechat.entity.Wshys_user;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class UnsubscribeHandler extends AbstractHandler {

  @Autowired
  private Wshys_userRepository wshys_userRepository;

  @Autowired
  private TourismRepository tourismRepository;

  @Autowired
  private BookRepository bookRepository;

  @Override
  public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                  Map<String, Object> context, WxMpService wxMpService,
                                  WxSessionManager sessionManager) throws WxErrorException {
    String openId = wxMessage.getFromUser();
    Integer userid = wshys_userRepository.findIdByOpenid(openId);

    wshys_userRepository.updateSubscribe("false",openId);
    this.logger.info("取消关注用户 OPENID: " + openId);
    // TODO 可以更新本地数据库为取消关注状态
////
//    if(userid != null) {
//      //发送模板消息
//      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//      Tourism tourism = tourismRepository.findByUserid(userid);
//      if (tourism != null) {
//        List<Wshys_user> users = wshys_userRepository.findByIdAndSubscribe(tourism.getTourismcommendid());
//        String openid = wshys_userRepository.findOpenidByID(tourism.getTourismcommendid());//取关用户所帮助的用户openid
//        Wshys_user wshys_user = wshys_userRepository.findByOpenid(openid);
//        String complete = wshys_user.getComplete();
//        System.out.println(complete);
//        Wshys_user unuser = wshys_userRepository.findByOpenid(openId);
//        //发送模板消息
//        // 测试服务器模板id:P0AGve613bOOT16vzjulopMYlqba2PxPD_RDY7-cJ50
//        //正式服务器模板id:Vui_Yd3t_5kTXee7M71zdp5zXy9IqimKRoydyw3E0tI
//        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
//                .toUser(openid)
//                .templateId("Vui_Yd3t_5kTXee7M71zdp5zXy9IqimKRoydyw3E0tI")
//                .url("")
//                .build();
//        templateMessage.addData(new WxMpTemplateData("first", "你有一个支持被取消。", "#C1272D"));//红色
//        if((complete==null||complete.isEmpty() || complete.equals("null")) && users.size()<people){
//          templateMessage.addData(new WxMpTemplateData("keyword1", unuser.getNickname(), "#1A1A1A"));
//          templateMessage.addData(new WxMpTemplateData("keyword2", df.format(new Date()), "#1A1A1A"));
//          templateMessage.addData(new WxMpTemplateData("remark", "目前你还差" + (people - users.size()) + "位小伙伴的支持，就可以免费领价值3280元双人6日云南游VIP卡啦。快快喊上亲朋好友来为你呐喊助威吧。", "#1A1A1A"));
//          wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
//        }
//      }
//    }

    return null;
  }

}
