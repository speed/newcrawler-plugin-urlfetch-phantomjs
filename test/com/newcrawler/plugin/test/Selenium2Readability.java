package com.newcrawler.plugin.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * https://github.com/speed/readability
 * https://github.com/speed/readable-proxy
 * @author wisers
 *
 */
public class Selenium2Readability  {
    public static void main(String[] args) throws IOException {
        // Create a new instance of the Firefox driver
        // Notice that the remainder of the code relies on the interface, 
        // not the implementation.
        
        Capabilities caps = DesiredCapabilities.phantomjs();
        
        ((DesiredCapabilities) caps).setJavascriptEnabled(true);      
        ((DesiredCapabilities) caps).setCapability(
                PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                "D:\\js\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        
        String[] arg = new String[] { "--ignore-ssl-errors=yes"};
        
        
        ((DesiredCapabilities) caps).setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, arg);
        ((DesiredCapabilities) caps).setCapability("phantomjs.page.customHeaders.User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");
        ((DesiredCapabilities) caps).setCapability("phantomjs.page.customHeaders.Accept-Encoding", "gzip, deflate, sdch");
        ((DesiredCapabilities) caps).setCapability("phantomjs.page.customHeaders.Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        ((DesiredCapabilities) caps).setCapability("phantomjs.page.customHeaders.Test", "no-cache");
        
        PhantomJSDriver   driver = new  PhantomJSDriver(caps);
        String url="http://www.bokon.net/novel-9/1229301/17739071.html";
        String runReadability="C:/Users/wisers/git/newcrawler-plugin-urlfetch-phantomjs/readability/Readability.js";
        String run="C:/Users/wisers/git/newcrawler-plugin-urlfetch-phantomjs/readability/run.js";
        
        run= FileUtils.readFileToString(new File(run));
        
        driver.get(url);
        driver.navigate().to(url);

        driver.executePhantomJS(""
				+ "var page = this;"
				+ run
				+ "page.injectJs('"+runReadability+"');"
				+ "var result = page.evaluate(runReadability, '"+url+"', page.settings.userAgent, page.content); page.result=result; console.log(page.url+' : '+JSON.stringify(result));"
				+ "return 'Done';"
				);
        
        Object result=driver.executePhantomJS("var page = this; return page.result;");
        
        // Check the title of the page
        System.out.println("Page source: " + result);

        //Close the browser
        driver.quit();
    }
}
