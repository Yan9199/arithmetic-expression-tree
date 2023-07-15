package exception;

/**
 * The comparison enumeration used in the exception detail message.
 *
 * @author Nhan Huynh
 * @see WrongNumberOfOperandsException
 * @see WrongOperandException
 */
public enum Comparison {

    /**
     * The comparison is equal.
     */
    EQUAL_TO,

    /**
     * The comparison is not equal.
     */
    DIFFERENT_FROM,

    /**
     * The comparison is greater than.
     */
    GREATER_THAN,

    /**
     * The comparison is less than.
     */
    LESS_THAN,

    /**
     * The comparison is between and interval.
     */
    BETWEEN
}
