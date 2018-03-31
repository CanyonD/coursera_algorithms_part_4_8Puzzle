import java.util.Comparator;
import java.util.Iterator;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private MinPQ<PQNode> pq;
    private MinPQ<PQNode> pqTwin;
    private PQNode finalMin = null;
    private boolean unsolvable = false;

    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)

        Board twin = initial.twin();

        PQNodeManhattenComparator comparator = new PQNodeManhattenComparator();

        pq = new MinPQ<PQNode>(comparator);
        pqTwin = new MinPQ<PQNode>(comparator);

        PQNode nodeInitial = new PQNode(initial, 0, null);
        PQNode nodeInitialTwin = new PQNode(twin, 0, null);

        pq.insert(nodeInitial);
        pqTwin.insert(nodeInitialTwin);

        PQNode min = (PQNode)pq.delMin();
        PQNode minTwin = (PQNode)pqTwin.delMin();

        while (!(min.board.isGoal() || minTwin.board.isGoal())) {
            Iterator<Board> neighbors = min.board.neighbors().iterator();
            Iterator<Board> neighborsTwin = minTwin.board.neighbors().iterator();

            while (neighbors.hasNext()) {
                Board neighbor = neighbors.next();
                if (!seenBefore(neighbor, min)) {
                    PQNode node = new PQNode(neighbor, min.moves + 1, min);
                    pq.insert(node);
                }
            }

            // twin
            while (neighborsTwin.hasNext()) {
                Board neighbor = neighborsTwin.next();
                if (!seenBefore(neighbor, minTwin)) {
                    PQNode node = new PQNode(neighbor, minTwin.moves + 1, minTwin);
                    pqTwin.insert(node);
                }
            }

            min = (PQNode)pq.delMin();
            minTwin = (PQNode)pqTwin.delMin();
        }

        if (minTwin.board.isGoal()) {
            unsolvable = true;
        }
        finalMin = min;
    }

    private boolean seenBefore(Board newBoard, PQNode existingBoards) {
        PQNode curNode = existingBoards;
        while (curNode != null) {
            if (newBoard.equals(curNode.board)) {
                return true;
            }
            curNode = curNode.prevBoardNode;
        }
        return false;
    }

    public boolean isSolvable() {
        // is the initial board solvable?
        return !unsolvable;
    }

    public int moves() {
        // min number of moves to solve initial board; -1 if no solution
        if (unsolvable) {
            return -1;
        }
        return finalMin.moves;
    }

    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if no solution
        if (unsolvable) {
            return null;
        }
        Stack<Board> history = new Stack<Board>();
        PQNode curNode = finalMin;
        while (curNode != null) {
            history.push(curNode.board);
            curNode = curNode.prevBoardNode;
        }
        return history;
    }

    public static void main(String[] args) {
        // solve a slider puzzle (given below)
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    private class PQNodeManhattenComparator implements Comparator<PQNode> {
        @Override
        public int compare(PQNode node1, PQNode node2) {
            return
                    new Integer(node1.board.manhattan() + node1.moves).compareTo(new Integer(node2.board.manhattan() + node2.moves));
        }
    }

    private class PQNode {
        Board board;
        int moves;
        PQNode prevBoardNode;

        public PQNode(Board board, int moves, PQNode prevBoardNode) {
            this.board = board;
            this.moves = moves;
            this.prevBoardNode = prevBoardNode;
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("PQNode (moves = " + moves + ", minManhattan = " + board.manhattan() + ")\n");
            sb.append(board.toString() + "\n");
            return sb.toString();
        }
    }
}