package com.newcrawler.plugin.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Selenium2ChromeTest  {
	
    public static void main(String[] args) throws IOException {
    	
    	ChromeDriver driver=null;
    	try{
    		System.setProperty("webdriver.chrome.driver", "/root/chromedriver");
            
        	
        	HashMap<String, Object> settings = new HashMap<String, Object>(); 
        	settings.put("images", 2); //disabled load images
            

            HashMap<String, Object> prefs = new HashMap<String, Object>(); 
            prefs.put("profile.managed_default_content_settings", settings); 
            

            ChromeOptions options =new ChromeOptions(); 
            options.setExperimentalOption("prefs", prefs); 

            DesiredCapabilities chromeCaps = DesiredCapabilities.chrome(); 
            chromeCaps.setCapability(ChromeOptions.CAPABILITY, options); 
            
        	driver = new ChromeDriver(chromeCaps);
        	
        	driver.navigate().to("http://list.jd.com/list.html?cat=9987%2C653%2C655&go=0");
        	
            String content=driver.getPageSource();
            
            FileUtils.writeStringToFile(new File("/root/list.html"), content);
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
    
}
