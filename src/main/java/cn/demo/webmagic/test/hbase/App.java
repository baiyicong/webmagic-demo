package cn.demo.webmagic.test.hbase;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.hadoop.hbase.HbaseTemplate;

/**
 * @Description:
 * @Author baiyicong
 * @Aate 2018年11月10日
 */
@SpringBootApplication
public class App {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	private String quorum = "192.168.130.130";
	private String port = "60010";

	@Bean
	public HbaseTemplate hbaseTemplate() {
		HbaseTemplate hbaseTemplate = new HbaseTemplate();
		org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", quorum);
		conf.set("hbase.zookeeper.port", port);
		hbaseTemplate.setConfiguration(conf);
		hbaseTemplate.setAutoFlush(true);
		return hbaseTemplate;
	}
	
	
}
