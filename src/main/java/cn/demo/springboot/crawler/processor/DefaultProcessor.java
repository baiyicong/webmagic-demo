package cn.demo.springboot.crawler.processor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cn.demo.springboot.config.SpiderConfig;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

/**
 * @author byc
 */
public class DefaultProcessor implements PageProcessor {

	private Site site = Site.me().setUserAgent(UserAgent.PC).setTimeOut(5 * 1000).setRetryTimes(2).setCycleRetryTimes(3)
			.setSleepTime(2000).setDisableCookieManagement(false); // 开启Cookie支持

	private SpiderConfig spiderConfig;

	
	public DefaultProcessor(SpiderConfig spiderConfig) {
		this.spiderConfig=spiderConfig;
	}

	@Override
	public void process(Page page) {
		Selectable urlSelectable = page.getUrl();

		// 是否是目标网址
		boolean isTargetUrl = false;
		for (String targetUrl : spiderConfig.getTargetUrls()) {
			if (urlSelectable.regex(targetUrl).match()) {
				isTargetUrl = true;
				break;
			}
		}

		// 是否跳过后续处理
		if (isTargetUrl) {
			page.setSkip(false);
		} else {
			page.setSkip(true);
		}

		// 添加其他符合要求的网址
		Selectable links = page.getHtml().links();
		Set<String> addUrlSet = new HashSet<>(64);

		// 添加目标页网址
		for (String targetUrl : spiderConfig.getTargetUrls()) {
			addUrlSet.addAll(links.regex(targetUrl, 0).all());
		}
		// 添加辅助页网址
		for (String helpUrl : spiderConfig.getHelpUrls()) {
			addUrlSet.addAll(links.regex(helpUrl, 0).all());
		}

		// 排除不要的网址
		for (String excludeUrl : spiderConfig.getExcludeUrls()) {
			addUrlSet.removeIf(url -> url.matches(excludeUrl));
		}
		page.putField("html", page.getHtml().get());
		page.addTargetRequests(new ArrayList<>(addUrlSet));

	}

	public DefaultProcessor setSite(Site site) {
		this.site = site;
		return this;
	}

	@Override
	public Site getSite() {
		return site;
	}

	public class UserAgent {
		public static final String PC = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36";
		public static final String ANDROID = "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Mobile Safari/537.36";
		public static final String IOS = "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1";
	}
}
