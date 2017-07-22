package net.coderodde.javadb;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class implements an individual table cell.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Dec 21, 2016)
 */
public final class TableCell {
    
    private static final byte NON_NULL_MASK = 0x10;
    
    static final byte INT_NULL     = 1;
    static final byte LONG_NULL    = 2;
    static final byte FLOAT_NULL   = 3;
    static final byte DOUBLE_NULL  = 4;
    static final byte STRING_NULL  = 5;
    static final byte BOOLEAN_NULL = 6;
    static final byte BLOB_NULL    = 7;

    static final byte INT_NOT_NULL     = INT_NULL     | NON_NULL_MASK;
    static final byte LONG_NOT_NULL    = LONG_NULL    | NON_NULL_MASK;
    static final byte FLOAT_NOT_NULL   = FLOAT_NULL   | NON_NULL_MASK;
    static final byte DOUBLE_NOT_NULL  = DOUBLE_NULL  | NON_NULL_MASK;
    static final byte STRING_NOT_NULL  = STRING_NULL  | NON_NULL_MASK;
    static final byte BOOLEAN_NOT_NULL = BOOLEAN_NULL | NON_NULL_MASK;
    static final byte BLOB_NOT_NULL    = BLOB_NULL    | NON_NULL_MASK;
    
    static final Integer DEFAULT_INT     = 0;
    static final Long    DEFAULT_LONG    = 0L;
    static final Float   DEFAULT_FLOAT   = 0.0f;
    static final Double  DEFAULT_DOUBLE  = 0.0;
    static final String  DEFAULT_STRING  = "";
    static final Boolean DEFAULT_BOOLEAN = Boolean.FALSE;
    static final byte[]  DEFAULT_BLOB    = new byte[0];
    
    /**
     * Number of bytes used to decode string and byte array lengths.
     */
    private static final int SIZE_BYTES = 4;
    
    static final EnumMap<TableCellType, Object> defaults = 
             new EnumMap<>(TableCellType.class);
    
    private static final Map<Byte, TableCellDeserializer> 
            deserializerDispatchMap = new HashMap<>();
    
    private static final Map<TableCellType, TableCellSerializer> 
            serializerDispatchMap = new HashMap<>();
    
    static {
        defaults.put(TableCellType.TYPE_INT,     DEFAULT_INT    );
        defaults.put(TableCellType.TYPE_LONG,    DEFAULT_LONG   );
        defaults.put(TableCellType.TYPE_FLOAT,   DEFAULT_FLOAT  );
        defaults.put(TableCellType.TYPE_DOUBLE,  DEFAULT_DOUBLE );
        defaults.put(TableCellType.TYPE_STRING,  DEFAULT_STRING );
        defaults.put(TableCellType.TYPE_BOOLEAN, DEFAULT_BOOLEAN);
        defaults.put(TableCellType.TYPE_BINARY,  DEFAULT_BLOB   );
        
        Map<Byte, TableCellDeserializer> d = deserializerDispatchMap;
        
        d.put(INT_NOT_NULL,     TableCell::deserializeInt);
        d.put(LONG_NOT_NULL,    TableCell::deserializeLong);
        d.put(FLOAT_NOT_NULL,   TableCell::deserializeFloat);
        d.put(DOUBLE_NOT_NULL,  TableCell::deserializeDouble);
        d.put(STRING_NOT_NULL,  TableCell::deserializeString);
        d.put(BOOLEAN_NOT_NULL, TableCell::deserializeBoolean);
        d.put(BLOB_NOT_NULL,    TableCell::deserializeBlob);
        
        d.put(INT_NULL,     TableCell::deserializeNullInt);
        d.put(LONG_NULL,    TableCell::deserializeNullLong);
        d.put(FLOAT_NULL,   TableCell::deserializeNullFloat);
        d.put(DOUBLE_NULL,  TableCell::deserializeNullDouble);
        d.put(STRING_NULL,  TableCell::deserializeNullString);
        d.put(BOOLEAN_NULL, TableCell::deserializeNullBoolean);
        d.put(BLOB_NULL,    TableCell::deserializeNullBlob);
        
        Map<TableCellType, TableCellSerializer> s = serializerDispatchMap;
        
        s.put(TableCellType.TYPE_INT,     TableCell::serializeInt);
        s.put(TableCellType.TYPE_LONG,    TableCell::serializeLong);
        s.put(TableCellType.TYPE_FLOAT,   TableCell::serializeFloat);
        s.put(TableCellType.TYPE_DOUBLE,  TableCell::serializeDouble);
        s.put(TableCellType.TYPE_STRING,  TableCell::serializeString);
        s.put(TableCellType.TYPE_BOOLEAN, TableCell::serializeBoolean);
        s.put(TableCellType.TYPE_BINARY,  TableCell::serializeBlob);
    }
    
