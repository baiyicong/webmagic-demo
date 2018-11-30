package cn.demo.springboot.crawler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;

import cn.demo.springboot.config.SpiderConfig;
import cn.demo.springboot.crawler.pipeline.HtmlDataPipeline;
import cn.demo.springboot.crawler.pipeline.MySqlPipeline;
import cn.demo.springboot.crawler.processor.DefaultProcessor;
import cn.demo.springboot.crawler.proxy.DefaultProxyProvider;
import cn.demo.springboot.mapper.DefaultHtmlMapper;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

/**
 * @Description:
 * @Author baiyicong
 * @Aate 2018年11月28日
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DefaultSpiderTest {
	@Autowired
	private DefaultHtmlMapper defaultHtmlMapper;

	static class DefaultProxyProviderImpl extends DefaultProxyProvider {

		public DefaultProxyProviderImpl(List<String> proxyList) {
			super(proxyList);
		}

		@Override
		protected List<String> listProxyProvider() {
			String url = "http://10.101.93.64:5010/get_all";
			String respStr = null;
			try {
				respStr = Jsoup.connect(url).ignoreContentType(true).execute().body();
			} catch (IOException e) {
				e.printStackTrace();
			}

			List<String> ipList = JSON.parseArray(respStr, String.class);
			return ipList;
		}

	}

	@Test
	public void test() {
		DefaultProxyProvider defaultProxyProvider = new DefaultProxyProviderImpl(null)
				 .setTestUrl("https://m.baike.so.com")
				.setCheckIpThreadNumber(5);

		HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
		httpClientDownloader.setProxyProvider(defaultProxyProvider);
		SpiderConfig spiderConfig = new SpiderConfig();
		
		spiderConfig.setTargetUrls(Arrays.asList("https?://m.baike.so.com/doc/([\\d-]+).html?([?#].*)?"));
		spiderConfig.setHtmlTableName("t_html_so_baike_m");
		Spider.create(new DefaultProcessor(spiderConfig)).addPipeline(new HtmlDataPipeline(Pattern.compile("https?://m.baike.so.com/doc/([\\d-]+).html?([?#].*)?"),1))
				.addPipeline(new MySqlPipeline(defaultHtmlMapper, spiderConfig.getHtmlTableName()))
				.setDownloader(httpClientDownloader).addUrl("https://m.baike.so.com")
				.thread(40).run();
	}
}
