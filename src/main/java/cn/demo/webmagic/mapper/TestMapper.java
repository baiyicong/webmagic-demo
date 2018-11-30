package cn.demo.webmagic.mapper;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TestMapper {
	@ResultType(HashMap.class)
	@Select("select * from  ${tableName}  where outer_id = #{id}")
	public Map<String, Object> getPojoById(@Param("tableName") String tableName, @Param("id") Long id);

}
