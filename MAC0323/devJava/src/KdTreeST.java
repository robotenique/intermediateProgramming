import edu.princeton.cs.algs4.*;

// Auxiliar class only to implement the Comparable
class Point2Dordered implements Comparable<Point2Dordered> {
    /* This represents a point, but they are ordered
     * by the distance from the current point to the
     * global query point (set before everything)
     */
    private static Point2D queryP;
    public Point2D point;
    private double dist;

    public Point2Dordered(double x, double y) {
        point = new Point2D(x, y);
        dist = point.distanceSquaredTo(queryP);
    }

    public static void setQueryP(Point2D qp) {
        queryP = qp;
    }

    @Override
    public int compareTo(Point2Dordered that) {
        if(this.dist < that.dist)
            return -1;
        else if(this.dist > that.dist)
            return 1;
        else
            return 0;
    }
}

public class KdTreeST<Value> {
    private int n;
    private Node root;
    private static final double pInf = Double.POSITIVE_INFINITY;
    private static final double nInf = Double.NEGATIVE_INFINITY;

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

    // recursively insert the point in the kdtree
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

    // returns the axis-aligned rectangle of a new point
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

    // recursively get the value associated with p, returns null if not in the tree
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
        // simple bfs to do level order traversal
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

    // all points that are inside the given rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if(rect == null)
            throw new java.lang.NullPointerException("NULL ARGUMENT!");
        Queue<Point2D> internalPoints = new Queue<>();
        if(isEmpty()) return internalPoints;
        internalPoints = rangeSearch(root, rect, internalPoints);
        return internalPoints;
    }

    // recursively make a range search
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

    // returns true if the tree contains the point p
    public boolean contains(Point2D p) {
        if(p == null)
            throw new java.lang.NullPointerException("NULL ARGUMENT!");
        return get(p) != null;
    }

    // a nearest neighbor to point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if(p == null)
            throw new java.lang.NullPointerException("NULL ARGUMENT!");
        if(isEmpty())   return null;
        Point2D closest = new Point2D(root.p.x(), root.p.y());
        closest = closestPoint(root, p, closest);
        return closest;
    }

    // recursively obtain the closest point
    private Point2D closestPoint(Node curr, Point2D p, Point2D closest) {
        if(curr == null)    return closest;
        if(p.distanceSquaredTo(closest) > p.distanceSquaredTo(curr.p))
            closest = curr.p;
        if(p.distanceSquaredTo(closest) > curr.rect.distanceSquaredTo(p)) {
            if(curr.depth%2 == 0)
                if(p.x() < curr.p.x()) {
                    closest = closestPoint(curr.lb, p, closest);
                    closest = closestPoint(curr.rt, p, closest);
                }
                else {
                    closest = closestPoint(curr.rt, p, closest);
                    closest = closestPoint(curr.lb, p, closest);
                }
            else
                if(p.y() < curr.p.y()) {
                    closest = closestPoint(curr.lb, p, closest);
                    closest = closestPoint(curr.rt, p, closest);
                }
                else {
                    closest = closestPoint(curr.rt, p, closest);
                    closest = closestPoint(curr.lb, p, closest);
                }
        }
        return closest;
    }

    // Return the k points that are closest to the query point (in any order)
    public Iterable<Point2D> nearest(Point2D p, int k) {
        if(p == null)   throw new java.lang.NullPointerException("NULL ARGUMENT!");
        if(k >= n)  return points();
        Point2Dordered.setQueryP(p);
        MaxPQ<Point2Dordered> nearestK = new MaxPQ<>();
        Queue<Point2D> iterable = new Queue<>();
        nearestK = searchNearestK(root, p, k, nearestK);
        for (Point2Dordered pord : nearestK)
            iterable.enqueue(pord.point);
        return iterable;
    }

    // recursively search and add to the priority queue the closests k points
    private MaxPQ<Point2Dordered> searchNearestK (Node curr, Point2D p, int k, MaxPQ<Point2Dordered> pq) {
        if(curr == null) return pq;
        if(pq.size() < k) {
            pq.insert(new Point2Dordered(curr.p.x(), curr.p.y()));
        }
        else if (p.distanceSquaredTo(pq.max().point) > p.distanceSquaredTo(curr.p)) {
            pq.delMax();
            pq.insert(new Point2Dordered(curr.p.x(), curr.p.y()));
        }
        if(p.distanceSquaredTo(pq.max().point) > curr.rect.distanceSquaredTo(p)) {
            if(curr.depth%2 == 0) {
                if (p.x() < curr.p.x()) {
                    pq = searchNearestK(curr.lb, p, k, pq);
                    pq = searchNearestK(curr.rt, p, k, pq);
                } else {
                    pq = searchNearestK(curr.rt, p, k, pq);
                    pq = searchNearestK(curr.lb, p, k, pq);
                }
            }
            else {
                if (p.y() < curr.p.y()) {
                    pq = searchNearestK(curr.lb, p, k, pq);
                    pq = searchNearestK(curr.rt, p, k, pq);
                } else {
                    pq = searchNearestK(curr.rt, p, k, pq);
                    pq = searchNearestK(curr.lb, p, k, pq);
                }
            }
        }
        return pq;
    }

    // unit testing (required)
    public static void main(String[] args) {
        
        KdTreeST<Integer> kdtree = new KdTreeST<>();
        StdOut.println("get em kdtree vazia = "+kdtree.get(new Point2D(2, 2)));
        kdtree.put(new Point2D(1, 2), 0);
        kdtree.put(new Point2D(5, 4), 1);
        kdtree.put(new Point2D(2, 3), 2);
        kdtree.put(new Point2D(4, 7), 3);
        kdtree.put(new Point2D(9, 6), 4);
        Point2D iwant = new Point2D(5, 5);
        kdtree.put(new Point2D(9, 6), 100); // update value
        StdOut.println("get (7, 2) = "+kdtree.get(new Point2D(7, 2)));
        StdOut.println("get (0, 0) = "+kdtree.get((new Point2D(0, 0))));
        StdOut.println("get (9, 6) = "+kdtree.get(new Point2D(9, 6)));
        StdOut.println("NEAREST POINT FROM (5, 5) = "+kdtree.nearest(new Point2D(5, 5)));
        StdOut.println("Searching the 3 closest points from (5, 5) : ");
        for (Point2D pClose : kdtree.nearest(new Point2D(5, 5), 20))
            StdOut.println(pClose);

        StdOut.println("===================KDTREE2===================");
        KdTreeST<Integer> kdtree2 = new KdTreeST<>();
        kdtree2.put(new Point2D(-10, 0), 0);
        kdtree2.put(new Point2D(0, 0), 1);
        kdtree2.put(new Point2D(1, 0), 2);
        kdtree2.put(new Point2D(1,1), 3);
        kdtree2.put(new Point2D(1,-1), 4);
        kdtree2.put(new Point2D(2,0), 5);
        for(Point2D p: kdtree2.points()) {
            StdOut.print(p + "Distance = "+p.distanceSquaredTo(new Point2D(-9, -1))+"\n");

        }
        StdOut.println("----------------------------------------------------");
        for (Point2D pClose : kdtree2.nearest(new Point2D(-9, -1), 3)) {
            StdOut.print(pClose);
            StdOut.print("Distance = "+pClose.distanceSquaredTo(new Point2D(-9, -1))+"\n");
        }

    }
}
