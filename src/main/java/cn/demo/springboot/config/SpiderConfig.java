package cn.demo.springboot.config;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author wangyupeng
 */
public class SpiderConfig {

    /**
     * 线程数
     */
    @NotNull
    @Range(min = 1, max = 50)
    private int thread;

    /**
     * 爬虫名称
     */
    @NotNull
    @Length(min = 1, max = 50)
    private String name;

    /**
     * 爬虫唯一标识
     */
    @NotNull
    @Length(min = 1, max = 255)
    private String uuid;

    /**
     * 浏览器标识
     */
    @NotNull
    @Length(min = 1, max = 255)
    private String userAgent;

    /**
     * 最初的种子请求
     */
    @NotEmpty
    private List<String> seedLinks;

    /**
     * 目标URL的正则列表
     */
    @NotEmpty
    private List<String> targetUrls=Collections.emptyList();


    /**
     * 辅助URL的正则列表
     */
    @NotNull
    private List<String> helpUrls=Collections.emptyList();

    /**
     * 排除URL的正则列表
     */
    @NotNull
    private List<String> excludeUrls=Collections.emptyList();


    /**
     * 每次爬取后睡眠时间
     */
    @Range(min = 0, max = 60_000)
    private int sleepTime;

    /**
     * 超时时间
     */
    @Range(min = 0, max = 60_000)
    private int timeOut;

    /**
     * 请求重试次数
     */
    @Range(min = 0, max = 10)
    private int retryTimes;

    /**
     * 循环重试次数
     */
    @Range(min = 0, max = 10)
    private int cycleRetryTimes;

    /**
     * 是否使用代理
     */
    private boolean useProxy = false;

    /**
     * 是否是移动页面
     */
    private boolean mobilePage = false;

    /**
     * 预估数量
     */
    private int expectedInsertions;

    /**
     * 标签，用于HBase的插入
     */
    @NotBlank
    private String tags;

    /**
     * HTML表名，用于MySQL的插入
     */
    private String htmlTableName;


    public int getThread() {
        return thread;
    }

    public SpiderConfig setThread(int thread) {
        this.thread = thread;
        return this;
    }

    public String getName() {
        return name;
    }

    public SpiderConfig setName(String name) {
        this.name = name;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public SpiderConfig setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public SpiderConfig setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public List<String> getSeedLinks() {
        return seedLinks;
    }

    public SpiderConfig setSeedLinks(List<String> seedLinks) {
        this.seedLinks = seedLinks;
        return this;
    }

    public List<String> getTargetUrls() {
        return targetUrls;
    }

    public SpiderConfig setTargetUrls(List<String> targetUrls) {
        this.targetUrls = targetUrls;
        return this;
    }

    public List<String> getHelpUrls() {
        return helpUrls;
    }

    public SpiderConfig setHelpUrls(List<String> helpUrls) {
        this.helpUrls = helpUrls;
        return this;
    }

    public List<String> getExcludeUrls() {
        return excludeUrls;
    }

    public SpiderConfig setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
        return this;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public SpiderConfig setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public SpiderConfig setTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public SpiderConfig setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public int getCycleRetryTimes() {
        return cycleRetryTimes;
    }

    public SpiderConfig setCycleRetryTimes(int cycleRetryTimes) {
        this.cycleRetryTimes = cycleRetryTimes;
        return this;
    }

    public boolean isUseProxy() {
        return useProxy;
    }

    public SpiderConfig setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
        return this;
    }

    public boolean isMobilePage() {
        return mobilePage;
    }

    public SpiderConfig setMobilePage(boolean mobilePage) {
        this.mobilePage = mobilePage;
        return this;
    }

    public int getExpectedInsertions() {
        return expectedInsertions;
    }

    public SpiderConfig setExpectedInsertions(int expectedInsertions) {
        this.expectedInsertions = expectedInsertions;
        return this;
    }

    public String getTags() {
        return tags;
    }

    public SpiderConfig setTags(String tags) {
        this.tags = tags;
        return this;
    }

    public String getHtmlTableName() {
        return htmlTableName;
    }

    public SpiderConfig setHtmlTableName(String htmlTableName) {
        this.htmlTableName = htmlTableName;
        return this;
    }
}
