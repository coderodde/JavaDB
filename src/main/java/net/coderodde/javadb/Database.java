package net.coderodde.javadb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class Database {

    private static final int SIZE_BYTES = Integer.BYTES;
    
    /**
     * The name of this database.
     */
    private String databaseName;
    
    /**
     * The map mapping the table name to the actual table.
     */
    private final Map<String, Table> tableMap = new LinkedHashMap<>();
    
    /**
     * The file this database was built from.
     */
    private File file;
    
    public Database(String databaseName) {
        this.databaseName = 
                Objects.requireNonNull(
                        databaseName, 
                        "The database name is null.");
    }
    
    public String getDatabaseName() {
        return databaseName;
    }
    
    public void setDatabaseName(String databaseName) {
        this.databaseName =
                Objects.requireNonNull(
                        databaseName, "The new database name is null.");
    }
    
    /**
     * Creates a new empty table with given columns.
     * 
     * @param tableName              the table name.
     * @param tableColumnDescriptors the column descriptors.
     */
    public Table createTable(String tableName,
                            TableColumnDescriptor... tableColumnDescriptors) {
        Objects.requireNonNull(tableName, "The table name is null");
        checkTableNameNotEmpty(tableName);
        checkTableNameNotOccupied(tableName);
        checkTableColumnDescriptors(tableColumnDescriptors);
        Table table = new Table(tableName);
        
        for (TableColumnDescriptor tableColumnDescriptor 
                : tableColumnDescriptors) {
            Objects.requireNonNull(tableColumnDescriptor,
                                   "The table column descriptor is null.");
            
            table.addTableColumnDescriptor(tableColumnDescriptor);
        }
        
        tableMap.put(tableName, table);
        return table;
    }
    
    public Table getTable(String tableName) {
        Objects.requireNonNull(tableName, "The input table name is null.");
        checkTableIsInThisDatabase(tableName);
        return tableMap.get(tableName);
    }
    
    public void deleteTable(String tableName) {
        Objects.requireNonNull(tableName, "The input table name is null.");
        checkTableIsInThisDatabase(tableName);
        tableMap.remove(tableName);
    }
    
    public void save() {
        if (file == null) {
            throw new IllegalStateException(
                    "This database was not previously saved.");
        }
        
        save(file);
    }
    
    public void save(File file) {
        ByteBuffer bb = serialize();
        byte[] bytes = bb.array();
        // Write the bytes.
        
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
        } catch (Exception ex) {
            throw new RuntimeException("Saving the databse \"" + databaseName +
                    "\" failed.", ex);
        } 
    }
    
    public void save(String path) {
        save(new File(path));
    }
    
    public static Database read(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[fis.available()];
            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length)
                                              .order(ByteOrder.LITTLE_ENDIAN);
            fis.read(bytes);
            byteBuffer.put(bytes);
            byteBuffer.position(0);
            return Database.deserialize(byteBuffer);
        } catch (Exception ex) {
            throw new RuntimeException("Reading the database from file \"" +
                    file.getAbsolutePath() + "\" failed.", ex);
        }
    }
    
    public static Database read(String path) {
        return read(new File(path));
    }
    
    public ByteBuffer serialize() {
        ByteBuffer byteBuffer = 
                ByteBuffer.allocate(getSerializationLength())
                          .order(ByteOrder.LITTLE_ENDIAN);
        
        // Output the database name length:
        byteBuffer.putInt(databaseName.length());
        
        // Output the database name:
        for (char c : databaseName.toCharArray()) {
            byteBuffer.putChar(c);
        }
        
        // Output the number of tables:
        byteBuffer.putInt(tableMap.size());
        
        for (Table table : tableMap.values()) {
            table.serialize(byteBuffer);
        }
        
        return byteBuffer;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        
        if (o == this) {
            return true;
        }
        
        if (!getClass().equals(o.getClass())) {
            return false;
        }
        
        Database other = (Database) o;
        
        if (!databaseName.equals(other.databaseName)) {
            return false;
        }
        
        return tableMap.equals(other.tableMap);
    }
    
    public static Database deserialize(ByteBuffer byteBuffer) {
        int databaseNameLength = byteBuffer.getInt();
        StringBuilder sb = new StringBuilder(databaseNameLength);
        
        for (int i = 0; i < databaseNameLength; ++i) {
            sb.append(byteBuffer.getChar());
        }
        
        String databaseName = sb.toString();
        Database database = new Database(databaseName);
        
        int numberOfTables = byteBuffer.getInt();
        
        for (int i = 0; i < numberOfTables; ++i) {
            Table table = Table.deserialize(byteBuffer);
            database.tableMap.put(table.getTableName(), table);
        }
        
        return database;
    }
    
    void onTableRename(Table table, String oldTableName, String newTableName) {
        tableMap.remove(oldTableName);
        tableMap.put(newTableName, table);
    }
    
    private int getSerializationLength() {
        int serializationLength = SIZE_BYTES; // Database name length.
        
        // Database name.
        serializationLength += databaseName.length() * Character.BYTES;
        serializationLength += SIZE_BYTES; // Table count.
        
        // Tables.
        for (Table table : tableMap.values()) {
            serializationLength += table.getSerializationLength();
        }
        
        return serializationLength;
    }
    
    private void checkTableNameNotOccupied(String tableName) {
        if (tableMap.containsKey(tableName)) {
            throw new IllegalStateException(
                    "Table name \"" + tableName + "\" is already occupied.");
        }
    }
    
    private void checkTableIsInThisDatabase(String tableName) {
        if (!tableMap.containsKey(tableName)) {
            throw new IllegalStateException(
                    "The table with name \"" + tableName + "\" is not in " +
                    "this database.");
        }
    }
    
    private void checkTableNameNotEmpty(String tableName) {
        if (tableName.isEmpty()) {
            throw new IllegalArgumentException("The table name is empty.");
        }
    }

    private void checkTableColumnDescriptors(
            TableColumnDescriptor[] tableColumnDescriptors) {
        if (tableColumnDescriptors.length == 0) {
            throw new IllegalArgumentException("No column descriptors given.");
        }
        
        for (int i = 0; i < tableColumnDescriptors.length; ++i) {
            Objects.requireNonNull(tableColumnDescriptors[i],
                    "The " + i + "th table column descriptor is null.");
        }
    }
    
    public static void main(String[] args) {
        Database db = new Database("my_db");
        
        TableColumnDescriptor table1Id =
                new TableColumnDescriptor("id", TableCellType.TYPE_INT);
        
        TableColumnDescriptor table1First =
                new TableColumnDescriptor("first_name", 
                                          TableCellType.TYPE_STRING);
        
        TableColumnDescriptor table1Last =
                new TableColumnDescriptor("last_name", 
                                          TableCellType.TYPE_STRING);
        
        Table table1 = db.createTable("person", 
                                      table1Id, 
                                      table1First, 
                                      table1Last);
        
        TableColumnDescriptor table2Id =
                new TableColumnDescriptor("id", TableCellType.TYPE_INT);
        
        TableColumnDescriptor table2PersonId =
                new TableColumnDescriptor("person_id", 
                                          TableCellType.TYPE_LONG);
        
        TableColumnDescriptor table2Msg =
                new TableColumnDescriptor("msg", 
                                          TableCellType.TYPE_STRING);
        
        Table table2 = db.createTable("msg", 
                                      table2Id, 
                                      table2PersonId, 
                                      table2Msg);
        
        table1.putTableRow(1, "Rodion", "Efremov");
        table1.putTableRow(2, "Violetta", "Ervasti");
        
        table2.putTableRow(10, 1L, "Hello!");
        table2.putTableRow(11, 2L, "Bye!");
        
        db.save("my_db.dat");
        
        Database db2 = Database.read("my_db.dat");
        
        Table table1copy = db2.getTable("person");
        Table table2copy = db2.getTable("msg");
        
        TableView view1 = table1copy.createTableView(table1Id,
                                                     table1First, 
                                                     table1Last);
        
        TableView view2 = table2copy.createTableView(table2Id,
                                                     table2PersonId,
                                                     table2Msg);
        
        view1.addTableRow(table1copy.getTableRow(0));
        view1.addTableRow(table1copy.getTableRow(1));
        
        view2.addTableRow(table2copy.getTableRow(0));
        view2.addTableRow(table2copy.getTableRow(1));
        
        System.out.println(view1.toString());
        System.out.println(view2);
    }
}
