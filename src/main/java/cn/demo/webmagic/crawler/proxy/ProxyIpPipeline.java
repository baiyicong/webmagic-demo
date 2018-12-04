package cn.demo.webmagic.crawler.proxy;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.proxy.Proxy;

import java.util.List;
import java.util.stream.Collectors;

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
        vivoProxyProvider.offerTestProxyIps(ipList.stream().map(proxy -> {
            String[] arr = proxy.split(":");
            return new Proxy(arr[0], Integer.parseInt(arr[1]));
        }).collect(Collectors.toList()));
    }

}
