package exception;

/**
 * Thrown to indicate that an identifier is illegal to use.
 *
 * @author Nhan Huynh
 */
public class IllegalIdentifierExceptions extends RuntimeException {

    /**
     * Constructs and initializes an illegal identifier exception with the given identifier as
     * detail message.
     */
    public IllegalIdentifierExceptions(String identifier) {
        super(identifier);
    }
}
