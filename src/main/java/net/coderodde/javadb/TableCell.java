package net.coderodde.javadb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This class implements an individual table cell.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Dec 21, 2016)
 */
public final class TableCell {
    
    private static final byte NON_NULL_MASK = 0x10;
    
    private static final byte INT_NULL     = 1;
    private static final byte LONG_NULL    = 2;
    private static final byte FLOAT_NULL   = 3;
    private static final byte DOUBLE_NULL  = 4;
    private static final byte STRING_NULL  = 5;
    private static final byte BOOLEAN_NULL = 6;
    private static final byte BLOB_NULL    = 7;
    
    private static final byte INT_NOT_NULL     = INT_NULL     & NON_NULL_MASK;
    private static final byte LONG_NOT_NULL    = LONG_NULL    & NON_NULL_MASK;
    private static final byte FLOAT_NOT_NULL   = FLOAT_NULL   & NON_NULL_MASK;
    private static final byte DOUBLE_NOT_NULL  = DOUBLE_NULL  & NON_NULL_MASK;
    private static final byte STRING_NOT_NULL  = STRING_NULL  & NON_NULL_MASK;
    private static final byte BOOLEAN_NOT_NULL = BOOLEAN_NULL & NON_NULL_MASK;
    private static final byte BLOB_NOT_NULL    = BLOB_NULL    & NON_NULL_MASK;
    
    private static final byte IS_NULL = 0;
    private static final byte IS_NOT_NULL = 1;
    
    private static final byte BOOLEAN_TRUE = 1;
    private static final byte BOOLEAN_FALSE = 0;

    private Object value;
    private TableCellType tableCellType;
    
    public TableCell(Integer intValue) {
        this.value = intValue;
        tableCellType = TableCellType.TYPE_INT;
    }
    
    public TableCell(Long longValue) {
        this.value = longValue;
        tableCellType = TableCellType.TYPE_LONG;
    }
    
    public TableCell(Float floatValue) {
        this.value = floatValue;
        tableCellType = TableCellType.TYPE_FLOAT;
    }
    
    public TableCell(Double doubleValue) {
        this.value = doubleValue;
        tableCellType = TableCellType.TYPE_DOUBLE;
    }
    
    public TableCell(String stringValue) {
        this.value = stringValue;
        tableCellType = TableCellType.TYPE_STRING;
    }
    
    public TableCell(Boolean booleanValue) {
        this.value = booleanValue;
        tableCellType = TableCellType.TYPE_BOOLEAN;
    }
    
    public TableCell(byte[] binaryData) {
        this.value = binaryData;
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
        return (Integer) value;
    }
    
    public Long getLongValue() {
        checkTypesMatchOnRead(TableCellType.TYPE_LONG);
        return (Long) value;
    }
    
    public Float getFloatValue() {
        checkTypesMatchOnRead(TableCellType.TYPE_FLOAT);
        return (Float) value;
    }
    
    public Double getDoubleValue() {
        checkTypesMatchOnRead(TableCellType.TYPE_DOUBLE);
        return (Double) value;
    }
    
    public String getStringValue() {
        checkTypesMatchOnRead(TableCellType.TYPE_STRING);
        return (String) value;
    }
    
    public Boolean getBooleanValue() {
        checkTypesMatchOnRead(TableCellType.TYPE_BOOLEAN);
        return (Boolean) value;
    }
    
    public byte[] getBinaryData() {
        checkTypesMatchOnRead(TableCellType.TYPE_BINARY);
        return (byte[]) value;
    }
    
    public void setIntValue(Integer intValue) {
        checkTypesMatchOnWrite(TableCellType.TYPE_INT);
        this.value = intValue;
    }
    
    public void setLongValue(Long longValue) {
        checkTypesMatchOnWrite(TableCellType.TYPE_LONG);
        this.value = longValue;
    }
    
    public void setFloatValue(Float floatValue) {
        checkTypesMatchOnWrite(TableCellType.TYPE_FLOAT);
        this.value = floatValue;
    }
    
    public void setDoubleValue(Double doubleValue) {
        checkTypesMatchOnWrite(TableCellType.TYPE_DOUBLE);
        this.value = doubleValue;
    }
    
    public void setStringValue(String stringValue) {
        checkTypesMatchOnWrite(TableCellType.TYPE_STRING);
        this.value = stringValue;
    }
    
    public void setBooleanValue(Boolean booleanValue) {
        checkTypesMatchOnWrite(TableCellType.TYPE_BOOLEAN);
        this.value = booleanValue;
    }
    
    public void setBinaryData(byte[] binaryData) {
        checkTypesMatchOnWrite(TableCellType.TYPE_BINARY);
        this.value = binaryData;
    }
    
