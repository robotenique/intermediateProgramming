import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import static java.util.Arrays.binarySearch;

public class Board {
    /* Construct a board from an N-by-N array of tiles
     * where tiles[i][j] = tile at row i, column j
     */

    private static int[][] solvedB;
    private static int sz;
    private static boolean wasSolv;
    private int n;
    private int [][] boardM;
    private int manhattan;
    private int hamming;
    private int blkPos;

    public Board(int[][] tiles) {
        n = tiles.length;
        boardM = new int[n][n];
        // Create a deep copy
        for (int i = 0; i < n; i++)
            boardM[i] = tiles[i].clone();
        blkPos = getBlkPos();
        manhattan = -1;
        hamming = -1;
        if(!wasSolv) {
            sz = n;
            calcSolved();
        }
    }

    private int getBlkPos() {
        for (int i = 0; i < n*n; i++)
            if(boardM[i/n][i%n] == 0)
                return i;
        return -1;
    }

    // Return tile at row i, column j (or 0 if blank)
    public int tileAt(int i, int j) {
        if(i < 0 || j < 0 || i >= n || j >= n)
            throw new java.lang.IndexOutOfBoundsException("Position out of range!");
    	return boardM[i][j];
    }
    // board size N
    public int size() {
    	return n;
    }
    //number of tiles out of place
    public int hamming() {
        if(this.hamming != -1)
            return this.hamming;
    	int hamming = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if(boardM[i][j] != 0)
                    if(i !=(boardM[i][j] - 1)/n || j != (boardM[i][j] - 1)%n)
                    hamming++;
        this.hamming = hamming;
        return hamming;
    }
    // sum of Manhattan distances beteen tiles and goal
    public int manhattan() {
        if(this.manhattan != -1)
            return this.manhattan;
        int manhattan = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if(boardM[i][j] != 0)
                    manhattan += Math.abs(i - (boardM[i][j] - 1)/n) + Math.abs(j - (boardM[i][j] - 1)%n);

        /*for (int i = 0; i < n; i++) {
            manhattan += this.inversions(0, i) * 2;
            manhattan += this.inversions(1, i) * 2;
        }*/
        this.manhattan = manhattan;
        return manhattan;
    }

    public static void calcSolved() {
        wasSolv = true;
        solvedB = new int[sz][sz];
        for (int i = 0; i < sz * sz - 1; i++) {
            solvedB[i/sz][i%sz] = i + 1;
        }
    }

    private int[] tuple(int axis, int pos) {
        int[] result = new int[n];
        switch (axis) {
            case 0: // row
                result = boardM[pos].clone();
                break;
            case 1: //column
                for (int i = 0; i < n; i++)
                    result[i] = boardM[i][pos];
                break;
        }
        return result;
    }

    private int[] idealTuple(int axis, int pos) {
        int[] result = new int[n];
        switch (axis) {
            case 0: //row
                result = solvedB[pos].clone();
                break;
            case 1: //column
                for (int i = 0; i < n; i++)
                    result[i] = solvedB[i][pos];
                break;
        }
        return result;
    }

    public int inversions(int axis, int pos) {
        int[] have = this.tuple(axis, pos);
        int[] want = this.idealTuple(axis, pos);
        int inversions = 0;
        // For each pair of squares, if both numbers are supposed to be in this
        // tuple, and neither is 0 (blank)...
        for (int i = 1, iPos; i < this.n; i++) {
            if (have[i] != 0 && 0 <= (iPos = binarySearch(want, have[i]))) {
                for (int j = 0, jPos; j < i; j++) {
                    if (have[j] != 0 && 0 <= (jPos = binarySearch(want, have[j]))) {
                        // ... and are inverted, count it as a conflict.
                        if ((have[i] < have[j]) != (i < j)) {
                            inversions++;
                        }
                    }
                }
            }
        }
        return inversions;
    }
    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if(boardM[i][j] != 0)
                    if(i !=(boardM[i][j] - 1)/n || j != (boardM[i][j] - 1)%n)
                        return false;

        return true;
    }
    //is this board solvable?
    public boolean isSolvable() {
        int inv = 0;
        int blkRow = -1;
        for (int i = 0; i < n*n - 1; i++) {
            if(boardM[i/n][i%n] == 0) {
                blkRow = i/n;
                continue;
            }
            for (int j = i + 1; j < n * n; j++)
                if (boardM[j/n][j%n] != 0 && boardM[i/n][i%n] > boardM[j/n][j%n])
                    inv++;
        }
        if(n%2 != 0)
            return inv%2 == 0;
        else
            return (inv+blkRow)%2 != 0;
    }
    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        for (int i = 0; i < that.size()*that.size(); i++)
            if(this.boardM[i/n][i%n] != that.boardM[i/n][i%n])
                return false;
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        RandomizedQueue<Board> bagN = new RandomizedQueue<>();
        int x = blkPos/n;
        int y = blkPos%n;
        int[][] cp = new int[n][n];
        for (int i = 0; i < n; i++)
            cp[i] = boardM[i].clone();
        int[][] nc = new int[][]{{x - 1, y},{x + 1, y},{x, y - 1},{x, y + 1}};
        for (int i = 0; i < 4; i++)
            if(nc[i][0] >= 0 && nc[i][0] < n && nc[i][1] >= 0 && nc[i][1] < n) {
                swap(nc[i][0], nc[i][1], cp);
                bagN.enqueue(new Board(cp));
                swap(nc[i][0], nc[i][1], cp);
            }
        return bagN;
    }

    // Swap a x,y position with the blank one in the given array
    private void swap(int x, int y, int [][] arr) {
        int aux = arr[blkPos/n][blkPos%n];
        arr[blkPos/n][blkPos%n] = arr[x][y];
        arr[x][y] = aux;
    }

    // string representation of this board
    public String toString() {
    	StringBuilder s = new StringBuilder();
    	s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        int i = 0;
        int[][] tiles = new int[n][n];
        while (!in.isEmpty()) {
            tiles[i/n][i%n] = in.readInt();
            i++;
        }
        Board b = new Board(tiles);
        StdOut.print(b);
        StdOut.println("Hamming  = "+ b.hamming());
        StdOut.println("Manhattan = "+ b.manhattan());
        StdOut.println("isGoal = "+ b.isGoal());
        StdOut.println("isSolvable = "+  b.isSolvable());
    }
}
