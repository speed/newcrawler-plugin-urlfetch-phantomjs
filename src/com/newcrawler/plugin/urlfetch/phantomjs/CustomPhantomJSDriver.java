package com.newcrawler.plugin.urlfetch.phantomjs;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public class CustomPhantomJSDriver extends PhantomJSDriver{
	public CustomPhantomJSDriver(Capabilities desiredCapabilities){
		super(desiredCapabilities);
	}
	
	public void startSession(Capabilities desiredCapabilities){
		super.startSession(desiredCapabilities);
	}
}
