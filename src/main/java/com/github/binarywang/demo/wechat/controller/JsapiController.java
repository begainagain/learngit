package com.github.binarywang.demo.wechat.controller;

import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/**
 * Created by Lucicol on 2018/5/9.
 */
@RestController
@RequestMapping(value = "/wx" , method = RequestMethod.GET, headers={"api-key=rrjiaoyi"})
public class JsapiController {

    @Autowired
    private WxMpService wxService;

    @GetMapping("/ticket")
    public String getJsapiTicket() throws WxErrorException {
        return this.wxService.getJsapiTicket();
    }

    @PostMapping("/signature")
    public WxJsapiSignature createJsapiSignature(@RequestParam("url") String url) throws WxErrorException {
        return this.wxService.createJsapiSignature(url);
    }
}
