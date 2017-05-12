import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
public class PointST<Value> {
    RedBlackBST<Point2D, Value> rbst;
    // construct an empty symbol table of points
    public PointST() {
        rbst = new RedBlackBST<Point2D, Value>();
    }
    // is the symbol talbe empty?
    public boolean isEmpty() {
        return rbst.isEmpty();
    }

    // number of points
    public int size() {
        return rbst.size();
    }
    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if(p == null || val == null)
            throw new java.lang.NullPointerException("NULL ARGUMENT!");
        rbst.put(p, val);
    }

    // value associated with point p
    public Value get(Point2D p) {
        if(p == null)
            throw new java.lang.NullPointerException("NULL ARGUMENT!");
        return rbst.get(p);
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        return rbst.keys();
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if(rect == null)
            throw new java.lang.NullPointerException("NULL ARGUMENT!");
        Queue<Point2D> internalPoints = new Queue<>();
        for(Point2D p : rbst.keys())
            if(rect.contains(p))
                internalPoints.enqueue(p);
        return internalPoints;
    }

    // a nearest neighbor to point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if(p == null)
            throw new java.lang.NullPointerException("NULL ARGUMENT!");
        if(isEmpty())
            return null;
        Point2D ptmp = new Point2D(0,0);
        double minD =  Double.POSITIVE_INFINITY;
        for(Point2D ptable : rbst.keys()) {
            double tmpD = p.distanceSquaredTo(ptable);
            if(tmpD < minD) {
                minD = tmpD;
                ptmp = ptable;
            }
        }
        return ptmp;
    }

    // unit testing (required)
    public static void main(String[] args) {

    }
}
