package com.github.binarywang.demo.wechat.controller;

import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.bean.menu.WxMenuRule;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMenuService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpGetSelfMenuInfoResult;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.github.binarywang.demo.wechat.utils.HtmlUtils.url_path;
import static me.chanjar.weixin.common.api.WxConsts.MenuButtonType;

/**
 * <pre>
 *  注意：此contorller 实现WxMpMenuService接口，仅是为了演示如何调用所有菜单相关操作接口，
 *      实际项目中无需这样，根据自己需要添加对应接口即可
 * </pre>
 *
 * @author Binary Wang(https://github.com/binarywang)
 */
@RestController
@RequestMapping("/wechat/menu")
public class WxMenuController implements WxMpMenuService {

    @Autowired
    private WxMpService wxService;

    /**
     * <pre>
     * 自定义菜单创建接口
     * 详情请见：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141013&token=&lang=zh_CN
     * 如果要创建个性化菜单，请设置matchrule属性
     * 详情请见：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1455782296&token=&lang=zh_CN
     * </pre>
     *
     * @param menu
     * @return 如果是个性化菜单，则返回menuid，否则返回null
     */
    @Override
    @PostMapping("/create")
    public String menuCreate(@RequestBody WxMenu menu) throws WxErrorException {
        return this.wxService.getMenuService().menuCreate(menu);
    }

