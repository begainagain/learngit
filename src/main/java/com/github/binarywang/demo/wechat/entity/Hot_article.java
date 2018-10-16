package com.github.binarywang.demo.wechat.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Hot_article {
    @Id
    @GeneratedValue
    private int id;

    private String title;

    private String summary;

    private String url;

    private String image_one;

    private String image_two;

    private String image_three;

    private int likecount;

    private int clickscount;

    private String author;

    private int originalflag;

    private String type;

    private Date updatetime;

    private String html;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getImage_one() {
        return image_one;
    }

    public void setImage_one(String image_one) {
        this.image_one = image_one;
    }

    public String getImage_two() {
        return image_two;
    }

    public void setImage_two(String image_two) {
        this.image_two = image_two;
    }

    public String getImage_three() {
        return image_three;
    }

    public void setImage_three(String image_three) {
        this.image_three = image_three;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }



    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }

    public int getClickscount() {
        return clickscount;
    }

    public void setClickscount(int clickscount) {
        this.clickscount = clickscount;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getOriginalflag() {
        return originalflag;
    }

    public void setOriginalflag(int originalflag) {
        this.originalflag = originalflag;
    }

    @Override
    public String toString() {
        return "Hot_article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", url='" + url + '\'' +
                ", image_one='" + image_one + '\'' +
                ", image_two='" + image_two + '\'' +
                ", image_three='" + image_three + '\'' +
                ", likecount=" + likecount +
                ", clickscount=" + clickscount +
                ", author='" + author + '\'' +
                ", originalflag=" + originalflag +
                ", type='" + type + '\'' +
                ", updatetime=" + updatetime +
                ", html='" + html + '\'' +
                '}';
    }

    public Hot_article() {
    }
}
