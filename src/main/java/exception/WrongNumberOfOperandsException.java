package exception;

/**
 * Thrown to indicate that the number of operands provided for an operation is incorrect.
 *
 * @author Nhan Huynh
 */
public class WrongNumberOfOperandsException extends RuntimeException {

    /**
     * Constructs and initializes a wrong number of operands exception with the given inputs as its
     * detail message.
     *
     * @param actual         the actual operand count
     * @param startInclusive the left bound of interval (inclusive) of the expected operand count
     * @param endInclusive   the right bound of interval (inclusive) of the expected operand count
     */
    public WrongNumberOfOperandsException(int actual, int startInclusive, int endInclusive) {
        super(String.format("%d not satisfying condition: %d <= x <= %d", actual, startInclusive, endInclusive));
    }
}
