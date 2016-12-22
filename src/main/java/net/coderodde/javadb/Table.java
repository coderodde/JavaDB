package net.coderodde.javadb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public final class Table implements Iterable<TableRow> {
    
    /**
     * The name of this table.
     */
    private String tableName;
    
    /**
     * The list of column descriptors.
     */
    private final List<TableColumnDescriptor> tableColumnDescriptorList = 
            new ArrayList<>();
    
    /**
     * The actual list of table rows.
     */
    private final List<TableRow> tableRowList = new ArrayList<>();
    
    /**
     * The owner database. The database must be up-to-date when it comes to the
     * tables it maintains.
     */
    private final Database ownerDatabase;
    
    /**
     * Constructs a new table for a given owner database.
     * 
     * @param tableName     the name of the new table.
     * @param ownerDatabase the owner database.
     */
    Table(String tableName, Database ownerDatabase) {
        this.tableName = Objects.requireNonNull(tableName, 
                                                "The table name is null.");
        this.ownerDatabase = 
                Objects.requireNonNull(ownerDatabase,  
                                       "The owner database is null.");
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public void renameTable(String newTableName) {
        this.tableName = Objects.requireNonNull(newTableName,
                                                "The new table name is null.");
        ownerDatabase.onTableRename(this, newTableName);
    }
    
    public void addTableColumn(int columnIndex, 
                               TableColumnDescriptor tableColumnDescriptor) {
        Objects.requireNonNull(tableColumnDescriptor, 
                               "The table column descriptor is null.");
        checkColumnDescriptorInsertionIndex(columnIndex);
        tableColumnDescriptorList.add(columnIndex, tableColumnDescriptor);
        
        addTableColumnToRows(columnIndex, tableColumnDescriptor, null);
    }
    
    public void addTableColumn(int columnIndex,
                               TableColumnDescriptor tableColumnDescriptor,
                               Object defaultValue) {
        Objects.requireNonNull(tableColumnDescriptor, 
                               "The table column descriptor is null.");
        checkColumnDescriptorInsertionIndex(columnIndex);
        tableColumnDescriptorList.add(columnIndex, tableColumnDescriptor);
        
        addTableColumnToRows(columnIndex, tableColumnDescriptor, defaultValue);
    }
    
    public void addTableColumn(TableColumnDescriptor tableColumnDescriptor) {
        addTableColumn(tableColumnDescriptorList.size(), 
                       tableColumnDescriptor);
    }
    
    public void setTableColumn(int columnIndex,
                               TableColumnDescriptor tableColumnDescriptor) {
        
    }
    
    public void removeTableColumn(int columnIndex) {
        
    }
    
    public void removeTableColumn(String columnName) {
        
    }
    
    public TableRow getRow(int index) {
        return tableRowList.get(index);
    }
    
    private void checkColumnDescriptorInsertionIndex(int columnIndex) {
        if (columnIndex < 0) {
            throw new IndexOutOfBoundsException(
                    "The column index is negative: " + columnIndex + ".");
        }
        
        if (columnIndex > tableColumnDescriptorList.size()) {
            throw new IndexOutOfBoundsException(
                    "The column index is too large: " + columnIndex + ". " +
                    "Must be at most " + tableColumnDescriptorList.size() +
                    ".");
        }
    }

    @Override
    public Iterator<TableRow> iterator() {
        return new Iterator(){
            private final Iterator<TableRow> iterator = tableRowList.iterator();
            
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Object next() {
                return iterator.next();
            }
        };
    }
    
    /**
     * Adds a new column cell to each table row. If the column does not allow
     * {@code null} values, the default value is used. If, however, the default
     * value is {@code null}, INTs, LONGs, FLOATs and DOUBLEs will be 
     * initialized with zero, BOOLEAN will be initialized with {@code false},
     * STRINGs with empty string, and BLOBs with byte arrays of zero length.
     * 
     * @param columnIndex           the column index.
     * @param tableColumnDescriptor the column descriptor.
     * @param defaultValue          the default value.
     */
    private void addTableColumnToRows(
            int columnIndex,
            TableColumnDescriptor tableColumnDescriptor,
            Object defaultValue) {
        int rows = tableRowList.size();
        
        if (tableColumnDescriptor.getNullNotAllowed() 
                || (defaultValue != null)) {
            if (defaultValue == null) {
                defaultValue = TableCell.defaults.get(
                        tableColumnDescriptor.getTableCellType());
            }
            
            for (int index = 0; index != rows; ++index) {
                TableCell newTableCell =
                        new TableCell(tableColumnDescriptor.getTableCellType());
                
                switch (tableColumnDescriptor.getTableCellType()) {
                    case TYPE_INT:
                        newTableCell.setIntValue((Integer) defaultValue);
                        break;
                        
                    case TYPE_LONG:
                        newTableCell.setLongValue((Long) defaultValue);
                        break;
                        
                    case TYPE_FLOAT:
                        newTableCell.setFloatValue((Float) defaultValue);
                        break;
                        
                    case TYPE_DOUBLE:
                        newTableCell.setDoubleValue((Double) defaultValue);
                        break;
                        
                    case TYPE_STRING:
                        newTableCell.setStringValue((String) defaultValue);
                        break;
                        
                    case TYPE_BOOLEAN:
                        newTableCell.setBooleanValue((Boolean) defaultValue);
                        break;
                        
                    case TYPE_BINARY:
                        newTableCell.setBinaryData((byte[]) defaultValue);
                        break;
                        
                    default:
                        throw new IllegalStateException(
                                "Unknown table cell type.");
                }
                
                TableRow currentTableRow = tableRowList.get(index);
                currentTableRow.add(columnIndex, newTableCell);
            }
        } else {
            for (int index = 0; index != rows; ++index) {
                TableCell newTableCell = 
                    new TableCell(tableColumnDescriptor.getTableCellType());
                TableRow currentTableRow = tableRowList.get(index);
                currentTableRow.add(columnIndex, newTableCell);
            }
        }
    }
}
