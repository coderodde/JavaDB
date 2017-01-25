package net.coderodde.javadb;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import net.coderodde.javadb.Misc.Pair;

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
    
    static final EnumMap<TableCellType, Object> defaults = 
             new EnumMap<>(TableCellType.class);
    
    static {
        defaults.put(TableCellType.TYPE_INT,     DEFAULT_INT    );
        defaults.put(TableCellType.TYPE_LONG,    DEFAULT_LONG   );
        defaults.put(TableCellType.TYPE_FLOAT,   DEFAULT_FLOAT  );
        defaults.put(TableCellType.TYPE_DOUBLE,  DEFAULT_DOUBLE );
        defaults.put(TableCellType.TYPE_STRING,  DEFAULT_STRING );
        defaults.put(TableCellType.TYPE_BOOLEAN, DEFAULT_BOOLEAN);
        defaults.put(TableCellType.TYPE_BINARY,  DEFAULT_BLOB   );
    }
    
    private static final byte BOOLEAN_TRUE  = 1;
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
    
    public ByteBuffer serialize() {
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
    
    private ByteBuffer serializeInt() {
        if (value == null) {
            return ByteBuffer.allocate(1).put(INT_NULL);
        }
        
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES + 1)
                                          .order(ByteOrder.LITTLE_ENDIAN);
        
        return byteBuffer.put(INT_NOT_NULL)
                         .putInt((int) value);
    }
    
    private ByteBuffer serializeLong() {
        if (value == null) {
            return ByteBuffer.allocate(1).put(LONG_NULL);
        }
        
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES + 1)
                                          .order(ByteOrder.LITTLE_ENDIAN);
        
        return byteBuffer.put(LONG_NOT_NULL)
                         .putLong((long) value);
    }
    
    private ByteBuffer serializeFloat() {
        if (value == null) {
            return ByteBuffer.allocate(1).put(FLOAT_NULL);
        }
        
        ByteBuffer byteBuffer = ByteBuffer.allocate(Float.BYTES + 1)
                                          .order(ByteOrder.LITTLE_ENDIAN);
        
        return byteBuffer.put(FLOAT_NOT_NULL)
                         .putFloat((float) value);
    }
    
    private ByteBuffer serializeDouble() {
        if (value == null) {
            return ByteBuffer.allocate(1).put(DOUBLE_NULL);
        }
        
        ByteBuffer byteBuffer = ByteBuffer.allocate(Double.BYTES + 1)
                                          .order(ByteOrder.LITTLE_ENDIAN);
        
        return byteBuffer.put(DOUBLE_NOT_NULL)
                         .putDouble((double) value);
    }
    
    private ByteBuffer serializeString() {
        if (value == null) {
            return ByteBuffer.allocate(1).put(STRING_NULL);
        }
        
        String string = (String) value;
        ByteBuffer byteBuffer = 
                ByteBuffer.allocate(
                        1 + Integer.BYTES + Character.BYTES * string.length())
                .order(ByteOrder.LITTLE_ENDIAN);
        
        byteBuffer.put(STRING_NOT_NULL)
                  .putInt(string.length());
        
        for (int i = 0; i != string.length(); ++i) {
            byteBuffer.putChar(string.charAt(i));
        }
        
        return byteBuffer;
    }
    
    private ByteBuffer serializeBoolean() {
        if (value == null) {
            return ByteBuffer.allocate(1).put(BOOLEAN_NULL);
        }
        
        ByteBuffer byteBuffer = ByteBuffer.allocate(2)
                                          .order(ByteOrder.LITTLE_ENDIAN);
        return byteBuffer.put(BOOLEAN_NOT_NULL)
                         .put((boolean) value ? BOOLEAN_TRUE : BOOLEAN_FALSE);
    }
    
    private ByteBuffer serializeBinaryData() {
        if (value == null) {
            return ByteBuffer.allocate(1).put(BLOB_NULL);
        }
        
        byte[] data = (byte[]) value;
        ByteBuffer byteBuffer =
                ByteBuffer.allocate(1 + Integer.BYTES + data.length)
                          .order(ByteOrder.LITTLE_ENDIAN);
        
        return byteBuffer.put(BLOB_NOT_NULL)
                         .putInt(data.length)
                         .put(data);
    }
    
    public static Pair<TableCell, Integer> deserialize(ByteBuffer byteBuffer,
                                                       int startIndex) {
        Objects.requireNonNull(byteBuffer, "The input data byte array is null.");
        checkTableCellValueFitsInByteBuffer(byteBuffer, startIndex, 1);
        byte tableCellType = byteBuffer.get(startIndex++);
        
        switch (tableCellType) {
            case INT_NOT_NULL:
                return deserializeInt(byteBuffer, startIndex);
                
            case LONG_NOT_NULL:
                return deserializeLong(byteBuffer, startIndex);
                
            case FLOAT_NOT_NULL:
                return deserializeFloat(byteBuffer, startIndex);
                
            case DOUBLE_NOT_NULL:
                return deserializeDouble(byteBuffer, startIndex);
                
            case STRING_NOT_NULL:
                return deserializeString(byteBuffer, startIndex);
                
            case BOOLEAN_NOT_NULL:
                return deserializeBoolean(byteBuffer, startIndex);
                
            case BLOB_NOT_NULL:
                return deserializeBinaryData(byteBuffer, startIndex);
                
            case INT_NULL:
                return new Pair<>(new TableCell(TableCellType.TYPE_INT), 0);
                
            case LONG_NULL:
                return new Pair<>(new TableCell(TableCellType.TYPE_LONG), 0);
                
            case FLOAT_NULL:
                return new Pair<>(new TableCell(TableCellType.TYPE_FLOAT), 0);
                
            case DOUBLE_NULL:
                return new Pair<>(new TableCell(TableCellType.TYPE_DOUBLE), 0);
                
            case STRING_NULL:
                return new Pair<>(new TableCell(TableCellType.TYPE_STRING), 0);
                
            case BOOLEAN_NULL:
                return new Pair<>(new TableCell(TableCellType.TYPE_BOOLEAN), 0);
                
            case BLOB_NULL:
                return new Pair<>(new TableCell(TableCellType.TYPE_BINARY), 0);
                
            default:
                throw new IllegalStateException(
                        "Invalid table cell type descriptor.");
        }
    }
    
    private static Pair<TableCell, Integer> 
        deserializeInt(ByteBuffer byteBuffer, int startIndex) {
        checkTableCellValueFitsInByteBuffer(byteBuffer, startIndex, Integer.BYTES);
        int value = byteBuffer.getInt(startIndex);
        TableCell tableCell = new TableCell(value);
        return new Pair<>(tableCell, Integer.BYTES);
    }
    
    private static Pair<TableCell, Integer> 
        deserializeLong(ByteBuffer byteBuffer, int startIndex) {
        checkTableCellValueFitsInByteBuffer(byteBuffer, startIndex, Long.BYTES);
        long value = byteBuffer.getLong(startIndex);
        TableCell tableCell = new TableCell(value);
        return new Pair<>(tableCell, Long.BYTES);
    }
    
    private static Pair<TableCell, Integer> 
        deserializeFloat(ByteBuffer byteBuffer, int startIndex) {
        checkTableCellValueFitsInByteBuffer(byteBuffer, startIndex, Float.BYTES);
        float value = byteBuffer.getFloat(startIndex);
        TableCell tableCell = new TableCell(value);
        return new Pair<>(tableCell, Float.BYTES);
    }
    
    private static Pair<TableCell, Integer> 
        deserializeDouble(ByteBuffer byteBuffer, int startIndex) {
        checkTableCellValueFitsInByteBuffer(byteBuffer, startIndex, Double.BYTES);
        double value = byteBuffer.getDouble(startIndex);
        TableCell tableCell = new TableCell(value);
        return new Pair<>(tableCell, Double.BYTES);
    }
    
    private static int getLengthFromByteArray(byte[] data, int startIndex) {
        byte byte1 = data[startIndex];
        byte byte2 = data[startIndex + 1];
        byte byte3 = data[startIndex + 2];
        byte byte4 = data[startIndex + 3];
        
        int stringLength = Byte.toUnsignedInt(byte1);
        stringLength |= (Byte.toUnsignedInt(byte2) << 8);
        stringLength |= (Byte.toUnsignedInt(byte3) << 16);
        stringLength |= (Byte.toUnsignedInt(byte4) << 24);
        
        return stringLength;
    }
    
    private static Pair<TableCell, Integer> 
        deserializeString(ByteBuffer byteBuffer, int startIndex) {
        // First check that the string length descriptor fits in:
        checkTableCellValueFitsInByteBuffer(byteBuffer, startIndex, Integer.BYTES);
        // Get the string length:
        int stringLength = byteBuffer.getInt(startIndex);
        startIndex += Integer.BYTES;
        
        // Now check whether the actual string fits in the data array:
        checkTableCellValueFitsInByteBuffer(byteBuffer,
                                      startIndex, 
                                      stringLength * Character.BYTES);
        
        StringBuilder sb = new StringBuilder(stringLength);
        
        for (int charIndex = 0; 
                charIndex != stringLength; 
                charIndex++, startIndex += Character.BYTES) {
            sb.append(byteBuffer.getChar(startIndex));
        }
        
        TableCell tableCell = new TableCell(sb.toString());
        return new Pair<>(tableCell, 
                          Integer.BYTES + stringLength * Character.BYTES);
    }
        
    private static Pair<TableCell, Integer>
        deserializeBinaryData(ByteBuffer byteBuffer, int startIndex) {
        // First check that the binary byte array length descriptor fits in:
        checkTableCellValueFitsInByteBuffer(byteBuffer, startIndex, Integer.BYTES);
        
        // Get the byte array length:
        int byteArrayLength = byteBuffer.getInt(startIndex);
        
        // Now check whether the actual byte array fits in the data array:
        checkTableCellValueFitsInByteBuffer(byteBuffer, 
                                      startIndex += Integer.BYTES,
                                      byteArrayLength);
        
        byte[] byteArray = new byte[byteArrayLength];
        
        for (int index = 0; index != byteArrayLength; ++index) {
            byteArray[index] = byteBuffer.get(startIndex + index);
        }
        
        TableCell tableCell = new TableCell(byteArray);
        return new Pair<>(tableCell, Integer.BYTES + byteArrayLength);
    }
    
    private static Pair<TableCell, Integer> 
        deserializeBoolean(ByteBuffer byteBuffer, int startIndex) {
        checkTableCellValueFitsInByteBuffer(byteBuffer, startIndex, 1);
        boolean value;
        
        switch (byteBuffer.get(startIndex)) {
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
        return new Pair<>(tableCell, 1);
    }
    
    private static void 
        checkTableCellValueFitsInByteBuffer(ByteBuffer byteBuffer,
                                            int startIndex,
                                            int cellValueLength) {
        if (startIndex + cellValueLength > byteBuffer.capacity()) {
            throw new BadDataFormatException(
                    "The data of expected length does not fit in the byte " +
                    "buffer. Start index: " + startIndex + 
                    ", cell value length: " + cellValueLength + 
                    ", byte buffer capacity: " + byteBuffer.capacity() + ".");
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
