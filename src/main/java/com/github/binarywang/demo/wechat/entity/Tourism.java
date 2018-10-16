package com.github.binarywang.demo.wechat.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Tourism {
    @Id
    @GeneratedValue
    private int id;

    private int userid;

    private int tourismcommendid;

    private Date jointime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getTourismcommendid() {
        return tourismcommendid;
    }

    public void setTourismcommendid(int tourismcommendid) {
        this.tourismcommendid = tourismcommendid;
    }

    public Date getJointime() {
        return jointime;
    }

    public void setJointime(Date jointime) {
        this.jointime = jointime;
    }

    @Override
    public String toString() {
        return "Tourism{" +
                "id=" + id +
                ", userid='" + userid + '\'' +
                ", tourismcommendid='" + tourismcommendid + '\'' +
                ", jointime=" + jointime +
                '}';
    }

    public Tourism() {
    }
}
