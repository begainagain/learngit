package com.github.binarywang.demo.wechat.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Share {
    @Id
    @GeneratedValue
    private int id;

    private Date sharetime;


    private String userid;


    private String shareid;


    private String helpid;

    public Share() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getSharetime() {
        return sharetime;
    }

    public void setSharetime(Date sharetime) {
        this.sharetime = sharetime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getShareid() {
        return shareid;
    }

    public void setShareid(String shareid) {
        this.shareid = shareid;
    }

    public String getHelpid() {
        return helpid;
    }

    public void setHelpid(String helpid) {
        this.helpid = helpid;
    }

    @Override
    public String toString() {
        return "Share{" +
                "id=" + id +
                ", sharetime=" + sharetime +
                ", userid=" + userid +
                ", shareid=" + shareid +
                '}';
    }
}