    static final byte BOOLEAN_TRUE  = 1;
    static final byte BOOLEAN_FALSE = 0;

    private Object value;
    private final TableCellType tableCellType;
    
    public Object getValue() {
        return value;
    }
    
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
    
    /**
     * Constructs a new table cell of particular data type with {@code null} 
     * value.
     * 
     * @param tableCellType the type of the cell.
     */
    public TableCell(TableCellType tableCellType) {
        this.tableCellType = Objects.requireNonNull(tableCellType,
                                                    "Table cell type is null.");
    }
    
    public TableCellType getTableCellType() {
        return tableCellType;
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
        
        TableCell other = (TableCell) o;
        
        if (!getTableCellType().equals(other.getTableCellType())) {
            return false;
        }
        
        if (getTableCellType().equals(TableCellType.TYPE_BINARY)) {
            return Arrays.equals((byte[]) value, (byte[]) other.value);
        }
        
        return Objects.equals(value, other.value);
    }
    
    int getSerializationLength() {
        switch (tableCellType) {
            case TYPE_INT:
                return 1 + (value != null ? Integer.BYTES : 0);
                
            case TYPE_LONG:
                return 1 + (value != null ? Long.BYTES : 0);
                
            case TYPE_FLOAT:
                return 1 + (value != null ? Float.BYTES : 0);
                
            case TYPE_DOUBLE:
                return 1 + (value != null ? Double.BYTES : 0);
                
            case TYPE_STRING:
                if (value == null) {
                    return 1;
                }
                
                int stringChars = ((String) value).length();
                int stringBytes = stringChars * Character.BYTES;
                return 1 + SIZE_BYTES + stringBytes;
                
            case TYPE_BOOLEAN:
                return 1 + (value != null ? 1 : 0);
                
            case TYPE_BINARY:
                if (value == null) {
                    return 1;
                }
                
                return 1 + SIZE_BYTES + ((byte[]) value).length;
                
            default:
                throw new IllegalStateException("Unknown table cell type.");
        }
    }
    
    void serialize(ByteBuffer byteBuffer) {
        TableCellSerializer tableCellSerializer = 
                serializerDispatchMap.get(getTableCellType());
        
        if (tableCellSerializer == null) {
            throw new IllegalStateException(
                    "This exception must never be thrown. One " + 
                    "possibility is that a new data type is introduced, " + 
                    "yet is not handled in this method.");
        }
        
        tableCellSerializer.serialize(byteBuffer, value);
    }
    
    static TableCell deserialize(ByteBuffer byteBuffer) {
        Objects.requireNonNull(byteBuffer, 
                               "The input data byte array is null.");
        
        byte tableCellType = byteBuffer.get();
        TableCellDeserializer deserializerRoutine = 
                deserializerDispatchMap.get(tableCellType);
        
        if (deserializerRoutine == null) {
            throw new IllegalStateException(
                    "Invalid table cell type descriptor.");
        }
        
        return deserializerRoutine.deserialize(byteBuffer);
    }
    
    private static void serializeInt(ByteBuffer byteBuffer, Object value) {
        if (value == null) {
            byteBuffer.put(INT_NULL);
        } else {
            byteBuffer.put(INT_NOT_NULL).putInt((int) value);
        }
    }
    
    private static void serializeLong(ByteBuffer byteBuffer, Object value) {
        if (value == null) {
            byteBuffer.put(LONG_NULL);
        } else {
            byteBuffer.put(LONG_NOT_NULL).putLong((long) value);
        }
    }
    
    private static void serializeFloat(ByteBuffer byteBuffer, Object value) {
        if (value == null) {
            byteBuffer.put(FLOAT_NULL);
        } else {
            byteBuffer.put(FLOAT_NOT_NULL).putFloat((float) value);
        }
    }
    
    private static void serializeDouble(ByteBuffer byteBuffer, Object value) {
        if (value == null) {
            byteBuffer.put(DOUBLE_NULL);
        } else {
            byteBuffer.put(DOUBLE_NOT_NULL).putDouble((double) value);
        }
    }
    
