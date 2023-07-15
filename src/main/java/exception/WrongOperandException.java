package exception;

import math.MyNumber;

/**
 * Thrown to indicate that the provided operand for an operation is incorrect.
 *
 * @author Nhan Huynh
 */
public class WrongOperandException extends RuntimeException {

    /**
     * Constructs and initializes a wrong operand exception with the given inputs as its detail
     * message.
     *
     * @param actual   the actual operand
     * @param cmp      the comparison between the actual and expected operand
     * @param expected the expected operand
     */
    public WrongOperandException(MyNumber actual, Comparison cmp, MyNumber expected) {
        super(String.format("The given number should be %s %s, but was %s", cmp.name().replace("_", " ").toLowerCase(), expected, actual));
    }
}
