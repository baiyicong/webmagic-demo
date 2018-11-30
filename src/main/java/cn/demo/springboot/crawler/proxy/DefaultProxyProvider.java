package cn.demo.springboot.crawler.proxy;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.ProxyProvider;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author baiyicong
 */
public abstract class DefaultProxyProvider implements ProxyProvider {
    private Logger logger = LoggerFactory.getLogger(getClass());
    // 测试通过的ip
    protected final Deque<Proxy> proxyIps = new ConcurrentLinkedDeque<Proxy>();

    // 即将测试的ip
    protected final Deque<Proxy> testProxyIps = new ConcurrentLinkedDeque<Proxy>();

    private AtomicInteger threadTaskCount = new AtomicInteger(0);

    protected String testUrl = "https://www.baidu.com/";
    // 测试校验ip的线程数
    protected Integer checkIpThreadNumber = 5;

    // proxyIps队列最小个数,当底于这个数时，会触发测试线程开启
    protected Integer minProxyIpsNumber = 20;

    // 创建线程池
    protected ExecutorService threadPool = Executors.newFixedThreadPool(checkIpThreadNumber);

    // 获得新的代理ip,
    protected abstract List<String> listProxyProvider();

    public DefaultProxyProvider(List<String> proxyList) {
        if (proxyList != null) {
            this.offerTestProxyIps(proxyList);
        }
        // 校验ip
        asyncCheckProxy();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Integer getCheckIpThreadNumber() {
        return checkIpThreadNumber;
    }

    public DefaultProxyProvider setCheckIpThreadNumber(Integer checkIpThreadNumber) {
        this.checkIpThreadNumber = checkIpThreadNumber;
        return this;
    }

    public String getTestUrl() {
        return testUrl;
    }

    public DefaultProxyProvider setTestUrl(String testUrl) {
        this.testUrl = testUrl;
        return this;
    }

    // 即将测试ip存放点
    public void offerTestProxyIps(List<String> proxyList) {
        if (proxyList != null) {
            for (String proxy : proxyList) {
                String[] arr = proxy.split(":");
                testProxyIps.addLast(new Proxy(arr[0], Integer.parseInt(arr[1])));
            }
        }
    }

    private void asyncCheckProxy() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                offerTestProxyIps(listProxyProvider());
                while (true) {
                    Proxy proxy = testProxyIps.pollLast();
                    if (proxy != null) {
                        threadTaskCount.incrementAndGet();
                        CheckProxyTask checkProxyTask = new CheckProxyTask(proxy, testUrl);
                        threadPool.submit(checkProxyTask);
                        logger.info("----------------------有效ip个数" + proxyIps.size());
                    } else if (proxyIps.size() < minProxyIpsNumber) {
                        offerTestProxyIps(listProxyProvider());
                    } else {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }).start();
    }

    public void offerLastProxy(Proxy proxy) {
        if (proxy != null) {
            proxyIps.offerLast(proxy);
        }
    }

    @Override
    public void returnProxy(Proxy proxy, Page page, Task task) {
        if (proxy != null) {
            // 下载页面失败则需要ip校验是否还能用
            if (!page.isDownloadSuccess()) {
                testProxyIps.push(proxy);
            } else {
                proxyIps.offerLast(proxy);
            }
        }
    }

    @Override
    public Proxy getProxy(Task task) {
        Proxy proxy = null;
        while ((proxy = proxyIps.pollFirst()) == null) {
            logger.warn("*********当前没有可用的代理ip");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return proxy;
    }

    // 用测试的url测试ip是否有效
    class CheckProxyTask implements Callable<Boolean> {
        private Proxy proxy;

        private String testUrl;

        public CheckProxyTask(Proxy proxy, String testUrl) {
            this.proxy = proxy;
            this.testUrl = testUrl;
        }

        @Override
        public Boolean call() {
            if (checkIpConnec(proxy.getHost(), proxy.getPort())) {
                // 有效放回队列尾部
                offerLastProxy(proxy);
                threadTaskCount.decrementAndGet();
                return true;
            }
            threadTaskCount.decrementAndGet();
            return false;
        }

        private boolean checkIpConnec(String ip, Integer port) {
            try {
                // http://1212.ip138.com/ic.asp 可以换成任何比较快的网页
                // Jsoup.connect("http://1212.ip138.com/ic.asp")
                Jsoup.connect(testUrl).timeout(2 * 1000).proxy(ip, port).get();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

}
