package net.coderodde.javadb;

/**
 * This enumeration communicates the type stored in a table cell.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Dec 20, 2016)
 */
public enum TableCellType {

    /**
     * 32-bit signed integer.
     */
    TYPE_INT     ((byte) 1, "an integer"),
    
    /**
     * 64-bit signed integer.
     */
    TYPE_LONG    ((byte) 2, "a long integer"),
    
    /**
     * Variable length string.
     */
    TYPE_STRING  ((byte) 3, "a string"),
    
    /**
     * A boolean value.
     */
    TYPE_BOOLEAN ((byte) 4, "a boolean"),
    
    /**
     * Binary data (BLOB).
     */
    TYPE_BINARY  ((byte) 5, "a binary object");
    
    /**
     * The actual type ID identifier.
     */
    private final byte typeId;
    private final String typeName;
    
    private TableCellType(byte typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }
    
    public String getTypeName() {
        return typeName;
    }
    
    public static TableCellType getTableCellType(byte typeId) {
        switch (typeId) {
            case 1:
                return TYPE_INT;
                
            case 2:
                return TYPE_LONG;
                
            case 3:
                return TYPE_STRING;
                
            case 4:
                return TYPE_BOOLEAN;
                
            case 5:
                return TYPE_BINARY;
                
            default:
                throw new IllegalArgumentException(
                        "Unknown type ID: " + typeId);
        }
    }
}
