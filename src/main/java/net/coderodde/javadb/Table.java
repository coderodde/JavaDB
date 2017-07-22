package net.coderodde.javadb;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class implements a database table. It consists of list of table column 
 * descriptors and the list of table rows.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 17, 2017)
 */
public final class Table implements Iterable<TableRow> {
    
    /**
     * Number of bytes used to decode the table name.
     */
    private static final int SIZE_BYTES = 4;
    
    /**
     * The name of this table.
     */
    private String tableName;
    
    /**
     * The database holding this table.
     */
    private Database ownerDatabase;
    
    /**
     * The list of column descriptors.
     */
    final List<TableColumnDescriptor> tableColumnDescriptorList = 
            new ArrayList<>();
    
    /**
     * Maps the name of a column to its index. The index in question is the
     * location index of the corresponding table column descriptor.
     */
    final Map<String, Integer> tableColumnNameIndexMap = 
            new HashMap<>();
    
    /**
     * The actual list of table rows.
     */
    private final List<TableRow> tableRowList = new ArrayList<>();
    
    /**
     * Constructs a new table for a given owner database.
     * 
     * @param tableName the name of the new table.
     */
    public Table(String tableName) {
        this.tableName = Objects.requireNonNull(tableName, 
                                                "The table name is null.");
    }
    
    /**
     * Returns the name of this table.
     * 
     * @return the name of this table.
     */
    public String getTableName() {
        return tableName;
    }
    
    /**
     * Renames this table.
     * 
     * @param newTableName the new table name.
     */
    public void setTableName(String newTableName) {
        String oldTableName = this.tableName;
        this.tableName =
                Objects.requireNonNull(
                        newTableName,
                        "The new table name is null.");
        
        this.ownerDatabase.onTableRename(this, oldTableName, newTableName);
    }
    /**
     * Inserts a new table row using {@code values} as the cell values.
     * 
     * @param index  the index at which to insert the new table row.
     * @param values the array of values to store.
     */
    public TableRow putTableRowAt(int index, Object... values) {
        checkInsertionIndex(index);
        checkNumberOfValuesNoLargerThanColumnCount(values);
        
        for (int i = 0; i < values.length; ++i) {
            checkValueType(values[i], tableColumnDescriptorList.get(i));
        }
        
        TableRow newTableRow = new TableRow(this);
        TableCell newTableCell;
        
        for (int i = 0; i < values.length; ++i) {
            switch (tableColumnDescriptorList.get(i).getTableCellType()) {
                case TYPE_INT:
                    newTableCell = new TableCell(TableCellType.TYPE_INT);
                    newTableCell.setIntValue((Integer) values[i]);
                    newTableRow.add(newTableCell);
                    break;
                    
                case TYPE_LONG:
                    newTableCell = new TableCell(TableCellType.TYPE_LONG);
                    newTableCell.setLongValue((Long) values[i]);
                    newTableRow.add(newTableCell);
                    break;
                    
                case TYPE_FLOAT:
                    newTableCell = new TableCell(TableCellType.TYPE_FLOAT);
                    newTableCell.setFloatValue((Float) values[i]);
                    newTableRow.add(newTableCell);
                    break;
                    
                case TYPE_DOUBLE:
                    newTableCell = new TableCell(TableCellType.TYPE_DOUBLE);
                    newTableCell.setDoubleValue((Double) values[i]);
                    newTableRow.add(newTableCell);
                    break;
                    
                case TYPE_STRING:
                    newTableCell = new TableCell(TableCellType.TYPE_STRING);
                    newTableCell.setStringValue((String) values[i]);
                    newTableRow.add(newTableCell);
                    break;
                    
                case TYPE_BINARY:
                    newTableCell = new TableCell(TableCellType.TYPE_BINARY);
                    newTableCell.setBinaryData((byte[]) values[i]);
                    newTableRow.add(newTableCell);
                    break;
                    
                case TYPE_BOOLEAN:
                    newTableCell = new TableCell(TableCellType.TYPE_BOOLEAN);
                    newTableCell.setBooleanValue((Boolean) values[i]);
                    newTableRow.add(newTableCell);
                    break;
                    
                default:
                    throw new IllegalStateException("Should not get here.");
            }
        }
        
        for (int i = values.length; i < tableColumnDescriptorList.size(); ++i) {
            switch (tableColumnDescriptorList.get(i).getTableCellType()) {
                case TYPE_INT:
                    newTableCell = new TableCell(TableCellType.TYPE_INT);
                    newTableRow.add(newTableCell);
                    break;
                    
                case TYPE_LONG:
                    newTableCell = new TableCell(TableCellType.TYPE_LONG);
                    newTableRow.add(newTableCell);
                    break;
                    
                case TYPE_FLOAT:
                    newTableCell = new TableCell(TableCellType.TYPE_FLOAT);
                    newTableRow.add(newTableCell);
                    break;
                    
                case TYPE_DOUBLE:
                    newTableCell = new TableCell(TableCellType.TYPE_DOUBLE);
                    newTableRow.add(newTableCell);
                    break;
                    
                case TYPE_STRING:
                    newTableCell = new TableCell(TableCellType.TYPE_STRING);
                    newTableRow.add(newTableCell);
                    break;
                    
                case TYPE_BINARY:
                    newTableCell = new TableCell(TableCellType.TYPE_BINARY);
                    newTableRow.add(newTableCell);
                    break;
                    
                case TYPE_BOOLEAN:
                    newTableCell = new TableCell(TableCellType.TYPE_BOOLEAN);
                    newTableRow.add(newTableCell);
                    break;
                    
                default:
                    throw new IllegalStateException("Should not get here.");
            }
        }
        
        newTableRow.ownerTable = this;
        tableRowList.add(index, newTableRow);
        return newTableRow;
    }
    
