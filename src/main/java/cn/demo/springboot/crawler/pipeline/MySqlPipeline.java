package cn.demo.springboot.crawler.pipeline;

import cn.demo.springboot.entry.Html;
import cn.demo.springboot.mapper.DefaultHtmlMapper;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * Mysql 插入/更新操作
 *
 * @author byc
 */
public class MySqlPipeline implements Pipeline {

	private DefaultHtmlMapper defaultHtmlMapper;
	private String htmlTableName;

	public MySqlPipeline(DefaultHtmlMapper defaultHtmlMapper,String htmlTableName ) {
		this.defaultHtmlMapper = defaultHtmlMapper;
		this.htmlTableName = htmlTableName;
	}

	@Override
	public void process(ResultItems resultItems, Task task) {
		Html html=new Html();
		html.setHtmlTableName(htmlTableName);
		html.setSourceId(resultItems.get("source_id"));
		html.setUrl(resultItems.get("url"));
		html.setHtml(resultItems.get("html"));
		defaultHtmlMapper.insert(html);
	}
}
