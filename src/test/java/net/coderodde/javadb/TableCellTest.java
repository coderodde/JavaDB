package net.coderodde.javadb;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import static net.coderodde.javadb.TableCellType.*;
import static net.coderodde.javadb.TableCell.*;

public class TableCellTest {
    
    private TableCell tableCell;
    
    @Test
    public void testGetTableCellType() {
        tableCell = new TableCell(TableCellType.TYPE_BINARY);
        
        assertEquals(TableCellType.TYPE_BINARY, tableCell.getTableCellType());
        
        assertNotEquals(TableCellType.TYPE_BOOLEAN, 
                        tableCell.getTableCellType());
        
        assertNotEquals(TableCellType.TYPE_DOUBLE, 
                        tableCell.getTableCellType());
        
        assertNotEquals(TableCellType.TYPE_FLOAT, 
                        tableCell.getTableCellType());
        
        assertNotEquals(TableCellType.TYPE_INT, 
                        tableCell.getTableCellType());
        
        assertNotEquals(TableCellType.TYPE_LONG, 
                        tableCell.getTableCellType());
        
        assertNotEquals(TableCellType.TYPE_STRING, 
                        tableCell.getTableCellType());
        
        assertNotEquals(TableCellType.TYPE_BOOLEAN, 
                        tableCell.getTableCellType());
        
        assertNotEquals(TableCellType.TYPE_DOUBLE, 
                        tableCell.getTableCellType());
        
        assertNotEquals(TableCellType.TYPE_FLOAT, 
                        tableCell.getTableCellType());
        
        assertEquals(TableCellType.TYPE_BINARY, 
                        tableCell.getTableCellType());
        
        assertNotEquals(TableCellType.TYPE_LONG, 
                        tableCell.getTableCellType());
        
        assertNotEquals(TableCellType.TYPE_STRING, 
                        tableCell.getTableCellType());
    }