    @GetMapping("/create")
    public String menuCreateone() throws WxErrorException{
        WxMenu menu = new WxMenu();

        WxMenuButton button1 = new WxMenuButton();
        button1.setName("抽大奖");




        WxMenuButton button2 = new WxMenuButton();
        button2.setType(MenuButtonType.VIEW);
        button2.setName("热文");
        button2.setUrl(url_path+"fm?v=1呸.04");


        WxMenuButton button3 = new WxMenuButton();
        button3.setName("游戏");





//    WxMenuButton button2 = new WxMenuButton();
//    button2.setName("热门活动");


//    button2.setType(MenuButtonType.VIEW);
//    button2.setName("活动页面");
//    button2.setUrl("http://rrtest.rrjiaoyi.com/fm//#/august?shareid=oJ_Nl0q08QpcjvmEfMfErpgwU8BU");
//    button2.setKey("EVENT_PAGE");

//        WxMenuButton button3 = new WxMenuButton();
//        button3.setName("推荐");


//        WxMenuButton button2 = new WxMenuButton();
//        button2.setType(WxConsts.BUTTON_MINIPROGRAM);
//        button2.setName("小程序");
//        button2.setAppId("wx286b93c14bbf93aa");
//        button2.setPagePath("pages/lunar/index.html");
//        button2.setUrl("http://mp.weixin.qq.com");


        menu.getButtons().add(button1);
        menu.getButtons().add(button2);
        menu.getButtons().add(button3);



//    button1.setKey("NOW_ACT");

        WxMenuButton button11 = new WxMenuButton();
        button11.setType(MenuButtonType.CLICK);
        button11.setName("抽奖");
        button11.setKey("LUCKY_DRAW");

        WxMenuButton button12 = new WxMenuButton();
        button12.setType(MenuButtonType.CLICK);
        button12.setName("领奖");
        button12.setKey("THE_PRIZE");

        button1.getSubButtons().add(button11);
        button1.getSubButtons().add(button12);



        WxMenuButton button31 = new WxMenuButton();
        button31.setType(MenuButtonType.VIEW);
        button31.setName("Flappy Bird");
        button31.setUrl("https://www.rrjiaoyi.com/flappy/");

        WxMenuButton button32 = new WxMenuButton();
        button32.setType(MenuButtonType.VIEW);
        button32.setName("考眼力");
        button32.setUrl("https://www.rrjiaoyi.com/games/zuiqiangyanli/");

        WxMenuButton button33 = new WxMenuButton();
        button33.setType(MenuButtonType.VIEW);
        button33.setName("打飞机");
        button33.setUrl("https://www.rrjiaoyi.com/games/plane/");

        WxMenuButton button34 = new WxMenuButton();
        button34.setType(MenuButtonType.VIEW);
        button34.setName("拆散贱人");
        button34.setUrl("https://www.rrjiaoyi.com/games/jianren/");

        button3.getSubButtons().add(button31);
        button3.getSubButtons().add(button32);
        button3.getSubButtons().add(button33);
        button3.getSubButtons().add(button34);

//
//        WxMenuButton button31 = new WxMenuButton();
//        button31.setType(MenuButtonType.CLICK);
//        button31.setName("观影券");
//        button31.setKey("CLICK_ZIYUAN");
//
//        WxMenuButton button33 = new WxMenuButton();
//        button33.setType(MenuButtonType.VIEW);
//        button33.setName("人人交易网");
//        button33.setUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.sm.stock.smstocktrade&channel=0002160650432d595942&fromcase=60001");
//        button33.setKey("DOWNLOAD_APP");

//    WxMenuButton button34 = new WxMenuButton();
//    button34.setType(MenuButtonType.CLICK);
//    button34.setName("客服");
//    button34.setKey("CLICK_KEFU");

//        button3.getSubButtons().add(button31);
//    button3.getSubButtons().add(button32);
//        button3.getSubButtons().add(button33);
//    button3.getSubButtons().add(button34);

        return this.wxService.getMenuService().menuCreate(menu);
    }

//  @GetMapping("/create")
//  public String menuCreateSample() throws WxErrorException {
//    WxMenu menu = new WxMenu();
//    WxMenuRule rule1 = new WxMenuRule();
//    rule1.setTagId("100");
//
//    WxMenuButton button1 = new WxMenuButton();
//    button1.setType(MenuButtonType.CLICK);
//    button1.setName("免费领书");
//    button1.setKey("NOW_ACT");
//
////    WxMenuButton button2 = new WxMenuButton();
////    button2.setType(MenuButtonType.VIEW);
////    button2.setName("了解我们");
////    button2.setUrl("http://www.rrjiaoyi.com/");
////    button2.setKey("KNOW_US");
//
//      WxMenuButton button3 = new WxMenuButton();
//      button3.setName("关于我们");
//
//
////        WxMenuButton button2 = new WxMenuButton();
////        button2.setType(WxConsts.BUTTON_MINIPROGRAM);
////        button2.setName("小程序");
////        button2.setAppId("wx286b93c14bbf93aa");
////        button2.setPagePath("pages/lunar/index.html");
////        button2.setUrl("http://mp.weixin.qq.com");
//
//
////
//    menu.getButtons().add(button1);
////    menu.getButtons().add(button2);
//    menu.getButtons().add(button3);
//    menu.setMatchRule(rule1);
////
//    WxMenuButton button31 = new WxMenuButton();
//    button31.setType(MenuButtonType.VIEW);
//    button31.setName("官网");
//    button31.setUrl("http://wang.mynatapp.cc/user/portal/authorize");
////    button31.setUrl("http://wang.mynatapp.cc/user/portal/authorize?returnUrl=/user/portal/register");
////
//    WxMenuButton button32 = new WxMenuButton();
//    button32.setType(MenuButtonType.CLICK);
//    button32.setName("客服");
//    button32.setKey("CLICK_KEFU");
////
//    WxMenuButton button33 = new WxMenuButton();
//    button33.setType(MenuButtonType.VIEW);
//    button33.setName("app下载");
//    button33.setUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.sm.stock.smstocktrade&channel=0002160650432d595942&fromcase=60001");
//    button33.setKey("DOWNLOAD_APP");
////
//    button3.getSubButtons().add(button31);
//    button3.getSubButtons().add(button32);
////    button3.getSubButtons().add(button33);
//
//
//    return this.wxService.getMenuService().menuCreate(menu);
//  }

