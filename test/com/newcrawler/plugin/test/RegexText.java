package com.newcrawler.plugin.test;

import java.util.regex.Pattern;

public class RegexText {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String urlString="https://hk.newcrawler.com/zh-cn/member/index.html";
		String jsFilterRegexs="https://hk.newcrawler.com/*|http://phantomjs.org/api/webpage/method/reload.html";
		
		String filterRegexs = null;
		if (jsFilterRegexs != null && !"".equals(jsFilterRegexs.trim())) {
			String[] regexs = jsFilterRegexs.split("\\Q|\\E");
			int len = regexs.length;
			for (int i = 0; i < len; i++) {
				String regex = regexs[i];
				regex = regex.trim();
				regex = "^\\Q"+regex+"\\E$";
				if(regex.indexOf("*")!=-1){
					regex = regex.replaceAll("\\*", "\\\\E.*\\\\Q");
				}
				if(filterRegexs==null){
					filterRegexs=regex;
				}else{
					filterRegexs=filterRegexs+"|"+regex;
				}
			}
		}
		System.out.println(filterRegexs);
		
		Pattern p = Pattern.compile(filterRegexs);
		if (p.matcher(urlString).find()) {
			System.out.println("true");
		}
	}

}
