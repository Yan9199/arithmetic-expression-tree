package tree;

import exception.*;
import math.MyInteger;
import math.MyRational;
import math.MyReal;
import math.Rational;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * This class is used to parse an expression and build a tree out of it.
 */
public final class ExpressionTreeHandler {

    /**
     * Don't let anyone instantiate this class.
     */
    private ExpressionTreeHandler() {
    }

    /**
     * Builds an arithmetic expression tree from a string recursively.
     *
     * @param expression the string representation of the arithmetic expression to parse
     * @return the root node of the arithmetic expression tree
     * @throws BadOperationException        if the iterator has no more tokens
     * @throws ParenthesesMismatchException if the parentheses are mismatched
     * @throws UndefinedOperatorException   if the operator is not defined
     */
    public static ArithmeticExpressionNode buildRecursively(Iterator<String> expression) {
        if (!expression.hasNext()) throw new BadOperationException("No expression");
        final String s = expression.next();
        if (!s.equals("(")) {
            if (expression.hasNext()) throw new BadOperationException(expression.next());
            return identifierOrLiteral(s);
        }
        checkExpression(expression);
        return buildOperationRecursively(expression, true);
    }

    private static ArithmeticExpressionNode buildOperationRecursively(Iterator<String> expression, boolean outerCall) {
        final String op = expression.next();
        checkExpression(expression);
        final Operator operator = checkOperator(op);
        final ListItem<ArithmeticExpressionNode> operands = new ListItem<>();
        buildOperandsRecursively(expression, operands, operator, outerCall);
        return new OperationExpressionNode(operator, operands.key == null ? null : operands);
    }

    private static void buildOperandsRecursively(Iterator<String> expression, ListItem<ArithmeticExpressionNode> operands, Operator operator, boolean outerCall) {
        final String s = expression.next();
        if (s.equals(")")) {
            if (expression.hasNext() && outerCall) throw new BadOperationException(expression.next());
            checkZeroOperands(operator);
            return;
        }
        if (s.equals("(")) operands.addRecursively(buildOperationRecursively(expression, false));
        else operands.addRecursively(identifierOrLiteral(s));
        checkExpression(expression);
        buildOperandsRecursivelyHelper(expression, operands, operator, outerCall, 1);
    }

    private static void buildOperandsRecursivelyHelper(Iterator<String> expression, ListItem<ArithmeticExpressionNode> operands, Operator operator, boolean outerCall, int numberOfOperands) {
        final String s = expression.next();
        if (s.equals(")")) {
            if (expression.hasNext() && outerCall) throw new BadOperationException(expression.next());
            checkNumberOfOperands(operator, numberOfOperands);
            return;
        }
        if (s.equals("(")) operands.addRecursively(buildOperationRecursively(expression, false));
        else operands.addRecursively(identifierOrLiteral(s));
        checkExpression(expression);
        buildOperandsRecursivelyHelper(expression, operands, operator, outerCall, ++numberOfOperands);
    }

    private static void checkNumberOfOperands(Operator operator, int n) {
        switch (operator.getSymbol()) {
            case "+", "*" -> {
            }
            case "-", "/" -> {
                if (n == 0) throw new WrongNumberOfOperandsException(0, 1, Integer.MAX_VALUE);
            }
            case "ln", "exp", "sqrt" -> {
                if (n != 1) throw new WrongNumberOfOperandsException(n, 1, 1);
            }
            case "expt", "log" -> {
                if (n != 2) throw new WrongNumberOfOperandsException(n, 2, 2);
            }
        }
    }

    private static void checkZeroOperands(Operator operator) {
        switch (operator.getSymbol()) {
            case "+", "*" -> {
            }
            case "-", "/" -> throw new WrongNumberOfOperandsException(0, 1, Integer.MAX_VALUE);
            case "ln", "exp", "sqrt" -> throw new WrongNumberOfOperandsException(0, 1, 1);
            case "expt", "log" -> throw new WrongNumberOfOperandsException(0, 2, 2);
        }
    }

    private static void checkExpression(Iterator<String> expression) {
        if (!expression.hasNext()) throw new ParenthesesMismatchException();
    }

    private static ArithmeticExpressionNode identifierOrLiteral(String s) {
        try {
            return new IdentifierExpressionNode(s);
        } catch (IllegalIdentifierExceptions e) {
            try {
                return new LiteralExpressionNode(new MyInteger(new BigInteger(s)));
            } catch (NumberFormatException ex) {
                try {
                    return new LiteralExpressionNode(new MyReal(new BigDecimal(s)));
                } catch (NumberFormatException exc) {
                    if (s.matches("-?\\d+/\\d+")) {
                        final String[] a = s.split("/");
                        return new LiteralExpressionNode(new MyRational(new Rational(new BigInteger(a[0]), new BigInteger(a[1]))));
                    }
                    throw new IllegalIdentifierExceptions(s);
                }
            }
        }
    }

