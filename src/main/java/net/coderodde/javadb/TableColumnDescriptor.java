package net.coderodde.javadb;

import java.util.Objects;

public final class TableColumnDescriptor {

    private String tableColumnName;
    private TableCellType tableCellType;
    private boolean nullNotAllowed;
    private boolean unique;
    
    public TableColumnDescriptor(String tableColumnName,
                                 TableCellType tableCellType,
                                 boolean nullNotAllowed,
                                 boolean unique) {
        setTableColumnName(tableColumnName);
        setTableCellType(tableCellType);
        setNullNotAllowed(nullNotAllowed);
        setUnique(unique);
    }
    
    public String getTableColumnName() {
        return tableColumnName;
    }
    
    public TableCellType getTableCellType() {
        return tableCellType;
    }
    
    public boolean getNullNotAllowed() {
        return nullNotAllowed;
    }
    
    public boolean getUnique() {
        return unique;
    }
    
    public void setTableColumnName(String tableColumnName) {
        this.tableColumnName =
                Objects.requireNonNull(tableColumnName, 
                                       "The table column name is null.");
    }
    
    public void setTableCellType(TableCellType tableCellType) {
        this.tableCellType = Objects.requireNonNull(tableCellType,
                                                    "Table cell type is null.");
    }
    
    public void setNullNotAllowed(boolean nullNotAllowed) {
        this.nullNotAllowed = nullNotAllowed;
    }
    
    public void setUnique(boolean unique) {
        this.unique = unique;
    }
}
