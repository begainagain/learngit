package com.github.binarywang.demo.wechat.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class User_habit {
    @Id
    @GeneratedValue
    private int id;

    private int userid;

    private int history_number;

    private int adver_number;

    private Date today;

    private Date history_time;

    private String weight;

    public int getAdver_number() {
        return adver_number;
    }

    public void setAdver_number(int adver_number) {
        this.adver_number = adver_number;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public Date getHistory_time() {
        return history_time;
    }

    public void setHistory_time(Date history_time) {
        this.history_time = history_time;
    }

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

    public int getHistory_number() {
        return history_number;
    }

    public void setHistory_number(int history_number) {
        this.history_number = history_number;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "User_habit{" +
                "id=" + id +
                ", userid=" + userid +
                ", history_number=" + history_number +
                ", weight='" + weight + '\'' +
                '}';
    }

    public User_habit() {
    }
}
