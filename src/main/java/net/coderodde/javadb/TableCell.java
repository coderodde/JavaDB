package net.coderodde.javadb;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<Byte> byteList = new ArrayList<>(data.length + Integer.BYTES + 1);
        byteList.add(BLOB_NOT_NULL);
        
        int blobLength = data.length;
        
        // Emit the length of the BLOB byte array:
        byteList.add((byte)(blobLength & 0xff));
        byteList.add((byte)((blobLength >>> 8) & 0xff));
        byteList.add((byte)((blobLength >>> 16) & 0xff));
        byteList.add((byte)((blobLength >>> 24) & 0xff));
        
        // Emit the actual byte array:
        for (byte b : data) {
            byteList.add(b);
        }
        
        return byteList;
    }
    
    public static Pair<TableCell, Integer> deserialize(byte[] data,
                                                       int startIndex) {
        Objects.requireNonNull(data, "The input data byte array is null.");
        checkTableCellValueFitsInData(data, startIndex, 1);
        
        byte tableCellType = data[startIndex++];
        
        switch (tableCellType) {
            case INT_NOT_NULL:
                return deserializeInt(data, startIndex);
                
            case LONG_NOT_NULL:
                return deserializeLong(data, startIndex);
                
            case FLOAT_NOT_NULL:
                return deserializeFloat(data, startIndex);
                
            case DOUBLE_NOT_NULL:
                return deserializeDouble(data, startIndex);
                
            case STRING_NOT_NULL:
                return deserializeString(data, startIndex);
                
            case BOOLEAN_NOT_NULL:
                return deserializeBoolean(data, startIndex);
                
            case BLOB_NOT_NULL:
                return deserializeBinaryData(data, startIndex);
                
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
    
    private static Pair<TableCell, Integer> deserializeInt(byte[] data,
                                                           int startIndex) {
        checkTableCellValueFitsInData(data, startIndex, Integer.BYTES);
        int value;
        
        byte byte1 = data[startIndex];
        byte byte2 = data[startIndex + 1];
        byte byte3 = data[startIndex + 2];
        byte byte4 = data[startIndex + 3];
        
        value  =  Byte.toUnsignedInt(byte1);
        value |= (Byte.toUnsignedInt(byte2) << 8);
        value |= (Byte.toUnsignedInt(byte3) << 16);
        value |= (Byte.toUnsignedInt(byte4) << 24);
        
        value |= (((int)(byte2)) << 8);
        value |= (((int)(byte3)) << 16);
        value |= (((int)(byte4)) << 24);
        
        TableCell tableCell = new TableCell(value);
        return new Pair<>(tableCell, Integer.BYTES);
    }
    
    private static Pair<TableCell, Integer> deserializeLong(byte[] data,
                                                            int startIndex) {
        checkTableCellValueFitsInData(data, startIndex, Long.BYTES);
        long value;
        
        byte byte1 = data[startIndex];
        byte byte2 = data[startIndex + 1];
        byte byte3 = data[startIndex + 2];
        byte byte4 = data[startIndex + 3];
        byte byte5 = data[startIndex + 4];
        byte byte6 = data[startIndex + 5];
        byte byte7 = data[startIndex + 6];
        byte byte8 = data[startIndex + 7];
        
        value  =  Byte.toUnsignedLong(byte1);
        value |= (Byte.toUnsignedLong(byte2) << 8);
        value |= (Byte.toUnsignedLong(byte3) << 16);
        value |= (Byte.toUnsignedLong(byte4) << 24);
        value |= (Byte.toUnsignedLong(byte5) << 32);
        value |= (Byte.toUnsignedLong(byte6) << 40);
        value |= (Byte.toUnsignedLong(byte7) << 48);
        value |= (Byte.toUnsignedLong(byte8) << 56);
        
        TableCell tableCell = new TableCell(value);
        return new Pair<>(tableCell, Long.BYTES);
    }
    
    private static Pair<TableCell, Integer> deserializeFloat(byte[] data,
                                                             int startIndex) {
        checkTableCellValueFitsInData(data, startIndex, Float.BYTES);
        
        byte byte1 = data[startIndex];
        byte byte2 = data[startIndex + 1];
        byte byte3 = data[startIndex + 2];
        byte byte4 = data[startIndex + 3];
        
        int floatValueAsInt = 0;
        
        floatValueAsInt  =  Byte.toUnsignedInt(byte1);
        floatValueAsInt |= (Byte.toUnsignedInt(byte2) << 8);
        floatValueAsInt |= (Byte.toUnsignedInt(byte3) << 16);
        floatValueAsInt |= (Byte.toUnsignedInt(byte4) << 24);
        
        float value = Float.intBitsToFloat(floatValueAsInt);
        TableCell tableCell = new TableCell(value);
        return new Pair<>(tableCell, Float.BYTES);
    }
    
    private static Pair<TableCell, Integer> deserializeDouble(byte[] data,
                                                              int startIndex) {
        checkTableCellValueFitsInData(data, startIndex, Double.BYTES);
        
        byte byte1 = data[startIndex];
        byte byte2 = data[startIndex + 1];
        byte byte3 = data[startIndex + 2];
        byte byte4 = data[startIndex + 3];
        byte byte5 = data[startIndex + 4];
        byte byte6 = data[startIndex + 5];
        byte byte7 = data[startIndex + 6];
        byte byte8 = data[startIndex + 7];
        
        long doubleValueAsLong = 0L;
        
        doubleValueAsLong  =  Byte.toUnsignedLong(byte1);
        doubleValueAsLong |= (Byte.toUnsignedLong(byte2) << 8);
        doubleValueAsLong |= (Byte.toUnsignedLong(byte3) << 16);
        doubleValueAsLong |= (Byte.toUnsignedLong(byte4) << 24);
        doubleValueAsLong |= (Byte.toUnsignedLong(byte5) << 32);
        doubleValueAsLong |= (Byte.toUnsignedLong(byte6) << 40);
        doubleValueAsLong |= (Byte.toUnsignedLong(byte7) << 48);
        doubleValueAsLong |= (Byte.toUnsignedLong(byte8) << 56);
        
        double value = Double.longBitsToDouble(doubleValueAsLong);
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
    
    private static Pair<TableCell, Integer> deserializeString(byte[] data,
                                                              int startIndex) {
        // First check that the string length descriptor fits in:
        checkTableCellValueFitsInData(data, startIndex, Integer.BYTES);
        
        // Get the string length:
        int stringLength = getLengthFromByteArray(data, startIndex);
        
        // Now check whether the actual string fits in the data array:
        checkTableCellValueFitsInData(data,
                                      startIndex += Integer.BYTES, 
                                      stringLength * Character.BYTES);
        
        StringBuilder sb = new StringBuilder(stringLength);
        
        for (int charIndex = 0; 
                charIndex != stringLength; 
                charIndex++, startIndex += 2) {
            byte byte1 = data[startIndex];
            byte byte2 = data[startIndex + 1];
            char c = (char)((Byte.toUnsignedInt(byte2) << 8) |
                             Byte.toUnsignedInt(byte1));
            sb.append(c);
        }
        
        TableCell tableCell = new TableCell(sb.toString());
        return new Pair<>(tableCell, 
                          Integer.BYTES + stringLength * Character.BYTES);
    }
        
    private static Pair<TableCell, Integer>
        deserializeBinaryData(byte[] data, int startIndex) {
        // First check that the binary byte array length descriptor fits in:
        checkTableCellValueFitsInData(data, startIndex, Integer.BYTES);
        
        // Get the byte array length:
        int byteArrayLength = getLengthFromByteArray(data, startIndex);
        
        // Now check whether the actual byte array fits in the data array:
        checkTableCellValueFitsInData(data, 
                                      startIndex += Integer.BYTES,
                                      byteArrayLength);
        
        byte[] byteArray = new byte[byteArrayLength];
        TableCell tableCell = new TableCell(byteArray);
        return new Pair<>(tableCell, Integer.BYTES + byteArrayLength);
    }
    
    private static Pair<TableCell, Integer> deserializeBoolean(byte[] data,
                                                               int startIndex) {
        checkTableCellValueFitsInData(data, startIndex, 1);
        boolean value;
        
        switch (data[startIndex]) {
            case BOOLEAN_TRUE:
                value = true;
                break;
                
            case BOOLEAN_FALSE:
                value = false;
                break;
                
            default:
                throw new IllegalStateException(
                        "Unknown boolean literal encoding.");
        }
        
        TableCell tableCell = new TableCell(value);
        return new Pair<>(tableCell, 1);
    }
    
    private static void checkTableCellValueFitsInData(byte[] data,
                                                      int startIndex,
                                                      int cellValueLength) {
        if (startIndex + cellValueLength > data.length) {
            throw new BadDataFormatException(
                    "The data of expected length does not fit in the byte " +
                    "array. Start index: " + startIndex + 
                    ", cell value length: " + cellValueLength + 
                    ", data array length: " + data.length);
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