    /**
     * Appends a new table row to this table.
     * 
     * @param values the values to use as the row cell values.
     */
    public TableRow putTableRow(Object... values) {
        return putTableRowAt(tableRowList.size(), values);
    }
    
    /**
     * Returns the {@code index} row of this table.
     * 
     * @param index the row index.
     * @return the table row.
     */
    public TableRow getTableRow(int index) {
        checkAccessIndex(index);
        return tableRowList.get(index);
    }
    
    /**
     * Removes the {@code index}th row from this table.
     * 
     * @param index the index of the row to remove.
     */
    public void removeRow(int index) {
        checkAccessIndex(index);
        TableRow tableRow = tableRowList.remove(index);
        tableRow.ownerTable = null;
    }
    
    /**
     * Returns the iterator that iterates over table rows in their natural
     * order.
     * 
     * @return the row iterator.
     */
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
    
    public TableView createTableView(
            TableColumnDescriptor... tableColumnDescriptors) {
        checkViewTableColumnDescriptors(tableColumnDescriptors);
        TableView tableView = 
                new TableView(this, Arrays.asList(tableColumnDescriptors));
        
        return tableView;
    }
    
    /**
     * Appends the table column descriptor to this table. The current rows are 
     * extended behind the scene.
     * 
     * @param tableColumnDescriptor the table column descriptor to append. 
     */
    public void addTableColumnDescriptor(
            TableColumnDescriptor tableColumnDescriptor) {
        String newTableColumnDescriptorName = 
                tableColumnDescriptor.getTableColumnName();
        
        if (tableColumnNameIndexMap.containsKey(newTableColumnDescriptorName)) {
            throw new IllegalStateException(
                    "The column with name \"" + newTableColumnDescriptorName +
                            " is already in this table.");
        }
        
        int index = tableColumnDescriptorList.size();
        
        Objects.requireNonNull(tableColumnDescriptor,
                "The table column descriptor is null.");
        
        tableColumnDescriptorList.add(tableColumnDescriptor);
        
        for (TableRow tableRow : tableRowList) {
            tableRow.add(new TableCell(
                    tableColumnDescriptor.getTableCellType()));
        }
        
        tableColumnNameIndexMap.put(tableColumnDescriptor.getTableColumnName(), 
                                    index);
    }
    
    /**
     * Returns the {@code columnIndex}th table column descriptor.
     * 
     * @param columnIndex the index of the column descriptor to return.
     * @return the table column descriptor.
     */
    public TableColumnDescriptor getTableColumnDescriptor(int columnIndex) {
        return tableColumnDescriptorList.get(columnIndex);
    }
    
    /**
     * Returns the table column descriptor with the given name.
     * 
     * @param columnName the name of the column.
     * 
     * @return the table column descriptor with the name {@code columnName}.
     * 
     * @throws IllegalArgumentException if there is no column with the given 
     *                                  name.
     */
    public TableColumnDescriptor getTableColumnDescriptor(String columnName) {
        Integer index = tableColumnNameIndexMap.get(columnName);
        
        if (index == null) {
            throw new IllegalArgumentException(
                    "\"" + columnName + "\": no such column.");
        }
        
        return tableColumnDescriptorList.get(index);
    }
    
