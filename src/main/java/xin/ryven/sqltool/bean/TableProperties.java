package xin.ryven.sqltool.bean;


/**
 * @author Administrator
 */
public class TableProperties {

    private String tableName;

    private String[] columns;

    private String[] fields;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }
}
