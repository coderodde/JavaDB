# JavaDB

JavaDB is a simplistic database management system. It supports cells holding the following data types: `int`, `long`, `float`, `double`, `boolean`, `String`, and `byte[]`.

Just like in conventional databases, a [`Database`](https://github.com/coderodde/JavaDB/blob/master/src/main/java/net/coderodde/javadb/Database.java) is a list of [`Table`](https://github.com/coderodde/JavaDB/blob/master/src/main/java/net/coderodde/javadb/Table.java)s, a [`Table`](https://github.com/coderodde/JavaDB/blob/master/src/main/java/net/coderodde/javadb/Table.java) is a list of [`TableRow`](https://github.com/coderodde/JavaDB/blob/master/src/main/java/net/coderodde/javadb/TableRow.java)s, and a [`TableRow`](https://github.com/coderodde/JavaDB/blob/master/src/main/java/net/coderodde/javadb/TableRow.java) is a list of [`TableCell`](https://github.com/coderodde/JavaDB/blob/master/src/main/java/net/coderodde/javadb/TableCell.java)s.

The `Database` provides a simple API for saving the entire database into a file, and later, reading it into main memory.

The API documentation is [here](http://coderodde.github.io/javadb/).