    public boolean containsTableColumnDescriptor(String columnName) {
        return tableColumnNameIndexMap.containsKey(columnName);
    }
    
    /**
     * Removes the {@code columnIndex}th column from this table.
     * 
     * @param columnIndex the index of the column to remove.
     * 
     * @return the table column descriptor.
     */
    public TableColumnDescriptor removeTableColumnDescriptor(int columnIndex) {
        TableColumnDescriptor tableColumnDescriptor = 
                tableColumnDescriptorList.remove(columnIndex);
        
        for (TableRow tableRow : tableRowList) {
            tableRow.remove(columnIndex);
        }
        
        return tableColumnDescriptor;
    }
    
    /**
     * Removes the table column with the given name.
     * 
     * @param columnName the name of the column to remove.
     * 
     * @return the table column descriptor.
     */
    public TableColumnDescriptor
         removeTableColumnDescriptor(String columnName) {
        Integer index = tableColumnNameIndexMap.get(columnName);
        
        if (index == null) {
            throw new IllegalArgumentException(
                    columnName + ": no such column.");
        }
        
        for (TableRow tableRow : tableRowList) {
            tableRow.remove(index);
        }
        
        TableColumnDescriptor tableColumnDescriptor = 
                tableColumnDescriptorList.get(index);
        
        tableColumnDescriptorList.remove(tableColumnDescriptor);
        tableColumnNameIndexMap.clear();
        
        // Remap the column index map:
        int columnIndex = 0;
        
        for (TableColumnDescriptor tcd
                : tableColumnDescriptorList) {
            tableColumnNameIndexMap.put(tcd.getTableColumnName(), 
                                        columnIndex++);
        }
        
        return tableColumnDescriptor;
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
        
        Table other = (Table) o;
        
        if (!tableColumnDescriptorList.equals(other.tableColumnDescriptorList)) {
            return false;
        }
        
        return tableRowList.equals(other.tableRowList);
    }
    
    int getSerializationLength() {
        int serializationLength = SIZE_BYTES + 
                Character.BYTES * tableName.length() + 2 * SIZE_BYTES;
        
        for (TableColumnDescriptor tableColumnDescriptor : 
                tableColumnDescriptorList) {
            serializationLength += 
                    tableColumnDescriptor.getSerializationLength();
        }
        
        for (TableRow tableRow : tableRowList) {
            serializationLength += tableRow.getSerializationLength();
        }
        
        return serializationLength;
    }
    
    void serialize(ByteBuffer byteBuffer) {
        int tableNameLength = tableName.length();
        // Dump the length of the table length:
        byteBuffer.putInt(tableNameLength);
        
        // Dump the actual name of the table:
        for (char c : tableName.toCharArray()) {
            byteBuffer.putChar(c);
        }
        
        // Dump the number of columns:
        byteBuffer.putInt(tableColumnDescriptorList.size());
        
        for (TableColumnDescriptor tableColumnDescriptor : 
                tableColumnDescriptorList) {
            tableColumnDescriptor.serialize(byteBuffer);
        }
        
        // Dump the number of rows:
        byteBuffer.putInt(tableRowList.size());
        
        for (TableRow tableRow : tableRowList) {
            tableRow.serialize(byteBuffer);
        }
    }
    
    static Table deserialize(ByteBuffer byteBuffer) {
        int tableNameLength = byteBuffer.getInt();
        StringBuilder sb = new StringBuilder(tableNameLength);
        
        for (int i = 0; i < tableNameLength; ++i) {
            sb.append(byteBuffer.getChar());
        }
        
        int numberOfColumns = byteBuffer.getInt();
        Table table = new Table(sb.toString());
        
        for (int i = 0; i < numberOfColumns; ++i) {
            TableColumnDescriptor tableColumnDescriptor =
                    TableColumnDescriptor.deserialize(byteBuffer);
            
            table.addTableColumnDescriptor(tableColumnDescriptor);
        }
        
        int numberOfRows = byteBuffer.getInt();
        
        for (int i = 0; i < numberOfRows; ++i) {
            TableRow tableRow = TableRow.deserialize(byteBuffer,
                                                     numberOfColumns);
            table.addRow(tableRow);
        }
        
        return table;
    }
    
    void onTableColumnRename(TableColumnDescriptor tableColumn, 
                             String oldTableColumnName, 
                             String newTableColumnName) {
        int index = tableColumnDescriptorList.indexOf(tableColumn);
        
        if (index < 0) {
            throw new IllegalStateException(
                    "Trying to update a column that is not in this table.");
        }
    }
    
