package exception;

/**
 * Thrown to indicate that an operator is undefined.
 *
 * @author Nhan Huynh
 */
public class UndefinedOperatorException extends RuntimeException {

    /**
     * Constructs and initializes an undefined operator exception with no detail message.
     */
    public UndefinedOperatorException(String message) {
        super(message);
    }
}
