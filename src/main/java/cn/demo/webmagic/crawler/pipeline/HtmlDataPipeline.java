package cn.demo.webmagic.crawler.pipeline;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 抽取HTML基础数据信息
 *
 * @author byc
 */
public class HtmlDataPipeline implements Pipeline {

    private Pattern sourceIdPattern;
    private int group;


    public HtmlDataPipeline() {
    }

    public HtmlDataPipeline(Pattern sourceIdPattern, int group) {
        this.sourceIdPattern = sourceIdPattern;
        this.group = group;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        String url = resultItems.getRequest().getUrl();
        String html = resultItems.get("html");

        String sourceId = url.replaceAll("^https?://", "");

        if (sourceIdPattern != null) {
            Matcher matcher = sourceIdPattern.matcher(url);
            if (matcher.find()) {
                sourceId = matcher.group(group);
            }
        }

        resultItems.put("source_id", sourceId);
        resultItems.put("url", url);
        resultItems.put("html", html);
    }
}
