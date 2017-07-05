import edu.princeton.cs.algs4.*;

import java.util.*;
import java.util.Vector;

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
                //try {Thread.sleep(1000);} catch (InterruptedException e) {}
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
            //try {Thread.sleep(200);} catch (InterruptedException e) {}
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
