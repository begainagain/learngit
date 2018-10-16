package com.github.binarywang.demo.wechat.utils.spider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 过往记忆
 * Create Data: 2012-8-10
 * Email: wyphao.2007@163.com
 * Blog: https://www.iteblog.com
 * 版权所有，翻版不究，但在修改本程序的时候必须加上这些注释！
 * 仅用于学习交流之用
 *
 * 代理服务器IP以及端口
 */
public class ProxyServer {
    public static List<String> proxyIP = new ArrayList<String>(){{add("121.60.77.61"); add("121.31.154.124");add("118.190.95.43"); add("106.75.9.39");add("118.190.95.35"); add("219.141.153.41");}};

    public static List<Integer> proxyPort = new ArrayList<Integer>(){{add(8010); add(8123);add(9001); add(8080);add(9001); add(80);}};
}

