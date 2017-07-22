package net.coderodde.javadb;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class implements a row in a table.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 16, 2017)
 */
public class TableRow {

    /**
     * This list stores the actual table cells.
     */
    private final List<TableCell> tableCellList = new ArrayList<>();
    
    /**
     * The table this row belongs to.
     */
    Table ownerTable;
    
    TableRow(Table onwerTable) {
        this.ownerTable = ownerTable;
    }
    
    /**
     * Returns the number of cells in this table row.
     * 
     * @return the number of cells.
     */
    public int getNumberOfCells() {
        return tableCellList.size();
    }
    
    /**
     * Returns the table cell by its respective column name.
     * 
     * @param columnName the name of the column.
     * 
     * @return the table cell with the given column name.
     */
    public TableCell get(String columnName) {
        if (ownerTable == null) {
            throw new IllegalStateException(
                    "This table row does not belong to a table. Use " +
                            "get(int) instead.");
        }
        
        Integer columnIndex = 
                ownerTable.tableColumnNameIndexMap.get(columnName);
        
        if (columnIndex == null) {
            throw new IllegalArgumentException("\"" +
                    columnName + "\": no such column.");
        }
        
        return tableCellList.get(columnIndex);
    }
    
    /**
     * Returns the {@code index}th cell.
     * 
     * @param index the index of the cell.
     * 
     * @return the table cell.
     */
    public TableCell get(int index) {
        checkAccessIndex(index);
        return tableCellList.get(index);
    }

    /**
     * Adds the table cell to this table row at a given 
     * 
     * @param index     the position index.
     * @param tableCell the table cell to add.
     */
    public void add(int index, TableCell tableCell) {        
        checkInsertionIndex(index);
        Objects.requireNonNull(tableCell, "The table cell is null.");
        tableCellList.add(index, tableCell);
    }
    
    public void add(TableCell tableCell) {
        add(tableCellList.size(), tableCell);
    }
    
    /**
     * Removes the {@code index}th table row cell from this row.
     * 
     * @param index the index of the cell to remove.
     */
    public void remove(int index) {
        checkAccessIndex(index);
        tableCellList.remove(index);
    }
    
    public int getSerializationLength() {
        int serializationLength = 0;
        
        for (TableCell tableCell : tableCellList) {
            serializationLength += tableCell.getSerializationLength();
        }
        
        return serializationLength;
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
        
        TableRow other = (TableRow) o;
        
        if (getNumberOfCells() != other.getNumberOfCells()) {
            return false;
        }
        
        for (int i = 0; i < getNumberOfCells(); ++i) {
            if (!get(i).equals(other.get(i))) {
                return false;
            }
        }
        
        return true;
    }
    
    public void serialize(ByteBuffer byteBuffer) {
        for (TableCell tableCell : tableCellList) {
            tableCell.serialize(byteBuffer);
        }
    }
    
    public static TableRow deserialize(ByteBuffer byteBuffer,
                                       int numberOfCells) {
        TableRow tableRow = new TableRow(null);
        
        for (int i = 0; i < numberOfCells; ++i) {
            tableRow.add(TableCell.deserialize(byteBuffer));
        }
        
        return tableRow;
    }
    
    private void checkAccessIndex(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(
                    "The column access index is negative: " + index + ".");
        }
        
        if (index >= tableCellList.size()) {
            throw new IndexOutOfBoundsException(
                    "The column access index is too large: " + index + ". " +
                    "The length of the row is " + tableCellList.size() + " " +
                    "cells.");
        }
    }
    
    private void checkInsertionIndex(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(
                    "The column insertion index is negative: " + index + ".");
        }
        
        if (index > tableCellList.size()) {
            throw new IndexOutOfBoundsException(
                    "The column insertion index is too large: " + index + ". " +
                    "The length of the row is " + tableCellList.size() + " " +
                    "cells.");
        }
    }
}
