package cn.demo.springboot.crawler.test;

import cn.demo.springboot.entry.Html;
import cn.demo.springboot.mapper.SoBaikeMapper;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @author baiyicong
 * @date 2018年11月7日
 */
public class SoBaikePipeline implements Pipeline {
	private SoBaikeMapper soBaikeMapper;

	public SoBaikePipeline(SoBaikeMapper soBaikeMapper) {
		this.soBaikeMapper = soBaikeMapper;
	}

	@Override
	public void process(ResultItems resultItems, Task task) {
		Object sourceId = resultItems.get("sourceId");
		Object url = resultItems.get("url");
		Object html = resultItems.get("html");
		if (sourceId != null && html != null && url != null) {
			Html htmlEnty = new Html();
			htmlEnty.setSourceId(sourceId.toString());
			htmlEnty.setUrl(url.toString());
			htmlEnty.setHtml(html.toString());
			soBaikeMapper.insert(htmlEnty);
		}

	}

}
