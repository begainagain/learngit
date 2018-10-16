package com.github.binarywang.demo.wechat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Wshys_user {
    @Id
    @GeneratedValue
//    @Column(name = "id")
    private int id;

    private String nickname;

    private String openid;

    private String sex;

    private String headimgurl;

    private  String mediaid;

    private  String uuid;

    private  String country;

    private  String province;

    private String complete;

    private  String city;

    private  String language;

    private String commenduserid;

    private String subscribe;

    private Date createtime;

    private Date postertime;

    public Wshys_user() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOpenid() {
        return openid;
    }

    public String getSex() {
        return sex;
    }

    public String getMediaid() {
        return mediaid;
    }

    public void setMediaid(String mediaid) {
        this.mediaid = mediaid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getCommenduserid() {
        return commenduserid;
    }

    public void setCommenduserid(String commenduserid) {
        this.commenduserid = commenduserid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getPostertime() {
        return postertime;
    }

    public void setPostertime(Date postertime) {
        this.postertime = postertime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", openid='" + openid + '\'' +
                ", sex='" + sex + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", mediaid='" + mediaid + '\'' +
                ", uuid='" + uuid + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", complete='" + complete + '\'' +
                ", city='" + city + '\'' +
                ", language='" + language + '\'' +
                ", commenduserid='" + commenduserid + '\'' +
                ", subscribe='" + subscribe + '\'' +
                ", createtime=" + createtime +
                ", postertime=" + postertime +
                '}';
    }
}
