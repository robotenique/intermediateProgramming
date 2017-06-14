import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;

class Pos{
    int x;
    int y;
    private static int width;
    private static int height;
    private static int diagW;
    private static int diagH;
    static void setWidth(int w) {
        width = w;
        Pos.diagW = Pos.width - 1;
    }
    static void setHeight(int h) {
        height = h;
        Pos.diagH = 0;
    }
    static void setDiagW(int d){
        Pos.diagW = d;
    }
    static void setDiagH(int d){
        Pos.diagH = d;
    }
    Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }
    private boolean canGoDown() {
        return (x+1 < Pos.width && y+1 < Pos.height);
    }
    private boolean canGoUp() {
        return (x+1 < Pos.width && y-1 >= 0);
    }
    private static boolean isWithin(int x, int y){
        return x >= 0 && x < Pos.width && y >= 0 && y < Pos.height;
    }
    Pos getNextH() {
        // Return the next position in topological order (Horizontally)
        if(!Pos.isSNode(this) && canGoUp()) {
            return new Pos(x + 1, y - 1);
        }
        else {
            if(this.y <= Pos.height - 1) { // If it's inside the image matrix (not a virtual node)
                Pos.diagH++;
                if (Pos.diagH >= Pos.height) {
                    Pos.diagH = Pos.height - 1;
                    Pos.diagW++;
                    if (Pos.diagW >= Pos.width)
                        return null;
                }
                return new Pos(Pos.diagW, Pos.diagH);
            }
            else {
                if (Pos.isSNode(this)) // Start Node (S)
                    return new Pos(0, 0);
                else // Terminal Node (T)
                    return null;
            }
        }
    }
    Pos getNext() {
        // Returns the next position in topological order
        if(canGoDown()) {
            return new Pos(x + 1, y + 1);
        }
        else {
            if(this.y <= Pos.height - 1) {
                Pos.diagW--;
                if (Pos.diagW < 0) {
                    Pos.diagW = 0;
                    Pos.diagH++;
                    if (Pos.diagH >= Pos.height)
                        return null;
                }
                return new Pos(Pos.diagW, Pos.diagH);
            }
            else {
                if (Pos.isSNode(this)) // Start Node (S)
                    return new Pos(Pos.width - 1, 0);
                else // Terminal Node (T)
                    return null;
            }
        }
    }
    static Queue<Pos> getChild(Pos p) {
        // Get all the childs of a current Position
        Queue<Pos> ret = new Queue<>();
        if(Pos.isSNode(p)) {
            // The first line of the matrix is child of the S node
            for (int i = 0; i < Pos.width; i++)
                ret.enqueue(new Pos(i, 0));
            return ret;
        }
        else if(Pos.isTNode(p))  // The T node has no child
            return null;
        else if(p.y == Pos.height - 1) { // The last line has only 1 child : the T node
            ret.enqueue(new Pos(1, Pos.height));
            return ret;
        }
        // Normal nodes can have up to 3 childs
        int[][] cand = new int[][]{{p.x-1, p.y+1}, {p.x, p.y+1}, {p.x+1, p.y+1}};
        for (int[] aCand : cand)
            if (isWithin(aCand[0], aCand[1]))
                ret.enqueue(new Pos(aCand[0], aCand[1]));
        return ret;
    }
    static Queue<Pos> getChildH(Pos p) {
        // Get all the childs of a current Position
        Queue<Pos> ret = new Queue<>();
        if(Pos.isSNode(p)) {
            // The first column of the matrix is child of the S node
            for (int i = Pos.height - 1; i >= 0; i--)
                ret.enqueue(new Pos(0, i));
            return ret;
        }
        else if(Pos.isTNode(p))  // The T node has no child
            return null;
        else if(p.x == Pos.width - 1) { // The last line has only 1 child : the T node
            ret.enqueue(new Pos(1, Pos.height));
            return ret;
        }
        // Normal nodes can have up to 3 childs
        int[][] cand = new int[][]{{p.x+1, p.y-1}, {p.x+1, p.y}, {p.x+1, p.y+1}};
        for (int[] aCand : cand)
            if (isWithin(aCand[0], aCand[1]))
                ret.enqueue(new Pos(aCand[0], aCand[1]));
        return ret;
    }
    static boolean isSNode(Pos p) {
        return p.y == Pos.height && p.x == 0;
    }
    static boolean isTNode(Pos p) {
        return p.y == Pos.height && p.x == 1;
    }
}

