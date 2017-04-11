import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;
/**
 * Created by juliano on 4/11/17.
 */
public class Deque<Item>  implements Iterable<Item>{
    private Node b;
    private Node e;
    private int n;
    private class Node {
        // Doubly linked list
        Item item;
        Node prev;
        Node next;

        public Node(Item item) {
            this.item = item;
        }
    }
    // construct an empty deque
    public Deque() {
        b = null;
        e = null;
        n = 0;
    }
    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }
    // return the number of items on the deque
    public int size() {
        return n;
    }
    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new java.lang.NullPointerException("You can't add a NULL item!");
        Node newItem = new Node(item);
        Node prevB = b;
        b = newItem;
        b.next = prevB;
        if(prevB != null)
            prevB.prev = b;

        if(n == 0)
            e = b;
        n++;
    }
    // add the item to the end
    public void addLast(Item item) {
        if (item == null)
            throw new java.lang.NullPointerException("You can't add a NULL item!");
        Node newItem = new Node(item);
        Node prevE = e;
        e = newItem;
        e.prev = prevE;
        if(prevE != null)
            prevE.next = e;

        if(n == 0)
            b = e;
        n++;
    }
    // remove and return the item from the front
    public Item removeFirst() {
        if(isEmpty())
            throw new java.util.NoSuchElementException("The deque is empty!");
        Item item = b.item;
        Node proxB = b.next;
        if(proxB != null)
            proxB.prev = null;
        b.next = null;
        b = proxB;
        n--;
        return item;
    }
    // remove and return the item from the end
    public Item removeLast() {
        if(isEmpty())
            throw new java.util.NoSuchElementException("The deque is empty!");
    Item item = e.item;
    Node prevE = e.prev;
    if(prevE != null)
        prevE.next = null;
    e.prev = null;
    e = prevE;
    n--;
    return item;
    }

    @Override
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        Iterator<Item> it = new Iterator<Item>() {
            private Node curr = b;

            @Override
            public boolean hasNext() {
                return curr != null;
            }

            @Override
            public Item next() {
                if(!hasNext())
                    throw new java.util.NoSuchElementException("There's no items in the deque!");
                Item currItem = curr.item;
                curr = curr.next;
                return currItem;
            }
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }

    //unit testing
    public static void main (String[] args) {
        Deque<String> dq = new Deque<>();
        String[] fEntry = {"A", "B", "C"};
        StdOut.println("size() : " + dq.size());
        StdOut.println("isEmpty(): "+dq.isEmpty());
        for (String s: fEntry)
            dq.addFirst(s);
        while (!dq.isEmpty())
            StdOut.println("Deleted = "+dq.removeLast());
        for (String s: fEntry)
            dq.addLast(s);
        StdOut.println("size() : " + dq.size());
        while (!dq.isEmpty())
            StdOut.println("Deleted = "+dq.removeFirst());
        StdOut.println("size() : " + dq.size());
        dq.addFirst("TOP");
        StdOut.println("isEmpty(): "+dq.isEmpty());
        StdOut.println("Deleted = "+dq.removeFirst());
        dq.addLast("TOP");
        StdOut.println("Deleted = "+dq.removeFirst());
        StdOut.println("isEmpty(): "+dq.isEmpty());
    }
}
