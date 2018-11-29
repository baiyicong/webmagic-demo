package cn.demo.springboot.crawler.processor;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.demo.springboot.crawler.processor.DefaultProcessor.UserAgent;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author wangyupeng
 */
public class ProxyTestProcessor implements PageProcessor {
	private Site site = Site.me().setUserAgent(UserAgent.PC).setTimeOut(5 * 1000).setRetryTimes(2).setCycleRetryTimes(3)
			.setSleepTime(2000).setDisableCookieManagement(false); // 开启Cookie支持
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public Site getSite() {
		return site;
	}
	@Override
	public void process(Page page) {
		// logger.error("取得结果：{}", page.getRawText());
		page.addTargetRequest("https://baike.so.com?t=" + System.currentTimeMillis());
		
	}

	public static void main(String[] args) throws IOException {
		String url = "http://10.101.93.64:5010/get_all";
		String respStr = Jsoup.connect(url).ignoreContentType(true).execute().body();

		List<String> ipList = JSON.parseArray(respStr, String.class);
		HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
//		VivoProxyProvider vivoProxyProvider = new VivoProxyProvider(ipList);
//		vivoProxyProvider.setTestUrl("https://baike.so.com");
//		httpClientDownloader.setProxyProvider(vivoProxyProvider);
		Spider spider = Spider.create(new ProxyTestProcessor()).addPipeline(new ConsolePipeline())
				.setDownloader(httpClientDownloader);

		spider.addUrl("https://baike.so.com");
		spider.run();
	}
}
