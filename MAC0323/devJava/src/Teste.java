import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;

/**
 * Created by juliano on 5/15/17.
 */

public class Teste {
    public static void main(String[] args) {
        HashSet<Integer> hset = new HashSet<>(20);
        int k = 0;
        do {
            k = StdIn.readInt();
            hset.add(k);
            StdOut.println("\n=====================");
            for(Integer i : hset)
                StdOut.print(i+" ");
        } while (k != 0);

    }
}
