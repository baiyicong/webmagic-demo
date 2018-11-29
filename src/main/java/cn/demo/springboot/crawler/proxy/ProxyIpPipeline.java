package cn.demo.springboot.crawler.proxy;

import java.util.List;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @Description:
 * @Author baiyicong
 * @Aate 2018年11月8日
 */
public class ProxyIpPipeline implements Pipeline {

	private DefaultProxyProvider vivoProxyProvider;

	public ProxyIpPipeline(DefaultProxyProvider vivoProxyProvider) {
		this.vivoProxyProvider = vivoProxyProvider;
	}

	@Override
	public void process(ResultItems resultItems, Task task) {
		List<String> ipList = (List<String>) resultItems.get("ipList");
		// 归还给测试队列
		vivoProxyProvider.offerTestProxyIps(ipList);
	}

}
