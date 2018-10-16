package com.github.binarywang.demo.wechat.entity.Recommend;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Article_Recommend {
    @Id
    @GeneratedValue
    private int id;

    private int weight;

    private int shishi;

    private int wenhua;

    private int minsheng;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Article_Recommend() {
    }

}
