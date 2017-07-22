package net.coderodde.javadb;

import java.nio.ByteBuffer;

public interface TableCellDeserializer {

    /**
     * Deserializes a particular table cell.
     * 
     * @param byteBuffer the buffer of bytes.
     * 
     * @return the table cell.
     */
    public TableCell deserialize(ByteBuffer byteBuffer);
}
