package com.github.binarywang.demo.wechat.controller;

import com.github.binarywang.demo.wechat.dao.Wshys_userRepository;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/act/portal")
public class BookController {
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private Wshys_userRepository wshys_userRepository;

    @GetMapping("/getbook")
    public void getbook(WxMpService wxService,WxMpXmlMessage wxMessage) throws Exception {

    }


//    @GetMapping(value = "register")
//    public String  userres(@RequestParam("code") String code
//                        ) throws WxErrorException, IOException {
//
//        String lang = "zh_CN";
//        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
////        WxMpQrcodeService wxMpQrcodeService = new WxMpQrcodeService();
//        WxMpUser oauth2getUserInfo = new WxMpUser();
//        try {
//            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
//        } catch (WxErrorException e) {
//            logger.error("【微信网页授权】{}", e);
//        }
//        Integer id = 0;
//        String openid = "";
//        String nickname = "";
//        String headimgurl = "";
//        String sex = "";
//        int expire_seconds = 604800;
//        oauth2getUserInfo = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, lang);
//        System.out.println(oauth2getUserInfo);
//        JSONObject json = JSONObject.fromObject(oauth2getUserInfo);
//        openid = json.getString("openId");
//        sex = json.getString("sexDesc");
//        nickname = json.getString("nickname");
//        headimgurl = json.getString("headImgUrl");
//        User result = userRepository.findByOpenid(openid);
//        String scene = openid;
//        if(result != null &&result.getCommenduserid()!=null){
//            result.setSex(sex);
//            result.setNickname(nickname);
//            result.setHeadimgurl(headimgurl);
//            userRepository.save(result);
//        } else if(result == null){
//            User user = new User();
//            user.setOpenid(openid);
//            user.setSex(sex);
//            user.setNickname(nickname);
//            user.setHeadimgurl(headimgurl);
//            userRepository.save(user);
//        }
//        WxMpQrCodeTicket ticket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(scene, expire_seconds);
//        File fromFile = wxMpService.getQrcodeService().qrCodePicture(ticket);
//        /**
//         * 移动文件
//         * @param fromFile
//         * @param toFile
//         */
//        File toFile =new  File("E:/erweima/"+openid+".jpg");
//        fromFile.renameTo(toFile);
//
//        ImageAndQRcode add = new ImageAndQRcode();
//        // 添加临时图片素材
//        String mediaType = "image";
//        WxMediaUploadResult res = wxMpService.getMaterialService().mediaUpload(mediaType, toFile);
//        result.setMediaid(res.getMediaId());
//        userRepository.save(result);
//        System.out.println(res.getMediaId());
//        try {
//            add.addImageQRcode("http://www.baidu.com", 300,   300,"jpg", "D:/pic/ss.jpg",
//                    "D:/pic/aa.jpg","D:/pic/new1.png",500,200);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        Cookie cookie = new Cookie(user.getOpenid());
//        System.out.println(fromFile);
////        System.out.println(toFile);
//        return "redirect:" + oauth2getUserInfo + fromFile + toFile;
//        return null;
//    }
//    PostMapping(value="finishInfo")
}

