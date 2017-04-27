/**
 * Created by juliano on 4/11/17.
 */
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
public class Subset {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        while(!StdIn.isEmpty())
            rq.enqueue(StdIn.readString());
        for (int i = 0; i < k; i++)
            StdOut.println(rq.dequeue());
    }
}
