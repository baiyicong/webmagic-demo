package cn.demo.webmagic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import cn.demo.webmagic.service.TestService;

@RestController
public class TestControll {
	@Autowired
	private TestService testService;
	@GetMapping("/getPojoById/{tableName}/{id}")
	public Object getPojoById(@PathVariable("tableName") String tableName,@PathVariable("id")Long id){
		return testService.getPojoById( tableName, id);
	}

}
