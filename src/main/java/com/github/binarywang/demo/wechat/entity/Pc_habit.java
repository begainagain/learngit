package com.github.binarywang.demo.wechat.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Pc_habit {
    @Id
    @GeneratedValue
    private int id;

    private String userid;

    private int history_number;

    private Date today;

    private Date history_time;

    private String weight;

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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
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
        return "Pc_habit{" +
                "id=" + id +
                ", userid='" + userid + '\'' +
                ", history_number=" + history_number +
                ", today=" + today +
                ", history_time=" + history_time +
                ", weight='" + weight + '\'' +
                '}';
    }

    public Pc_habit() {
    }
}
