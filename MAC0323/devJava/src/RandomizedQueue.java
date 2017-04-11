import java.util.Iterator;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
/**
 * Created by juliano on 4/11/17.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int n;
    private int max;
    // construct an empty randomized queue
    public RandomizedQueue() {
        max = 2;
        items = (Item[]) new Object[max];
    }
    // Is the queue empty?
    public boolean isEmpty() {
        return n == 0;
    }
    // return the number of items on the queue
    public int size() {
        return n;
    }
    // add the item
    public void enqueue(Item item) {
        if(item == null)
            throw new java.lang.NullPointerException("enqueue(): Can't enqueue a null item!");
        if(n == max)
            resize();
        items[n] = item;
        n++;
    }


    // remove and return a random item
    public Item dequeue() {
        if(isEmpty())
            throw new java.util.NoSuchElementException("dequeue(): The queue is empty!");
        // choose a random number
        int rPos = StdRandom.uniform(n);
        Item k = items[rPos];
        items[rPos] = items[n - 1];
        n--;
        if(n <= max/4)
            resize();
        return k;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if(isEmpty())
            throw new java.util.NoSuchElementException("sample(): The queue is empty!");
        int rPos = StdRandom.uniform(n);
        Item k = items[rPos];
        return k;
    }

    // reduce the array to 2*n
    private void resize() {
        max = 2*n;
        Item[] newArr = (Item[]) new Object[max];
        for (int i = 0; i < n; i++) {
            newArr[i] = items[i];
            items[i] = null;
        }
        items = newArr;
    }

    // return an independent iterator over items in random order
    @Override
    public Iterator<Item> iterator() {
        return new RndList();
    }

    private class RndList implements Iterator<Item> {
        Item[] cp;
        int curr;
        RndList() {
            curr = 0;
            cp = (Item[]) new Object[n];
            for (int i = 0; i < n; i++)
                cp[i] = items[i];
            StdRandom.shuffle(cp);
        }

        @Override
        public boolean hasNext() {
            return curr != n;
        }

        @Override
        public Item next() {
            if(!hasNext())
                throw new java.util.NoSuchElementException("There's no items in the randomized queue!");
            return cp[curr++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    //unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        String[] teste = {"ANALIA", "BORBOLETA", "CAMPANHA", "DISNEY",
        "ESTUPENDO", "FATÍDICO", "GROTESCO", "HILÁRIO", "INSÍPIDO",
         "JAGUAR", "KARL MARX", "LEÔNIDAS"};
        StdOut.println("isEmpty() : "+rq.isEmpty());
        StdOut.println("size() : "+rq.size());
        for (String s: teste)
            rq.enqueue(s);
        StdOut.println("=============PRIMEIRO ITERATOR============= ");
        for (String s: rq)
            StdOut.println("it() - "+s);
        StdOut.println("=====================FIM=====================");
        StdOut.println("=============SEGUNDO ITERATOR============= ");
        for (String s: rq)
            StdOut.println("it() - "+s);
        StdOut.println("=====================FIM=====================");
        StdOut.println("size() : "+rq.size());
        StdOut.println("size() : "+rq.size());
        StdOut.println("sample() : "+rq.sample());
        StdOut.println("sample() : "+rq.sample());
        StdOut.println("sample() : "+rq.sample());
        StdOut.println("dequeue() : "+rq.dequeue());
        StdOut.println("dequeue() : "+rq.dequeue());
        StdOut.println("dequeue() : "+rq.dequeue());
        StdOut.println("dequeue() : "+rq.dequeue());
        rq.dequeue();
        rq.dequeue();
        rq.dequeue();
        rq.dequeue();
        rq.dequeue();
        rq.dequeue();
        rq.dequeue();
        rq.dequeue();
        StdOut.println("size() : "+rq.size());
        StdOut.println("isEmpty() : "+rq.isEmpty());
    }
}
