/**
 * Name: Arnab Bhowmik
 * ID: 115610923
 * Recitation Section: 1
 */

/**
 * Exception to be thrown when attempting to pop or peek an empty stack
 */
public class EmptyStackException extends Exception {

    /**
     * Constructs an EmptyStackException with the specified error message
     *
     * @param message The error message describing the exception
     */
    public EmptyStackException(String message) {
        super(message);
    }
}

