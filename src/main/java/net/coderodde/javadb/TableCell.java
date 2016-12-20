package net.coderodde.javadb;

import java.util.Objects;

public class TableCell {

    private Integer intValue;
    private Long longValue;
    private Float floatValue;
    private Double doubleValue;
    private String stringValue;
    private Boolean booleanValue;
    private byte[] binaryData;
    private TableCellType tableCellType;
    
    public TableCell(Integer intValue) {
        this.intValue = intValue;
        tableCellType = TableCellType.TYPE_INT;
    }
    
    public TableCell(Long longValue) {
        this.longValue = longValue;
        tableCellType = TableCellType.TYPE_LONG;
    }
    
    public TableCell(Float floatValue) {
        this.floatValue = floatValue;
        tableCellType = TableCellType.TYPE_FLOAT;
    }
    
    public TableCell(Double doubleValue) {
        this.doubleValue = doubleValue;
        tableCellType = TableCellType.TYPE_DOUBLE;
    }
    
    public TableCell(String stringValue) {
        this.stringValue = stringValue;
        tableCellType = TableCellType.TYPE_STRING;
    }
    
    public TableCell(Boolean booleanValue) {
        this.booleanValue = booleanValue;
        tableCellType = TableCellType.TYPE_BOOLEAN;
    }
    
    public TableCell(byte[] binaryData) {
        this.binaryData = binaryData;
        tableCellType = TableCellType.TYPE_BINARY;
    }
    
    public TableCellType getTableCellType() {
        return tableCellType;
    }
    
    public void setTableCellType(TableCellType tableCellType) {
        nullify();
        this.tableCellType =
                Objects.requireNonNull(tableCellType, 
                                       "The table cell type is null.");
    }
    
    public Integer getIntValue() {
        checkTypesMatchOnRead(TableCellType.TYPE_INT);
        return intValue;
    }
    
    public Long getLongValue() {
        checkTypesMatchOnRead(TableCellType.TYPE_LONG);
        return longValue;
    }
    
    public Float getFloatValue() {
        checkTypesMatchOnRead(TableCellType.TYPE_FLOAT);
        return floatValue;
    }
    
    public Double getDoubleValue() {
        checkTypesMatchOnRead(TableCellType.TYPE_DOUBLE);
        return doubleValue;
    }
    
    public String getStringValue() {
        checkTypesMatchOnRead(TableCellType.TYPE_STRING);
        return stringValue;
    }
    
    public Boolean getBooleanValue() {
        checkTypesMatchOnRead(TableCellType.TYPE_BOOLEAN);
        return booleanValue;
    }
    
    public byte[] getBinaryData() {
        checkTypesMatchOnRead(TableCellType.TYPE_BINARY);
        return binaryData;
    }
    
    public void setIntValue(Integer intValue) {
        checkTypesMatchOnWrite(TableCellType.TYPE_INT);
        this.intValue = intValue;
    }
    
    public void setLongValue(Long longValue) {
        checkTypesMatchOnWrite(TableCellType.TYPE_LONG);
        this.longValue = longValue;
    }
    
    public void setFloatValue(Float floatValue) {
        checkTypesMatchOnWrite(TableCellType.TYPE_FLOAT);
        this.floatValue = floatValue;
    }
    
    public void setDoubleValue(Double doubleValue) {
        checkTypesMatchOnWrite(TableCellType.TYPE_DOUBLE);
        this.doubleValue = doubleValue;
    }
    
    public void setStringValue(String stringValue) {
        checkTypesMatchOnWrite(TableCellType.TYPE_STRING);
        this.stringValue = stringValue;
    }
    
    public void setBooleanValue(Boolean booleanValue) {
        checkTypesMatchOnWrite(TableCellType.TYPE_BOOLEAN);
        this.booleanValue = booleanValue;
    }
    
    public void setBinaryData(byte[] binaryData) {
        checkTypesMatchOnWrite(TableCellType.TYPE_BINARY);
        this.binaryData = binaryData;
    }
    
    @SuppressWarnings("UnnecessaryReturnStatement")
    public void nullify() {
        switch (tableCellType) {
            case TYPE_INT:
                intValue = null;
                return;
                
            case TYPE_LONG:
                longValue = null;
                return;
                
            case TYPE_FLOAT:
                floatValue = null;
                return;
                
            case TYPE_DOUBLE:
                doubleValue = null;
                return;
                
            case TYPE_STRING:
                stringValue = null;
                return;
                
            case TYPE_BOOLEAN:
                booleanValue = null;
                return;
                
            case TYPE_BINARY:
                binaryData = null;
                return;
        }
    }
    
    private void checkTypesMatchOnRead(TableCellType requestedType) {
        if (!tableCellType.equals(requestedType)) {
            throw new IllegalStateException(
            "Attempted to read " + requestedType.getTypeName() + 
            " from a table cell holding " + tableCellType.getTypeName() + ".");
        }
    }
    
    private void checkTypesMatchOnWrite(TableCellType requestedType) {
        if (!tableCellType.equals(requestedType)) {
            throw new IllegalStateException(
            "Attempted to write " + requestedType.getTypeName() + 
            " to a table cell holding " + tableCellType.getTypeName() + ".");
        }
    }
}