    private static Operator checkOperator(String s) {
        return Operator.valueOf(
                switch (s) {
                    case "+" -> "ADD";
                    case "-" -> "SUB";
                    case "*" -> "MUL";
                    case "/" -> "DIV";
                    case "exp" -> "EXP";
                    case "expt" -> "EXPT";
                    case "ln" -> "LN";
                    case "log" -> "LOG";
                    case "sqrt" -> "SQRT";
                    default -> throw new UndefinedOperatorException(s);
                });
    }

    /**
     * Builds an arithmetic expression tree from a string iteratively.
     *
     * @param expression the string representation of the arithmetic expression to parse
     * @return the root node of the arithmetic expression tree
     * @throws BadOperationException        if the iterator has no more tokens
     * @throws ParenthesesMismatchException if the parentheses are mismatched
     * @throws UndefinedOperatorException   if the operator is not defined
     */
    public static ArithmeticExpressionNode buildIteratively(Iterator<String> expression) {
        if (!expression.hasNext()) throw new BadOperationException("No expression");
        final String s = expression.next();
        if (!s.equals("(")) {
            if (expression.hasNext()) throw new ParenthesesMismatchException();
            return identifierOrLiteral(s);
        }
        checkExpression(expression);
        final String op = expression.next();
        checkExpression(expression);
        final Stack<Operator> operatorStack = new Stack<>();
        operatorStack.push(checkOperator(op));
        final Stack<Integer> integerStack = new Stack<>();
        integerStack.push(0);
        final Stack<ListItem<ArithmeticExpressionNode>> stack = new Stack<>();
        stack.push(new ListItem<>());
        buildOperandsIteratively(expression, stack, operatorStack, integerStack);
        final ListItem<ArithmeticExpressionNode> l = stack.pop();
        return new OperationExpressionNode(operatorStack.pop(), l.key == null ? null : l);
    }

    private static void buildOperandsIteratively(Iterator<String> expression, Stack<ListItem<ArithmeticExpressionNode>> stack, Stack<Operator> operatorStack, Stack<Integer> integerStack) {
        while (true) {
            final String s = expression.next();
            if (s.equals(")")) {
                final int size = stack.size();
                if (size > 1) {
                    ListItem<ArithmeticExpressionNode> list = stack.pop();
                    stack.peek().add(new OperationExpressionNode(operatorStack.pop(), list.key == null ? null : list));
                    checkExpression(expression);
                    continue;
                }
                if (expression.hasNext()) throw new BadOperationException(expression.next());
                checkNumberOfOperands(operatorStack.peek(), integerStack.peek());
                return;
            }
            integerStack.set(integerStack.size() - 1, integerStack.peek() + 1);
            if (s.equals("(")) {
                checkExpression(expression);
                final String op = expression.next();
                checkExpression(expression);
                operatorStack.push(checkOperator(op));
                integerStack.push(0);
                stack.push(new ListItem<>());
                buildOperandsIterativelyHelper(expression, stack, operatorStack, integerStack);
            } else stack.peek().add(identifierOrLiteral(s));
            checkExpression(expression);
        }
    }

    private static void buildOperandsIterativelyHelper(Iterator<String> expression, Stack<ListItem<ArithmeticExpressionNode>> stack, Stack<Operator> operatorStack, Stack<Integer> integerStack) {
        while (true) {
            final String s = expression.next();
            if (s.equals(")")) {
                checkNumberOfOperands(operatorStack.peek(), integerStack.pop());
                final ListItem<ArithmeticExpressionNode> list = stack.pop();
                stack.peek().add(new OperationExpressionNode(operatorStack.pop(), list.key == null ? null : list));
                return;
            }
            integerStack.set(integerStack.size() - 1, integerStack.peek() + 1);
            if (s.equals("(")) {
                checkExpression(expression);
                final String op = expression.next();
                checkExpression(expression);
                operatorStack.push(checkOperator(op));
                integerStack.push(0);
                stack.push(new ListItem<>());
            } else {
                stack.peek().add(identifierOrLiteral(s));
                checkExpression(expression);
            }
        }
    }

    /**
     * Reconstructs the string representation of the arithmetic expression tree.
     *
     * @param root the root node of the arithmetic expression tree
     * @return the string representation of the arithmetic expression tree
     */
    public static List<String> reconstruct(ArithmeticExpressionNode root) {
        final ArrayList<String> l = new ArrayList<>();
        if (root instanceof OperationExpressionNode o) addOperator(l, o);
        else l.add(root.toString());
        return l;
    }

    private static void addOperator(ArrayList<String> l, OperationExpressionNode o) {
        l.add("(");
        l.add(o.getOperator().getSymbol());
        addOperands(l, o.getOperands());
    }

    private static void addOperands(ArrayList<String> l, ListItem<ArithmeticExpressionNode> o) {
        for (; o != null; o = o.next) {
            final ArithmeticExpressionNode a = o.key;
            if (a instanceof OperationExpressionNode op) addOperator(l, op);
            else l.add(a.toString());
        }
        l.add(")");
    }
}
