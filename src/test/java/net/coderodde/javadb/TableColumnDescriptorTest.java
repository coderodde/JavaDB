package net.coderodde.javadb;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TableColumnDescriptorTest {
     
    @Test
    public void testSerialize() {
        TableColumnDescriptor tableColumnDescriptor = 
                new TableColumnDescriptor("colx",
                                          TableCellType.TYPE_STRING);
        
        ByteBuffer bb = ByteBuffer.allocate(100).order(ByteOrder.LITTLE_ENDIAN);
        tableColumnDescriptor.serialize(bb);
        
        bb.position(0);
        
        assertEquals(4, bb.getInt());
        
        for (char c : "colx".toCharArray()) {
            assertEquals(c, bb.getChar());
        }
        
        int flags = bb.get();
        
        assertTrue((flags & 0b111) == TableCellType.TYPE_STRING.getTypeId());
    }
    
    @Test
    public void testDeserialize() {
        ByteBuffer bb = ByteBuffer.allocate(100).order(ByteOrder.LITTLE_ENDIAN);
        bb.putInt(4);
        
        for (char c : "colx".toCharArray()) {
            bb.putChar(c);
        }
        
        byte flags = 0;
        flags |= (byte) TableCellType.TYPE_STRING.getTypeId();
        bb.put(flags);
        
        bb.position(0);
        
        TableColumnDescriptor tcd = TableColumnDescriptor.deserialize(bb);
        
        assertEquals("colx", tcd.getTableColumnName());
        assertEquals(TableCellType.TYPE_STRING, tcd.getTableCellType());
    }
}
