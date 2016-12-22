package net.coderodde.javadb;

/**
 * This class implements an exception which is thrown whenever deserializing a 
 * database from a corrupt data stream.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Dec 22, 2016)
 */
public final class BadDataFormatException extends RuntimeException {

    public BadDataFormatException(String message) {
        super(message);
    }
}
