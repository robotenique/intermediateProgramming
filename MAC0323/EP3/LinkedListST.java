/** ***********************************************************************
 *  Compilation:  javac LinkedListST.java
 *  Execution:    java LinkedListST
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/31elementary/tinyST.txt
 *
 *  Symbol table implementation with an ordered linked list.
 *
 *  % more tinyST.txt
 *  S E A R C H E X A M P L E
 *
 *  % java LinkedListST < tinyST.txt
 *  A 8
 *  C 4
 *  E 12
 *  H 5
 *  L 11
 *  M 9
 *  P 10
 *  R 3
 *  S 0
 *  X 7
 * *************************************************************************/
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

public class LinkedListST<Key extends Comparable<Key>, Value> {
    // atributos de estado
    private Node first;
    private int total;

    private class Node {
        Key key;
        Value val;
        Node next;
    }

    public LinkedListST() {
        // Maybe irrelevant...
        total = 0;
    }

    /** Is the key in this symbol table?
     */
    public boolean contains(Key key) {
        Node p = first;
        Node ant = null;
        for(p = first; p != null && p.key.compareTo(key) < 0; p = p.next);
        if(p == null)
            return false;
        return p.key.compareTo(key) == 0;
    }

    /** Returns the number of (key,value) pairs in this symbol table.
     */
    public int size() {
        return total;
    }

    /** Is this symbol table empty?
     */
    public boolean isEmpty() {
        return total == 0;
    }

    /** Returns the value associated with the given key,
     *  or null if no such key.
     *  Argument key must be nonnull.
     */
    public Value get(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        Node p;
        for(p = first; p != null && p.key.compareTo(key) < 0; p = p.next);
        if(p == null || p.key.compareTo(key) > 0)
            return  null;
        return  p.val;
    }

    /** Returns the number of keys in the table
     *  that are strictly smaller than the given key.
     *  Argument key must be nonnull.
     */
    public int rank(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to rank() is null");
        int i = 0;
        Node p = first;
        for(; p != null && p.key.compareTo(key) < 0; i++, p = p.next);
        return i;
    }

    /** Search for key in this symbol table.
     * If key is in the table, update the corresponding value.
     * Otherwise, add the (key,val) pair to the table.
     * Argument key must be nonnull.
     * If argument val is null, the key must be deleted from the table.
     */
    public void put(Key key, Value val)  {
        Node p = first;
        Node ant = null;
        Node newEntry;
        if (key == null) throw new IllegalArgumentException("argument to put() is null");
        if (val == null) {
            delete(key);
            return;
        }

        for(; p != null && (p.key).compareTo(key) < 0; ant = p, p = p.next);
        if(p != null && p.key.compareTo(key) == 0) {
            p.val = val;
        }
        else {
            newEntry = new Node();
            newEntry.key = key;
            newEntry.val = val;
            if(ant == null)
                first = newEntry;
            else
                ant.next = newEntry;
            newEntry.next = p;
            total++;
        }
    }


    /** Remove key (and the corresponding value) from this symbol table.
     * If key is not in the table, do nothing.
     */
    public void delete(Key key)  {
        if (key == null) throw new IllegalArgumentException("argument to put() is null");
        Node p = first;
        Node ant = null;
        // Special case for head
        if (first != null && first.key.equals(key)) {
            first = first.next;
            total--;
            return;
        }
        for(; p != null && (p.key).compareTo(key) != 0; ant = p, p = p.next);
        if(p != null) {
            // garbage collection will take care of p
            ant.next = p.next;
            p.next = null; //make sure we clear it
            total--;
        }
    }

    /** Delete the minimum key and its associated value
     * from this symbol table.
     * The symbol table must be nonempty.
     */
    public void deleteMin() {
        if (isEmpty()) throw new java.util.NoSuchElementException("deleteMin(): Symbol table underflow error");
        //assumes the ST is ordered
        delete(first.key);
    }

    /** Delete the maximum key and its associated value
     * from this symbol table.
     */
    public void deleteMax() {
        Node p = first;
        Node ant = null;
        if (isEmpty()) throw new java.util.NoSuchElementException("deleteMax(): Symbol table underflow error");
        for(; p != null; ant = p, p = p.next);
        //assumes the ST is ordered
        delete(ant.key); // If it's not empty, ant != null

    }


    /***************************************************************************
     *  Ordered symbol table methods
     **************************************************************************/

