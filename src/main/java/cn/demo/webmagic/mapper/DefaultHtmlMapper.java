package cn.demo.webmagic.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import cn.demo.webmagic.entry.Html;

/**
* @author baiyicong
* @date 2018年11月7日
*/
@Mapper
public interface DefaultHtmlMapper {

	@Insert("INSERT  INTO ${htmlTableName}(`source_id`,`url`,`html`) VALUES (#{sourceId}, #{url},#{html}) ON DUPLICATE KEY UPDATE `url`=#{url},`html`=#{html}") 
//	@Insert("INSERT  INTO ${htmlTableName}(`source_id`,`url`,`html`) VALUES (#{sourceId}, #{url},#{html}) ") 
    void insert(Html html);
}
