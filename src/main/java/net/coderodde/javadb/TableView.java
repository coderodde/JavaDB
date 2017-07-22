package net.coderodde.javadb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class TableView {

    private final List<TableRow> tableRowList = new ArrayList<>();
    private final List<TableColumnDescriptor> tableColumnDescriptorList;
    private final Map<TableColumnDescriptor, Integer> cellMap = new HashMap<>();
    
    TableView(Table ownerTable, 
              List<TableColumnDescriptor> tableColumnDescriptorList) {
        this.tableColumnDescriptorList = tableColumnDescriptorList;
        
        for (int i = 0; i < ownerTable.tableColumnDescriptorList.size(); ++i) {
            if (tableColumnDescriptorList
                    .contains(ownerTable.tableColumnDescriptorList.get(i))) {
                cellMap.put(ownerTable.tableColumnDescriptorList.get(i), i);
            }
        }
    }
    
    public void addTableRow(TableRow tableRow) {
        tableRowList.add(Objects.requireNonNull(tableRow, "Table row is null."));
    }
    
    public TableRow removeTableRow(int index) {
        return tableRowList.remove(index);
    }
    
    public TableRow getTableRow(int index) {
        return tableRowList.get(index);
    }
    
    @Override
    public String toString() {
        Map<TableColumnDescriptor, Integer> columnWidthMap =
                getColumnWidthMap();
        
        int viewWidth = getViewWidth(columnWidthMap);
        int viewHeight = tableRowList.size() + 4;
        StringBuilder sb = new StringBuilder(viewHeight * (viewWidth + 1) - 1);
        StringBuilder separatorBar = getSeparatorBar(columnWidthMap, viewWidth);
        sb.append(separatorBar).append('\n');
        sb.append(header(viewWidth, columnWidthMap)).append('\n');
        sb.append(separatorBar);
        
        for (TableRow tableRow : tableRowList) {
            sb.append('\n');
            sb.append(tableRowToString(tableRow, viewWidth, columnWidthMap));
        }
        
        return sb.append('\n').append(separatorBar).toString();
    }
    
    private StringBuilder tableRowToString(
            TableRow tableRow,
            int width,
            Map<TableColumnDescriptor, Integer> columnWidthMap) {
        StringBuilder sb = new StringBuilder(width);
        sb.append('|');
        
        for (TableColumnDescriptor tableColumnDescriptor 
                : tableColumnDescriptorList) {
            Object value = tableRow.get(cellMap.get(tableColumnDescriptor))
                                   .getValue();
            String text;
            
            if (value == null) {
                text = "NULL";
            } else {
                text = value.toString();
            }
            
            sb.append(getCellText(columnWidthMap.get(tableColumnDescriptor),
                                  text));
        }
        
        return sb;
    }
    
    private StringBuilder getCellText(int width, String text) {
        StringBuilder sb = new StringBuilder(width);
        int len = width - 1;
        sb.append(String.format(" %-" + len + "s|", text));
        return sb;
    }
    
    private StringBuilder 
        header(int width, Map<TableColumnDescriptor, Integer> columnWidthMap) {
        StringBuilder sb = new StringBuilder(width);
        sb.append('|');
        
        for (TableColumnDescriptor tableColumnDescriptor 
                : tableColumnDescriptorList) {
            String columnName = tableColumnDescriptor.getTableColumnName();
            int len = columnWidthMap.get(tableColumnDescriptor) - 1;
            sb.append(String.format(" %-" + len + "s|", columnName));
        }
        
        return sb;
    }
    
    private StringBuilder 
        getSeparatorBar(Map<TableColumnDescriptor, Integer> columnWidthMap,
                        int viewWidth) {
        StringBuilder sb = new StringBuilder(viewWidth).append('+');
        
        for (TableColumnDescriptor tableColumnDescriptor :
                tableColumnDescriptorList) {
            sb.append(getBar(columnWidthMap.get(tableColumnDescriptor)))
              .append('+');
        }
        
        return sb;
    }
    
    private String getBar(int width) {
        StringBuilder sb = new StringBuilder(width);
        
        for (int i = 0; i < width; ++i) {
            sb.append('-');
        }
        
        return sb.toString();
    }
        
    private int getViewWidth(
            Map<TableColumnDescriptor, Integer> columnWidthMap) {
        int width = columnWidthMap.size() + 1;
        
        for (Map.Entry<TableColumnDescriptor, Integer> e 
                : columnWidthMap.entrySet()) {
            width += e.getValue();
        }
        
        return width;
    }
    
    private Map<TableColumnDescriptor, Integer> getColumnWidthMap() {
        Map<TableColumnDescriptor, Integer> columnLengthMap = new HashMap<>();
        
        for (TableColumnDescriptor tableColumnDescriptor : 
                tableColumnDescriptorList) {
            columnLengthMap.put(tableColumnDescriptor, 
                                tableColumnDescriptor.getTableColumnName()
                                                     .length());
        }
        
        for (TableColumnDescriptor tableColumnDescriptor :
                    tableColumnDescriptorList) {
            int index = cellMap.get(tableColumnDescriptor);
            int maxColumnWidth = columnLengthMap.get(tableColumnDescriptor);
            
            for (TableRow tableRow : tableRowList) {
                String cellContent = tableRow.get(index).getValue().toString();
                int cellContentWidth = cellContent.length();
                maxColumnWidth = Math.max(maxColumnWidth, cellContentWidth);
            }
            
            columnLengthMap.put(tableColumnDescriptor, maxColumnWidth);
        }
        
        for (Map.Entry<TableColumnDescriptor, Integer> e 
                : columnLengthMap.entrySet()) {
            // Add the left and right margin spaces.
            e.setValue(e.getValue() + 2);
        }
        
        return columnLengthMap;
    }
    
    public static void main(String[] args) {
        Database db = new Database("mydb");
        
        TableColumnDescriptor col1 = 
                new TableColumnDescriptor("first_name",
                                          TableCellType.TYPE_STRING);
        
        TableColumnDescriptor col2 = 
                new TableColumnDescriptor("last_name",
                                          TableCellType.TYPE_STRING);
        
        TableColumnDescriptor col3 = 
                new TableColumnDescriptor("age", TableCellType.TYPE_INT);
        
        db.createTable("person", col1, col2, col3);
        
        Table table = db.getTable("person");
        
        TableRow row1 = table.putTableRow("Violetta", "Efremov", 24);
        TableRow row2 = table.putTableRow("Rodion", "Efremov", 28);
        
        TableView tableView = table.createTableView(col3, col2, col1);
        
        tableView.addTableRow(row2);
        tableView.addTableRow(row1);
        
        System.out.println(tableView.toString());
    }
}
