package com.github.binarywang.demo.wechat.entity;

public class Article_class implements Comparable<Article_class>{
    private String article_class;

    private int random;

    public String getArticle_class() {
        return article_class;
    }

    public void setArticle_class(String article_class) {
        this.article_class = article_class;
    }

    public int getRandom() {
        return random;
    }

    public void setRandom(int random) {
        this.random = random;
    }

    @Override
    public String toString() {
        return "Article_Class{" +
                "article_class='" + article_class + '\'' +
                ", random=" + random +
                '}';
    }

    public Article_class() {
    }

    @Override
    public int compareTo(Article_class o) {
        return this.random - o.random;
    }
}
