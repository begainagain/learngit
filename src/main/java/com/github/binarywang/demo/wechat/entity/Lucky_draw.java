package com.github.binarywang.demo.wechat.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Lucky_draw{
    @Id
    @GeneratedValue
    private int id;

    private String openid;

    private int times;

    private int random_num1;

    private int random_num2;

    private int random_num3;

    private int random_num4;

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

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getRandom_num1() {
        return random_num1;
    }

    public void setRandom_num1(int random_num1) {
        this.random_num1 = random_num1;
    }

    public int getRandom_num2() {
        return random_num2;
    }

    public void setRandom_num2(int random_num2) {
        this.random_num2 = random_num2;
    }

    public int getRandom_num3() {
        return random_num3;
    }

    public void setRandom_num3(int random_num3) {
        this.random_num3 = random_num3;
    }

    public int getRandom_num4() {
        return random_num4;
    }

    public void setRandom_num4(int random_num4) {
        this.random_num4 = random_num4;
    }

    public Lucky_draw() {
    }

    @Override
    public String toString() {
        return "Lucky_draw{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", times=" + times +
                ", random_num1=" + random_num1 +
                ", random_num2=" + random_num2 +
                ", random_num3=" + random_num3 +
                ", random_num4=" + random_num4 +
                '}';
    }
}