    public void nullify() {
        value = null;
    }
    
    public List<Byte> serialize() {
        switch (getTableCellType()) {
            case TYPE_INT:
                return serializeInt();
                
            case TYPE_LONG:
                return serializeLong();
                
            case TYPE_FLOAT:
                return serializeFloat();
                
            case TYPE_DOUBLE:
                return serializeDouble();
                
            case TYPE_STRING:
                return serializeString();
                
            case TYPE_BOOLEAN:
                return serializeBoolean();
                
            case TYPE_BINARY:
                return serializeBinaryData();
                
            default:
                throw new IllegalStateException(
                        "This exception must never be thrown. One " + 
                        "possibility is that a new data type is introduced, " + 
                        "yet is not handled in this method.");
        }
    }
    
    private List<Byte> serializeInt() {
        if (value == null) {
            return Arrays.asList(INT_NULL);
        }
        
        List<Byte> byteList = new ArrayList<>(Integer.BYTES + 1);
        int primitiveValue = (Integer) value;
        byteList.add(INT_NOT_NULL);
        
        for (int i = 0; i != Integer.BYTES; ++i, primitiveValue >>>= 8) {
            byteList.add((byte)(primitiveValue & 0xff));
        }
        
        return byteList;
    }
    
    private List<Byte> serializeLong() {
        if (value == null) {
            return Arrays.asList(LONG_NULL);
        }
        
        List<Byte> byteList = new ArrayList<>(Long.BYTES + 1);
        long primitiveValue = (Long) value;
        byteList.add(LONG_NOT_NULL);
        
        for (int i = 0; i != Long.BYTES; ++i, primitiveValue >>>= 8) {
            byteList.add((byte)(primitiveValue & 0xffL));
        }
            
        return byteList;
    }
    
    private List<Byte> serializeFloat() {
        if (value == null) {
            return Arrays.asList(FLOAT_NULL);
        }
        
        List<Byte> byteList = new ArrayList<>(Float.BYTES + 1);
        int primitiveValue = Float.floatToIntBits((Float) value);
        byteList.add(FLOAT_NOT_NULL);
        
        for (int i = 0; i != Float.BYTES; ++i, primitiveValue >>>= 8) {
            byteList.add((byte)(primitiveValue & 0xff));
        }
        
        return byteList;
    }
    
    private List<Byte> serializeDouble() {
        if (value == null) {
            return Arrays.asList(DOUBLE_NULL);
        } 
        
        List<Byte> byteList = new ArrayList<>(Double.BYTES + 1);
        long primitiveValue = Double.doubleToLongBits((Double) value);
        byteList.add(DOUBLE_NOT_NULL);
        
        for (int i = 0; i != Double.BYTES; ++i, primitiveValue >>>= 8) {
            byteList.add((byte)(primitiveValue & 0xffL));
        }
        
        return byteList;
    }
    
    private List<Byte> serializeString() {
        if (value == null) {
            return Arrays.asList(STRING_NULL);
        }
        
        String string = (String) value;
        // Header byte, 32-bit string length, and actual string:
        List<Byte> byteList = new ArrayList<>(Character.BYTES * string.length()
                                              + Integer.BYTES + 1);
        byteList.add(STRING_NOT_NULL);
        
        int stringLength = string.length();
        
        // Emit the string length in characters:
        byteList.add((byte) (stringLength & 0xff));
        byteList.add((byte)((stringLength >>> 8)  & 0xff));
        byteList.add((byte)((stringLength >>> 16) & 0xff));
        byteList.add((byte)((stringLength >>> 24) & 0xff));
        
        // Emit the actual string:
        for (int i = 0; i != stringLength; ++i) {
            char c = string.charAt(i);
            short binaryChar = (short) c;
            
            byteList.add((byte)(binaryChar & 0xff));
            byteList.add((byte)((binaryChar >>> 8) & 0xff));
        }
        
        return byteList;
    }
    
    private List<Byte> serializeBoolean() {
        if (value == null) {
            return Arrays.asList(BOOLEAN_NULL);
        }
        
        List<Byte> byteList = new ArrayList<>(2);
        byteList.add(BOOLEAN_NOT_NULL);
        byteList.add((Boolean) value ? BOOLEAN_TRUE : BOOLEAN_FALSE);
        return byteList;
    }
    
    private List<Byte> serializeBinaryData() {
        if (value == null) {
            return Arrays.asList(BLOB_NULL);
        }
        
        byte[] data = (byte[]) value;
        List<Byte> byteList = new ArrayList<>(data.length + 1);
        byteList.add(BLOB_NOT_NULL);
        
        for (byte b : data) {
            byteList.add(b);
        }
        
        return byteList;
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
