import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.LinkedStack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Solver {
    private int moves;
    private MinPQ<SearchNode> pq;
    private LinkedStack<Board> solutionB;
    private SearchNode snInitial;
    private SearchNode snFinal;
    private int aux;

    public Solver(Board initial) {
        if(initial == null)
            throw new java.lang.NullPointerException("The initial board is NULL!");
        else if (!initial.isSolvable())
            throw new java.lang.IllegalArgumentException("Unsolvable puzzle");
        snInitial = new SearchNode(initial, moves, null);

        // Choose the algorithm to solve
        solveIDAStar(snInitial);
        //solveAStar(snInitial);

        createPath(snFinal);
    }

    private void solveIDAStar(SearchNode root) {
    /* Solve the sliding puzzle game using the Iterative deepening A* (IDA*) algorithm:
     * Make iterative DFS on the tree, while the priority of the nodes are
     * less than a certain threshold:
     *  The IDA* algorithm works as follows:
     * -> If it finds the goal board, then it creates the path.
     * -> If the priority of the current node is greater than the priority of
     *    the threshold, the IDA* recursion stops, the threshold is updated with
     *    this new priority found, and -->the DFS starts from the beginning<--.
     * -> If neither of the last cases happen, we call recursively the IDA* for
     *    each of the neighbors of the current node.
     *
     * The advantage of using IDA* is that the space is LINEAR, not exponential
     * like the A*!
     */
		int threshold = snInitial.getManhattan(snInitial.b);
		aux = threshold;
		boolean done = false;
		while(!done) {
			done = idaStar(root, threshold);
			if(!done) threshold = aux; // Update the threshold
		}
    }
    
    private boolean idaStar(SearchNode sn, int threshold) {
        int f = sn.getManhattan(sn.b);
        boolean watch = true;
        boolean done;
        if(sn.b.isGoal()) {
            moves = sn.nMovs;
            this.snFinal = sn;
            return true;
        }
        if(f > threshold) {
            this.aux = f;
            return false;
        }
        for (Board neighB : sn.b.neighbors())
            if (watch && neighB.equals(sn.b))
                watch = false;
            else {
                done = idaStar(new SearchNode(neighB, sn.nMovs + 1, sn), threshold);
                if (done)
                    return true;
            }
         return false;
    }

    private boolean solveAStar(SearchNode init) {
    /* Solve the sliding puzzle game using the A* algorithm:
     * Put the root node in the priority queue. Then, while the solution
     * isn't found:
     * -> dequeue the node with the minimum priority (calculated with manhattan / hamming)
     * -> check if the node is the goal node, if it is, return the final path.
     * -> if it's not, enqueue every neighbor if the current node in the pq
     *    if the neighbor is different than the current node.
     * -> return to the loop
     */
        SearchNode sn;
        boolean watch;
        pq = new MinPQ<>();
        pq.insert(init);
        moves = 0;
        while (true) {
            sn = pq.delMin();
            if(sn.b.isGoal())
                return createPath(sn);
            watch = true;
            for(Board neighB : sn.b.neighbors()) {
                if(watch && neighB.equals(sn.b))
                    watch = false;
                else
                    pq.insert(new SearchNode(neighB, sn.nMovs + 1, sn));
            }
        }
    }

    private boolean createPath(SearchNode end) {
        solutionB = new LinkedStack<>();
        SearchNode curr = end;
        moves = end.nMovs;
        while (curr != null) {
            solutionB.push(curr.b);
            curr = curr.parent;
        }
        return  true;
    }

    // min number of moves to solve initial board
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return solutionB;
    }

    class SearchNode implements Comparable<SearchNode> {
        Board b;
        int nMovs;
        SearchNode parent;
        public SearchNode(Board b, int nMovs, SearchNode parent) {
            this.b = b;
            this.nMovs = nMovs;
            this.parent = parent;
        }

        @Override
        public int compareTo(SearchNode that) {
            Integer d1, d2;
            //d1 = getHamming(this.b);
            //d2 = getHamming(that.b);
            // Using manhattan distance
            d1 = this.getManhattan(this.b);
            d2 = that.getManhattan(that.b);
            return d1.compareTo(d2);
        }
        private int getHamming(Board bd) {
            return bd.hamming() + nMovs;
        }

        private int getManhattan(Board bd) {
            return bd.manhattan() + nMovs;
        }
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
        try{
            Solver sv = new Solver(new Board(tiles));
            StdOut.println("Minimum number of moves = "+sv.moves());
            for(Board b : sv.solution())
                StdOut.print(b);
        }catch(java.lang.IllegalArgumentException e){
            StdOut.println(e.getMessage());
        }

    }
}
