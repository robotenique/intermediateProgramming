import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.StdRandom;


public class PointST<Value> {
    RedBlackBST<Point2D, Value> rbst;
    // construct an empty symbol table of points
    public PointST() {
        rbst = new RedBlackBST<Point2D, Value>();
    }
    // is the symbol table empty?
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

    public boolean contains(Point2D p) {
        if(p == null)
            throw new java.lang.NullPointerException("NULL ARGUMENT!");
        return rbst.contains(p);
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

    public Iterable<Point2D> nearest(Point2D p, int k) {
        if(p == null)   throw new java.lang.NullPointerException("NULL ARGUMENT!");
        if(k >= rbst.size()) return points();
        int i = 0;
        PointST<Value> temp = new PointST<>();
        Queue<Point2D> iterable = new Queue<>();
        for (Point2D point : rbst.keys())
            temp.put(point, rbst.get(point));
        while (i < k) {
            Point2D closest = temp.nearest(p);
            iterable.enqueue(closest);
            temp.rbst.delete(closest);
            i++;
        }
        return iterable;
    }


    // unit testing (required)
    public static void main(String[] args) {
        int limit = 100000;
        String filename = args[0];
        String filename2 = args[1];
        In in = new In(filename);
        PointST<Integer> brute = new PointST<>();
        Stopwatch timer = new Stopwatch();

        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.put(p, i);
        }

        in = new In(filename2);
        Queue<Point2D> q = new Queue<>();
        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            q.enqueue(p);
        }


        double time = timer.elapsedTime();
        StdOut.println("Size(PointST) = "+brute.size());
        StdOut.println("Time elapsed = " +time+"s");

        timer = new Stopwatch();
        Point2D pd;
        for(int i = 0; i < limit && !q.isEmpty(); i++) {
            brute.nearest(q.dequeue());
        }
        time = timer.elapsedTime();
        StdOut.println("Time elapsed nearest() = " +time+"s"+"\n");




    }
}
