package tree;

import exception.IllegalIdentifierExceptions;
import math.MyNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Evaluates an arithmetic expression by replacing the variables (identifiers) of the expression
 * with their values.
 */
public class ArithmeticExpressionEvaluator {

    /**
     * The arithmetic expression tree to evaluate.
     */
    private ArithmeticExpressionNode root;

    /**
     * The map of variables and their values.
     */
    private final Map<String, MyNumber> identifiers;

    private boolean b = true;

    /**
     * Constructs and initializes an arithmetic expression evaluator.
     *
     * @param root        the root of the arithmetic expression tree to evaluate
     * @param identifiers the map of variables and their values
     */
    public ArithmeticExpressionEvaluator(ArithmeticExpressionNode root, Map<String, MyNumber> identifiers) {
        this.root = root.clone();
        this.identifiers = identifiers;
    }

    /**
     * Returns the root of the arithmetic expression tree to evaluate.
     *
     * @return the root of the arithmetic expression tree to evaluate
     */
    public ArithmeticExpressionNode getRoot() {
        return root;
    }

    /**
     * Returns the map of variables and their values.
     *
     * @return the map of variables and their values
     */
    public Map<String, MyNumber> getIdentifiers() {
        return identifiers;
    }

    /**
     * Evaluates the arithmetic expression tree by replacing the variables (identifiers) of the
     * expression with their values and evaluates the most inner expressions.
     *
     * @return the list of tokens representing the evaluation
     */
    public List<String> nextStep() {
        if (root instanceof LiteralExpressionNode) return ExpressionTreeHandler.reconstruct(root);
        if (root instanceof IdentifierExpressionNode i) {
            final MyNumber n;
            if ((n = identifiers.get(i.getValue())) == null) throw new IllegalIdentifierExceptions("<unknown!>");
            final ArrayList<String> a = new ArrayList<>(1);
            a.add(n.toString());
            return ExpressionTreeHandler.reconstruct(root = ExpressionTreeHandler.buildRecursively(a.iterator()));
        }
        if (b) {
            final OperationExpressionNode o = (OperationExpressionNode) root;
            if (checkIdentifiers(o)) {
                final ArrayList<String> a = new ArrayList<>();
                replaceIdentifiers(a, o);
                root = ExpressionTreeHandler.buildRecursively(a.iterator());
            }
            b = false;
        }
        final ArrayList<String> a = new ArrayList<>();
        buildList(a, (OperationExpressionNode) root);
        root = ExpressionTreeHandler.buildRecursively(a.iterator());
        return a;
    }

    private boolean checkIdentifiers(OperationExpressionNode o) {
        for (ListItem<ArithmeticExpressionNode> l = o.getOperands(); l != null; l = l.next) {
            final ArithmeticExpressionNode a = l.key;
            if (a instanceof IdentifierExpressionNode) return true;
            if (a instanceof OperationExpressionNode op) if (checkIdentifiers(op)) return true;
        }
        return false;
    }

    private void replaceIdentifiers(ArrayList<String> a, OperationExpressionNode o) {
        a.add("(");
        a.add(o.getOperator().getSymbol());
        for (ListItem<ArithmeticExpressionNode> l = o.getOperands(); l != null; l = l.next) {
            final ArithmeticExpressionNode node = l.key;
            if (node instanceof OperationExpressionNode op) replaceIdentifiers(a, op);
            else if (node instanceof IdentifierExpressionNode i) {
                final MyNumber n;
                if ((n = identifiers.get(i.getValue())) == null) throw new IllegalIdentifierExceptions("<unknown!>");
                a.add(n.toString());
            } else a.add(node.toString());
        }
        a.add(")");
    }

    private void buildList(ArrayList<String> a, OperationExpressionNode o) {
        ListItem<ArithmeticExpressionNode> l = o.getOperands();
        if (checkEmbedded(l)) {
            a.add(o.evaluate(identifiers).toString());
            return;
        }
        a.add("(");
        a.add(o.getOperator().getSymbol());
        for (; l != null; l = l.next) {
            final ArithmeticExpressionNode node = l.key;
            if (node instanceof OperationExpressionNode op) buildList(a, op);
            else a.add(node.toString());
        }
        a.add(")");
    }

    private boolean checkEmbedded(ListItem<ArithmeticExpressionNode> l) {
        for (; l != null; l = l.next) if (l.key.isOperation()) return false;
        return true;
    }
}
