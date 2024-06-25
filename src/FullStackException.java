/**
 * Name: Arnab Bhowmik
 * ID: 115610923
 * Recitation Section: 1
 */

/**
 * Exception to be thrown when attempting to push an item onto a full stack
 */
public class FullStackException extends Exception {
    /**
     * Constructs a FullStackException with the specified error message
     *
     * @param message The error message describing the exception
     */
    public FullStackException(String message) {
        super(message);
    }
}


