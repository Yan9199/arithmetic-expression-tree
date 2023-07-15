package tree;

import exception.IllegalIdentifierExceptions;
import exception.UndefinedIdentifierException;
import math.MyNumber;

import java.util.Map;

/**
 * Represents an arithmetic expression node.
 */
public interface ArithmeticExpressionNode extends Cloneable {

    /**
     * THe left bracket to group a term.
     */
    String LEFT_BRACKET = "(";

    /**
     * The right bracket to group a term.
     */
    String RIGHT_BRACKET = ")";

    /**
     * Evaluates the arithmetic expression.
     *
     * @param identifiers a map of identifiers and their values
     * @return the result of the arithmetic expression
     * @throws IllegalIdentifierExceptions  if the identifier in the map is illegal to use
     * @throws UndefinedIdentifierException if the identifier is not defined for use in the map
     */
    MyNumber evaluate(Map<String, MyNumber> identifiers);

    /**
     * Returns {@code true} if this node is an operand.
     *
     * @return {@code true} if this node is an operand
     */
    boolean isOperand();

    /**
     * Returns {@code true} if this node is an operation.
     *
     * @return {@code true} if this node is an operation
     */
    boolean isOperation();

    /**
     * Returns a clone of this node (deep copy).
     *
     * @return a clone of this node
     */
    ArithmeticExpressionNode clone();

    /**
     * Returns the string representation of this node.
     *
     * @return the string representation of this node
     */
    @Override
    String toString();
}
