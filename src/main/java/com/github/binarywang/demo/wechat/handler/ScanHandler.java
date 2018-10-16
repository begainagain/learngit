package com.github.binarywang.demo.wechat.handler;

import com.github.binarywang.demo.wechat.builder.TextBuilder;
import com.github.binarywang.demo.wechat.dao.TourismRepository;
import com.github.binarywang.demo.wechat.dao.Wshys_userRepository;
import com.github.binarywang.demo.wechat.entity.Tourism;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public  class ScanHandler extends AbstractHandler {
    @Autowired
    private TourismRepository tourismRepository;

    @Autowired
    private Wshys_userRepository wshys_userRepository;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {
//        // 获取微信用户基本信息
        String commendid = wxMessage.getEventKey();
//        System.out.println(commendid);
//        int userid = userRepository.findIdByOpenid(wxMessage.getFromUser());
//        Tourism tourism = tourismRepository.findByUserid(userid);
//        if(wxMessage.getFromUser().equals(commendid)){
//            return new TextBuilder().build("请让朋友帮您扫码关注!!!!!!", wxMessage, weixinService);
//        }
//        if(null == tourism){
//            int tourismcommendid = userRepository.findIdByOpenid(commendid);
//            Tourism tour = new Tourism();
//            tour.setJointime(new Date());
//            tour.setUserid(userid);
//            tour.setCommendid(tourismcommendid);
//            tourismRepository.save(tour);
//            return new TextBuilder().build("帮助成功!!!!!!!!!!!!!", wxMessage, weixinService);
//        }else if(tourismRepository.findIdByUserid(userid) != null){
//            return new TextBuilder().build("帮助过了!!!!!!!!!!!!!",wxMessage, weixinService);
//        }
//        System.out.println("ta扫码了");
//        try {
//            return new TextBuilder().build("感谢关注!!!!!!!!!!!!!", wxMessage, weixinService);
//        } catch (Exception e) {
//            this.logger.error(e.getMessage(), e);
//        }
        System.out.println(commendid);

        return null;

    }
}
