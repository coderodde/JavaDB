package net.coderodde.javadb;

import java.nio.ByteBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

public class DatabaseTest {
    
    @Test
    public void testSerializeDeserialize() {
        Database db = new Database("hello_db");
        
        TableColumnDescriptor cola1 = 
                new TableColumnDescriptor("cola1", TableCellType.TYPE_BINARY);
        
        TableColumnDescriptor cola2 = 
                new TableColumnDescriptor("cola2", TableCellType.TYPE_BOOLEAN);
        
        TableColumnDescriptor colb1 = 
                new TableColumnDescriptor("colb1", TableCellType.TYPE_FLOAT);
        
        TableColumnDescriptor colb2 = 
                new TableColumnDescriptor("colb2", TableCellType.TYPE_STRING);
        
        TableColumnDescriptor colb3 = 
                new TableColumnDescriptor("colb3", TableCellType.TYPE_LONG);
        
        Table tablea = db.createTable("tablea", cola1, cola2);
        Table tableb = db.createTable("tableb", colb1, colb2, colb3);
        
        TableRow rowa1 = tablea.putTableRow(new byte[] { 1, 2, 3}, true);
        TableRow rowa2 = tablea.putTableRow(null, false);
        
        TableRow rowb1 = tableb.putTableRow(1.0f, "hello", 100L);
        TableRow rowb2 = tableb.putTableRow(1.1f, "funky", 101L);
        TableRow rowb3 = tableb.putTableRow(1.2f, "you", 102L);
        
        ByteBuffer byteBuffer = db.serialize();
        byteBuffer.position(0);
        
        Database db2 = Database.deserialize(byteBuffer);
        
        assertEquals(db, db2);
        
        tableb.removeTableColumnDescriptor("colb2");
        rowb2.get("colb3");
        ByteBuffer bb2 = db.serialize();
        bb2.position(0);
        db2 = Database.deserialize(bb2);
        
        assertEquals(db, db2);
        assertEquals(2, rowb1.getNumberOfCells());
        assertEquals(2, rowb2.getNumberOfCells());
        assertEquals(2, rowb3.getNumberOfCells());
    }
    
    @Test
    public void test1() {
        Database db = new Database("hello_db");
        assertEquals("hello_db", db.getDatabaseName());
        
        TableColumnDescriptor col1 = 
                new TableColumnDescriptor("col1", TableCellType.TYPE_STRING);
        
        TableColumnDescriptor col2 = 
                new TableColumnDescriptor("col2", TableCellType.TYPE_LONG);
        
        db.createTable("table1", col1, col2);
        
        assertEquals("table1", db.getTable("table1").getTableName());
        
        Table table = db.getTable("table1");
        TableRow row1 = table.putTableRow("Yeah", 12L);
        TableRow row2 = table.putTableRowAt(0, "Ok", 26L);
        
        assertEquals("Yeah", row1.get("col1").getStringValue());
        assertEquals((Long) 12L, row1.get("col2").getLongValue());
        
        assertEquals("Ok", row2.get("col1").getStringValue());
        assertEquals((Long) 26L, row2.get("col2").getLongValue());
        
        table.addTableColumnDescriptor(
                new TableColumnDescriptor("col3", TableCellType.TYPE_BINARY));
        
        assertEquals(3, row1.getNumberOfCells());
        assertEquals(3, row2.getNumberOfCells());
        
        assertNull(row1.get(2).getBinaryData());
        assertNull(row2.get(2).getBinaryData());
        
        assertNull(row1.get("col3").getBinaryData());
        assertNull(row2.get("col3").getBinaryData());
        
        table.removeTableColumnDescriptor("col2");
        
        assertEquals(2, row1.getNumberOfCells());
        assertEquals(2, row2.getNumberOfCells());
        
        assertFalse(table.containsTableColumnDescriptor("col2"));
        assertTrue(table.containsTableColumnDescriptor("col1"));
        assertTrue(table.containsTableColumnDescriptor("col3"));
        
        assertNull(row1.get(1).getValue());
        assertNull(row2.get(1).getValue());
        
        assertEquals("Yeah", row1.get(0).getStringValue());
        assertEquals("Ok", row2.get(0).getStringValue());
        
        assertEquals("Yeah", row1.get("col1").getStringValue());
        assertEquals("Ok", row2.get("col1").getStringValue());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTableGetThrowsOnNonExistentTable() {
        Database db = new Database("hello_db");
        db.createTable("fds"); // No columns here!
    }
    
    @Test(expected = NullPointerException.class)
    public void testTableGetThrowsOnNullColumnDescriptor() {
        Database db = new Database("hello_db");
        
        db.createTable("fds", 
                new TableColumnDescriptor("col1", TableCellType.TYPE_INT),
                null,
                new TableColumnDescriptor("col3", TableCellType.TYPE_FLOAT));
    }
}