    @Test
    public void testGetIntValue() {
        tableCell = new TableCell(11);
        assertEquals(Integer.valueOf(11), tableCell.getIntValue());
        tableCell.setIntValue(12);
        assertEquals(Integer.valueOf(12), tableCell.getIntValue());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetIntValueThrowsOnWrongType() {
        tableCell = new TableCell(11);
        tableCell.getBooleanValue();
    }

    @Test
    public void testGetLongValue() {
        tableCell = new TableCell(111L);
        assertEquals(Long.valueOf(111L), tableCell.getLongValue());
        tableCell.setLongValue(123L);
        assertEquals(Long.valueOf(123L), tableCell.getLongValue());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetLongValueThrowsOnWrongType() {
        tableCell = new TableCell(11L);
        tableCell.getIntValue();
    }
    
    @Test
    public void testGetFloatValue() {
        tableCell = new TableCell(1.0f);
        assertEquals(Float.valueOf(1.0f), tableCell.getFloatValue());
        tableCell.setFloatValue(2.0f);
        assertEquals(Float.valueOf(2.0f), tableCell.getFloatValue());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetFloatValueThrowsOnWrongType() {
        tableCell = new TableCell(1.0f);
        tableCell.getDoubleValue();
    }

    @Test
    public void testGetDoubleValue() {
        tableCell = new TableCell(1.0);
        assertEquals(Double.valueOf(1.0), tableCell.getDoubleValue());
        tableCell.setDoubleValue(2.0);
        assertEquals(Double.valueOf(2.0), tableCell.getDoubleValue());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetDoubleValueThrowsOnWrongType() {
        tableCell = new TableCell(1.0);
        tableCell.getFloatValue();
    }

    @Test
    public void testGetStringValue() {
        tableCell = new TableCell("hello");
        assertEquals("hello", tableCell.getStringValue());
        tableCell.setStringValue("world");
        assertEquals("world", tableCell.getStringValue());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetStringValueThrowsOnWrongType() {
        tableCell = new TableCell("yeah");
        tableCell.getBinaryData();
    }

    @Test
    public void testGetBooleanValue() {
        tableCell = new TableCell(false);
        assertEquals(Boolean.FALSE, tableCell.getBooleanValue());
        tableCell.setBooleanValue(true);
        assertEquals(Boolean.TRUE, tableCell.getBooleanValue());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetBooleanValueThrowsOnWrongType() {
        tableCell = new TableCell(true);
        tableCell.getIntValue();
    }

    @Test
    public void testGetBinaryData() {
        tableCell = new TableCell(new byte[]{ 4, 5 });
        assertTrue(Arrays.equals(new byte[]{ 4, 5 }, 
                   tableCell.getBinaryData()));
        tableCell.setBinaryData(new byte[]{ 1, 2, 3 });
        assertTrue(Arrays.equals(new byte[]{ 1, 2, 3 }, 
                   tableCell.getBinaryData()));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testGetBinaryDataThrowsOnWrongType() {
        tableCell = new TableCell(new byte[]{ 2, 3, 4 });
        tableCell.getBooleanValue();
    }
    
    @Test(expected = IllegalStateException.class)
    public void throwsOnWrongSetInt() {
        tableCell = new TableCell(1);
        tableCell.setLongValue(2L);
    }
    
    @Test(expected = IllegalStateException.class)
    public void throwsOnWrongSetLong() {
        tableCell = new TableCell(1L);
        tableCell.setIntValue(2);
    }
    
    @Test(expected = IllegalStateException.class)
    public void throwsOnWrongSetFloat() {
        tableCell = new TableCell(1.0f);
        tableCell.setDoubleValue(2.0);
    }
    
    @Test(expected = IllegalStateException.class)
    public void throwsOnWrongSetDouble() {
        tableCell = new TableCell(1.0);
        tableCell.setFloatValue(2.0f);
    }
    
    @Test(expected = IllegalStateException.class)
    public void throwsOnWrongSetString() {
        tableCell = new TableCell("yeah");
        tableCell.setBinaryData(new byte[]{});
    }
    
    @Test(expected = IllegalStateException.class)
    public void throwsOnWrongSetByteData() {
        tableCell = new TableCell(new byte[]{});
        tableCell.setStringValue("yeah");
    }
    
    @Test(expected = IllegalStateException.class)
    public void throwsOnWrongSetBoolean() {
        tableCell = new TableCell(true);
        tableCell.setIntValue(1);
    }

    @Test
    public void testNullify() {
        tableCell = new TableCell(10);
        assertEquals(Integer.valueOf(10), tableCell.getIntValue());
        tableCell.nullify();
        assertNull(tableCell.getIntValue());
    }

    @Test
    public void testSerializeNullInt() {
        tableCell = new TableCell(TableCellType.TYPE_INT);
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.put((byte) 10);
        tableCell.serialize(bb);
        
        bb.position(0);
        
        assertEquals((byte) 10, bb.get());
        assertEquals(INT_NULL, bb.get());
    }
    
    @Test
    public void testSerializeNullLong() {
        tableCell = new TableCell(TableCellType.TYPE_LONG);
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.put((byte) 11);
        tableCell.serialize(bb);
        assertEquals((byte) 11, bb.get(0));
        assertEquals(LONG_NULL, bb.get(1));
    }
    
    @Test
    public void testSerializeNullFloat() {
        tableCell = new TableCell(TableCellType.TYPE_FLOAT);
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.put((byte) 12);
        tableCell.serialize(bb);

        bb.position(0);
        
        assertEquals((byte) 12, bb.get());
        assertEquals(FLOAT_NULL, bb.get());
    }

    @Test
    public void testSerializeNullDouble() {
        tableCell = new TableCell(TableCellType.TYPE_DOUBLE);
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.put((byte) 13);
        
        
        tableCell.serialize(bb);
        bb.position(0);
        
        assertEquals((byte) 13, bb.get());
        assertEquals(DOUBLE_NULL, bb.get());
    }
    
    @Test
    public void testSerializeNullString() {
        tableCell = new TableCell(TableCellType.TYPE_STRING);
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.put((byte) 14);
        tableCell.serialize(bb);
        
        bb.position(0);
        
        assertEquals((byte) 14, bb.get());
        assertEquals(STRING_NULL, bb.get());
    }
    
    @Test
    public void testSerializeNullBoolean() {
        tableCell = new TableCell(TableCellType.TYPE_BOOLEAN);
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.put((byte) 15);
        tableCell.serialize(bb);
        bb.position(0);
        assertEquals((byte) 15, bb.get());
        assertEquals(BOOLEAN_NULL, bb.get());
    }
    
    @Test
    public void testSerializeNullBlob() {
        tableCell = new TableCell(TableCellType.TYPE_BINARY);
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.put((byte) 16);
        tableCell.serialize(bb);
        assertEquals((byte) 16, bb.get(0));
        assertEquals(BLOB_NULL, bb.get(1));
    }
    
    @Test
    public void testDeserializeNullInt() {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.put((byte) 20);
        bb.put(INT_NULL);
        
        bb.position(1);
        
        TableCell tableCell = TableCell.deserialize(bb);
        assertNull(tableCell.getValue());
        assertEquals(TYPE_INT, tableCell.getTableCellType());
    }
    
    @Test
    public void testDeserializeNullLong() {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.put((byte) 21);
        bb.put(LONG_NULL);
        
        bb.position(1);
        
        TableCell tableCell = TableCell.deserialize(bb);
        assertNull(tableCell.getValue());
        assertEquals(TYPE_LONG, tableCell.getTableCellType());
    }
    
    @Test
    public void testDeserializeNullFloat() {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.put((byte) 22);
        bb.put(FLOAT_NULL);
        
        bb.position(1);
        
        TableCell tableCell = TableCell.deserialize(bb);
        assertNull(tableCell.getValue());
        assertEquals(TYPE_FLOAT, tableCell.getTableCellType());
    }
    
    @Test
    public void testDeserializeNullDouble() {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.put((byte) 23);
        bb.put(DOUBLE_NULL);
        
        bb.position(1);
        
        TableCell tableCell = TableCell.deserialize(bb);
        assertNull(tableCell.getValue());
        assertEquals(TYPE_DOUBLE, tableCell.getTableCellType());
    }
    
    @Test
    public void testDeserializeNullString() {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.put((byte) 24);
        bb.put(STRING_NULL);
        
        bb.position(1);
        
        TableCell tableCell = TableCell.deserialize(bb);
        assertNull(tableCell.getValue());
        assertEquals(TYPE_STRING, tableCell.getTableCellType());
    }
    
    @Test
    public void testDeserializeNullBoolean() {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.put((byte) 25);
        bb.put(BOOLEAN_NULL);
        
        bb.position(1);
        
        TableCell tableCell = TableCell.deserialize(bb);
        assertNull(tableCell.getValue());
        assertEquals(TYPE_BOOLEAN, tableCell.getTableCellType());
    }
    
    @Test
    public void testDeserializeNullBlob() {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.put((byte) 26);
        bb.put(BLOB_NULL);
        
        bb.position(1);
        
        TableCell tableCell = TableCell.deserialize(bb);
        assertNull(tableCell.getValue());
        assertEquals(TYPE_BINARY, tableCell.getTableCellType());
    }
    
    @Test
    public void testSerializeInt() {
        tableCell = new TableCell(100);
        ByteBuffer bb = ByteBuffer.allocate(1000)
                                  .order(ByteOrder.LITTLE_ENDIAN);
        tableCell.serialize(bb);
        
        assertEquals(INT_NOT_NULL, bb.get(0));
        assertEquals(100, bb.getInt(1));
    }
    
    @Test
    public void testDeserializeInt() {
        ByteBuffer bb = ByteBuffer.allocate(10);
        bb.put(0, INT_NOT_NULL);
        bb.putInt(1, 121);
        
        TableCell tc = TableCell.deserialize(bb);
        assertEquals(TYPE_INT, tc.getTableCellType());
        assertEquals((Integer) 121, tc.getIntValue());
    }
    
    @Test
    public void testSerializeLong() {
        tableCell = new TableCell(376L);
        ByteBuffer bb = ByteBuffer.allocate(1000)
                                  .order(ByteOrder.LITTLE_ENDIAN);
        tableCell.serialize(bb);
        
        assertEquals(LONG_NOT_NULL, bb.get(0));
        assertEquals(376L, bb.getLong(1));
    }
    
    @Test
    public void testDeserializeLong() {
        ByteBuffer bb = ByteBuffer.allocate(10);
        bb.put(0, LONG_NOT_NULL);
        bb.putLong(1, 376L);
        
        TableCell tc = TableCell.deserialize(bb);
        assertEquals(TYPE_LONG, tc.getTableCellType());
        assertEquals((Long) 376L, tc.getLongValue());
    }
    
    @Test
    public void testSerializeFloat() {
        tableCell = new TableCell(3.14f);
        ByteBuffer bb = ByteBuffer.allocate(1000)
                                  .order(ByteOrder.LITTLE_ENDIAN);
        tableCell.serialize(bb);
        
        assertEquals(FLOAT_NOT_NULL, bb.get(0));
        assertEquals(3.14f, bb.getFloat(1), 0.0f);
    }
    
    @Test
    public void testDeserializeFloat() {
        ByteBuffer bb = ByteBuffer.allocate(10);
        bb.put(0, FLOAT_NOT_NULL);
        bb.putFloat(1, 3.14f);
        
        TableCell tc = TableCell.deserialize(bb);
        assertEquals(TYPE_FLOAT, tc.getTableCellType());
        assertEquals((Float) 3.14f, tc.getFloatValue());
    }
    
    @Test
    public void testSerializeDouble() {
        tableCell = new TableCell(1.618);
        ByteBuffer bb = ByteBuffer.allocate(1000)
                                  .order(ByteOrder.LITTLE_ENDIAN);
        tableCell.serialize(bb);
        
        assertEquals(DOUBLE_NOT_NULL, bb.get(0));
        assertEquals(1.618, bb.getDouble(1), 0.0);
    }
    
    @Test
    public void testDeserializeDouble() {
        ByteBuffer bb = ByteBuffer.allocate(10);
        bb.put(0, DOUBLE_NOT_NULL);
        bb.putDouble(1, 1.618);
        
        TableCell tc = TableCell.deserialize(bb);
        assertEquals(TYPE_DOUBLE, tc.getTableCellType());
        assertEquals((Double) 1.618, tc.getDoubleValue());
    }
    
    @Test
    public void testSerializeBoolean() {
        tableCell = new TableCell(false);
        ByteBuffer bb = ByteBuffer.allocate(1000)
                                  .order(ByteOrder.LITTLE_ENDIAN);
        tableCell.serialize(bb);
        
        assertEquals(BOOLEAN_NOT_NULL, bb.get(0));
        assertEquals(BOOLEAN_FALSE, bb.get(1));
    }
    
    @Test
    public void testDeserializeBoolean() {
        ByteBuffer bb = ByteBuffer.allocate(10).order(ByteOrder.LITTLE_ENDIAN);
        bb.put(0, BOOLEAN_NOT_NULL);
        bb.put(1, BOOLEAN_TRUE);
        
        TableCell tc = TableCell.deserialize(bb);
        assertEquals(TYPE_BOOLEAN, tc.getTableCellType());
        assertEquals((Boolean) true, tc.getBooleanValue());
    }
    
    @Test
    public void testSerializationString() {
        tableCell = new TableCell("Hello!");
        ByteBuffer bb = ByteBuffer.allocate(50).order(ByteOrder.LITTLE_ENDIAN);
        
        tableCell.serialize(bb);
        bb.position(0);
        
        assertEquals(STRING_NOT_NULL, bb.get());
        assertEquals("Hello!".length(), bb.getInt());
        
        for (char c : "Hello!".toCharArray()) {
            assertEquals(c, bb.getChar());
        }
    }
    
    @Test
    public void testDeserializationString() {
        ByteBuffer bb = ByteBuffer.allocate(50).order(ByteOrder.LITTLE_ENDIAN);
        bb.put(STRING_NOT_NULL);
        bb.putInt(4);
        
        for (char c : "funk".toCharArray()) {
            bb.putChar(c);
        }
        
        bb.position(0);
        
        TableCell tc = TableCell.deserialize(bb);
        assertEquals(TYPE_STRING, tc.getTableCellType());
        assertEquals("funk", tc.getStringValue());
    }
    
    @Test
    public void testSerializationBinary() {
        tableCell = new TableCell(new byte[] { 2, 6, 9 });
        ByteBuffer bb = ByteBuffer.allocate(50).order(ByteOrder.LITTLE_ENDIAN);
        
        tableCell.serialize(bb);
        bb.position(0);
        
        assertEquals(BLOB_NOT_NULL, bb.get());
        assertEquals(3, bb.getInt());
        assertEquals((byte) 2, bb.get());
        assertEquals((byte) 6, bb.get());
        assertEquals((byte) 9, bb.get());
    }
    
    @Test
    public void testDeserializationBinary() {
        ByteBuffer bb = ByteBuffer.allocate(50).order(ByteOrder.LITTLE_ENDIAN);
        bb.put(BLOB_NOT_NULL);
        bb.putInt(3);
        bb.put((byte) 2);
        bb.put((byte) 6);
        bb.put((byte) 9);
        
        bb.position(0);
        
        TableCell tc = TableCell.deserialize(bb);
        assertEquals(TYPE_BINARY, tc.getTableCellType());
        assertTrue(Arrays.equals(new byte[] {2, 6, 9}, tc.getBinaryData()));
    }
}
