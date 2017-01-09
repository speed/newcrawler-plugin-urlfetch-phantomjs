package com.newcrawler.plugin.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.newcrawler.plugin.urlfetch.phantomjs.UrlFetchPluginService;
import com.soso.plugin.UrlFetchPlugin;
import com.soso.plugin.bo.HttpCookieBo;
import com.soso.plugin.bo.UrlFetchPluginBo;

public class ThreadTest {
	
	public static final String PROXY_IP = "proxy.ip";
	public static final String PROXY_PORT = "proxy.port";
	public static final String PROXY_USER = "proxy.username";
	public static final String PROXY_PASS = "proxy.password";
	public static final String PHANTOMJS_PATH = "phantomjs.path";
	public static final String PROXY_TYPE = "proxy.type";
	
	public static void main(String[] args) {
		Map<String, String> properties=new HashMap<String, String>(); 
		properties.put(PROXY_IP, "127.0.0.1");
		properties.put(PROXY_PORT, String.valueOf(6666));
		properties.put(PROXY_TYPE, "socks5");
		
		properties.put(PHANTOMJS_PATH, "D:/Workspace/workspace j2ee/newcrawler-plugin-urlfetch-selenium/phantomjs/windows/phantomjs.exe");
		
		Map<String, String> headers=new HashMap<String, String>(); 
		
		UrlFetchPluginService urlFetchPluginService=new UrlFetchPluginService();
		
		ThreadTest threadTest=new ThreadTest();
		
		for(int i=0; i<=50; i++){
			Open open=threadTest.new Open(urlFetchPluginService, properties, headers, i);
			Thread t1=new Thread(open);
			t1.run();
		}
		System.out.println("成功.");
	}

	class Open implements Runnable{
		private UrlFetchPluginService urlFetchPluginService;
		private Map<String, String> properties;
		private Map<String, String> headers;
		private String method=null; 
		private List<HttpCookieBo> cookieList=null; 
		private String userAgent="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.36 Safari/535.7"; 
		private String encoding="GB2312";
		private int num;
		
		public Open(UrlFetchPluginService urlFetchPluginService, Map<String, String> properties, Map<String, String> headers, int num){
			this.urlFetchPluginService=urlFetchPluginService;
			this.properties=properties;
			this.headers=headers;
			this.num=num;
		}
		@Override
		public void run() {
			long time=System.currentTimeMillis();
			String crawlUrl="http://www.newcrawler.com/header?ver="+num; 
			
			UrlFetchPluginBo urlFetchPluginBo=new UrlFetchPluginBo(properties, headers, crawlUrl, method, cookieList, userAgent, encoding);
			Map<String, Object> map=null;
			try {
				map = urlFetchPluginService.execute(urlFetchPluginBo);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			time=System.currentTimeMillis()-time;
			System.out.println(num+":"+time);
			System.out.println(map.get(UrlFetchPlugin.RETURN_DATA_KEY_CONTENT));
		}
		
	}
}
