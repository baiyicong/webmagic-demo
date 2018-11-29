package cn.demo.springboot.entry;

/**
 * @Description:
 * @Author baiyicong
 * @Aate 2018年11月7日
 */
public class Html {
	private static final long serialVersionUID = 1L;
	
	private String htmlTableName;

	private Integer id;

	private String sourceId;

	private String url;

	private String html;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getHtmlTableName() {
		return htmlTableName;
	}

	public void setHtmlTableName(String htmlTableName) {
		this.htmlTableName = htmlTableName;
	}

	
}
