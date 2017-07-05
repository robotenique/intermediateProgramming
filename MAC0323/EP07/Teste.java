import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
public class Teste {
    static Bag<String> b = new Bag<>();

    public Teste() {
        b.add("loko"+StdRandom.uniform(23));
    }

    public Bag<String> getB() {
        return b;
    }

    public static void main(String[] args) {
        Teste t = new Teste();
        Bag<String> bii = t.getB();
        for (String s: bii)
            StdOut.println(s);
        StdOut.print("Hello WORLD!!!\n");
    }
}
