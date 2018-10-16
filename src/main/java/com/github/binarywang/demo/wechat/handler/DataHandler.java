package com.github.binarywang.demo.wechat.handler;

import org.springframework.stereotype.Component;

@Component
public class DataHandler {
//
//    @Autowired
//    private AnalysisRepository analysisRepository;
//
//    @Autowired
//    private WxMpService wxService;
//
//    private List<ProxyIP> list = null;
//
//    @Scheduled(cron = "0 0 7-23 * * ?")
////    @Scheduled(cron = "0 * 15 * * ?")
//    public void task() throws WxErrorException {
//        int times=0;
//        Date d = new Date();
//        Random random= new Random();
//        int hours = d.getHours();
//        if(hours==7||hours==13||hours==18){
//            times = random.nextInt(46)+80;
//        }else {
//            times = random.nextInt(42)+45;
//        }
//        String ur = "https://www.rrjiaoyi.com/fm?v=1.04";
////        System.out.println(times);
//
//        for(int i=0;i<times;i++){
//            System.out.println(i);
//            try {
//                int index = new Random().nextInt(list.size());
//                ImageSpider.proxyInit("D:\\新建文件夹\\代理ip.txt");//代理ip文本路径
//                ProxyConfig proxyConfig =new ProxyConfig();
//                proxyConfig.setProxyHost(list.get(index).getIp());
//                proxyConfig.setProxyPort(list.get(index).getPort());
//                WebClient webClient = new WebClient();
//                webClient.getOptions().setJavaScriptEnabled(true);
//                webClient.getOptions().setCssEnabled(false);
//                webClient.setAjaxController(new NicelyResynchronizingAjaxController());
//                webClient.getOptions().setTimeout(5*1000);
//                webClient.getOptions().setThrowExceptionOnScriptError(false);
//                webClient.getOptions().setProxyConfig(proxyConfig);
//                webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
//                HtmlPage rootPage= webClient.getPage(ur);
//                String str = rootPage.getTitleText();
//                Thread.sleep(5*1000);
//                webClient.close();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
