package com.newcrawler.plugin.test;

import java.util.Map;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Selenium2Example  {
    public static void main(String[] args) {
        // Create a new instance of the Firefox driver
        // Notice that the remainder of the code relies on the interface, 
        // not the implementation.
        
        Capabilities caps = DesiredCapabilities.phantomjs();
        
        ((DesiredCapabilities) caps).setJavascriptEnabled(true);      
        ((DesiredCapabilities) caps).setCapability(
                PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                "D:\\js\\phantomjs-2.0.0-windows\\bin\\phantomjs.exe");
        
        String[] arg = new String[] { "--ignore-ssl-errors=yes" };
        ((DesiredCapabilities) caps).setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, arg);
        ((DesiredCapabilities) caps).setCapability("phantomjs.page.customHeaders.User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");
        ((DesiredCapabilities) caps).setCapability("phantomjs.page.customHeaders.Accept-Encoding", "gzip, deflate, sdch");
        ((DesiredCapabilities) caps).setCapability("phantomjs.page.customHeaders.Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        ((DesiredCapabilities) caps).setCapability("phantomjs.page.customHeaders.Test", "no-cache");
        
        PhantomJSDriver   driver = new  PhantomJSDriver(caps);
        
        driver.get("http://www.newcrawler.com/header");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("http://www.google.com");

        // Check the title of the page
        System.out.println("Page source: " + driver.getPageSource());
        Object obj=driver.getCapabilities().asMap().get("phantomjs.page.customHeaders.User-Agent");
        Object obj2="Mozilla/6.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36";
        
        driver.executePhantomJS(""
        		+ "var page = this;"
        		+ "page.customHeaders  = {'User-Agent':'Mozilla/6.0', 'Accept-Encoding':'gzip'};"
        		);
        driver.navigate().to("http://www.newcrawler.com/header");

        // Check the title of the page
        System.out.println("Page source: " + driver.getPageSource());
        //Close the browser
        driver.quit();
    }
}
