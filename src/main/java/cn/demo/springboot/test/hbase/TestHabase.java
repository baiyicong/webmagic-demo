package cn.demo.springboot.test.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author wangyupeng
 */
public class TestHabase {
	Connection conn = null;
	Admin admin = null;

	/**
	 * 创建名称空间
	 *
	 * @throws Exception
	 */
	public void getConnection() throws Exception {
		// 取得一个数据库连接的配置参数对象
		Configuration conf = HBaseConfiguration.create();

		// 设置连接参数：HBase数据库所在的主机IP
		conf.set("hbase.zookeeper.quorum", "192.168.130.130");

		// 设置连接参数：HBase数据库使用的端口
		conf.set("hbase.zookeeper.property.clientPort", "2181");

		// 取得一个数据库连接对象
		conn = ConnectionFactory.createConnection(conf);

		// 数据库元数据操作对象
		admin=conn.getAdmin();

	}

	/**
	 * 创建表
	 */
	public void createTable() {

		// 数据表表名
		String tableNameString = "student";
		String columnName = "info";

		// 新建一个数据表表名对象
		TableName tableName = TableName.valueOf(tableNameString);

		// 如果需要新建的表已经存在
		try {
			if (admin.tableExists(tableName)) {
				System.out.println("表已经存在！");
			} else {
				// 数据表描述对象
				HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);

				// 列族描述对象
				HColumnDescriptor columnDescriptor = new HColumnDescriptor(columnName);

				// 在数据表中新建一个列族
				tableDescriptor.addFamily(columnDescriptor);

				// 新建数据表
				admin.createTable(tableDescriptor);
				System.out.println("创建表成功!");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("创建表失败!");
		}
	}

	/**
	 * 获取所有表名
	 */
	public void getTablesName() {
		TableName[] tableNames;
		try {
			tableNames = admin.listTableNames();
			for (TableName tableName : tableNames) {
				System.out.println("已存在的表名：" + tableName);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("获取所有表名失败！");
		}
	}

	/**
	 * 删除表
	 * 
	 * @param tableNameStr
	 */
	public void deleteTable(String tableNameStr) {
		TableName tableName = TableName.valueOf(tableNameStr);
		try {
			if (admin.tableExists(tableName)) {
				admin.disableTable(tableName);
				admin.deleteTable(tableName);
				System.out.println("表删除成功！");
			} else {
				System.out.println("表不存在！");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("表删除失败！");
		}
	}

	/**
	 * 删除一条记录
	 * 
	 * @param tableName
	 * @param rowKey
	 */
	public void deleteRecord(String tableName, String rowKey) {
		try {
			Table table = conn.getTable(TableName.valueOf(tableName));
			Delete del = new Delete(rowKey.getBytes());
			table.delete(del);
			System.out.println(tableName + " 表删除数据成功！");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(tableName + " 表删除数据失败！");
		}
	}

	/**
	 * 插入一条数据
	 * 
	 * @param tableName
	 * @param rowKey
	 * @param columnFamily
	 * @param qualifier
	 * @param value
	 * @return
	 */
	public boolean insertRecord(String tableName, String rowKey, String columnFamily, String qualifier, String value) {
		try {
			Table table = conn.getTable(TableName.valueOf(tableName));
			Put put = new Put(rowKey.getBytes());
			put.addColumn(columnFamily.getBytes(), qualifier.getBytes(), value.getBytes());
			table.put(put);
			System.out.println("插入数据成功！！！");
			return true;
		} catch (IOException e) {
			System.out.println("插入数据失败！！！");
		}
		return false;
	}

	/**
	 * 查询一条记录
	 * 
	 * @param tableName
	 * @param rowKey
	 * @return
	 */
	public Result getOneRecord(String tableName, String rowKey) {
		try {
			Table table = conn.getTable(TableName.valueOf(tableName));
			Get get = new Get(rowKey.getBytes());
			Result rs = table.get(get);
			System.out.println(tableName + " 表获取数据成功！");
			for (Cell cell : rs.rawCells()) {
				System.out.println("family:"
						+ Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength()));
				System.out.println("qualifier:" + Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(),
						cell.getQualifierLength()));
				System.out.println(
						"value:" + Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
				System.out.println("Timestamp:" + cell.getTimestamp());
				System.out.println("---------------");
			}
			return rs;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 查询所有的记录
	 * 
	 * @param tableName
	 * @return
	 */
	public List<Result> getAllRecords(String tableName) {
		ResultScanner scanner = null;
		List<Result> list = new ArrayList<Result>();
		try {
			Table table = conn.getTable(TableName.valueOf(tableName));
			Scan scan = new Scan();
			scanner = table.getScanner(scan);
			Iterator<Result> iterator = scanner.iterator();

			while (iterator.hasNext()) {
				Result rs = iterator.next();
				list.add(rs);
				for (Cell cell : rs.rawCells()) {
					System.out.println("Family: "
							+ Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength()));
					System.out.println("Qualifier: " + Bytes.toString(cell.getQualifierArray(),
							cell.getQualifierOffset(), cell.getQualifierLength()));
					System.out.println("Value: "
							+ Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
					System.out.println("----------------------");
				}
			}
			System.out.println(list);
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (!Objects.isNull(scanner)) {
				scanner.close();
			}
		}
		return list;
	}
	
	public static void main(String[] args) throws Exception {
		TestHabase testHabase = new TestHabase();
		testHabase.getConnection();
//		testHabase.createTable();
		testHabase.insertRecord("student", UUID.randomUUID().toString(), "columnFamily", "qualifier", "value");
//		testHabase.getAllRecords("student");
		
	}
}
