import edu.princeton.cs.algs4.*;

import java.util.*;
import java.util.Vector;

/* The WordFinder class represents a word finder that receives in
 * the constructor an array of Strings, in which the Strings are made of
 * words separated by spaces, without punctuation or special characters.
 * The class has some methods to count and find the frequency of a given word.
 */
public class WordFinder {
    static SeparateChainingHashST<String, Boolean> tempST;

    private int arrSize;
    private String[] strArr;
    private LinearProbingHashST<String, Integer> strCount = new LinearProbingHashST<>();
    private LinearProbingHashST<String, HashSet<Integer>> stable = new LinearProbingHashST<>();

    public WordFinder(String [] starr) {
        fillST(starr);
    }

    public int size() {
        return strCount.size();
    }

    private void fillST(String[] starr) {
        String[] words;
        arrSize = starr.length;
        strArr = starr;
        for (int i = 0; i < starr.length; i++) {
             words = starr[i].split(" ");
            tempST = new SeparateChainingHashST<>();
            for (String word : words) {
                word = word.replace(" ","");
                if(word.isEmpty())
                    continue;
                insertCounter(word);
                insertStable(word, i);
            }

        }
    }

    public void  debugHashSet() {
        for (String s : stable.keys()) {
            HashSet<Integer> hset = stable.get(s);
            StdOut.println("\n==================");
            StdOut.print(s+" : ");
            for (Integer inteiro : hset)
                StdOut.print(inteiro+" ");

        }
    }

    private void insertStable(String w, int i) {
        HashSet<Integer> hset = stable.get(w);
        if(hset != null) {
            if (!hset.contains(i))
                hset.add(i);
        }
        else {
            hset = new HashSet<>();
            hset.add(i);
            stable.put(w, hset);
        }
    }

    private void insertCounter(String w) {
        Integer cValue;
        if(!tempST.contains(w)) {
            tempST.put(w, true);
            cValue = strCount.get(w);
            if(cValue == null) strCount.put(w, 1);
            else    strCount.put(w, ++cValue);
        }
    }
    /* getMax(): Returns the most repeated word in the String array */
    public String getMax() {
        int nMax = 0, count;
        String max = "";
        for (String s : strCount.keys()) {
            count = strCount.get(s);
            if(count > nMax) {
                nMax = count;
                max = s;
            }
        }
        return max;
    }
    /* Returns a word that appears both in the string of the indexes a and b
     * of the array. If no words are shared between a and b, returns null
     */
    public String containedIn(int a, int b) {
        if(a < 0 || a > arrSize || b < 0 || b > arrSize)
            throw new java.lang.IllegalArgumentException("Values out of bound!");
        for (String w : strArr[a].split(" ")){
            HashSet<Integer> hset = stable.get(w);
            if(hset.contains(b))
                return w;
        }
        return null;
    }

    /* Returns an array with the indexes of the initial array where
     * the string 's' appears. Returns an empty array if the string
     * doesn't appear anywhere.
     */
    public int[] appearsIn(String s) {
        HashSet<Integer> hset = stable.get(s);
        int k = 0;
        if(hset != null) {
            int[] indexes = new int[hset.size()];
            for (Integer i : hset)
                indexes[k++] = i;
            return indexes;
        }
        return new int[0];
    }

    // Unit testing
    public static void main(String[] args) {
        String filename = args[0];
        StringBuilder sb = new StringBuilder(200);
        Vector<String> strVec = new Vector<>();
        In in = new In(filename);
        while (!in.isEmpty())
            strVec.add(in.readLine().replace("\n",""));
        String[] strr = new String[strVec.size()];
        for (int i = 0; i < strVec.size(); i++)
            strr[i] = strVec.elementAt(i);
        Stopwatch timer = new Stopwatch();
        WordFinder wf = new WordFinder(strr);
        double time = timer.elapsedTime();
        StdOut.println("Elapsed time to build the WordFinder = "+time);
        StdOut.println("Total size = "+wf.size());
        StdOut.println("getMax() = "+wf.getMax());
        StdOut.println("containedIn(false) = "+wf.containedIn(0, 880));
        StdOut.println("containedIn(true) = "+wf.containedIn(2511, 3472));
        StdOut.println("appearsIn(false) = ");
        for (int i : wf.appearsIn("jose"))
            StdOut.print(i + " ");
        StdOut.println(" \n");
        StdOut.println("appearsIn(true) = ");
        for (int i : wf.appearsIn("get"))
            StdOut.print(i + " ");
        StdOut.println(" \n");
        //wf.debugHashSet();
    }
}
