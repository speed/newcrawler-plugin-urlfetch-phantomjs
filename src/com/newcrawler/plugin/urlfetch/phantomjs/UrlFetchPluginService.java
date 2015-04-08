package com.newcrawler.plugin.urlfetch.phantomjs;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.soso.plugin.UrlFetchPlugin;

public class UrlFetchPluginService implements UrlFetchPlugin{
	
	private final static Log logger = LogFactory.getLog(UrlFetchPluginService.class);
	public static final String PROXY_IP = "proxy.ip";
	public static final String PROXY_PORT = "proxy.port";
	public static final String PROXY_USER = "proxy.username";
	public static final String PROXY_PASS = "proxy.password";
	public static final String PROXY_TYPE = "proxy.type";
	public static final String PHANTOMJS_PATH = "phantomjs.path";
	
	private PhantomJSDriver driver=null;
	
	public static void main(String[] args){
		Map<String, String> properties=new HashMap<String, String>(); 
		properties.put(PROXY_IP, "127.0.0.1");
		properties.put(PROXY_PORT, String.valueOf(6666));
		properties.put(PROXY_TYPE, "socks5");
		
		properties.put(PHANTOMJS_PATH, "D:/Workspace/workspace j2ee/newcrawler-plugin-urlfetch-phantomjs/phantomjs/windows/phantomjs.exe");
		
		Map<String, String> headers=new HashMap<String, String>(); 
		String crawlUrl="http://item.jd.com/832705.html"; 
		String method=null; 
		String cookie="A2=\"2|1:0|10:1427611918|2:A2|56:ZGE5YzEzNTYxZDBmZDZmMWUyZTkyYTBhN2NkZmU4OTk2NmZmYjVmMw==|1c1b88ecfa96a974658f6d7dc6e0a7d63de0f82e671f4b6f32c1205de96bd75c\"; V2EX_REFERRER=\"2|1:0|10:1428372188|13:V2EX_REFERRER|8:bHh4MQ==|f182265687e841f02e23d4c5cdb5268f8421d8a9cd1b0919691212854f168bef\"; PB3_SESSION=\"2|1:0|10:1428380636|11:PB3_SESSION|40:djJleDoxMDQuMjM4LjE2MC4xODM6NTUxMjUwMDk=|72a80e953db1c21e3e8cc94667e4dd90c853835847c5450a2fb3d20ed6858e1f\"; V2EX_TAB=\"2|1:0|10:1428380636|8:V2EX_TAB|4:YWxs|0239b3413d6b0a74f1681fdd702894180596341ea32ebc2b23830b9c0374f291\"; _gat=1; V2EX_LANG=zhcn; _ga=GA1.2.371759343.1423409771"; 
		String userAgent="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.36 Safari/535.7"; 
		String encoding="GB2312";
		UrlFetchPluginService urlFetchPluginService=new UrlFetchPluginService();
		//urlFetchPluginService.execute(properties, crawlUrl, method, cookie, userAgent, encoding);
		
		long time=System.currentTimeMillis();
		crawlUrl="http://www.newcrawler.com/header"; 
		Map<String, Object> map = urlFetchPluginService.execute(properties, headers, crawlUrl, method, cookie, userAgent, encoding);
		time=System.currentTimeMillis()-time;
		System.out.println("1:"+time);
		System.out.println(map.get(RETURN_DATA_KEY_CONTENT));
		
		
		/*time=System.currentTimeMillis();
		crawlUrl="http://www.v2ex.com"; 
		map = urlFetchPluginService.execute(properties, headers, crawlUrl, method, cookie, userAgent, encoding);
		time=System.currentTimeMillis()-time;
		System.out.println("2:"+time);
		System.out.println(map.get(RETURN_DATA_KEY_CONTENT));
		*/
		urlFetchPluginService.destory();
	}
	
	public void destory(){
		driver.quit();
	}
	
