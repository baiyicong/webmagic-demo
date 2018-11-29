package cn.demo.springboot.crawler.processor;

import java.util.List;

import com.alibaba.fastjson.JSON;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author baiyicong
 */
public class ProxyIpProcessor implements PageProcessor {

	private Site site = Site.me()
			.setUserAgent(
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36")
			.setTimeOut(5 * 1000).setRetryTimes(2).setCycleRetryTimes(3).setSleepTime(1000 * 60 * 10)
			.setDisableCookieManagement(false); // 开启Cookie支持

	private  String url = "http://10.101.93.64:5010/get_all?t=";

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		page.addTargetRequest(url + System.currentTimeMillis());
		String rawText = page.getRawText();
		List<String> ipList = JSON.parseArray(rawText, String.class);
		page.putField("ipList", ipList);
	}

}
