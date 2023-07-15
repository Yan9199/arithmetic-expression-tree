package exception;

/**
 * Thrown to indicate that parentheses are mismatched.
 *
 * @author Nhan Huynh
 */
public class ParenthesesMismatchException extends RuntimeException {

    /**
     * Constructs and initializes a parentheses mismatch exception  with no detail message.
     */
    public ParenthesesMismatchException() {
        super();
    }
}
