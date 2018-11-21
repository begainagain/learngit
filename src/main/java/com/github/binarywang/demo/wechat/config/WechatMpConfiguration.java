package com.github.binarywang.demo.wechat.config;

import com.github.binarywang.demo.wechat.handler.*;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.constant.WxMpEventConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static me.chanjar.weixin.common.api.WxConsts.*;

/**
 * wechat mp configuration
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@Configuration
@ConditionalOnClass(WxMpService.class)
@EnableConfigurationProperties(WechatMpProperties.class)
public class WechatMpConfiguration {
  @Autowired
  protected LogHandler logHandler;
  @Autowired
  protected NullHandler nullHandler;
  @Autowired
  protected KfSessionHandler kfSessionHandler;
  @Autowired
  protected StoreCheckNotifyHandler storeCheckNotifyHandler;
  @Autowired
  private WechatMpProperties properties;
  @Autowired
  private LocationHandler locationHandler;
  @Autowired
  private MenuHandler menuHandler;
  @Autowired
  private MsgHandler msgHandler;
  @Autowired
  private UnsubscribeHandler unsubscribeHandler;
  @Autowired
  private SubscribeHandler subscribeHandler;
  @Autowired
  private ScanHandler scanHandler;


  @Bean
  @ConditionalOnMissingBean
  public WxMpConfigStorage configStorage() {
    WxMpInMemoryConfigStorage configStorage = new WxMpInMemoryConfigStorage();
    configStorage.setAppId("wx320ec4c3f5f21074");//无声正式号
//    configStorage.setAppId("wxeceb2410ecc7e715");//人人交易网APP正式号
//    configStorage.setAppId("wx17c2fdd868e0b31d");//测试号
    configStorage.setSecret("a642dc74a7b0262753c54bc049ccb519");//无声正式号
//    configStorage.setSecret("b2108e68a03f7a0d4161bf58e26e73b8");//人人交易网APP正式号
//    configStorage.setSecret("1542ea3b49a908b01dee1e3662cb2f12");//测试号
    configStorage.setToken("rrjywwspb");
    configStorage.setAesKey("o5kGnSUdOxBZ1jOY6of5oMlKgA8uBWHQuLIMTdRM0w0");//无声正式号
//    configStorage.setAesKey("EsFtgRoFLTnrz84GKnSsZIveOsjarhMfzEAkA2EGDvm");//人人交易网APP正式号
//    configStorage.setAesKey("ILCdDPP2ScfqHud3BXqA7G8yy2I33GNaRB2lHuLztaO");//测试号
    return configStorage;
  }

  @Bean
  @ConditionalOnMissingBean
  public WxMpService wxMpService(WxMpConfigStorage configStorage) {
//        WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.okhttp.WxMpServiceImpl();
//        WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.jodd.WxMpServiceImpl();
//        WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.apache.WxMpServiceImpl();
    WxMpService wxMpService = new me.chanjar.weixin.mp.api.impl.WxMpServiceImpl();
    wxMpService.setWxMpConfigStorage(configStorage);
    return wxMpService;
  }

  @Bean
  public WxMpMessageRouter router(WxMpService wxMpService) {
    final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

    // 记录所有事件的日志 （异步执行）
    newRouter.rule().handler(this.logHandler).next();

    // 接收客服会话管理事件
    newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
        .event(WxMpEventConstants.CustomerService.KF_CREATE_SESSION)
        .handler(this.kfSessionHandler).end();
    newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
        .event(WxMpEventConstants.CustomerService.KF_CLOSE_SESSION)
        .handler(this.kfSessionHandler)
        .end();
    newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
        .event(WxMpEventConstants.CustomerService.KF_SWITCH_SESSION)
        .handler(this.kfSessionHandler).end();

    // 门店审核事件
    newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
        .event(WxMpEventConstants.POI_CHECK_NOTIFY)
        .handler(this.storeCheckNotifyHandler).end();

    // 自定义菜单事件
    newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
        .event(MenuButtonType.CLICK).handler(this.getMenuHandler()).end();

    // 点击菜单连接事件
    newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
        .event(MenuButtonType.VIEW).handler(this.nullHandler).end();

    // 关注事件
    newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
        .event(EventType.SUBSCRIBE).handler(this.getSubscribeHandler())
        .end();

    // 取消关注事件
    newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
        .event(EventType.UNSUBSCRIBE)
        .handler(this.getUnsubscribeHandler()).end();

    // 上报地理位置事件
    newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
        .event(EventType.LOCATION).handler(this.getLocationHandler())
        .end();

    // 接收地理位置消息
    newRouter.rule().async(false).msgType(XmlMsgType.LOCATION)
        .handler(this.getLocationHandler()).end();

    // 扫码事件
    newRouter.rule().async(false).msgType(XmlMsgType.EVENT)
        .event(EventType.SCAN).handler(this.getScanHandler()).end();

    // 默认
    newRouter.rule().async(false).handler(this.getMsgHandler()).end();

    return newRouter;
  }

  protected MenuHandler getMenuHandler() {
    return this.menuHandler;
  }

  protected SubscribeHandler getSubscribeHandler() {
    return this.subscribeHandler;
  }

  protected UnsubscribeHandler getUnsubscribeHandler() {
    return this.unsubscribeHandler;
  }

  protected KfSessionHandler getKfSessionHandler(){return  this.kfSessionHandler;}

  protected AbstractHandler getLocationHandler() {
    return this.locationHandler;
  }

  protected MsgHandler getMsgHandler() {
    return this.msgHandler;
  }

  protected AbstractHandler getScanHandler() {
    return this.scanHandler;
  }

}