    private static void serializeString(ByteBuffer byteBuffer, Object value) {
        if (value == null) {
            byteBuffer.put(STRING_NULL);
        } else {
            byteBuffer.put(STRING_NOT_NULL);
            byteBuffer.putInt(((String) value).length());
            
            for (char c : ((String) value).toCharArray()) {
                byteBuffer.putChar(c);
            }
        }
    }
    
    private static void serializeBoolean(ByteBuffer byteBuffer, Object value) {
        if (value == null) {
            byteBuffer.put(BOOLEAN_NULL);
        } else {
            byteBuffer.put(BOOLEAN_NOT_NULL);
            byteBuffer.put(((boolean) value) ? BOOLEAN_TRUE : BOOLEAN_FALSE);
        }
    }
    
    private static void serializeBlob(ByteBuffer byteBuffer, Object value) {
        if (value == null) {
            byteBuffer.put(BLOB_NULL);
        } else {
            byteBuffer.put(BLOB_NOT_NULL);
            byte[] blob = (byte[]) value;
            byteBuffer.putInt(blob.length);
            
            for (int i = 0; i < blob.length; ++i) {
                byteBuffer.put(blob[i]);
            }
        }
    }
    
    private static TableCell deserializeNullInt(ByteBuffer byteBuffer) {
        return new TableCell(TableCellType.TYPE_INT);
    }
            
    private static TableCell deserializeNullLong(ByteBuffer byteBuffer) {
        return new TableCell(TableCellType.TYPE_LONG);
    }
            
    private static TableCell deserializeNullFloat(ByteBuffer byteBuffer) {
        return new TableCell(TableCellType.TYPE_FLOAT);
    }
            
    private static TableCell deserializeNullDouble(ByteBuffer byteBuffer) {
        return new TableCell(TableCellType.TYPE_DOUBLE);
    }
        
    private static TableCell deserializeNullString(ByteBuffer byteBuffer) {
        return new TableCell(TableCellType.TYPE_STRING);
    }
            
    private static TableCell deserializeNullBoolean(ByteBuffer byteBuffer) {
        return new TableCell(TableCellType.TYPE_BOOLEAN);           
    }
            
    private static TableCell deserializeNullBlob(ByteBuffer byteBuffer) {
        return new TableCell(TableCellType.TYPE_BINARY);           
    }
    
    private static TableCell deserializeInt(ByteBuffer byteBuffer) {
        int value = byteBuffer.getInt();
        TableCell tableCell = new TableCell(value);
        return tableCell;
    }
    
    private static TableCell deserializeLong(ByteBuffer byteBuffer) {
        long value = byteBuffer.getLong();
        TableCell tableCell = new TableCell(value);
        return tableCell;
    }
    
    private static TableCell deserializeFloat(ByteBuffer byteBuffer) {
        float value = byteBuffer.getFloat();
        TableCell tableCell = new TableCell(value);
        return tableCell;
    }
    
    private static TableCell deserializeDouble(ByteBuffer byteBuffer) {
        double value = byteBuffer.getDouble();
        TableCell tableCell = new TableCell(value);
        return tableCell;
    }
    
    private static TableCell deserializeString(ByteBuffer byteBuffer) {
        // Get the string length:
        int stringLength = byteBuffer.getInt();
        StringBuilder sb = new StringBuilder(stringLength);
        
        for (int i = 0; i < stringLength; ++i) {
            sb.append(byteBuffer.getChar());
        }
        
        TableCell tableCell = new TableCell(sb.toString());
        return tableCell;
    }
        
    private static TableCell deserializeBlob(ByteBuffer byteBuffer) {
        // Get the byte array length:
        int byteArrayLength = byteBuffer.getInt();
        byte[] byteArray = new byte[byteArrayLength];
        
        for (int index = 0; index != byteArrayLength; ++index) {
            byteArray[index] = byteBuffer.get();
        }
        
        TableCell tableCell = new TableCell(byteArray);
        return tableCell;
    }
    
    private static TableCell deserializeBoolean(ByteBuffer byteBuffer) {
        boolean value;
        
        switch (byteBuffer.get()) {
            case BOOLEAN_TRUE:
                value = true;
                break;
                
            case BOOLEAN_FALSE:
                value = false;
                break;
                
            default:
                throw new BadDataFormatException(
                        "Unknown boolean literal encoding.");
        }
        
        TableCell tableCell = new TableCell(value);
        return tableCell;
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
