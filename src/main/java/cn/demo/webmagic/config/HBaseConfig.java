package cn.demo.webmagic.config;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.demo.webmagic.test.hbase.HBaseUtils;

/**
 * @author baiyicong
 */
//@Configuration
public class HBaseConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(HBaseUtils.class);
	private static Connection CONNECTION = null;

	/**
	 * 初始化HBase配置
	 */
	@PostConstruct
	public void initHBase() {
		org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
		// 设置连接参数：HBase数据库所在的主机IP
		conf.set("hbase.zookeeper.quorum", "myhbase");
		// 设置连接参数：HBase数据库使用的端口
		conf.set("hbase.zookeeper.property.clientPort", "2181");
		ExecutorService pool = Executors.newScheduledThreadPool(10);
		try {
			CONNECTION = ConnectionFactory.createConnection(conf, pool);
			HBaseUtils.setconnection(CONNECTION);
		} catch (IOException e) {
			LOGGER.warn("HBase连接创建异常", e);
		}
	}

	/**
	 * 关闭HBase
	 */
	@PreDestroy
	public void closeHbase() {
		try {
			CONNECTION.close();
		} catch (IOException e) {
			LOGGER.warn("HBase连接关闭异常", e);
		}
	}

}
