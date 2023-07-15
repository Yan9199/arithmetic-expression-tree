package tree;

import exception.WrongNumberOfOperandsException;
import math.MyInteger;
import math.MyNumber;

import java.util.Map;
import java.util.Objects;

/**
 * This class represents an operation arithmetic expression node. An operation expression node
 * contains and operator followed by n operands depending on its arity.
 */
public class OperationExpressionNode implements ArithmeticExpressionNode {

    /**
     * The operator of this node.
     */
    private final Operator operator;

    /**
     * The operands of this node.
     */
    private final ListItem<ArithmeticExpressionNode> operands;

    /**
     * Contracts and initializes an operation expression node with the given operator and operands.
     *
     * @param operator the operator of this node
     * @param operands the operands of this node
     * @throws NullPointerException if the operator is {@code null}
     */
    public OperationExpressionNode(Operator operator, ListItem<ArithmeticExpressionNode> operands) {
        Objects.requireNonNull(operator, "operator null");
        final int sequenceLength = ListItem.getSequenceLength(operands);
        switch (operator.getSymbol()) {
            case "+", "*" -> {
            }
            case "-", "/" -> {
                if (sequenceLength == 0) throw new WrongNumberOfOperandsException(0, 1, Integer.MAX_VALUE);
            }
            case "ln", "exp", "sqrt" -> {
                if (sequenceLength != 1) throw new WrongNumberOfOperandsException(sequenceLength, 1, 1);
            }
            case "expt", "log" -> {
                if (sequenceLength != 2) throw new WrongNumberOfOperandsException(sequenceLength, 2, 2);
            }
        }
        this.operator = operator;
        this.operands = operands;
    }

    private OperationExpressionNode(Operator o, ListItem<ArithmeticExpressionNode> op, boolean ignored) {
        operator = o;
        operands = op;
    }

    /**
     * Returns the operator of this node.
     *
     * @return the operator of this node
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Returns the operands of this node.
     *
     * @return the operands of this node
     */
    public ListItem<ArithmeticExpressionNode> getOperands() {
        return operands;
    }

    @SuppressWarnings("all")
    @Override
    public MyNumber evaluate(Map<String, MyNumber> identifiers) {
        final String symbol = operator.getSymbol();
        if (operands == null) return symbol.equals("+") ? MyInteger.ZERO : MyInteger.ONE;
        final MyNumber n = operands.key.evaluate(identifiers);
        final ListItem<ArithmeticExpressionNode> next = operands.next;
        return switch (symbol) {
            case "+" -> addRecursively(identifiers, operands);
            case "*" -> multiplyRecursively(identifiers, operands);
            case "-" -> next == null ? n.minus() : n.minus(addRecursively(identifiers, next));
            case "/" -> next == null ? n.divide() : n.divide(multiplyRecursively(identifiers, next));
            case "exp" -> n.exp();
            case "expt" -> n.expt(next.key.evaluate(identifiers));
            case "ln" -> n.ln();
            case "log" -> n.log(next.key.evaluate(identifiers));
            default -> n.sqrt();
        };
    }

    private MyNumber addRecursively(Map<String, MyNumber> i, ListItem<ArithmeticExpressionNode> o) {
        final MyNumber n = o.key.evaluate(i);
        final ListItem<ArithmeticExpressionNode> next = o.next;
        return next == null ? n : n.plus(addRecursively(i, next));
    }

    private MyNumber multiplyRecursively(Map<String, MyNumber> i, ListItem<ArithmeticExpressionNode> o) {
        final MyNumber n = o.key.evaluate(i);
        final ListItem<ArithmeticExpressionNode> next = o.next;
        return next == null ? n : n.times(multiplyRecursively(i, next));
    }

    @Override
    public boolean isOperand() {
        return false;
    }

    @Override
    public boolean isOperation() {
        return true;
    }

    @SuppressWarnings("all")
    @Override
    public ArithmeticExpressionNode clone() {
        final Operator o = Operator.valueOf(operator.name());
        if (operands == null) return new OperationExpressionNode(o, null, false);
        final ListItem<ArithmeticExpressionNode> l = new ListItem<>(operands.key.clone());
        for (ListItem<ArithmeticExpressionNode> p1 = l, p2 = operands.next; p2 != null; p2 = p2.next)
            p1 = p1.next = new ListItem<>(p2.key.clone());
        return new OperationExpressionNode(o, l, false);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(LEFT_BRACKET);
        sb.append(operator);
        for (ListItem<ArithmeticExpressionNode> node = operands; node != null; node = node.next)
            sb.append(" ").append(node.key);
        sb.append(RIGHT_BRACKET);
        return sb.toString();
    }
}
