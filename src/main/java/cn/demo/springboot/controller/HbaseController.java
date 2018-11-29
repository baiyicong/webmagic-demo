package cn.demo.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.demo.springboot.test.hbase.HBaseUtils;

/**
 * @Description:
 * @Author baiyicong
 * @Aate 2018年11月13日
 */
@RestController
@RequestMapping("/hbase")
public class HbaseController {
	@GetMapping("/list/{tableName}/{prefix}")
	public Object list(@PathVariable("tableName") String tableName,@PathVariable("prefix")String prefix) {
		return HBaseUtils.list(tableName,prefix);
	}
}
