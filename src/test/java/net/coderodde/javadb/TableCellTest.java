package net.coderodde.javadb;

import java.util.Arrays;
import java.util.List;
import net.coderodde.javadb.Misc.Pair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TableCellTest {
    
    private TableCell tableCell;
    private List<Byte> data;
    private byte[] bytes;
    private Pair<TableCell, Integer> pair;
    
    private static byte[] toArray(List<Byte> byteList) {
        byte[] ret = new byte[byteList.size()];
        
        for (int i = 0; i != ret.length; ++i) {
            ret[i] = byteList.get(i);
        }
        
        return ret;
    }
    
    public TableCellTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

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
        
        tableCell.setTableCellType(TableCellType.TYPE_INT);
        
        assertNotEquals(TableCellType.TYPE_BOOLEAN, 
                        tableCell.getTableCellType());
        
        assertNotEquals(TableCellType.TYPE_DOUBLE, 
                        tableCell.getTableCellType());
        
        assertNotEquals(TableCellType.TYPE_FLOAT, 
                        tableCell.getTableCellType());
        
        assertNotEquals(TableCellType.TYPE_BINARY, 
                        tableCell.getTableCellType());
        
        assertNotEquals(TableCellType.TYPE_LONG, 
                        tableCell.getTableCellType());
        
        assertNotEquals(TableCellType.TYPE_STRING, 
                        tableCell.getTableCellType());
    }

    @Test
    public void testSetTableCellType() {
        tableCell = new TableCell(Integer.valueOf(10));
        tableCell.setTableCellType(TableCellType.TYPE_FLOAT);
        assertNull(tableCell.getFloatValue());
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
        data = tableCell.serialize();
        assertEquals(1, data.size());
        assertEquals((Byte) TableCell.INT_NULL, data.get(0));
    }
    
    @Test
    public void testSerializeNullLong() {
        tableCell = new TableCell(TableCellType.TYPE_LONG);
        data = tableCell.serialize();
        assertEquals(1, data.size());
        assertEquals((Byte) TableCell.LONG_NULL, data.get(0));
    }
    
    @Test
    public void testSerializeNullFloat() {
        tableCell = new TableCell(TableCellType.TYPE_FLOAT);
        data = tableCell.serialize();
        assertEquals(1, data.size());
        assertEquals((Byte) TableCell.FLOAT_NULL, data.get(0));
    }

    @Test
    public void testSerializeNullDouble() {
        tableCell = new TableCell(TableCellType.TYPE_DOUBLE);
        data = tableCell.serialize();
        assertEquals(1, data.size());
        assertEquals((Byte) TableCell.DOUBLE_NULL, data.get(0));
    }
    
    @Test
    public void testSerializeNullString() {
        tableCell = new TableCell(TableCellType.TYPE_STRING);
        data = tableCell.serialize();
        assertEquals(1, data.size());
        assertEquals((Byte) TableCell.STRING_NULL, data.get(0));
    }
    
    @Test
    public void testSerializeNullBoolean() {
        tableCell = new TableCell(TableCellType.TYPE_BOOLEAN);
        data = tableCell.serialize();
        assertEquals(1, data.size());
        assertEquals((Byte) TableCell.BOOLEAN_NULL, data.get(0));
    }
    
    @Test
    public void testSerializeNullBlob() {
        tableCell = new TableCell(TableCellType.TYPE_BINARY);
        data = tableCell.serialize();
        assertEquals(1, data.size());
        assertEquals((Byte) TableCell.BLOB_NULL, data.get(0));
    }
    
    @Test
    public void testDeserializeNullInt() {
        bytes = new byte[]{ 0, TableCell.INT_NULL, 10 };
        pair = TableCell.deserialize(bytes, 1);
        assertNull(pair.first.getIntValue());
    }
    
    @Test
    public void testDeserializeNullLong() {
        bytes = new byte[]{ 0, 0, TableCell.LONG_NULL, 11, 11 };
        pair = TableCell.deserialize(bytes, 2);
        assertNull(pair.first.getLongValue());
    }
    
    @Test
    public void testDeserializeNullFloat() {
        bytes = new byte[]{ TableCell.FLOAT_NULL };
        pair = TableCell.deserialize(bytes, 0);
        assertNull(pair.first.getFloatValue());
    }
    
    @Test
    public void testDeserializeNullDouble() {
        bytes = new byte[]{ TableCell.DOUBLE_NULL, 10 };
        pair = TableCell.deserialize(bytes, 0);
        assertNull(pair.first.getDoubleValue());
    }
    
    @Test
    public void testDeserializeNullString() {
        bytes = new byte[]{ 0, TableCell.STRING_NULL, 10 };
        pair = TableCell.deserialize(bytes, 1);
        assertNull(pair.first.getStringValue());
    }
    
    @Test
    public void testDeserializeNullBoolean() {
        bytes = new byte[]{ TableCell.BOOLEAN_NULL };
        pair = TableCell.deserialize(bytes, 0);
        assertNull(pair.first.getBooleanValue());
    }
    
    @Test
    public void testDeserializeNullBlob() {
        bytes = new byte[]{ 0, 0, 0, TableCell.BLOB_NULL };
        pair = TableCell.deserialize(bytes, 3);
        assertNull(pair.first.getBinaryData());
    }
    
    @Test
    public void testSerializeDeserializeInt() {
        tableCell = new TableCell(100);
        bytes = toArray(tableCell.serialize());
        pair = TableCell.deserialize(bytes, 0);
        assertEquals((Integer) Integer.BYTES, pair.second);
        assertEquals(tableCell.getIntValue(), pair.first.getIntValue());
    }
    
    @Test
    public void testSerializeDeserializeLong() {
        tableCell = new TableCell(200L);
        bytes = toArray(tableCell.serialize());
        pair = TableCell.deserialize(bytes, 0);
        assertEquals((Integer) Long.BYTES, pair.second);
        assertEquals(tableCell.getLongValue(), pair.first.getLongValue());
    }
    
    @Test
    public void testSerializeDeserializeFloat() {
        tableCell = new TableCell(20.0f);
        bytes = toArray(tableCell.serialize());
        pair = TableCell.deserialize(bytes, 0);
        assertEquals((Integer) Float.BYTES, pair.second);
        assertEquals(tableCell.getFloatValue(), pair.first.getFloatValue());
    }
    
    @Test
    public void testSerializeDeserializeDouble() {
        tableCell = new TableCell(10.0);
        bytes = toArray(tableCell.serialize());
        pair = TableCell.deserialize(bytes, 0);
        assertEquals((Integer) Double.BYTES, pair.second);
        assertEquals(tableCell.getDoubleValue(), pair.first.getDoubleValue());
    }
    
    @Test
    public void testSerializeDeserializeString() {
        String fuckYeah = "fuck yeah";
        tableCell = new TableCell(fuckYeah);
        bytes = toArray(tableCell.serialize());
        pair = TableCell.deserialize(bytes, 0);
        assertEquals((Integer)(Integer.BYTES 
                + Character.BYTES * fuckYeah.length()), pair.second);
        assertEquals(tableCell.getStringValue(), pair.first.getStringValue());
    }
 
    @Test
    public void testSerializeDeserializeBoolean() {
        tableCell = new TableCell(true);
        bytes = toArray(tableCell.serialize());
        pair = TableCell.deserialize(bytes, 0);
        assertEquals((Integer) 1, pair.second);
        assertEquals(tableCell.getBooleanValue(), pair.first.getBooleanValue());
    }
    
    @Test
    public void testSerializeDeserializeBinaryData() {
        byte[] shit = { 100, 90, 80, 70, 60 };
        tableCell = new TableCell(shit);
        bytes = toArray(tableCell.serialize());
        pair = TableCell.deserialize(bytes, 0);
        assertEquals((Integer)(Integer.BYTES + shit.length), pair.second);
        assertTrue(Arrays.equals(tableCell.getBinaryData(), 
                                 pair.first.getBinaryData()));
    }
}
