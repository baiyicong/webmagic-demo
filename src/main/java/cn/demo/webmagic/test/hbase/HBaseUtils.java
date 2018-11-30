package cn.demo.webmagic.test.hbase;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;

import cn.demo.webmagic.config.HBaseConfig;
import cn.demo.webmagic.test.hbase.HBaseEntity.Data;

public class HBaseUtils {

	private static Connection connection = null;

	public static void setconnection(Connection connection) {
		HBaseUtils.connection = connection;
	}

	/**
	 * 插入一行记录
	 *
	 * @param tableName
	 *            表名
	 * @param rowKey
	 *            行名称
	 * @param dataMap
	 *            （列族名：column）和值的Map
	 * @throws UncheckedIOException
	 *             操作异常
	 */
	public static void insertRecord(String tableName, String rowKey, Map<String, String> dataMap) {
		TableName name = TableName.valueOf(tableName);
		try (Table table = connection.getTable(name)) {
			Put put = new Put(Bytes.toBytes(rowKey));

			dataMap.forEach((key, value) -> {
				if (value == null || StringUtils.isBlank(value)) {
					return;
				}
				byte[] byteValue = Bytes.toBytes(value);
				String[] column = key.split(":");
				String family = column[0];
				String qualifier = column[1];

				put.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), byteValue);
			});
			table.put(put);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * @param tableName
	 *            表名
	 * @param rowKey
	 *            键
	 * @return 结果
	 * @throws UncheckedIOException
	 *             操作异常
	 */
	public static HBaseEntity getRow(String tableName, String rowKey) throws UncheckedIOException {
		try (Table table = connection.getTable(TableName.valueOf(tableName))) {
			Get g = new Get(Bytes.toBytes(rowKey));
			Result rs = table.get(g);
			return getEntity(rs);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static List<HBaseEntity> getRows(String tableName, List<String> rowKeys) {
		List<HBaseEntity> entityList = new ArrayList<>(rowKeys.size());
		List<Get> getList = new ArrayList<>(rowKeys.size());
		for (String rowKey : rowKeys) {
			getList.add(new Get(Bytes.toBytes(rowKey)));
		}
		try (Table table = connection.getTable(TableName.valueOf(tableName))) {
			Result[] results = table.get(getList);
			for (Result rs : results) {
				if (!rs.isEmpty()) {
					entityList.add(getEntity(rs));
				}
			}
			return entityList;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private static HBaseEntity getEntity(Result result) {
		String key = Bytes.toString(result.getRow());
		HashMap<String, HBaseEntity.Data> dataMap = new HashMap<>();
		HBaseEntity entity = new HBaseEntity();
		entity.setRowKey(key);
		entity.setDataMap(dataMap);
		for (Cell cell : result.listCells()) {

			String family = new String(CellUtil.cloneFamily(cell));
			String rowName = new String(CellUtil.cloneQualifier(cell));
			String value = new String(CellUtil.cloneValue(cell));
			long timestamp = cell.getTimestamp();

			HBaseEntity.Data data = new HBaseEntity.Data();
			data.setFamily(family);
			data.setRowName(rowName);
			data.setValue(value);
			data.setTimestamp(timestamp);
			dataMap.put(String.format("%s:%s", family, rowName), data);
		}
		return entity;
	}

	public static Table getTable(String tableName) throws IOException {
		return connection.getTable(TableName.valueOf(tableName));
	}

	/**
	 * 查询所有的记录
	 * 
	 * @param tableName
	 * @return
	 */
	public static List<HBaseEntity> list(String tableName,String prefix) {
		List<HBaseEntity> entityList = new ArrayList<>();
		ResultScanner scanner = null;
		try {
			Table table = connection.getTable(TableName.valueOf(tableName));
			Filter filter = new PrefixFilter(Bytes.toBytes(prefix));
			Scan scan = new Scan();
			scan.setFilter(filter);
			scanner = table.getScanner(scan);
			Iterator<Result> iterator = scanner.iterator();

			while (iterator.hasNext()) {
				Result rs = iterator.next();
				if (!rs.isEmpty()) {
					entityList.add(getEntity(rs));
				}

			}
			return entityList;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static void main(String[] args) {
		HBaseConfig hBaseConfig = new HBaseConfig();
		hBaseConfig.initHBase();
		
		List<HBaseEntity> list = list("metabase_webpage","com");
		for (HBaseEntity hBaseEntity : list) {
			Data data = hBaseEntity.getDataMap().get("info:url");
			System.err.println(data);
		}
	}
}