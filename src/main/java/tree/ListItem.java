package tree;

/**
 * An instance of this class represents a sequence of list items (wrapped elements) of an ordered
 * sequence that can be addressed by means of a reference to the direct successor.
 */
public class ListItem<T> {

    /**
     * The value of this list item.
     */
    public T key;

    /**
     * The successor node of this list item.
     */
    public ListItem<T> next;

    /**
     * Constructs and initializes an empty list item.
     */
    public ListItem() {
    }

    public ListItem(T k) {
        key = k;
    }

    public void add(T k) {
        if (key == null) {
            key = k;
            return;
        }
        ListItem<T> p = this;
        while (p.next != null) p = p.next;
        p.next = new ListItem<>(k);
    }

    public void addRecursively(T k) {
        if (key == null) {
            key = k;
            return;
        }
        addRecursivelyHelper(k, this);
    }

    private void addRecursivelyHelper(T k, ListItem<T> p) {
        ListItem<T> next;
        if ((next = p.next) == null) {
            p.next = new ListItem<>(k);
            return;
        }
        addRecursivelyHelper(k, next);
    }

    public static <T> int getSequenceLength(ListItem<T> head) {
        int sequenceLength = 0;
        for (; head != null; head = head.next) sequenceLength++;
        return sequenceLength;
    }

    @Override
    public String toString() {
        return String.format("[%s|%s]", key, next == null ? "null" : next.key);
    }
}
