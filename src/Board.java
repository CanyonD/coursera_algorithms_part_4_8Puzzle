import java.util.IllegalFormatException;

public class Board {
    private int[][] blocks;
    public Board(int[][] blocks)           // construct a board from an n-by-n array of blocks
    {
        if (blocks == null) throw new IllegalArgumentException();
        this.blocks = blocks;
    }
    // (where blocks[i][j] = block in row i, column j)
    public int dimension()                 // board dimension n
    {
        return blocks.length;
    }
    public int hamming()                   // number of blocks out of place
    {
        return 0;   // TODO just do it
    }
    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        int manhattan = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (blocks[i][j] == 0) {
                    continue;
                }
                int row = blocks[i][j] / dimension();
                int col = dimension();
                if (blocks[i][j] % dimension() > 0) {
                    col = blocks[i][j] % dimension();
                    row++;
                }
                manhattan += Math.abs(row - i + 1) + Math.abs(col - j + 1);
            }
        }
        return manhattan;
    }
    public boolean isGoal()                // is this board the goal board?
    {
        return manhattan() == 0;
    }
    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {
        return this;    // TODO just do it
    }
    public boolean equals(Object y)        // does this board equal y?
    {
        return false;   // TODO just do it
    }
    public Iterable<Board> neighbors()     // all neighboring boards
    {
        return null;
    }
    public String toString()               // string representation of this board (in the output format specified below)
    {
        String out = Integer.toString(dimension());
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                out += " " + blocks[i][j];
            }
            out += "\n";
        }
        return out;
    }

    public static void main(String[] args) // unit tests (not graded)
    {

    }
}
