package tree;

/**
 * Represents the available operators.
 */
public enum Operator {

    /**
     * The addition operator.
     */
    ADD("+"),

    /**
     * The subtraction operator.
     */
    SUB("-"),

    /**
     * The multiplication operator.
     */
    MUL("*"),

    /**
     * The division operator.
     */
    DIV("/"),

    /**
     * The exponential function operator.
     */
    EXP("exp"),

    /**
     * The exponentiation operator.
     */
    EXPT("expt"),

    /**
     * The natural logarithm operator.
     */
    LN("ln"),

    /**
     * The logarithm operator.
     */
    LOG("log"),

    /**
     * The square root operator.
     */
    SQRT("sqrt");

    /**
     * The operator's symbol.
     */
    private final String symbol;

    /**
     * Constructs and initializes an operator with the given symbol.
     *
     * @param symbol the operator's symbol.
     */
    Operator(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the operator's symbol.
     *
     * @return the operator's symbol.
     */
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
