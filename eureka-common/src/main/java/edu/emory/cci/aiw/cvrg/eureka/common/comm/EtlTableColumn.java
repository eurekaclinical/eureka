package edu.emory.cci.aiw.cvrg.eureka.common.comm;

/**
 *
 * @author Andrew Post
 */
public class EtlTableColumn {
    
    private String tableName;
    private String columnName;
    private String path;
    private String format;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
    
}
