import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private final MinPQ pq = new MinPQ();

    private searchNode solutionG = null;
    private boolean solvableG = false;
    private int movesG = -1;

    private class searchNode implements Comparable<searchNode> {
        private Board board;
        private searchNode previous;
        private int nOfMoves;
        private boolean twin=false;

        public int compareTo(searchNode that) {
            int thisWeight = this.board.manhattan() + this.nOfMoves;
            int thatWeight = that.board.manhattan() + that.nOfMoves;
            if (thisWeight > thatWeight)
                return 1;
            if (thisWeight < thatWeight)
                return -1;
            if(this.board.manhattan()>that.board.manhattan()) return 1;
            if(this.board.manhattan()<that.board.manhattan()) return -1;
            return 0;
        }

    }

    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        searchNode s = new searchNode();
        s.board = initial;
        s.previous = null;
        s.nOfMoves = 0;

        searchNode stwin = new searchNode();
        stwin.board = initial.twin();
        stwin.previous = null;
        stwin.nOfMoves = 0;
        stwin.twin=true;

        pq.insert(s);
        pq.insert(stwin);
        while (!solve()) {
        }
        if (this.isSolvable())
            solutionG = (searchNode) pq.delMin();
        else
            solutionG = null;
    }

    private boolean solve() {
        searchNode minNode = (searchNode) pq.delMin();


        if (minNode.board.isGoal()) {
            pq.insert(minNode);
            solvableG = true;
            movesG = minNode.nOfMoves;
            if(minNode.twin){
                solvableG = false;
                movesG = -1;
            }
            return true;
        }
        Iterable<Board> neighbors = minNode.board.neighbors();

        for (Board next : neighbors) {
            boolean doEnq=true;
            if(minNode.previous==null){
                doEnq=true;
            }
            else{
                if(minNode.previous.board.equals(next)) doEnq=false;
            }
            if(doEnq){
                searchNode sn = new searchNode();
                sn.previous = minNode;
                sn.board = next;
                sn.nOfMoves = minNode.nOfMoves + 1;
                sn.twin = minNode.twin;
                pq.insert(sn);
            }

        }
        return false;

    }

    public boolean isSolvable()            // is the initial board solvable?
    {
        return this.solvableG;
    }
    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
        return this.movesG;
    }
    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        if (solutionG == null) {
            return null;
        }

        Stack<Board> stack = new Stack<Board>();
        searchNode node = solutionG;
        stack.push(node.board);
        while (node.previous != null) {
            stack.push(node.previous.board);
            node = node.previous;
        }

        return stack;
    }
    public static void main(String[] args) // solve a slider puzzle (given below)
    {

    }
}
