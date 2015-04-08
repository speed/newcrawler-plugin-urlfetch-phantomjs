package com.newcrawler.plugin.test;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
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
                "D:/Workspace/workspace j2ee/newcrawler-plugin-urlfetch-selenium/phantomjs/windows/phantomjs.exe");
        
        String[] arg = new String[] { "--ignore-ssl-errors=yes" };
        ((DesiredCapabilities) caps).setCapability(
                PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
                arg);
        
        WebDriver   driver = new  PhantomJSDriver(caps);

        driver.get("http://list.jd.com/list.html?cat=652,653,655");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("http://www.google.com");

        // Check the title of the page
        System.out.println("Page source: " + driver.getPageSource());

        
        //Close the browser
        driver.quit();
    }
}
