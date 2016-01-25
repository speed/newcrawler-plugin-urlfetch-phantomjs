package com.newcrawler.plugin.urlfetch.phantomjs;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.soso.plugin.UrlFetchPlugin;
import com.soso.plugin.bo.HttpCookieBo;
import com.soso.plugin.bo.UrlFetchPluginBo;

/**
 * http://phantomjs.org/api/webpage/property/settings.html
 * @author Liao
 *
 */
public class UrlFetchPluginService implements UrlFetchPlugin{
	
	private final static Log logger = LogFactory.getLog(UrlFetchPluginService.class);
	
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
	private static final String DEFAULT_JS_FILTER_TYPE = "include";
	
	private PhantomJSDriver driver=null;
	private DesiredCapabilities capabilities;
	
	public static void main(String[] args){
		Map<String, String> properties=new HashMap<String, String>(); 
		properties.put(PROXY_IP, "127.0.0.1");
		properties.put(PROXY_PORT, String.valueOf(8888));
		properties.put(PROXY_TYPE, "http");
		
		properties.put(PROPERTIES_JS_FILTER_TYPE, "include");
		//properties.put(PROPERTIES_JS_FILTER_REGEXS, "http://static.360buyimg.com/*|$|http://item.jd.com/*");
		
		properties.put(PHANTOMJS_PATH, "D:\\js\\phantomjs-2.0.0-windows\\bin\\phantomjs.exe");
		properties.put(PHANTOMJS_PATH, "D:\\js\\phantomjs-1.9.8-windows\\phantomjs.exe");
		
		Map<String, String> headers=new HashMap<String, String>(); 
		String crawlUrl="http://item.jd.com/1510479.html"; 
		String method=null; 
		String userAgent="Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.36 Safari/535.7"; 
		String encoding="GB2312";
		List<HttpCookieBo> cookieList=null;
		
		UrlFetchPluginBo urlFetchPluginBo=new UrlFetchPluginBo(properties, headers, crawlUrl, method, cookieList, userAgent, encoding);
		
		UrlFetchPluginService urlFetchPluginService=new UrlFetchPluginService();
		Map<String, Object> map1 = urlFetchPluginService.execute(urlFetchPluginBo);
		//System.out.println(map1.get(RETURN_DATA_KEY_CONTENT));
		
		long time=System.currentTimeMillis();
		//crawlUrl="http://www.newcrawler.com/header"; 
		urlFetchPluginBo=new UrlFetchPluginBo(properties, headers, crawlUrl, method, cookieList, userAgent, encoding);
		Map<String, Object> map = urlFetchPluginService.execute(urlFetchPluginBo);
		time=System.currentTimeMillis()-time;
		System.out.println("1:"+time);
		urlFetchPluginService.destory();
	}
	public UrlFetchPluginService(){
		
	}
	
	public void destory(){
		driver.quit();
	}
	