public class SeamCarver {
    private Picture pic;
    private double[][] distTo;
    private Pos[][]     edgeTo;
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        pic = new Picture(picture);
    }
    private int[][] xBorder(int x, int y) {
        return new int[][]{{Math.floorMod(x+1, width()), y}, {Math.floorMod(x-1, width()), y}};
    }
    private int[][] yBorder(int x, int y) {
        return new int[][]{{x, Math.floorMod(y+1, height())}, {x, Math.floorMod(y-1, height())}};
    }
    private double deltaDiff(int[][] pos) {
        int[] p1 = pos[0];
        int[] p2 = pos[1];
        Color a = pic.get(p1[0], p1[1]);
        Color b = pic.get(p2[0], p2[1]);
        return Math.pow(a.getRed() - b.getRed(), 2) +
               Math.pow(a.getGreen() - b.getGreen(), 2) +
               Math.pow(a.getBlue() - b.getBlue(), 2);
    }

    // current picture
    public Picture picture() { return  new Picture(pic);}
    // width of current picture
    public     int width()   {  return pic.width(); }
    // height of current picture
    public     int height() {   return pic.height(); }
    // energy of pixel at column x and row y
    public  double energy(int x, int y)    {
        if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1)
            throw new IndexOutOfBoundsException("Coordinates out of range!");
        double calcDelta = deltaDiff(xBorder(x, y)) + deltaDiff(yBorder(x, y));
        return Math.sqrt(calcDelta);
    }

    // sequence of indices for vertical seam
    public   int[] findVerticalSeam()   {
        Queue<Pos> q;
        initArr();
        for (Pos v = new Pos(0, height()); v != null; v = v.getNext()){
            q = Pos.getChild(v);
            if(q != null)
                for (Pos w : q)
                    relax(w, v, distTo, edgeTo);
        }
        return getPath(new Pos(1, height()));
    }

    // sequence of indices for horizontal seam
    public   int[] findHorizontalSeam() {
        Queue<Pos> q;
        initArr();
        // Important!
        Pos.setDiagH(0);
        Pos.setDiagW(0);
        for (Pos v = new Pos(0, height()); v != null; v = v.getNextH()){
            q = Pos.getChildH(v);
            if(q != null)
                for (Pos w : q)
                    relax(w, v, distTo, edgeTo);
        }
        return getPathH(new Pos(1, height()));
    }

    // remove horizontal seam from current picture
    public    void removeHorizontalSeam(int[] seam)  {
        //TODO: fix this thing
        Picture temp = new Picture(width(), height() - 1);
        for (int i = 0; i < width(); i++) {
            int j = 0, h = 0;
            while(seam[i] != j) {
                temp.set(i, h, pic.get(i, j));
                j++;
                h++;
            }
            j++;
            while(j < height()){
                temp.set(i, h, pic.get(i, j));
                j++;
                h++;
            }
        }
        pic = temp;
    }
    // remove vertical seam from current picture
    public    void removeVerticalSeam(int[] seam) {
        Picture temp = new Picture(width() - 1, height());
        for (int i = 0; i < height(); i++) {
            int j = 0, w = 0;
            while(seam[i] != j) {
                temp.set(w, i, pic.get(j, i));
                j++;
                w++;
            }
            j++;
            while(j < width()){
                temp.set(w, i, pic.get(j, i));
                j++;
                w++;
            }
        }
        pic = temp;
    }

    //----------- Helper methods ----------- //
    // get the energy of a given coordinate, but accepts anything!
    private double energyP(int x, int y){
        // Energy at Source or Terminal node is 0
        if(Pos.isSNode(new Pos(x, y)) || Pos.isTNode(new Pos(x, y)))
            return 0;
        // Outer bounds has no energy
        if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1)
            return 0;
        double calcDelta = deltaDiff(xBorder(x, y)) + deltaDiff(yBorder(x, y));
        return Math.sqrt(calcDelta);
    }
    // Relax a given v->w edge
    private void relax(Pos w, Pos v, double[][] distTo, Pos[][] edgeTo) {
        if(distTo[w.y][w.x] > distTo[v.y][v.x] + energyP(w.x, w.y)){
            distTo[w.y][w.x] = distTo[v.y][v.x] + energyP(w.x, w.y);
            edgeTo[w.y][w.x] = v;
        }
    }
    // Returns the VERTICAL path in correct order
    private int[] getPath(Pos T) {
        Stack<Pos> s = new Stack<>();
        for (Pos at = edgeTo[T.y][T.x]; at != null; at = edgeTo[at.y][at.x])
            s.push(at);
        s.pop();
        int[] ret = new int[height()];
        for (int j = 0; !s.isEmpty(); j++)
            ret[j] = s.pop().x;
        return ret;
    }
    // Returns the HORIZONTAL path in correct
    private int[] getPathH(Pos T) {
        Stack<Pos> s = new Stack<>();
        for (Pos at = edgeTo[T.y][T.x]; at != null; at = edgeTo[at.y][at.x])
            s.push(at);
        if(!s.isEmpty())
            s.pop();
        int[] ret = new int[width()];
        for (int j = 0; !s.isEmpty(); j++)
            ret[j] = s.pop().y;
        return ret;
    }
    // Initializes the matrices used in the seam detection
    private void initArr(){
        Pos.setHeight(height());
        Pos.setWidth(width());
        distTo = new double[height() + 1][width()];
        edgeTo = new Pos[height() + 1][width()];
        for (int i = 0; i < width(); i++)
            for (int j = 0; j < height() + 1; j++)
                distTo[j][i] = Double.POSITIVE_INFINITY;
        edgeTo[height()][0] = null; // The S node don't come from anything!
        distTo[height()][0] = 0;    // The distance to S is 0
    }

    // do unit testing of this class
    public static void main(String[] args)  {
        SeamCarver sc = new SeamCarver(new Picture("inner.jpg"));
    }

}