    /**
     * <pre>
     * 自定义菜单创建接口
     * 详情请见： https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141013&token=&lang=zh_CN
     * 如果要创建个性化菜单，请设置matchrule属性
     * 详情请见：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1455782296&token=&lang=zh_CN
     * </pre>
     *
     * @param json
     * @return 如果是个性化菜单，则返回menuid，否则返回null
     */
    @Override
    @GetMapping("/create/{json}")
    public String menuCreate(@PathVariable String json) throws WxErrorException {
        return this.wxService.getMenuService().menuCreate(json);
    }

    /**
     * <pre>
     * 自定义菜单删除接口
     * 详情请见: https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141015&token=&lang=zh_CN
     * </pre>
     */
    @Override
    @GetMapping("/delete")
    public void menuDelete() throws WxErrorException {
        this.wxService.getMenuService().menuDelete();
    }

    /**
     * <pre>
     * 删除个性化菜单接口
     * 详情请见: https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1455782296&token=&lang=zh_CN
     * </pre>
     *
     * @param menuId 个性化菜单的menuid
     */
    @Override
    @GetMapping("/delete/{menuId}")
    public void menuDelete(@PathVariable String menuId) throws WxErrorException {
        this.wxService.getMenuService().menuDelete(menuId);
    }

    /**
     * <pre>
     * 自定义菜单查询接口
     * 详情请见： https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141014&token=&lang=zh_CN
     * </pre>
     */
    @Override
    @GetMapping("/get")
    public WxMpMenu menuGet() throws WxErrorException {
        return this.wxService.getMenuService().menuGet();
    }

    /**
     * <pre>
     * 测试个性化菜单匹配结果
     * 详情请见: http://mp.weixin.qq.com/wiki/0/c48ccd12b69ae023159b4bfaa7c39c20.html
     * </pre>
     *
     * @param userid 可以是粉丝的OpenID，也可以是粉丝的微信号。
     */
    @Override
    @GetMapping("/menuTryMatch/{userid}")
    public WxMenu menuTryMatch(@PathVariable String userid) throws WxErrorException {
        return this.wxService.getMenuService().menuTryMatch(userid);
    }

    /**
     * <pre>
     * 获取自定义菜单配置接口
     * 本接口将会提供公众号当前使用的自定义菜单的配置，如果公众号是通过API调用设置的菜单，则返回菜单的开发配置，而如果公众号是在公众平台官网通过网站功能发布菜单，则本接口返回运营者设置的菜单配置。
     * 请注意：
     * 1、第三方平台开发者可以通过本接口，在旗下公众号将业务授权给你后，立即通过本接口检测公众号的自定义菜单配置，并通过接口再次给公众号设置好自动回复规则，以提升公众号运营者的业务体验。
     * 2、本接口与自定义菜单查询接口的不同之处在于，本接口无论公众号的接口是如何设置的，都能查询到接口，而自定义菜单查询接口则仅能查询到使用API设置的菜单配置。
     * 3、认证/未认证的服务号/订阅号，以及接口测试号，均拥有该接口权限。
     * 4、从第三方平台的公众号登录授权机制上来说，该接口从属于消息与菜单权限集。
     * 5、本接口中返回的图片/语音/视频为临时素材（临时素材每次获取都不同，3天内有效，通过素材管理-获取临时素材接口来获取这些素材），本接口返回的图文消息为永久素材素材（通过素材管理-获取永久素材接口来获取这些素材）。
     *  接口调用请求说明:
     * http请求方式: GET（请使用https协议）
     * https://api.weixin.qq.com/cgi-bin/get_current_selfmenu_info?access_token=ACCESS_TOKEN
     * </pre>
     */
    @Override
    @GetMapping("/getSelfMenuInfo")
    public WxMpGetSelfMenuInfoResult getSelfMenuInfo() throws WxErrorException {
        return this.wxService.getMenuService().getSelfMenuInfo();
    }
}