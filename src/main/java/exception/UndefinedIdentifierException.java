package exception;

/**
 * Thrown to indicate that an identifier is undefined.
 *
 * @author Nhan Huynh
 */
public class UndefinedIdentifierException extends RuntimeException {

    /**
     * Constructs and initializes an undefined identifier exception with no detail message.
     */
    public UndefinedIdentifierException(String message) {
        super(message);
    }
}
