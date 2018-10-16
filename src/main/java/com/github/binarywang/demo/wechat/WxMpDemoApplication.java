package com.github.binarywang.demo.wechat;

import com.github.binarywang.demo.wechat.dao.AnalysisRepository;
import com.github.binarywang.demo.wechat.entity.Analysis;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpDataCubeService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpDataCubeServiceImpl;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeUserSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@SpringBootApplication
@EnableScheduling
public class WxMpDemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(WxMpDemoApplication.class, args);
  }


}
