import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

public class Solver {
    private Node goalNode;
    private static MinPQ<Node> pq = new MinPQ<Node>();
    private static MinPQ<Node> pqTwin = new MinPQ<Node>();

    private class Node implements Comparable<Node>{
        public Board board;
        public Node previous;
        public int moves;

        public int compareTo(Node that){
            //StdOut.println("i:" + this.priority() + " j:" + that.priority() + " "+ ((this.priority() > that.priority()) ? 1 :  -1));
            if(this.priority() == that.priority()) return 0;
            return (this.priority() > that.priority()) ? 1 :  -1;
        }

        public Node(Board b, Node prev, int m){
            board = b;
            previous = prev;
            moves = m;
        }

        public int priority(){
            return board.manhattan() + moves;
        }
    }

    public Solver(Board initial){
        if (initial == null) {
            throw new java.lang.NullPointerException();
        }
        Node currentNode = new Node(initial, null, 0);
        Node currentTwin = new Node(initial.twin(), null, 0);
        pq.insert(currentNode);
        pqTwin.insert(currentTwin);

        while(!currentNode.board.isGoal() && !currentNode.board.isGoal()){

            currentNode = pq.delMin();
            currentTwin = pqTwin.delMin();

            for(Board b : currentNode.board.neighbors()) {
                if(!b.equals(currentNode.board))
                    pq.insert(new Node(b, currentNode, currentNode.moves + 1));
            }

            for(Board b : currentTwin.board.neighbors()) {
                if(!b.equals(currentNode.board))
                    pqTwin.insert(new Node(b, currentTwin, currentTwin.moves + 1));
            }
        }

        if(currentNode.board.isGoal())
            goalNode = currentNode;
        else
            goalNode = currentTwin;
    }

    public Iterable<Board> solution(){
        Queue<Board> trace = new Queue<Board>();
        trace.enqueue(goalNode.board);
        while (goalNode.previous != null){
            goalNode = goalNode.previous;
            trace.enqueue(goalNode.board);
        }

        return trace;
    }

    public boolean isSolvable(){
        return goalNode != null;
    }

    public int moves(){
        if (isSolvable())
            return goalNode.moves;
        else
            return -1;
    }
}
