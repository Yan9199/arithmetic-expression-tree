package tree;

import math.MyNumber;
import math.MyReal;

import java.math.BigDecimal;

/**
 * Represents predefined identifiers (constants).
 */
public enum Identifier {

    /**
     * The {@code MyNumber} value that is closer than any other to <i>e</i>, the base of the natural
     * logarithms.
     */
    E("e", new MyReal(BigDecimal.valueOf(Math.E))),

    /**
     * The {@code MyNumber} value that is closer than any other to <i>pi</i>, the ratio of the
     * circumference of a circle to its diameter.
     */
    PI("pi", new MyReal(BigDecimal.valueOf(Math.PI)));

    /**
     * The name of this identifier.
     */
    private final String name;

    /**
     * The value of this identifier.
     */
    private final MyNumber value;

    /**
     * Constructs and initializes an identifier with the given name and value.
     *
     * @param name  the name of the identifier
     * @param value the value of the identifier
     */
    Identifier(String name, MyNumber value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the name of this identifier.
     *
     * @return the name of this identifier
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value of this identifier.
     *
     * @return the value of this identifier
     */
    public MyNumber getValue() {
        return value;
    }
}
