import java.util.Comparator;

/**
 *  The CircularSuffixArray class provides a structure that represents
 *  a Circular Suffix Array and it's sorted counterpart.
 *  The 'circular' method is rotation to the left, as shown below.
 *  For example, consider the string 'abac!'. The CSA of this string is:
 *       Original         Sorted        index(i)
 *      [0] abac!        [0] !abac        [4]
 *      [1] bac!a        [1] abac!        [0]
 *      [2] ac!ab        [2] ac!ab        [2]
 *      [3] c!aba        [3] bac!a        [1]
 *      [4] !abac        [4] c!aba        [3]
 *
 * The sorting of the rotations array is made using an Optimized
 * merge sort implementation. The strings are built implicitly:
 * For a given rotation X (original string rotated X times to the left),
 * the character at position K in this string is obtained by:
 *  char c = original.charAt((K + X)%N);
 * So, no strings are built explicitly, and the sorting doesn't create
 * the string either.
 *  @author Juliano Garcia de Oliveira
 */
public class CircularSuffixArray {
    public static final int R = 256; // alphabet length
    String original; // the original string
    int N; // The length of the string, also the length of the array
    int origPos = 0; // Position in which the original string ended up in the sorted array
    Integer[] rotations; // Array with all string rotations

    // Comparator for MergeSort
    class CmpHelper implements Comparator<Integer>{
        /* Compares two string rotations x and y.
         * Do an implicit strcmp by obtaining the char and comparing it
         */
        @Override
        public int compare(Integer x, Integer y) {
            int n = N;
            int i = 0;
            int j = 0;
            while (n-- != 0) {
                char c1 = original.charAt((i++ + x)%N);
                char c2 = original.charAt((j++ + y)%N);
                if(c1 != c2)
                    return c1 - c2;
            }
            return 0;
        }
    }

    // circular suffix array of s
    public CircularSuffixArray(String s){
        if(s == null)
            throw new NullPointerException("The string can't be null!");
        original = s;
        N = s.length();
        rotations = new Integer[N];
        // At the beginning, rotation[x] = original string rotated by x
        for (int i = 0; i < N; i++)
            rotations[i] = i;
        // Sort the rotations using a CmpHelper to compare each rotation
        MergeStr.sort(rotations, new CmpHelper());
        // Obtain the origPos value
        for (int i = 0; i < N; i++)
            if(rotations[i] == 0) {
                origPos = i;
                break;
            }
    }

    // length of s
    public int length(){
        return N;
    }

    // returns index of ith sorted suffix
    public int index(int i)  {
        /* Returns the number of rotations made in the original
         * string to obtain the string in sorted[i]
         */
        if(i < 0 || i > N)
            throw new IndexOutOfBoundsException("The index is out of range!");
        return rotations[i];
    }

    /* Sorting using Least Significant Digit Radix.
     * Unused at the moment because it's quadratic most of the time
     */
    private void sortLSD() {
        int[] aux = new int[N];
        // sort by key-indexed counting on dth character
        for (int d = N - 1; d >= 0; d--) {
            // compute frequency counts
            int[] count = new int[R + 1];
            for (int i = 0; i < N; i++)
                count[original.charAt((d + rotations[i])%N) + 1]++;
            // compute cumulates
            for (int r = 0; r < R; r++)
                count[r + 1] += count[r];
            // move data
            for (int i = 0; i < N; i++)
                aux[count[original.charAt((d + rotations[i])%N)]++] = rotations[i];
            for (int i = 0; i < N; i++)
                rotations[i] = aux[i];
        }
        // Set the original position for later use
        for (int i = 0; i < N; i++)
            if(rotations[i] == 0)
                origPos = i;
    }
    // Returns the original string rotated by x (to the left)
    private String repr(int x){
        int i;
        int c;
        StringBuilder sb = new StringBuilder(N); //Optimization
        for(i = x, c = 0;c < N; i = (i + 1)%N, c++)
            sb.append(original.charAt(i));
        return sb.toString();
    }

