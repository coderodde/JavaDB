package net.coderodde.javadb;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.junit.Test;
import static org.junit.Assert.*;

public class TableTest {

    @Test
    public void testSerializeDeserialize() {
//        Table table = new Table("hello_table");
//        
//        TableColumnDescriptor column1 = 
//                new TableColumnDescriptor("col1",
//                                          TableCellType.TYPE_STRING);
//        
//        TableColumnDescriptor column2 = 
//                new TableColumnDescriptor("col2", 
//                                          TableCellType.TYPE_INT);
//        
//        TableColumnDescriptor column3 = 
//                new TableColumnDescriptor("col3",
//                                          TableCellType.TYPE_BINARY);
//        
//        table.addTableColumnDescriptor(column1);
//        table.addTableColumnDescriptor(column2);
//        table.addTableColumnDescriptor(column3);
//        
//        TableRow row1 = new TableRow(null);
//        TableRow row2 = new TableRow(null);
//        
//        row1.add(0, new TableCell(TableCellType.TYPE_STRING));
//        row1.add(1, new TableCell(123));
//        row1.add(2, new TableCell(new byte[] {4, 3, 2, 1, 0}));
//        
//        row2.add(0, new TableCell("Funky"));
//        row2.add(1, new TableCell(TableCellType.TYPE_INT));
//        row2.add(2, new TableCell(TableCellType.TYPE_BINARY));
//        
//        table.put
//        table.addRow(row1);
//        table.addRow(row2);
//        
//        ByteBuffer byteBuffer =
//                ByteBuffer.allocate(table.getSerializationLength())
//                          .order(ByteOrder.LITTLE_ENDIAN);
//        
//        table.serialize(byteBuffer);
//        
//        byteBuffer.position(0);
//        
//        Table table2 = Table.deserialize(byteBuffer);
//        
//        assertEquals(table, table2);
    }

    @Test
    public void testEquals() {
//        Table table1 = new Table("table1");
//        Table table2 = new Table("table2");
//        
//        assertTrue(table1.equals(table2));
//        assertTrue(table2.equals(table2));
//        assertTrue(table1.equals(table1));
//        assertFalse(table1.equals(null));
//        
//        TableColumnDescriptor column1 = 
//                new TableColumnDescriptor("col1", 
//                                          TableCellType.TYPE_BINARY);
//        
//        TableColumnDescriptor column2 = 
//                new TableColumnDescriptor("col2",
//                                          TableCellType.TYPE_DOUBLE);
//        
//        table1.addTableColumnDescriptor(column1);
//        assertFalse(table1.equals(table2));
//        
//        table2.addTableColumnDescriptor(column1);
//        assertTrue(table1.equals(table2));
//        
//        table1.addTableColumnDescriptor(column2);
//        assertFalse(table1.equals(table2));
//        
//        table2.addTableColumnDescriptor(column2);
//        assertTrue(table1.equals(table2));
//        
//        TableRow row1 = new TableRow();
//        TableRow row2 = new TableRow();
//        
//        row1.add(new TableCell(new byte[] { 2, 9 }));
//        row1.add(new TableCell(45.6));
//        
//        table1.addRow(row1);
//        
//        assertFalse(table1.equals(table2));
//        
//        row2.add(new TableCell(new byte[] { 2, 9 }));
//        row2.add(new TableCell(45.6));
//        
//        table1.addRow(row2);
//        
//        assertFalse(table1.equals(table2));
//        
//        table2.addRow(row1);
//        table2.addRow(row2);
//        
//        assertTrue(table1.equals(table2));
    }    
}
