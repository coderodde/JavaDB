package net.coderodde.javadb;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TableRow {

    private static final int DEFAULT_CAPACITY = 2;
    
    /**
     * This list stores the actual table cells.
     */
    private final List<TableCell> tableCellList =
            new ArrayList<>(DEFAULT_CAPACITY);
    
    public TableCell get(int index) {
        checkAccessIndex(index);
        return tableCellList.get(index);
    }
    
    public void set(int index, TableCell cell) {
        checkAccessIndex(index);
        Objects.requireNonNull(cell, "The table cell is null.");
        tableCellList.set(index, cell);
    }
    
    public void add(int index, TableCell cell) {
        checkInsertionIndex(index);
        Objects.requireNonNull(cell, "The table cell is null.");
        tableCellList.add(index, cell);
    }
    
    public void remove(int index) {
        checkAccessIndex(index);
        tableCellList.remove(index);
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
