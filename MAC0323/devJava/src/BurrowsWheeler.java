import edu.princeton.cs.algs4.*;

import java.util.HashMap;
import java.util.HashSet;

public class BurrowsWheeler {
    private static final int R = CircularSuffixArray.R;
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform(){
        String s = StdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        StringBuilder t = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++)
            t.append(csa.rots[i].valueAt(s.length() - 1));
        BinaryStdOut.write(csa.origPos);
        String tString = t.toString();
        for(int i = 0; i < tString.length(); i++)
            BinaryStdOut.write(tString.charAt(i));
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        int N = t.length();
        int[] next = new int[t.length()];
        int[] count = new int[R + 1];
        char leadChar = 0;
        HashMap<Character, MinPQ<Integer>> locator = new HashMap<>();
        createMappers(t, N, count, locator);
        // Construct the next[] array
        for (int i = 0; i < N; i++) {
            leadChar = getLeadChar(leadChar, count);
            next[i] = obtainNext(leadChar, locator);
        }
        String result = inverseData(t, N, first, next);
        StdOut.println(result);
    }

    private static String inverseData(String t, int N, int first, int[] next) {
        String sortedArr[] = new String[N];
        StringBuilder sb = new StringBuilder(N);
        for (int i = 0; i < N; i++)
            sortedArr[i] = Character.toString(t.charAt(i));
        LSD.sort(sortedArr, 1);
        int c = 0;
        for (int i = first; c < N; i = next[i], c++)
            sb.append(sortedArr[i]);
        return sb.toString();
    }

    private static int obtainNext(char leadChar, HashMap<Character, MinPQ<Integer>> locator) {
        int pos = (locator.get(leadChar)).delMin();
        if(locator.get(leadChar).isEmpty())
            locator.remove(leadChar);
        return pos;
    }


    private static char getLeadChar(char c, int[] count) {
        int i;
        for (i = c; i < count.length && count[i] == 0; i++);
        count[i]--;
        return (char) i;
    }

    private static void createMappers(String t, int n, int[] count, HashMap<Character, MinPQ<Integer>> locator) {
        for (int i = 0; i < n; i++) {
            MinPQ<Integer> temp = locator.get(t.charAt(i));
            if(temp != null) {
                temp.insert(i);
            }
            else {
                temp = new MinPQ<>();
                temp.insert(i);
                locator.put(t.charAt(i), temp);
            }
            count[t.charAt(i)]++;
        }
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args){
        switch (args[0]) {
            case "-":
                transform();
                break;
            case "+":
                inverseTransform();
                break;
            default:
                throw new UnsupportedOperationException("Invalid argument!");
        }
    }
}
