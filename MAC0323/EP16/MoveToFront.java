import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.HashMap;
/**
 *  The main objective of the MoveToFront class is to put the
 *  most frequent chars of the alphabet in the front of the alphabet,
 *  so that the frequent chars are in the beginning of it, which
 *  is to make the Huffman coding easier.
 *
 *  @author Juliano Garcia de Oliveira
 */
public class MoveToFront {
    private static final int R = CircularSuffixArray.R;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    /* Reads a sequence of chars from the standard input. Creates an alphabet
     * sequence, and when reading a char 'c', move that char 'c' to the beginning
     * of the alphabet, then prints out where the 'c' char was previously.
     * For instance, if our alphabet consist of '!ABC', and we read
     * the string 'C!BAA', we have:
     *      sequence        in      out
     *      --------        --      ---
     *      ! A B C         C        3
     *      C ! A B         !        1
     *      ! C A B         B        3
     *      B ! C A         A        3
     *      A B ! C         A        0
     *
     * So the enconding will print out 31330.
     */
    public static void encode(){
        char[] sequence = new char[R];
        // Map a char to a position in the sequence array
        HashMap<Character, Integer> pointTo = getMappers(sequence);
        while (!BinaryStdIn.isEmpty()){
            char c = BinaryStdIn.readChar();
            int originPos = pointTo.get(c);
            BinaryStdOut.write((char)originPos);
            moveUp(originPos, sequence, pointTo);
        }
        BinaryStdOut.close();
    }

    private static HashMap<Character, Integer> getMappers(char[] sequence) {
        HashMap<Character, Integer> pointTo = new HashMap<>();
        for (int i = 0; i < R; i++){
            sequence[i] = (char) i;
            pointTo.put((char)i, i);
        }
        return pointTo;
    }

    // Move the char c to the beginning and rotate the other chars
    private static void moveUp(int originPos, char[] sequence, HashMap<Character, Integer> pointTo) {
        char myChar = sequence[originPos];
        for (int i = originPos; i > 0; i--) {
            sequence[i] = sequence[i - 1];
            pointTo.put(sequence[i], i);
        }
        pointTo.put(myChar, 0);
        sequence[0] = myChar;
    }


    // apply move-to-front decoding, reading from standard input and writing to standard output
    /* Read a given number i (from 0 to R), print the corresponding character
     * and move the ith char to the beginning of the sequence.
     * So, if it received 31330 and our alphabet is only '!ABC', it should print 'C!BAA'.
     */
    public static void decode(){
        char[] sequence = new char[R];
        HashMap<Character, Integer> pointTo = getMappers(sequence);
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            BinaryStdOut.write(sequence[c]);
            moveUp((int)c, sequence, pointTo);
        }
        BinaryStdOut.close();
    }


    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args){
        if(args.length < 0)
            throw new NullPointerException("No arguments given!");
        switch (args[0]) {
            case "-":
                encode();
                break;
            case "+":
                decode();
                break;
            default:
                throw new UnsupportedOperationException("Invalid argument!");
        }
    }

}
