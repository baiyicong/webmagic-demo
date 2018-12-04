package cn.demo.webmagic.crawler;

import cn.demo.webmagic.config.SpiderConfig;
import cn.demo.webmagic.crawler.pipeline.HtmlDataPipeline;
import cn.demo.webmagic.crawler.pipeline.MySqlPipeline;
import cn.demo.webmagic.crawler.processor.DefaultProcessor;
import cn.demo.webmagic.crawler.proxy.DefaultProxyProvider;
import cn.demo.webmagic.mapper.DefaultHtmlMapper;
import com.alibaba.fastjson.JSON;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author baiyicong
 * @Aate 2018年11月28日
 */
@Component
public class DefaultSpiderTest {
    @Resource
    private DefaultHtmlMapper defaultHtmlMapper;


    // @PostConstruct
    // public void testSoBaikeM() {
    //     DefaultProxyProvider defaultProxyProvider = new DefaultProxyProviderImpl(null)
    //             .setTestUrl("https://m.baike.so.com")
    //             .setCheckIpThreadNumber(2);
    //
    //     HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
    //     httpClientDownloader.setProxyProvider(defaultProxyProvider);
    //     SpiderConfig spiderConfig = new SpiderConfig();
    //
    //     spiderConfig.setTargetUrls(Arrays.asList("https?://m.baike.so.com/doc/([\\d-]+).html?([?#].*)?"));
    //     spiderConfig.setHtmlTableName("t_html_so_baike_m");
    //     Spider.create(new DefaultProcessor(spiderConfig)).addPipeline(new HtmlDataPipeline(Pattern.compile("https?://m.baike.so.com/doc/([\\d-]+).html?([?#].*)?"), 1))
    //             .addPipeline(new MySqlPipeline(defaultHtmlMapper, spiderConfig.getHtmlTableName()))
    //             .setDownloader(httpClientDownloader).addUrl("https://m.baike.so.com")
    //             .thread(10).run();
    // }

    // @Test
    public void testMbalibWiki() {
        DefaultProxyProvider defaultProxyProvider = new DefaultProxyProvider(() -> {
            String url = "http://10.101.93.64:5010/get_all";
            String respStr = null;
            try {
                respStr = Jsoup.connect(url).ignoreContentType(true).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<String> ipList = JSON.parseArray(respStr, String.class);
            return ipList.stream().map(proxy -> {
                String[] arr = proxy.split(":");
                return new Proxy(arr[0], Integer.parseInt(arr[1]));
            }).collect(Collectors.toList());
        })
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
