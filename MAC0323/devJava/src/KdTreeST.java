import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

public class KdTreeST<Value> {
    private int n;
    private Node root;
    private final double pInf = Double.POSITIVE_INFINITY;
    private final double nInf = Double.NEGATIVE_INFINITY;
    private class Node {
        private Point2D p; // the point
        private Value val; // the symbol table maps the point to this value
        private RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb; // the left/bottom subtree
        private Node rt; // the right/top subtree
        private int depth; // the depth of a given node

        public Node(Point2D p, Value val, RectHV rect, int depth) {
            this.p = p;
            this.val = val;
            this.rect = rect;
            this.depth = depth;
        }

        // TODO: remove this debug method
        public void printDebug() {
            StdOut.println(p+" : "+val+" rect = "+rect+" depth = "+depth);
        }
    }

    // construct an empty symbol table of points
    public KdTreeST() {
        n = 0;
    }

    // is the symbol tablee empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // number of points
    public int size() {
        return n;
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if(p == null || val == null)
            throw new java.lang.NullPointerException("NULL ARGUMENT!");
        if(root == null) {
            RectHV rr = new RectHV(nInf, nInf, pInf, pInf);
            root = new Node(p, val, rr, 0);
        }
        else
            root = put(root, p, val, null);
        n++;
    }

    private Node put(Node x, Point2D p, Value val, Node parent) {
        if(x == null)
            return new Node(p, val, getRect(p, parent),parent.depth + 1);
        if(x.p.equals(p))
            x.val = val;
        else if (x.depth%2 == 0) {
            if (p.x() < x.p.x())
                x.lb = put(x.lb, p, val, x);
            else
                x.rt = put(x.rt, p, val, x);
        }
        else {
            if(p.y() < x.p.y())
                x.lb = put(x.lb, p, val, x);
            else
                x.rt = put(x.rt, p, val, x);
        }
        return x;
    }

    private RectHV getRect(Point2D p, Node parent) {
        RectHV rr;
        if(parent.depth%2 == 0)
            if(p.x() < parent.p.x())
                return new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.p.x(), parent.rect.ymax());
            else
                return new RectHV(parent.p.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
        else
            if(p.y() < parent.p.y())
                return new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.p.y());
            else
                return new RectHV(parent.rect.xmin(), parent.p.y(), parent.rect.xmax(), parent.rect.ymax());
    }

    // value associated with point p
    public Value get(Point2D p) {
        if(p == null)
            throw new java.lang.NullPointerException("NULL ARGUMENT!");
        return get(root, p);
    }

    private Value get(Node x, Point2D p) {
        if(x == null)
            return null;
        if(x.p.equals(p))
            return x.val;
        if(x.depth%2==0) {
            if(p.x() < x.p.x())
                return get(x.lb, p);
            else
                return get(x.rt, p);
        }
        else {
            if(p.y() < x.p.y())
                return get(x.lb, p);
            else
                return get(x.rt, p);
        }
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        // simple bfs
        Node curr;
        Queue<Node> bfsq = new Queue<>();
        Queue<Point2D> iterable = new Queue<>();
        if(root == null) return iterable;
        bfsq.enqueue(root);
        while(!bfsq.isEmpty()) {
            curr = bfsq.dequeue();
            iterable.enqueue(curr.p);
            if (curr.lb != null)
                bfsq.enqueue(curr.lb);
            if(curr.rt != null)
                bfsq.enqueue(curr.rt);
        }
        return  iterable;
    }

    // todo: remove this debug method
    public Iterable<Node> nodes() {
        Node curr;
        Queue<Node> bfsq = new Queue<>();
        Queue<Node> iterable = new Queue<>();
        if(root == null) return iterable;
        bfsq.enqueue(root);
        while(!bfsq.isEmpty()) {
            curr = bfsq.dequeue();
            iterable.enqueue(curr);
            if(curr.lb != null)
                bfsq.enqueue(curr.lb);
            if(curr.rt != null)
                bfsq.enqueue(curr.rt);
        }
        return iterable;
    }


    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if(rect == null)
            throw new java.lang.NullPointerException("NULL ARGUMENT!");
        Queue<Point2D> internalPoints = new Queue<>();
        if(isEmpty()) return internalPoints;
        internalPoints = rangeSearch(root, rect, internalPoints);
        return internalPoints;
    }

    private Queue<Point2D> rangeSearch(Node curr, RectHV rect, Queue<Point2D> internalPoints) {
        if(curr == null) return internalPoints;
        if(rect.intersects(curr.rect)) {
            if(rect.contains(curr.p))
                internalPoints.enqueue(curr.p);
            internalPoints = rangeSearch(curr.lb, rect, internalPoints);
            internalPoints = rangeSearch(curr.rt, rect, internalPoints);
        }
        return internalPoints;
    }

    // a nearest neighbor to point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if(p == null)
            throw new java.lang.NullPointerException("NULL ARGUMENT!");
        if(isEmpty())   return null;
        return null;
    }

    // Return the k points that are closest to the query point (in any order)
    public Iterable<Point2D> nearest(Point2D p, int k) {
        return null;
    }

    // unit testing (required)
    public static void main(String[] args) {
        KdTreeST<Integer> kdtree = new KdTreeST<>();
        StdOut.println("get em kdtree vazia = "+kdtree.get(new Point2D(2, 2)));
        kdtree.put(new Point2D(7, 2), 0);
        kdtree.put(new Point2D(5, 4), 1);
        kdtree.put(new Point2D(2, 3), 2);
        kdtree.put(new Point2D(4, 7), 3);
        kdtree.put(new Point2D(9, 6), 4);
        for (KdTreeST.Node n : kdtree.nodes())
            n.printDebug();
        kdtree.put(new Point2D(9, 6), 100); // update value
        StdOut.println("get (7, 2) = "+kdtree.get(new Point2D(7, 2)));
        StdOut.println("get (0, 0) = "+kdtree.get((new Point2D(0, 0))));
        StdOut.println("get (9, 6) = "+kdtree.get(new Point2D(9, 6)));
    }
}
