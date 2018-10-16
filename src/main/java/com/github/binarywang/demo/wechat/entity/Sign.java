package com.github.binarywang.demo.wechat.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Sign {
    @Id
    @GeneratedValue
    private int openid;

    private String issign;

    private Date signtime;

    private String signdays;

    private String allsign;

    private String signmonth;

    public int getOpenid() {
        return openid;
    }

    public void setOpenid(int openid) {
        this.openid = openid;
    }

    public String getIssign() {
        return issign;
    }

    public void setIssign(String issign) {
        this.issign = issign;
    }

    public Date getSigntime() {
        return signtime;
    }

    public void setSigntime(Date signtime) {
        this.signtime = signtime;
    }

    public String getSigndays() {
        return signdays;
    }

    public void setSigndays(String signdays) {
        this.signdays = signdays;
    }

    public String getAllsign() {
        return allsign;
    }

    public void setAllsign(String allsign) {
        this.allsign = allsign;
    }

    public String getSignmonth() {
        return signmonth;
    }

    public void setSignmonth(String signmonth) {
        this.signmonth = signmonth;
    }
}
