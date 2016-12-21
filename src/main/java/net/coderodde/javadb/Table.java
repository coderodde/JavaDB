package net.coderodde.javadb;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Table {

    private final List<TableColumnDescriptor> tableColumnDescriptorList = 
            new ArrayList<>();
    
    private final List<TableRow> tableRowList = new ArrayList<>();
    
    
    public void addTableColumn(int columnIndex, 
                               TableColumnDescriptor tableColumnDescriptor) {
        checkColumnDescriptorInsertionIndex(columnIndex);
        Objects.requireNonNull(tableColumnDescriptor, 
                               "The table column descriptor is null.");
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
}
