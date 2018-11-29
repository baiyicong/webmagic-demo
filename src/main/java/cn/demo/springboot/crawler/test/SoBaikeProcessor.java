package cn.demo.springboot.crawler.test;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cn.demo.springboot.crawler.processor.DefaultProcessor;
import cn.demo.springboot.crawler.processor.DefaultProcessor.UserAgent;
import cn.demo.springboot.crawler.proxy.DefaultProxyProvider;
import cn.demo.springboot.mapper.SoBaikeMapper;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

/**
 * @author baiyicong
 * @date 2018年11月7日
 */
// @Component
public class SoBaikeProcessor implements PageProcessor {

	private Site site = Site.me().setUserAgent(UserAgent.PC).setTimeOut(5 * 1000).setRetryTimes(2).setCycleRetryTimes(3)
			.setSleepTime(2000).setDisableCookieManagement(false); // 开启Cookie支持
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Site getSite() {
		return site;
	}

	@Autowired
	private SoBaikeMapper soBaikeMapper;

	@Override
	public void process(Page page) {

		String url = page.getUrl().get();
		Html html = page.getHtml();

		Request request = page.getRequest();

		Selectable links = html.links();
		List<String> allUrl = links.regex("https?://baike.so.com/doc/([\\d-]+).html?([?#].*)?", 0).all();
		page.addTargetRequests(allUrl);
		page.putField("sourceId", page.getUrl().regex("https?://baike.so.com/doc/([\\d-]+).html?([?#].*)?", 1).get());
		page.putField("url", url);
		page.putField("html", html);

	}

	class VivoProxyProviderImpl extends DefaultProxyProvider {

		public VivoProxyProviderImpl(List<String> proxyList) {
			super(proxyList);
		}

		@Override
		protected List<String> listProxyProvider() {
			String url = "http://10.101.93.64:5010/get_all";
			String respStr = null;
			try {
				respStr = Jsoup.connect(url).ignoreContentType(true).execute().body();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			List<String> ipList = JSON.parseArray(respStr, String.class);
			return ipList;
		}

	}

	// @PostConstruct
	public void init() throws IOException {
		// String url = "http://10.101.93.64:5010/get_all";
		// String respStr =
		// Jsoup.connect(url).ignoreContentType(true).execute().body();
		//
		// List<String> ipList = JSON.parseArray(respStr, String.class);
		DefaultProxyProvider vivoProxyProvider = new VivoProxyProviderImpl(null).setTestUrl("https://baike.so.com")
				.setCheckIpThreadNumber(20);

		HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
		httpClientDownloader.setProxyProvider(vivoProxyProvider);
		Spider.create(new SoBaikeProcessor()).thread(1)
				// .addPipeline(new ConsolePipeline())
				.addPipeline(new SoBaikePipeline(soBaikeMapper)).setDownloader(httpClientDownloader)
				.addUrl("https://baike.so.com").run();

		// Spider.create(new ProxyIpProcessor()).thread(1).addPipeline(new
		// ProxyIpPipeline(vivoProxyProvider))
		// .addUrl("http://10.101.93.64:5010/get_all?t=").runAsync();

	}

	public static void main(String[] args) throws IOException {
		new SoBaikeProcessor().init();
	}

	public void test() {

		// 获取代理ip
		// ProxyCralwerUnusedVPN proxyCrawler = new
		// ProxyCralwerUnusedVPN("https://baike.so.com");
		// proxyCrawler.startCrawler(2);
		// List<ProxyInfo> proxyInfos = proxyCrawler.localProxyInfos.get();
		// Proxy[] proxys=new Proxy[proxyInfos.size()];
		// for (int i = 0; i < proxyInfos.size(); i++) {
		// ProxyInfo proxyInfo = proxyInfos.get(i);
		// proxys[i]=new
		// Proxy(proxyInfo.getIp(),Integer.parseInt(proxyInfo.getPort()));
		// }

		HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
		// httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(proxys));

		Spider.create(new SoBaikeProcessor()).setDownloader(httpClientDownloader)
				.addUrl("https://baike.so.com/doc/7372432.html#7372432-7640361-3")
				.addPipeline(new SoBaikePipeline(soBaikeMapper))
				// .addPipeline(new ConsolePipeline())
				.thread(1).run();
	}
}
