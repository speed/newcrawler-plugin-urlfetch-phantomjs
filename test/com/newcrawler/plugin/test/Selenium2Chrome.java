package com.newcrawler.plugin.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Selenium2Chrome  {
	
    public static void main(String[] args) throws IOException {
    	String cookieString="";
    	String urlString="http://weibo.com/p/1001018008644010000000000/checkin?page=4";
    	
    	ChromeDriver driver=null;
    	try{
    		System.setProperty("webdriver.chrome.driver", "D:/js/chromedriver.exe");
            
        	
        	HashMap<String, Object> settings = new HashMap<String, Object>(); 
        	settings.put("images", 2); //disabled load images
            

            HashMap<String, Object> prefs = new HashMap<String, Object>(); 
            prefs.put("profile.managed_default_content_settings", settings); 
            

            ChromeOptions options =new ChromeOptions(); 
            options.setExperimentalOption("prefs", prefs); 

            DesiredCapabilities chromeCaps = DesiredCapabilities.chrome(); 
            chromeCaps.setCapability(ChromeOptions.CAPABILITY, options); 
            
        	driver = new ChromeDriver(chromeCaps);
        	
        	driver.navigate().to("http://weibo.com");
        	
            driver.manage().deleteAllCookies();
            
            URL url=new URL(urlString);
            
            List<Cookie> list=cookie(url, cookieString);
            for(Cookie cookie:list){
            	driver.manage().addCookie(cookie);
            }
            
            driver.navigate().to(url);
            String content=driver.getPageSource();
            
            FileUtils.writeStringToFile(new File("D:/temp/test.html"), content);
            // Check the title of the page
            System.out.println("Page source: " + content);
            System.out.println("getCurrentUrl: " + driver.getCurrentUrl());
    	}finally{
    		 //Close the browser
    		if(driver!=null){
    			driver.quit();
    		}
    	}
    }
    
    private static List<Cookie> cookie(URL url, String cookieString){
    	String cookies[]=cookieString.split("; ");
    	
    	List<Cookie> list=new ArrayList<Cookie>();
    	for(String c:cookies){
    		String temp[]=c.split("=", 2);
    		
    		Cookie cookie = new Cookie.Builder(temp[0], temp[1])
    			    .domain(url.getHost())
    			    .path("/")
    			    .build();
    		
    		list.add(cookie);
    	}
    	return list;
    }
}
