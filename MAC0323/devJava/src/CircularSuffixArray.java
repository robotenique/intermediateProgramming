import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    public static final int R = 256; // alphabet length
    String original;
    int N;
    int origPos = 0;
    public Rotation[] rots;

    class Rotation{
        int x; // rotation of original string by x
        Rotation(int x){
            this.x = x;
        }
        // The char at position 'k' in the string rotated by x
        char valueAt(int k){
            if(k < 0 || k > N)
                throw new IndexOutOfBoundsException("k position isn't in the string!");
            return original.charAt((k + x)%N);
        }
        // String representation of a rotation by x
        @Override
        public String toString() {
            int i;
            int c;
            StringBuilder sb = new StringBuilder(N);
            for(i = x, c = 0;c < N; i = (i + 1)%N, c++)
                sb.append(original.charAt(i));
            return sb.toString();
        }
    }

    // circular suffix array of s
    public CircularSuffixArray(String s){
        if(s == null)
            throw new NullPointerException("The string can't be null!");
        original = s;
        N = s.length();
        rots = new Rotation[N];
        for (int i = 0; i < N; i++)
            rots[i] = new Rotation(i);
        sortLSD();
    }

    private void sortLSD() {
        Rotation[] aux = new Rotation[N];
        // sort by key-indexed counting on dth character
        for (int d = N - 1; d >= 0; d--) {
            // compute frequency counts
            int[] count = new int[R + 1];
            for (int i = 0; i < N; i++)
                count[rots[i].valueAt(d) + 1]++;
            // compute cumulates
            for (int r = 0; r < R; r++)
                count[r + 1] += count[r];
            // move data
            for (int i = 0; i < N; i++)
                aux[count[rots[i].valueAt(d)]++] = rots[i];
            // copy back
            System.arraycopy(aux, 0, rots, 0, N);
        }
        // Set the original position for later use
        for (int i = 0; i < N; i++)
            if(rots[i].x == 0)
                origPos = i;
    }

    // length of s
    public int length(){
        return N;
    }

    // returns index of ith sorted suffix
    public int index(int i)  {
        if(i < 0 || i > N)
            throw new IndexOutOfBoundsException("The index is out of range!");
        return rots[i].x;
    }

    // unit testing (not graded)
    public static void main(String[] args){
        String s = StdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for (int i = 0; i < s.length(); i++) {
            StdOut.println(csa.index(i));
        }
    }
}
