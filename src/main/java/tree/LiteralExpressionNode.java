package tree;

import math.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Objects;

/**
 * This class represents a literal operand arithmetic expression node. A literal operand is a {@link
 * MyNumber}.
 */
public class LiteralExpressionNode extends OperandExpressionNode {

    /**
     * The literal operand.
     */
    private final MyNumber value;

    /**
     * Constructs and initializes a literal operand arithmetic expression node with the given
     * value.
     *
     * @param value the literal operand
     * @throws NullPointerException if the value is {@code null}
     */
    public LiteralExpressionNode(MyNumber value) {
        this.value = Objects.requireNonNull(value, "value null");
    }

    private LiteralExpressionNode(MyNumber n, boolean ignored) {
        value = n;
    }

    /**
     * Returns the literal operand.
     *
     * @return the literal operand
     */
    public MyNumber getValue() {
        return value;
    }

    @Override
    public MyNumber evaluate(Map<String, MyNumber> identifiers) {
        return value;
    }

    @Override
    public ArithmeticExpressionNode clone() {
        final MyNumber n;
        if (value instanceof MyInteger) n = new MyInteger(new BigInteger(String.valueOf(value.toInteger())));
        else if (value instanceof MyReal) n = new MyReal(new BigDecimal(String.valueOf(value.toReal())));
        else {
            final Rational r = value.toRational();
            n = new MyRational(new Rational(new BigInteger(String.valueOf(r.getNumerator())), new BigInteger(String.valueOf(r.getDenominator()))));
        }
        return new LiteralExpressionNode(n, false);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
