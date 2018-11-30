package cn.demo.webmagic.test.hbase;


import java.util.Map;

public class HBaseEntity {

    public static class Data {

        private long timestamp;
        private String rowName;
        private String family;
        private String value;

        public long getTimestamp() {
            return timestamp;
        }

        public Data setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public String getRowName() {
            return rowName;
        }

        public Data setRowName(String rowName) {
            this.rowName = rowName;
            return this;
        }

        public String getFamily() {
            return family;
        }

        public Data setFamily(String family) {
            this.family = family;
            return this;
        }

        public String getValue() {
            return value;
        }

        public Data setValue(String value) {
            this.value = value;
            return this;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "timestamp=" + timestamp +
                    ", rowName='" + rowName + '\'' +
                    ", family='" + family + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    /**
     * 主键
     */
    private String rowKey;

    /**
     * 数据集
     */
    private Map<String, Data> dataMap;

    public String getRowKey() {
        return rowKey;
    }

    public HBaseEntity setRowKey(String rowKey) {
        this.rowKey = rowKey;
        return this;
    }

    public Map<String, Data> getDataMap() {
        return dataMap;
    }

    public HBaseEntity setDataMap(Map<String, Data> dataMap) {
        this.dataMap = dataMap;
        return this;
    }

    @Override
    public String toString() {
        return "HBaseEntity{" +
                "rowKey='" + rowKey + '\'' +
                ", dataMap=" + dataMap +
                '}';
    }
}
