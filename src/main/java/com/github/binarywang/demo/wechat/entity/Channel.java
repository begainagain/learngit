package com.github.binarywang.demo.wechat.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Channel {
    @Id
    @GeneratedValue
    private int id;

    private String openid;

    private String channel;

    private Date recent_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Date getRecent_time() {
        return recent_time;
    }

    public void setRecent_time(Date recent_time) {
        this.recent_time = recent_time;
    }

    public Channel() {
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", channel='" + channel + '\'' +
                ", recent_time=" + recent_time +
                '}';
    }
}
