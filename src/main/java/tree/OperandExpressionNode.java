package tree;

/**
 * This class represents an abstract operand arithmetic expression node.
 */
public abstract class OperandExpressionNode implements ArithmeticExpressionNode {

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract ArithmeticExpressionNode clone();

    @Override
    public boolean isOperand() {
        return true;
    }

    @Override
    public boolean isOperation() {
        return false;
    }
}
