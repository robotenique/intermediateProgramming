import edu.princeton.cs.algs4.*;
import java.util.HashMap;

/**
 *  The BurrowsWheeler class is used to transform a text into a form more
 *  easily compressed, by putting equal characters side by side.
 *  It has two basic operations: Transform and Inverse Transform
 *  Transform:
 *      This operation reads a String from the standart input, and then
 *      creates a CSA of the string. Then gets the last character of each
 *      sorted suffix and saves to a string t. The prints the position where
 *      the original string went when sorted, and the string t.
 *      For example, for the string 'abac!' the transform operation results in:
 *      1c!baa
 *
 * Inverse Transform:
 *      Given a number 'first' and a string 't', it transforms back to the
 *      original string. It can transform the '1c!baa' back to 'abac!'. It is
 *      a more tricky algorithm, better explained below.
 *
 *  @author Juliano Garcia de Oliveira
 */
public class BurrowsWheeler {
    // Alphabet size
    private static final int R = CircularSuffixArray.R;
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform(){
        while(!BinaryStdIn.isEmpty()) {
            String s = BinaryStdIn.readString();
            transformString(s);
        }
        BinaryStdOut.close();
    }

    private static void transformString(String s) {
        // Create the CSA of this string
        CircularSuffixArray csa = new CircularSuffixArray(s);
        // Obtain the sorted array of circular suffixes
        Integer[] rotations = csa.rotations;
        int N = s.length();
        // t is the string with the last char of each rotation in sorted order
        StringBuilder t = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++)
            t.append(s.charAt((N - 1 + rotations[i])%N));
        // Write the original Pos
        BinaryStdOut.write(csa.origPos);
        String tString = t.toString();
        // Write each char in the string t
        for(int i = 0; i < tString.length(); i++)
            BinaryStdOut.write(tString.charAt(i));
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        while(!BinaryStdIn.isEmpty()){
            int first = BinaryStdIn.readInt();
            String t = BinaryStdIn.readString();
            inverseTransformString(first, t);
        }
        BinaryStdOut.close();
    }

    /* For the inverse transform, we define the 'next' array as such:
     * If the jth original suffix (original string, shifted j characters to the left)
     * is the ith row in the sorted order, we define next[i] to be the row in the
     * sorted order where the (j + 1)st original suffix appears.
     *
     * With only the 'first' and the string 't' we can deduce the array 'next';
     * And, with the 'next' array, we can obtain the original string by following
     * the 'next' array and obtaining the first character in each rotation.
     * The first column of the sorted suffixes is obtained by sorting the string t.
     * For example, for the transformation '1c!baa' we have:
     *  i          sorted Suffixes t          next[i]
     * ---          ----------------          -------
     *  0           !??????????????c            1
     *  1           a??????????????!            3
     *  2           a??????????????b            2
     *  3           b??????????????a            4
     *  4           c??????????????a            0
     * To construct the 'next' array, I use a HashMap of MinPQ, where the key
     * is a char of t, and the value is a MinPQ which have all the positions
     * where the key appears in the string t.
     * So in the example of t = 'c!baa', the 'locator' mapper is:
     *      locator = {'c'=> {0}, '!'=> {1}, 'b'=> {2}, 'a'=> {3, 4}}
     * Then, we build a frequency key indexed array, so we don't have to
     * explicitly create the sorted array of t. For our example, this array
     * 'count' is:
     *      count[!] = 1, count[a] = 2, count[b] = 1, count[c] = 1.
     * Count have R positions (our alphabet). If the char X doesn't appears
     * in the string t, then count[X] = 0.
     *
     * The algorithm to create next[] is to basically have a pointer in the
     * count array pointing to a character which frequency is greater than 0.
     * Then, reduce the frequency of this character by one, and return the character.
     * This returned character is the lead character of this given iteration. For
     * example, in iteration 0, the leadChar is equal to '!', then in iteration 1 is
     *  'a', then 'a' again, then 'b', and so forth.
     * So, with the leadChar of the current iteration, we search in the locator this
     * char, and get the minimum value of the refereced priority queue. If the priority
     * queue is empty, we remove that entry from the HashMap as we don't need it
     * anymore. So, in the first iteration, we search for '!' and get 1.
     * So then, we now know that next[0] = 1, and so forth we build the whole array.
     *
     */
    private static void inverseTransformString(int first, String t) {
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
        // Obtain the original string
        String result = inverseData(t, N, first, next);
        BinaryStdOut.write(result);
    }

    /* To get the original String, we just have to get the character
     * of the sorted order in the order given by the next array.
     * So in this, we just sort the 't' string for convenience.
     * Given that the string t has length N, we'll use the LSD sort
     * for optimization. Four example where t = 'c!baa', the initial array
     * is sortedArr = ['c', '!', 'b', 'a', 'a'], and the sorted one is
     *    sortedArr = ['!', 'a', 'a', 'b', 'c']
     *
     * We could just use a 'count' array like before, but we know
     * that the length of each string in the sortedArr has length 1,
     * thus giving the LSD radix a linear complexity of O(N).
     *
     * Then, by using the next[] array, we constuct the original string,
     * as shown in the code below.
     */
    private static String inverseData(String t, int N, int first, int[] next) {
        String sortedArr[] = new String[N];
        StringBuilder sb = new StringBuilder(N);
        for (int i = 0; i < N; i++)
            sortedArr[i] = Character.toString(t.charAt(i));
        LSD.sort(sortedArr, 1); // Sort using LSD
        int c = 0;
        // Construct the original string by following the next[] array
        for (int i = first; c < N; i = next[i], c++)
            sb.append(sortedArr[i]);
        return sb.toString();
    }

    /* obtain the minimum position in the string 't' where the
     * leadChar appears and return it.
     */
    private static int obtainNext(char leadChar, HashMap<Character, MinPQ<Integer>> locator) {
        int pos = (locator.get(leadChar)).delMin();
        if(locator.get(leadChar).isEmpty())
            locator.remove(leadChar);
        return pos;
    }


    // Get the next lead char
    private static char getLeadChar(char c, int[] count) {
        int i;
        for (i = c; i < count.length && count[i] == 0; i++);
        count[i]--;
        return (char) i;
    }

    // Create the count[] key indexed frequency array, and the locator HashMap
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
        if(args.length < 0)
            throw new NullPointerException("No arguments given!");
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