    private void addRow(TableRow tableRow) {
        tableRow.ownerTable = this;
        tableRowList.add(tableRow);
    }

    private void checkInsertionIndex(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("The insertion index is " +
                    "negative: " + index);
        }
        
        if (index > tableRowList.size()) {
            throw new IndexOutOfBoundsException("The insertion index is " +
                    "too large: " + index + ". Must be at most " + 
                    tableRowList.size());
        }
    }
    
    private void checkAccessIndex(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("The access index is " +
                    "negative: " + index);
        }
        
        if (index >= tableRowList.size()) {
            throw new IndexOutOfBoundsException("The insertion index is " +
                    "too large: " + index + ". Must be at most " + 
                    (tableRowList.size() - 1));
        }
    }

    private void checkNumberOfValuesNoLargerThanColumnCount(Object[] values) {
        if (values.length > tableColumnDescriptorList.size()) {
            throw new IllegalArgumentException("More values (" +
                    values.length + ") than number of columns (" +
                    tableColumnDescriptorList.size() + ".");
        }
    }

    private void checkValueType(Object value, 
                                TableColumnDescriptor tableColumnDescriptor) {
        if (value == null) {
            return;
        }
        
        if (value instanceof Integer) {
            if (!tableColumnDescriptor.getTableCellType()
                                      .equals(TableCellType.TYPE_INT)) {
                throw new IllegalArgumentException("Expected type mismatch. " +
                        "Expected " + 
                        tableColumnDescriptor.getTableCellType()
                                             .getTypeName() + ", " + 
                        "integer received.");                        
            }
        }
        
        if (value instanceof Long) {
            if (!tableColumnDescriptor.getTableCellType()
                                      .equals(TableCellType.TYPE_LONG)) {
                throw new IllegalArgumentException("Expected type mismatch. " +
                        "Expected " + 
                        tableColumnDescriptor.getTableCellType()
                                             .getTypeName() + ", " + 
                        "long received.");                        
            }
        }
        
        if (value instanceof Float) {
            if (!tableColumnDescriptor.getTableCellType()
                                      .equals(TableCellType.TYPE_FLOAT)) {
                throw new IllegalArgumentException("Expected type mismatch. " +
                        "Expected " + 
                        tableColumnDescriptor.getTableCellType()
                                             .getTypeName() + ", " + 
                        "float received.");                        
            }
        }
        
        if (value instanceof Double) {
            if (!tableColumnDescriptor.getTableCellType()
                                      .equals(TableCellType.TYPE_DOUBLE)) {
                throw new IllegalArgumentException("Expected type mismatch. " +
                        "Expected " + 
                        tableColumnDescriptor.getTableCellType()
                                             .getTypeName() + ", " + 
                        "double received.");                        
            }
        }
        
        if (value instanceof String) {
            if (!tableColumnDescriptor.getTableCellType()
                                      .equals(TableCellType.TYPE_STRING)) {
                throw new IllegalArgumentException("Expected type mismatch. " +
                        "Expected " + 
                        tableColumnDescriptor.getTableCellType()
                                             .getTypeName() + ", " + 
                        "string received.");                        
            }
        }
        
        if (value instanceof byte[]) {
            if (!tableColumnDescriptor.getTableCellType()
                                      .equals(TableCellType.TYPE_BINARY)) {
                throw new IllegalArgumentException("Expected type mismatch. " +
                        "Expected " + 
                        tableColumnDescriptor.getTableCellType()
                                             .getTypeName() + ", " + 
                        "binary received.");                        
            }
        }
        
        if (value instanceof Boolean) {
            if (!tableColumnDescriptor.getTableCellType()
                                      .equals(TableCellType.TYPE_BOOLEAN)) {
                throw new IllegalArgumentException("Expected type mismatch. " +
                        "Expected " + 
                        tableColumnDescriptor.getTableCellType()
                                             .getTypeName() + ", " + 
                        "boolean received.");                        
            }
        }
    }

    private void checkViewTableColumnDescriptors(
            TableColumnDescriptor[] tableColumnDescriptors) {
        Objects.requireNonNull(tableColumnDescriptors, 
                               "Table view descriptors are null.");
        
        for (TableColumnDescriptor tableColumnDescriptor 
                : tableColumnDescriptors) {
            Objects.requireNonNull(
                    tableColumnDescriptor, 
                    "The table column descriptor is null.");
            
            if (!this.tableColumnDescriptorList
                     .contains(tableColumnDescriptor)) {
                throw new IllegalArgumentException(
                        "The table column descriptor is not in this table.");
            }
        }
    }
}
