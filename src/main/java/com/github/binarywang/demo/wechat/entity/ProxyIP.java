package com.github.binarywang.demo.wechat.entity;

public class ProxyIP {
    private String ip;
    private int port;

    public ProxyIP(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }
}
