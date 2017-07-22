package net.coderodde.javadb;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.junit.Test;
import static org.junit.Assert.*;

public class TableRowTest {
    
    @Test
    public void testSerialization() {
//        TableCell tableCell1 = new TableCell(TableCellType.TYPE_BINARY);
//        TableCell tableCell2 = new TableCell("Hello!");
//        
//        TableRow row = new TableRow();
//        
//        row.add(tableCell1);
//        row.add(tableCell2);
//        
//        ByteBuffer bb = ByteBuffer.allocate(1000)
//                                  .order(ByteOrder.LITTLE_ENDIAN);
//        
//        row.serialize(bb);
//        
//        bb.position(0);
//        
//        assertEquals(TableCell.BLOB_NULL, bb.get());
//        assertEquals(TableCell.STRING_NOT_NULL, bb.get());
//        assertEquals(6, bb.getInt());
//        
//        for (char c : "Hello!".toCharArray()) {
//            assertEquals(c, bb.getChar());
//        }
    }
    
    @Test
    public void testDeserialization() {
        ByteBuffer bb = ByteBuffer.allocate(100).order(ByteOrder.LITTLE_ENDIAN);
        bb.put(TableCell.BLOB_NULL);
        bb.put(TableCell.STRING_NOT_NULL);
        bb.putInt(6);
        
        for (char c : "Hello!".toCharArray()) {
            bb.putChar(c);
        }
        
        bb.position(0);
        
        TableRow row = TableRow.deserialize(bb, 2);
        
        assertEquals(2, row.getNumberOfCells());
        
        TableCell tc1 = row.get(0);
        TableCell tc2 = row.get(1);
        
        assertEquals(TableCellType.TYPE_BINARY, tc1.getTableCellType());
        assertEquals(TableCellType.TYPE_STRING, tc2.getTableCellType());
        
        assertNull(tc1.getValue());
        assertEquals("Hello!", tc2.getStringValue());
    }
    
    @Test
    public void testEquals() {
//        TableCell tableCell1 = new TableCell(12321);
//        TableCell tableCell2 = new TableCell("hello");
//        TableCell tableCell3 = new TableCell(new byte[] { 1, 5, 8 });
//        TableCell tableCell3a = new TableCell(4.5f);
//        
//        TableRow tableRow1 = new TableRow();
//        TableRow tableRow2 = new TableRow();
//        
//        assertTrue(tableRow1.equals(tableRow2));
//        assertTrue(tableRow1.equals(tableRow1));
//        assertFalse(tableRow1.equals(null));
//        
//        tableRow1.add(0, tableCell1);
//        tableRow1.add(1, tableCell2);
//        
//        tableRow2.add(0, tableCell1);
//        tableRow2.add(1, tableCell2);
//        
//        assertTrue(tableRow1.equals(tableRow2));
//        
//        tableRow1.add(2, tableCell3);
//        
//        assertFalse(tableRow1.equals(tableRow2));
//        
//        tableRow2.add(2, tableCell3a);
//
//        assertFalse(tableRow1.equals(tableRow2));
//        
//        tableRow2.set(2, new TableCell(new byte[] {1, 5}));
//        
//        assertFalse(tableRow1.equals(tableRow2));
//    
//        tableRow2.set(2, new TableCell(new byte[] { 1, 5, 9 }));
//        
//        assertFalse(tableRow1.equals(tableRow2));
//    
//        tableRow2.set(2, new TableCell(new byte[] { 1, 5, 8 }));
//        assertTrue(tableRow1.equals(tableRow2));
    }
}
