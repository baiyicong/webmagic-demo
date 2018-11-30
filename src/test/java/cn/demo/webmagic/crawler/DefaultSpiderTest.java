package cn.demo.webmagic.crawler;

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

import cn.demo.webmagic.config.SpiderConfig;
import cn.demo.webmagic.crawler.pipeline.HtmlDataPipeline;
import cn.demo.webmagic.crawler.pipeline.MySqlPipeline;
import cn.demo.webmagic.crawler.processor.DefaultProcessor;
import cn.demo.webmagic.crawler.proxy.DefaultProxyProvider;
import cn.demo.webmagic.mapper.DefaultHtmlMapper;
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
	public void testSoBaikeM() {
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
				.thread(30).run();
	}

    //@Test
    public void testMbalibWiki() {
        DefaultProxyProvider defaultProxyProvider = new DefaultProxyProviderImpl(null)
                .setTestUrl("https://wiki.mbalib.com/wiki/%E9%A6%96%E9%A1%B5")
                .setCheckIpThreadNumber(5);

        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(defaultProxyProvider);
        SpiderConfig spiderConfig = new SpiderConfig();

        spiderConfig.setTargetUrls(Arrays.asList("https?://wiki.mbalib.com/wiki/.*"));
        spiderConfig.setHtmlTableName("t_html_mbalib_wiki");
        Spider.create(new DefaultProcessor(spiderConfig)).addPipeline(new HtmlDataPipeline())
                .addPipeline(new MySqlPipeline(defaultHtmlMapper, spiderConfig.getHtmlTableName()))
                .setDownloader(httpClientDownloader).addUrl("https://wiki.mbalib.com/wiki/%E9%A6%96%E9%A1%B5")
                .thread(40).run();
    }
}
