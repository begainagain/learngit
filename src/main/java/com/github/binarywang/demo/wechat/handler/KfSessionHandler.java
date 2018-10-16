package com.github.binarywang.demo.wechat.handler;

import com.github.binarywang.demo.wechat.builder.TextBuilder;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class KfSessionHandler extends AbstractHandler {


  @Override
  public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                  Map<String, Object> context, WxMpService wxMpService,
                                  WxSessionManager sessionManager) throws WxErrorException {
    //TODO 对会话做处理
    if(wxMessage.getEvent().equals("SUBSCRIBE")) {
      try {
        return new TextBuilder().build("嗨：你，欢迎来到微护造口家园\n" +
                "\n" + "做了造口手足无措，造口袋不知道用什么？\n" +
                "\n" + "你可在下方回复【造口礼包】，生成你的专属海报", wxMessage, wxMpService);
      } catch (Exception e) {
        this.logger.error(e.getMessage(), e);
      }
    }
      return null;
  }
}