    public static void main(String[] args){

    }
}

// Optimized Merge Sort class
class MergeStr {
    private static final int CUTOFF = 7;  // cutoff to insertion sort
    private static void merge(Comparable[] src, Comparable[] dst, int lo, int mid, int hi) {
        int i = lo, j = mid+1;
        for (int k = lo; k <= hi; k++) {
            if      (i > mid)              dst[k] = src[j++];
            else if (j > hi)               dst[k] = src[i++];
            else if (less(src[j], src[i])) dst[k] = src[j++];   // to ensure stability
            else                           dst[k] = src[i++];
        }
    }

    private static void sort(Comparable[] src, Comparable[] dst, int lo, int hi) {
        // if (hi <= lo) return;
        if (hi <= lo + CUTOFF) {
            insertionSort(dst, lo, hi);
            return;
        }
        int mid = lo + (hi - lo) / 2;
        sort(dst, src, lo, mid);
        sort(dst, src, mid+1, hi);

        // if (!less(src[mid+1], src[mid])) {
        //    for (int i = lo; i <= hi; i++) dst[i] = src[i];
        //    return;
        // }

        // using System.arraycopy() is a bit faster than the above loop
        if (!less(src[mid+1], src[mid])) {
            System.arraycopy(src, lo, dst, lo, hi - lo + 1);
            return;
        }

        merge(src, dst, lo, mid, hi);
    }

    /**
     * Rearranges the array in ascending order, using the natural order.
     * @param a the array to be sorted
     */
    public static void sort(Comparable[] a) {
        Comparable[] aux = a.clone();
        sort(aux, a, 0, a.length-1);
    }

    // sort from a[lo] to a[hi] using insertion sort
    private static void insertionSort(Comparable[] a, int lo, int hi) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j-1]); j--)
                exch(a, j, j-1);
    }


    /*******************************************************************
     *  Utility methods.
     *******************************************************************/

    // exchange a[i] and a[j]
    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    // is a[i] < a[j]?
    private static boolean less(Comparable a, Comparable b) {
        return a.compareTo(b) < 0;
    }

    // is a[i] < a[j]?
    private static boolean less(Object a, Object b, Comparator comparator) {
        return comparator.compare(a, b) < 0;
    }


    /*******************************************************************
     *  Version that takes Comparator as argument.
     *******************************************************************/

    /**
     * Rearranges the array in ascending order, using the provided order.
     *
     * @param a the array to be sorted
     * @param comparator the comparator that defines the total order
     */
    public static void sort(Object[] a, Comparator comparator) {
        Object[] aux = a.clone();
        sort(aux, a, 0, a.length-1, comparator);
    }

    private static void merge(Object[] src, Object[] dst, int lo, int mid, int hi, Comparator comparator) {
        int i = lo, j = mid+1;
        for (int k = lo; k <= hi; k++) {
            if      (i > mid)                          dst[k] = src[j++];
            else if (j > hi)                           dst[k] = src[i++];
            else if (less(src[j], src[i], comparator)) dst[k] = src[j++];
            else                                       dst[k] = src[i++];
        }
    }


    private static void sort(Object[] src, Object[] dst, int lo, int hi, Comparator comparator) {
        // if (hi <= lo) return;
        if (hi <= lo + CUTOFF) {
            insertionSort(dst, lo, hi, comparator);
            return;
        }
        int mid = lo + (hi - lo) / 2;
        sort(dst, src, lo, mid, comparator);
        sort(dst, src, mid+1, hi, comparator);

        // using System.arraycopy() is a bit faster than the above loop
        if (!less(src[mid+1], src[mid], comparator)) {
            System.arraycopy(src, lo, dst, lo, hi - lo + 1);
            return;
        }

        merge(src, dst, lo, mid, hi, comparator);
    }

    // sort from a[lo] to a[hi] using insertion sort
    private static void insertionSort(Object[] a, int lo, int hi, Comparator comparator) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j-1], comparator); j--)
                exch(a, j, j-1);
    }

}