	@Override
	public Map<String, Object> execute(Map<String, String> properties, Map<String, String> headers, String crawlUrl, String method, String cookie, String userAgent, String encoding) {
		String proxyIP=null;
		int proxyPort=-1;
		String proxyUsername=null;
		String proxyPassword=null;
		String proxyType=null;
		
		String phantomjsPath=null;
		
		if (properties != null) {
			if (properties.containsKey(PROXY_IP) && !"".equals(properties.get(PROXY_IP).trim())) {
				proxyIP = properties.get(PROXY_IP).trim();
			}
			if (properties.containsKey(PROXY_PORT) && !"".equals(properties.get(PROXY_PORT).trim())) {
				proxyPort = Integer.parseInt(properties.get(PROXY_PORT).trim());
			}

			if (properties.containsKey(PROXY_USER) && !"".equals(properties.get(PROXY_USER).trim())) {
				proxyUsername = properties.get(PROXY_USER).trim();
			}
			
			if (properties.containsKey(PROXY_PASS) && !"".equals(properties.get(PROXY_PASS).trim())) {
				proxyPassword = properties.get(PROXY_PASS).trim();
			}
			
			if (properties.containsKey(PROXY_TYPE) && !"".equals(properties.get(PROXY_TYPE).trim())) {
				proxyType = properties.get(PROXY_TYPE).trim();
			}
			
			if (properties.containsKey(PHANTOMJS_PATH) && !"".equals(properties.get(PHANTOMJS_PATH).trim())) {
				phantomjsPath = properties.get(PHANTOMJS_PATH).trim();
			}
		}
		if(headers==null){
			headers = new HashMap<String, String>();
		}
		if(StringUtils.isNoneBlank(cookie)){
			headers.put("Cookie", cookie);
		}
		if(StringUtils.isNoneBlank(userAgent)){
			headers.put("User-Agent", userAgent);
		}
		
		Map<String, Object> map=null;
		try {
			map=read(proxyIP, proxyPort, proxyUsername, proxyPassword, proxyType, phantomjsPath, headers, crawlUrl, method, encoding);
		} catch (SocketException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return map;
	}
	private DesiredCapabilities capabilities;
	private Map<String, Object> read(String proxyIP, int proxyPort, final String proxyUsername, final String proxyPassword, final String proxyType, String phantomjsPath, Map<String, String> headers, String crawlUrl, String method, String encoding) throws IOException{
		if(driver==null){
			synchronized (this) {
				if(driver==null){
					capabilities = DesiredCapabilities.phantomjs();
			        capabilities.setJavascriptEnabled(true);      
			        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomjsPath);
			        
			        List<String> cliArgsCap = new ArrayList<String>();
			        if(proxyIP!=null){
			        	cliArgsCap.add("--proxy="+proxyIP+":"+proxyPort);
			        	if(proxyUsername!=null && proxyPassword!=null){
			        		cliArgsCap.add("--proxy-auth="+proxyUsername+":"+proxyPassword);
			        	}
			        	if(proxyType!=null){
			        		cliArgsCap.add("--proxy-type="+proxyType);
			        	}else{
			        		cliArgsCap.add("--proxy-type=http");
			        	}
			        }
			        cliArgsCap.add("--ignore-ssl-errors=yes");
		        	capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
			        
		        	for(String key:headers.keySet()){
		        		capabilities.setCapability("phantomjs.page.customHeaders."+key, headers.get(key));
		        	}
			        driver = new PhantomJSDriver(capabilities);
				}
			}
		}
        driver.get(crawlUrl);
       
        Map<String, String> cookies=new HashMap<String, String>();;
        Set<Cookie> cookieSet = driver.manage().getCookies();
		for (Cookie cookieObj : cookieSet) {
			cookies.put(cookieObj.getName(), cookieObj.getValue());
		}
		
		Map<String, Object> resHeaders=new HashMap<String, Object>();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(RETURN_DATA_KEY_COOKIES, cookies);
		map.put(RETURN_DATA_KEY_CONTENT, driver.getPageSource());
		map.put(RETURN_DATA_KEY_REALURL, driver.getCurrentUrl());
		map.put(RETURN_DATA_KEY_HEADERS, resHeaders);
		return map;
	}
	
}
