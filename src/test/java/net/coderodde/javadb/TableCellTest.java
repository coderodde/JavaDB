package net.coderodde.javadb;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TableCellTest {
    
    private TableCell tableCell;
    
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

    @Test
    public void testGetFloatValue() {
        
    }

    @Test
    public void testGetDoubleValue() {
        
    }

    @Test
    public void testGetStringValue() {
        
    }

    @Test
    public void testGetBooleanValue() {
        
    }

    @Test
    public void testGetBinaryData() {
        
    }

    @Test
    public void testSetIntValue() {
        
    }

    @Test
    public void testSetLongValue() {
        
    }

    @Test
    public void testSetFloatValue() {
        
    }

    @Test
    public void testSetDoubleValue() {
        
    }

    @Test
    public void testSetStringValue() {
        
    }

    @Test
    public void testSetBooleanValue() {
        
    }

    @Test
    public void testSetBinaryData() {

    }

    @Test
    public void testNullify() {
        
    }

    @Test
    public void testSerialize() {
        
    }

    @Test
    public void testDeserialize() {
        
    }
}
