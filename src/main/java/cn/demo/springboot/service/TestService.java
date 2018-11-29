package cn.demo.springboot.service;

import java.util.Map;

public interface TestService {

	public Map<String, Object>  getPojoById(String tableName, Long id);

}