	@Override
	public Map<String, Object> execute(UrlFetchPluginBo urlFetchPluginBo) {
		Map<String, String> properties=urlFetchPluginBo.getProperties();
		Map<String, String> headers=urlFetchPluginBo.getHeaders();
		String crawlUrl=urlFetchPluginBo.getCrawlUrl();
		String method=urlFetchPluginBo.getMethod();
		List<HttpCookieBo> cookieList=urlFetchPluginBo.getCookieList();
		String userAgent=urlFetchPluginBo.getUserAgent();
		String encoding=urlFetchPluginBo.getEncoding();
		
		String jsFilterRegexs = null;
		String jsFilterType = DEFAULT_JS_FILTER_TYPE;
		String jsCacheRegexs = null;
		int timeoutConnection=60000;
		int timeoutJavascript=5000;
		
		String proxyIP=null;
		int proxyPort=-1;
		String proxyUsername=null;
		String proxyPassword=null;
		String proxyType=null;
		
		String phantomjsPath=null;
		
		if (properties != null) {
			if (properties.containsKey(PROPERTIES_JS_FILTER_REGEXS) && properties.get(PROPERTIES_JS_FILTER_REGEXS)!=null 
					&& !"".equals(properties.get(PROPERTIES_JS_FILTER_REGEXS).trim())) {
				jsFilterRegexs = properties.get(PROPERTIES_JS_FILTER_REGEXS).trim();
			}
			
			if (properties.containsKey(PROPERTIES_JS_CACHE_REGEXS) && properties.get(PROPERTIES_JS_CACHE_REGEXS)!=null 
					&& !"".equals(properties.get(PROPERTIES_JS_CACHE_REGEXS).trim())) {
				jsCacheRegexs = properties.get(PROPERTIES_JS_CACHE_REGEXS).trim();
			}

			if (properties.containsKey(PROPERTIES_JS_FILTER_TYPE) && properties.get(PROPERTIES_JS_FILTER_TYPE)!=null 
					&& !"".equals(properties.get(PROPERTIES_JS_FILTER_TYPE).trim())) {
				jsFilterType = properties.get(PROPERTIES_JS_FILTER_TYPE).trim();
			}
			

			if (properties.containsKey(PROPERTIES_TIMEOUT_JAVASCRIPT) && properties.get(PROPERTIES_TIMEOUT_JAVASCRIPT)!=null 
					&& !"".equals(properties.get(PROPERTIES_TIMEOUT_JAVASCRIPT).trim())) {
				timeoutJavascript = Integer.parseInt(properties.get(PROPERTIES_TIMEOUT_JAVASCRIPT).trim());
			}
			if (properties.containsKey(PROPERTIES_TIMEOUT_CONNECTION) && properties.get(PROPERTIES_TIMEOUT_CONNECTION)!=null 
					&& !"".equals(properties.get(PROPERTIES_TIMEOUT_CONNECTION).trim())) {
				timeoutConnection = Integer.parseInt(properties.get(PROPERTIES_TIMEOUT_CONNECTION).trim());
			}
			
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
		

		String filterRegexs = "";
		if (jsFilterRegexs != null && !"".equals(jsFilterRegexs.trim())) {
			String[] regexs = jsFilterRegexs.split("\\Q|$|\\E");
			int len = regexs.length;
			for (int i = 0; i < len; i++) {
				String regex = regexs[i];
				regex = regex.trim();
				regex = "^"+conversionRegexToStr(regex)+"$";
				if(regex.indexOf("*")!=-1){
					regex = regex.replaceAll("\\*", ".*");
				}
				if("".equals(filterRegexs)){
					filterRegexs=regex;
				}else{
					filterRegexs=filterRegexs+"|"+regex;
				}
			}
		}
		String cacheRegexs = "";
		if (jsCacheRegexs != null && !"".equals(jsCacheRegexs.trim())) {
			String[] regexs = jsCacheRegexs.split("\\Q|$|\\E");
			int len = regexs.length;
			for (int i = 0; i < len; i++) {
				String regex = regexs[i];
				regex = regex.trim();
				regex = "^"+conversionRegexToStr(regex)+"$";
				if(regex.indexOf("*")!=-1){
					regex = regex.replaceAll("\\*", ".*");
				}
				if("".equals(cacheRegexs)){
					cacheRegexs=regex;
				}else{
					cacheRegexs=cacheRegexs+"|"+regex;
				}
			}
		}
		
		if(headers==null){
			headers = new HashMap<String, String>();
		}
		if(cookieList!=null && !cookieList.isEmpty()){
			String cookie=getCookies(cookieList);
			if(StringUtils.isNoneBlank(cookie)){
				headers.put("Cookie", cookie);
			}
		}
		if(StringUtils.isNoneBlank(userAgent)){
			headers.put("User-Agent", userAgent);
		}
		List<String> jsList = new ArrayList<String>();
		Map<String, Object> map=null;
		try {
			map=read(proxyIP, proxyPort, proxyUsername, proxyPassword, proxyType, phantomjsPath, headers, crawlUrl, method, encoding, 
					jsFilterType, filterRegexs, jsList, cacheRegexs, timeoutConnection, timeoutJavascript);
		} catch (SocketException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return map;
	}

	private final String getCookies(List<HttpCookieBo> cookieList){
		String cookie="";
		for(HttpCookieBo httpCookie:cookieList){
			cookie+=httpCookie.getName()+"="+httpCookie.getValue()+";";
		}
		return cookie;
	}
	/**
	 * http://phantomjs.org/api/webpage/property/custom-headers.html
	 * @return
	 * @throws IOException
	 */
	private Map<String, Object> read(String proxyIP, int proxyPort, final String proxyUsername, final String proxyPassword, final String proxyType, String phantomjsPath, Map<String, String> headers, String crawlUrl, String method, String encoding,
			String jsFilterType, String jsFilterRegexs, List<String> jsList, String jsCacheRegexs, final long pageLoadTimeout, final long scriptTimeout) throws IOException{
		if(driver==null){
			synchronized (this) {
				if(driver==null){
					capabilities = DesiredCapabilities.phantomjs();
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
			        
		        	capabilities.setCapability("phantomjs.page.settings.loadImages", false);
		        	
		        	for(String key:headers.keySet()){
		        		capabilities.setCapability("phantomjs.page.customHeaders."+key, headers.get(key));
		        	}
			        driver = new PhantomJSDriver(capabilities);
				}
				driver.manage().timeouts().pageLoadTimeout(pageLoadTimeout, TimeUnit.MILLISECONDS);
				driver.manage().timeouts().setScriptTimeout(scriptTimeout, TimeUnit.MILLISECONDS);
			}
		}
		driver.executePhantomJS(""
				+ "var page = this;"
				+ "var urls = Array(); "
				+ "page.onResourceRequested = function(requestData, networkRequest) {"
				+ "		if ('"+jsFilterRegexs+"' != '') {"
				+ "			var match = requestData.url.match(/"+jsFilterRegexs+"/gi); "
				+ "			if ('"+jsFilterType+"' == 'include') {"
				+ "				if (match == null) {"
				+ "					console.log('abort url: ' + requestData['url']); "
				+ "					networkRequest.abort(); "
				+ "					return; "
				+ "				};"
				+ "			}else{"
				+ "				if (match != null) {"
				+ "					console.log('abort url: ' + requestData['url']); "
				+ "					networkRequest.abort(); "
				+ "					return; "
				+ "				};"
				+ "			};"
				+ "		};"
				+ "		urls.push(requestData.url);"
				+ "};"
				
				+ "page.onLoadFinished = function(status) {"
				+ "		page.urls=urls.join('|$|');"
				+ "};"
				+ "return 'Done';"
				);
        driver.get(crawlUrl);
        
        Object outputEncoding=driver.executePhantomJS("return phantom.outputEncoding;");
        
        Object jsUrl=driver.executePhantomJS("var page = this; return page.urls;");
        String[] jsUrls = jsUrl.toString().split("\\Q|$|\\E");
        for(String js:jsUrls){
        	jsList.add(js);
        }
        
        Date nowDate=new Date();
		long nowTime=nowDate.getTime();
        List<HttpCookieBo> cookieList=new ArrayList<HttpCookieBo>();
        Set<Cookie> cookieSet = driver.manage().getCookies();
		for (Cookie cookieObj : cookieSet) {
			HttpCookieBo pluginCookie=new HttpCookieBo(cookieObj.getName(), cookieObj.getValue());
			pluginCookie.setDomain(cookieObj.getDomain());
			pluginCookie.setPath(cookieObj.getPath());
			pluginCookie.setSecure(cookieObj.isSecure());
			
			long expiry=-1;
			Date expiryDate=cookieObj.getExpiry();
			if(expiryDate!=null){
				expiry=cookieObj.getExpiry().getTime()-nowTime;
				expiry=expiry/1000;
			}
			
			pluginCookie.setMaxAge(expiry);//
			pluginCookie.setHttpOnly(cookieObj.isHttpOnly());
			cookieList.add(pluginCookie);
		}
		driver.manage().deleteAllCookies();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(RETURN_DATA_KEY_CONTENT, driver.getPageSource());
		map.put(RETURN_DATA_KEY_REALURL, driver.getCurrentUrl());
		map.put(RETURN_DATA_KEY_INCLUDE_JS, jsList);
		map.put(RETURN_DATA_KEY_COOKIES, cookieList);
		map.put(RETURN_DATA_KEY_ENCODING, outputEncoding);
		driver.executePhantomJS("var page = this; page.clearMemoryCache();");
		return map;
	}
	
	private static String conversionRegexToStr(String s) {
		StringBuffer sb = new StringBuffer();
        for (int i=0, len=s.length(); i<len; i++) {
            char c = s.charAt(i);
            switch (c) {
			case '^':
			case '$':
			case '(':
			case ')':
			case '+':
			case '?':
			case '.':
			case '[':
			case '/':
			case '{':
			case '|':
				sb.append('\\');
				sb.append(c);
				break;
			case '\\':
				int j=i+1;
				if(j<len){
					char c2 = s.charAt(j);
					switch (c2) {
					case 'd':
					case 'D':
					case 'w':
					case 'W':
					case 's':
					case 'S':
					case 'Q':
					case 'E':
					case 'b':
					case 'B':
						sb.append(c);
						sb.append(c);
						break;
					case '\\':
						sb.append(c);
						sb.append(c);
						i++;
						break;
					default:
						sb.append(c);//+ 20140714 jd page .replaceWith("<div id=\"plist\" class
						sb.append(c);
						break;
					}
				}else{
					sb.append(c);//+ 20140714
					sb.append(c);
				}
				break;	
			default:
				sb.append(c);
				break;
			}
        }
        return sb.toString();
	}
}