    /** Returns the smallest key in this table.
     * Returns null if the table is empty.
     */
    public Key min() {
        if(isEmpty()) return null;
        return first.key;
    }


    /** Returns the greatest key in this table.
     * Returns null if the table is empty.
     */
    public Key max() {
        if(isEmpty()) return null;
        Node p = first;
        Node ant = null;
        for (; p != null; ant = p, p = p.next);
        return ant.key;

    }

    /** Returns a key that is strictly greater than
     * (exactly) k other keys in the table.
     * Returns null if k < 0.
     * Returns null if k is greater that or equal to
     * the total number of keys in the table.
     */
    public Key select(int k) {
        // Basically returns the (k+1)nth key
        // Remember, there's no duplicate keys in ST
        if(k < 0 || k >= size())
            return null;
        Node p = first;
        for(int i = 0; i < k; i++, p = p.next);
        return p.key;
    }

    /** Returns the greatest key that is
     * smaller than or equal to the given key.
     * Argument key must be nonnull.
     * If there is no such key in the table
     * (i.e., if the given key is smaller than any key in the table),
     * returns null.
     */
    public Key floor(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to floor() is null");
        if (isEmpty()) return null;
        Node p = first;
        Node ant = null;
        if(key.compareTo(first.key) < 0)
            return null;
        for(; p != null && p.key.compareTo(key) <= 0; ant = p, p = p.next);
        return ant.key;

    }

    /** Returns the smallest key that is
     * greater than or equal to the given key.
     * Argument key must be nonnull.
     * If there is no such key in the table
     * (i.e., if the given key is greater than any key in the table),
     * returns null.
     */
    public Key ceiling(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to ceiling() is null");
        if (isEmpty()) return null;
        Node p = first;
        Node ant = null;
        for(; p != null && p.key.compareTo(key) < 0; ant = p, p = p.next);
        if(p == null)
            return null;
        return p.key;

    }

    /** Returns all keys in the symbol table as an Iterable.
     * To iterate over all of the keys in the symbol table named st, use the
     * foreach notation: for (Key key : st.keys()).
     */
    public Iterable<Key> keys() {
        return new ListKeys();
    }

    /**
     * implements Iterable<Key> significa que essa classe deve
     * ter um método iterator(), acho...
     */
    private class ListKeys implements Iterable<Key> {
        /**
         * Devolve um iterador que itera sobre os itens da ST
         * da menor até a maior chave.<br>
         */
        public Iterator<Key> iterator() {
            return new KeysIterator();
        }

        private class KeysIterator implements Iterator<Key> {
            // variáveis do iterador
            private Node curr = first;
            public boolean hasNext() {
                return curr != null;
            }

            public Key next() {
                Key keyIt = curr.key;
                curr = curr.next;
                return keyIt;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }


    /***************************************************************************
     *   Check internal invariants: pode ser útil durante o desenvolvimento
     **************************************************************************/

    // are the items in the linked list in ascending order?
    private boolean isSorted() {
        Node p;
        Node ant;
        for (p = first, ant = p; p != null; ant = p, p = p.next)
            if ((p.key).compareTo(ant.key) < 0)
                return false;

        return true;
    }

    /** Test client.
     * Reads a sequence of strings from the standard input.
     * Builds a symbol table whose keys are the strings read.
     * The value of each string is its position in the input stream
     * (0 for the first string, 1 for the second, and so on).
     * Then prints all the (key,value) pairs.
     */
    public static void main(String[] args) {
        LinkedListST<String, Integer> st;
        st = new LinkedListST<>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String key = StdIn.readString();
            st.put(key, i);
        }

        StdOut.println("isEmpty(): "+st.isEmpty());
        StdOut.println("get(E)"+st.get("E"));
        StdOut.println("get(\"bola\")"+st.get("bola"));
        StdOut.println("size()"+st.size());
        StdOut.println("rank(E)"+st.rank("E"));
        StdOut.println("Min()"+st.min());
        StdOut.println("max()"+st.max());
        StdOut.println("ceiling(J)"+st.ceiling("J"));
        StdOut.println("floor(J)"+st.floor("J"));
        StdOut.println("select(4)"+st.select(4));
        StdOut.println("deleteMin()");
        StdOut.println("deleteMax()");
        st.deleteMin();
        st.deleteMax();

        for (String s : st.keys())
            StdOut.println(s + " " + st.get(s));
    }
}

