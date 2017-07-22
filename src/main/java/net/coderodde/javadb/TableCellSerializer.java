package net.coderodde.javadb;

import java.nio.ByteBuffer;

/**
 * This interface defines the API for all table cell serialization routines.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Jul 13, 2017)
 */
public interface TableCellSerializer {

    /**
     * Serializes a value into a byte buffer.
     * 
     * @param byteBuffer the byte buffer to which to append the binary data.
     * @param value      the value to serialize.
     */
    public void serialize(ByteBuffer byteBuffer, Object value);
}
