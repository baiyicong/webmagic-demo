package cn.demo.springboot.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import cn.demo.springboot.entry.Html;

/**
* @author baiyicong
* @date 2018年11月7日
*/
@Mapper
public interface SoBaikeMapper {

	@Insert("INSERT IGNORE INTO t_html_so_baike(`source_id`,`url`,`html`) VALUES (#{sourceId}, #{url},#{html})") 
    void insert(Html html);
}
