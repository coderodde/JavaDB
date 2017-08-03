package net.coderodde.javadb;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * This class implements a table column descriptor. It holds the name of the 
 * column and its data type.
 * 
 * @author Rodion "rodde" Efremov 
 * @version 1.6 (Jul 16, 2017)
 */
public final class TableColumnDescriptor {

    /**
     * Number of bytes it takes to encode a size count.
     */
    private static final int SIZE_BYTES = 4;;
    
    /**
     * The name of this table column.
     */
    private String tableColumnName;
    
    /**
     * The data type of this table column.
     */
    private TableCellType tableCellType;
    
    /**
     * The table that owns this table column.
     */
    private Table ownerTable;
    
    /**
     * Constructs a new table column descriptor.
     * 
     * @param tableColumnName the name of the column.
     * @param tableCellType   the data type of the column.
     */
    public TableColumnDescriptor(String tableColumnName,
                                 TableCellType tableCellType) {
        Objects.requireNonNull(tableColumnName, "Table column name is null.");
        Objects.requireNonNull(tableCellType, "Table cell type is null.");
        setTableColumnName(checkTableColumnName(tableColumnName));
        setTableCellType(tableCellType);
    }
    
    /**
     * Returns the name of this column.
     * 
     * @return the name of this column. 
     */
    public String getTableColumnName() {
        return tableColumnName;
    }
    
    /**
     * Returns the data type of this column.
     * 
     * @return the data type of this column.
     */
    public TableCellType getTableCellType() {
        return tableCellType;
    }
    
    /**
     * Renames this table column.
     * 
     * @param newTableColumnName the new table column name.
     */
    public void setTableColumnName(String newTableColumnName) {
        String oldTableColumnName = this.tableColumnName;
        
        this.tableColumnName =
                Objects.requireNonNull(newTableColumnName, 
                                       "The table column name is null.");
        if (ownerTable != null) {
            ownerTable.onTableColumnRename(this,
                                           oldTableColumnName,
                                           newTableColumnName);
        }
    }
    
    public void setTableCellType(TableCellType tableCellType) {
        this.tableCellType = Objects.requireNonNull(tableCellType,
                                                    "Table cell type is null.");
    }
    
    public int getSerializationLength() {
        return SIZE_BYTES + Character.BYTES * tableColumnName.length() + 1;
    }
    
    @Override
    public int hashCode() {
        return tableColumnName.hashCode() ^ tableCellType.getTypeId();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        
        if (o == this) {
            return true;
        }
        
        if (!getClass().equals(o.getClass())) {
            return false;
        }
        
        TableColumnDescriptor other = (TableColumnDescriptor) o;
        
        return tableColumnName.equals(other.tableColumnName)
                && tableCellType.equals(other.tableCellType);
    }
    
    void serialize(ByteBuffer byteBuffer) {
        byteBuffer.putInt(tableColumnName.length());
        
        for (char c : tableColumnName.toCharArray()) {
            byteBuffer.putChar(c);
        }
        
        byteBuffer.put(tableCellType.getTypeId());
    }
    
    public static TableColumnDescriptor deserialize(ByteBuffer byteBuffer) {
        int tableColumnNameLength = byteBuffer.getInt();
        StringBuilder sb = new StringBuilder(tableColumnNameLength);
        
        for (int i = 0; i < tableColumnNameLength; ++i) {
            sb.append(byteBuffer.getChar());
        }
        
        byte typeId = byteBuffer.get();
        
        TableColumnDescriptor tableColumnDescriptor = 
                new TableColumnDescriptor(
                        sb.toString(), 
                        TableCellType.getTableCellType(typeId));
        
        return tableColumnDescriptor;
    }
    
    private String checkTableColumnName(String tableColumnName) {
        tableColumnName = tableColumnName.trim().toLowerCase();
        
        if (tableColumnName.equals("null")) {
            throw new IllegalArgumentException(
                    "\"null\" is not a valid table column name.");
        }
        
        return  tableColumnName;
    }
}
