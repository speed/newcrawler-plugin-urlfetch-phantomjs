package com.newcrawler.plugin.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.newcrawler.plugin.urlfetch.phantomjs.UrlFetchPluginService;
import com.soso.plugin.UrlFetchPlugin;
import com.soso.plugin.bo.HttpCookieBo;
import com.soso.plugin.bo.UrlFetchPluginBo;

/**
 * http://phantomjs.org/api/webpage/property/settings.html
 * @author Liao
 *
 */
public class UrlFetchPluginServiceTest{
	
	public static final String PROPERTIES_TIMEOUT_JAVASCRIPT = "timeout.javascript";
	public static final String PROPERTIES_TIMEOUT_CONNECTION = "timeout.connection";
	
	public static final String PROXY_IP = "proxy.ip";
	public static final String PROXY_PORT = "proxy.port";
	public static final String PROXY_USER = "proxy.username";
	public static final String PROXY_PASS = "proxy.password";
	public static final String PROXY_TYPE = "proxy.type";
	public static final String PHANTOMJS_PATH = "phantomjs.path";
	
	public static final String PROPERTIES_JS_FILTER_REGEXS = "js.filter.regexs";
	public static final String PROPERTIES_JS_FILTER_TYPE = "js.filter.type";
	public static final String PROPERTIES_JS_CACHE_REGEXS = "js.cache.regexs";

	
	public static void main(String[] args){
		//Fiddler Proxy
		Map<String, String> properties=new HashMap<String, String>(); 
		properties.put(PROXY_IP, "192.168.44.244");
		properties.put(PROXY_PORT, String.valueOf(8888));
		properties.put(PROXY_TYPE, "http");
		
		properties.put(PROPERTIES_JS_FILTER_TYPE, "include");
		//properties.put(PROPERTIES_JS_FILTER_REGEXS, "http://static.360buyimg.com/*|$|http://item.jd.com/*");
		
		properties.put(PHANTOMJS_PATH, "D:\\js\\phantomjs-2.0.0-windows\\bin\\phantomjs.exe");
		properties.put(PHANTOMJS_PATH, "/Users/liaolianwu/Documents/workspace/newcrawler-plugin-urlfetch-phantomjs/phantomjs/phantomjs-2.1.1-macosx/bin/phantomjs");
		
		Map<String, String> headers=new HashMap<String, String>(); 
		String crawlUrl="https://www.lagou.com";
		//crawlUrl="http://www.lagou.com/jobs/list_%E7%88%AC%E8%99%AB?px=default&city=%E6%B7%B1%E5%9C%B3#filterBox";
		
		String method=null; 
		String userAgent="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.36 Safari/535.7"; 
		String encoding=null;
		List<HttpCookieBo> cookieList=new ArrayList<HttpCookieBo>();
		
		UrlFetchPluginBo urlFetchPluginBo=new UrlFetchPluginBo(properties, headers, crawlUrl, method, cookieList, userAgent, encoding);
		
		UrlFetchPluginService urlFetchPluginService=new UrlFetchPluginService();
		Map<String, Object> map1 = urlFetchPluginService.execute(urlFetchPluginBo);
		
		List<HttpCookieBo> cookieList2=(List<HttpCookieBo>)map1.get(UrlFetchPluginService.RETURN_DATA_KEY_COOKIES);
		if(cookieList2!=null){
			//cookieList.addAll(cookieList2);
		}
		headers=new HashMap<String, String>(); 
		crawlUrl="https://a.lagou.com/collect?v=1&_v=j31&a=1233001226&t=pageview&_s=1&dl=https%3A%2F%2Fwww.lagou.com%2F&ul=zh-cn&de=UTF-8&dt=&sd=32-bit&sr=1024x768&vp=400x300&je=0&_u=MEAAAAQAK~&jid=&cid=&tid=UA-41268416-1&_r=1&z=1444070207";
		urlFetchPluginBo=new UrlFetchPluginBo(properties, headers, crawlUrl, method, cookieList, userAgent, encoding);
		map1 = urlFetchPluginService.execute(urlFetchPluginBo);
		
		List<HttpCookieBo> cookieList3=(List<HttpCookieBo>)map1.get(UrlFetchPluginService.RETURN_DATA_KEY_COOKIES);
		if(cookieList3!=null){
			for(HttpCookieBo httpCookieBo:cookieList3){
				int index=cookieList.indexOf(httpCookieBo);
				if(index!=-1){
					cookieList.set(index, httpCookieBo);
				}else{
					cookieList.add(httpCookieBo);
				}
			}
		}
		HttpCookieBo httpCookieBoTest=new HttpCookieBo("newcrawler", "newcralwer");
		
		cookieList.add(httpCookieBoTest);
		headers=new HashMap<String, String>(); 
		crawlUrl="http://www.lagou.com/jobs/list_%E7%88%AC%E8%99%AB?px=default&city=%E6%B7%B1%E5%9C%B3#filterBox";
		urlFetchPluginBo=new UrlFetchPluginBo(properties, headers, crawlUrl, method, cookieList, userAgent, encoding);
		map1 = urlFetchPluginService.execute(urlFetchPluginBo);
		
		System.out.println(map1.get(UrlFetchPluginService.RETURN_DATA_KEY_CONTENT));
		urlFetchPluginService.destory();
	}
	
}
