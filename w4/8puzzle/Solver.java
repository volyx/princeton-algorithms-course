import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Solver {

    private final List<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        this.solution = solve(initial);
    }

    private List<Board> solve(Board initial) {

        final MinPQ<SearchNode> minPQ = new MinPQ<>();

        minPQ.insert(new SearchNode(null, initial));
        minPQ.insert(new SearchNode(null, initial.twin()));

        while (!minPQ.min().board.isGoal()) {
            final SearchNode searchNode = minPQ.delMin();
            for (Board childBoard : searchNode.board.neighbors()) {
                SearchNode childNode = new SearchNode(searchNode, childBoard);
                if (isAncestors(searchNode, childNode)) {
                    continue;
                }
                minPQ.insert(childNode);
            }
        }

        List<Board> path = new ArrayList<>();
        SearchNode currentNode = minPQ.min();
        while (currentNode != null) {
            path.add(currentNode.board);
            currentNode = currentNode.previous;
        }
        Collections.reverse(path);

        if (path.isEmpty()) {
            return Collections.emptyList();
        }

        return path.get(0).equals(initial) ? path : null;
    }

    private static boolean isAncestors(SearchNode searchNode, SearchNode childNode) {
        if (searchNode.previous == null) {
            return false;
        }
        if (searchNode.previous.hamming != childNode.hamming) {
            return false;
        }
        if (searchNode.previous.manhattan != childNode.manhattan) {
            return false;
        }
        return searchNode.previous.board.equals(childNode.board);
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return !isSolvable() ? -1 : solution.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    private static class SearchNode implements Comparable<SearchNode> {

        private final SearchNode previous;
        private final Board board;
        private final int moves;
        private final int hamming;
        private final int manhattan;

        private SearchNode(SearchNode previous, Board board) {
            this.previous = previous;
            this.board = board;
            this.moves = previous == null ? 0 : previous.moves + 1;
            this.hamming = board.hamming();
            this.manhattan = board.manhattan();
        }

        public int compareTo(SearchNode that) {
            int compare = Integer
                    .compare(this.moves + this.manhattan,
                             that.moves + that.manhattan);

            if (compare != 0) {
                return compare;
            }
            return Integer.compare(this.manhattan, that.manhattan);
        }

        public boolean equals(Object object) {
            if (this == object) return true;
            if (object == null || getClass() != object.getClass()) return false;
            SearchNode that = (SearchNode) object;
            return moves == that.moves &&
                    Objects.equals(previous, that.previous) &&
                    Objects.equals(board, that.board);
        }

        public int hashCode() {
            return Objects.hash(previous, board, moves);
        }
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

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

}
