package tree;

import exception.IllegalIdentifierExceptions;
import exception.UndefinedIdentifierException;
import math.MyNumber;

import java.util.Map;
import java.util.Objects;

/**
 * This class represents an identifier operand arithmetic expression node. An identifier operand is
 * a variable name.
 */
public class IdentifierExpressionNode extends OperandExpressionNode {
    /**
     * The identifier name.
     */
    private final String value;

    /**
     * Constructs and initializes an identifier expression node with the given value.
     *
     * @param value the identifier name
     * @throws IllegalArgumentException    if the identifier name is not valid
     * @throws IllegalIdentifierExceptions if the identifier name is not valid
     * @throws NullPointerException        if the identifier name is {@code null}
     */
    public IdentifierExpressionNode(String value) {
        final int length = Objects.requireNonNull(value).length();
        if (length == 0) throw new IllegalArgumentException("empty string");
        boolean b = false;
        for (int i = 0; i < length; i++) {
            final char c = value.charAt(i);
            if (c == '-') continue;
            if (Character.isLetter(c)) {
                if (b(c)) throw new IllegalIdentifierExceptions(value);
                b = true;
                continue;
            }
            throw new IllegalIdentifierExceptions(value);
        }
        if (!b) throw new IllegalIdentifierExceptions(value);
        this.value = value;
    }

    private IdentifierExpressionNode(String s, boolean ignored) {
        value = s;
    }

    private static boolean b(char c) {
        return c == 'Ä' || c == 'ä' || c == 'Ö' || c == 'ö' || c == 'Ü' || c == 'ü' || c == 'ß';
    }

    /**
     * Returns the identifier name.
     *
     * @return the identifier name
     */
    public String getValue() {
        return value;
    }

    @Override
    public MyNumber evaluate(Map<String, MyNumber> identifiers) {
        if (value.equals(Identifier.E.getName()) || value.equals(Identifier.PI.getName()))
            throw new IllegalIdentifierExceptions(value);
        final MyNumber n;
        if ((n = identifiers.get(value)) == null) throw new UndefinedIdentifierException(value);
        return n;
    }

    @Override
    public ArithmeticExpressionNode clone() {
        return new IdentifierExpressionNode(value, false);
    }

    @Override
    public String toString() {
        return value;
    }
}
