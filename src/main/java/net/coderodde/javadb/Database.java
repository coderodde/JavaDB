package net.coderodde.javadb;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class Database {

    /**
     * The map mapping the table name to the actual table.
     */
    private final Map<String, Table> tableMap = new LinkedHashMap<>();
    
    public void createTable(String tableName,
                            TableColumnDescriptor... tableColumnDescriptors) {
        Objects.requireNonNull(tableName, "The table name is null");
        checkTableNameNotEmpty(tableName);
        Table table = new Table(tableName, this);
        
        for (TableColumnDescriptor tableColumnDescriptor 
                : tableColumnDescriptors) {
            Objects.requireNonNull(tableColumnDescriptor,
                                   "The table column descriptor is null.");
            table.addTableColumn(tableColumnDescriptor);
        }
    }
    
    public Table getTable(String tableName) {
        Objects.requireNonNull(tableName, "The input table name is null.");
        checkTableIsInThisDatabase(tableName);
        return tableMap.get(tableName);
    }
    
    public void deleteTable(String tableName) {
        Objects.requireNonNull(tableName, "The input table name is null.");
        checkTableIsInThisDatabase(tableName);
        tableMap.remove(tableName);
    }
    
    void onTableRename(Table table, String newTableName) {
        Objects.requireNonNull(table, "The input table is null.");
        Objects.requireNonNull(newTableName, "The new table name is null.");
        checkTableNameNotEmpty(newTableName);
        checkTableIsInThisDatabase(table.getTableName());
        checkTableNameNotOccupied(newTableName);
        tableMap.remove(table.getTableName());
        tableMap.put(newTableName, table);
    }
    
    private void checkTableNameNotOccupied(String tableName) {
        if (tableMap.containsKey(tableName)) {
            throw new IllegalStateException(
                    "Table name \"" + tableName + "\" is already occupied.");
        }
    }
    
    private void checkTableIsInThisDatabase(String tableName) {
        if (!tableMap.containsKey(tableName)) {
            throw new IllegalStateException(
                    "The table with name \"" + tableName + "\" is not in " +
                    "this database.");
        }
    }
    
    private void checkTableNameNotEmpty(String tableName) {
        if (tableName.isEmpty()) {
            throw new IllegalArgumentException("The table name is empty.");
        }
    }
}
