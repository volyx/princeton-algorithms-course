import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private static final int[][] DIRECTIONS = {
            { 0, 1 },
            { 0, -1 },
            { 1, 0 },
            { -1, 0 }
    };

    private final int[][] tiles;
    private final int hammingDistance;
    private final int manhattanDistance;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException();
        }
        int n = tiles.length;
        this.tiles = copyTiles(tiles);

        this.hammingDistance = countHammingDistance(this.tiles);

        int manhattan = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] == 0) {
                    continue;
                }
                int cols = getColumn(this.tiles, i, j);
                int rows = getRow(this.tiles, i, j);

                if (rows != i || cols != j) {
                    manhattan = manhattan + Math.abs(rows - i) + Math.abs(cols - j);
                }
            }
        }

        this.manhattanDistance = manhattan;
    }

    private static int countHammingDistance(int[][] tiles) {
        int hamming = 0;
        int n = tiles.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    continue;
                }
                int rows = getRow(tiles, i, j);
                int cols = getColumn(tiles, i, j);

                if (rows != i || cols != j) {
                    hamming++;
                }
            }
        }
        return hamming;
    }

    private static int[][] copyTiles(int[][] tiles) {
        int n = tiles.length;
        int[][] tilesCopy = new int[n][n];
        int[] distinct = new int[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tilesCopy[i][j] = tiles[i][j];
                distinct[tiles[i][j]]++;
            }
        }

        for (int i = 0; i < distinct.length; i++) {
            if (distinct[i] != 1) {
                throw new IllegalArgumentException(Arrays.toString(distinct));
            }
        }
        return tilesCopy;
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(tiles.length).append("\n");
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                int tile = this.tiles[i][j];
                if (tile < 10) {
                    sb.append(" ").append(tile).append(" ");
                }
                else {
                    sb.append(tile).append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        return this.hammingDistance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattanDistance;
    }

    private static int getColumn(int[][] tiles, int i, int j) {
        if (tiles[i][j] == 0) {
            return tiles.length - 1;
        }
        return tiles[i][j] - 1 - (tiles.length * getRow(tiles, i, j));
    }

    private static int getRow(int[][] tiles, int i, int j) {
        if (tiles[i][j] == 0) {
            return tiles.length - 1;
        }
        return (tiles[i][j] - 1) / tiles.length;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hammingDistance == 0;
    }

    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        Board board = (Board) that;
        return Arrays.deepEquals(tiles, board.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

        final List<Board> boards = new ArrayList<>();

        int n = this.tiles.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] == 0) {
                    for (int[] direction : DIRECTIONS) {
                        int row = i + direction[0];
                        int col = j + direction[1];
                        if (row >= 0 && row < n && col >= 0 && col < n) {
                            int[][] tilesCopy = copyTiles(this.tiles);
                            tilesCopy[i][j] = tilesCopy[row][col];
                            tilesCopy[row][col] = 0;
                            boards.add(new Board(tilesCopy));
                        }
                    }
                    break;
                }
            }
        }

        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (tiles[0][0] != 0 && tiles[0][1] != 0) {
            int[][] newTiles = copyTiles(tiles);
            swap(newTiles, 0, 0, 0, 1);
            return new Board(newTiles);
        }
        int[][] newTiles = copyTiles(tiles);
        swap(newTiles, 1, 0, 1, 1);
        return new Board(newTiles);
    }

    private static void swap(int[][] tiles, int i1, int j1, int i2, int j2) {
        int temp = tiles[i1][j1];
        tiles[i1][j1] = tiles[i2][j2];
        tiles[i2][j2] = temp;
    }

    // unit testing (not graded)
    public static void main(String[] args) {

        Board board = new Board(new int[][] {
                new int[] { 1, 0 },
                new int[] { 3, 2 }
        });

        assert !board.isGoal();
        StdOut.println(board);

        StdOut.println("neighbors");
        int neighborsCount = 0;
        for (Board b : board.neighbors()) {
            StdOut.println(b);
            neighborsCount++;
        }
        assert neighborsCount == 2;

        board = new Board(new int[][] {
                new int[] { 1, 2 },
                new int[] { 3, 0 }
        });
        StdOut.println("new board");
        assert board.isGoal() : "board.isGoal()" + board;
        assert board.hamming() == 0 : "board.hamming() " + board.hamming();
        assert board.manhattan() == 0 : "board.manhattan() " + board.manhattan();
        StdOut.println(board);

        StdOut.println("new board");
        board = new Board(new int[][] {
                new int[] { 8, 1, 3 },
                new int[] { 4, 0, 2 },
                new int[] { 7, 6, 5 }
        });
        assert board.hamming() == 5 : "board.hamming() " + board.hamming();
        assert board.manhattan() == 10 : "board.manhattan() " + board.manhattan();
        List<Board> neighbors = new ArrayList<>();
        for (Board b : board.neighbors()) {
            neighbors.add(b);
        }
        assert neighbors.size() == 4 : "board.neighbors() " + neighbors;
        StdOut.println("neighbors");
        StdOut.println("neighbor0");
        StdOut.println(neighbors.get(0));
        StdOut.println("neighbor1");
        StdOut.println(neighbors.get(1));
        StdOut.println("neighbor2");
        StdOut.println(neighbors.get(2));
        StdOut.println("neighbor3");
        StdOut.println(neighbors.get(3));
        StdOut.println(board);

        board = new Board(new int[][] {
                new int[] { 2, 1 },
                new int[] { 3, 0 },
                });

        assert board.hamming() == 2;
        assert board.manhattan() == 2;

        board = new Board(new int[][] {
                new int[] { 1, 2, 3 },
                new int[] { 4, 5, 6 },
                new int[] { 8, 7, 0 }
        });

        assert board.hamming() == 2;
        assert board.manhattan() == 2;

        board = new Board(new int[][] {
                new int[] { 1, 2, 3, 4 },
                new int[] { 5, 6, 7, 8 },
                new int[] { 9, 10, 11, 12 },
                new int[] { 13, 15, 14, 0 }
        });

        assert board.hamming() == 2;
        assert board.manhattan() == 2;


        Board board1 = new Board(new int[][] {
                new int[] { 2, 1 },
                new int[] { 3, 0 },
                });

        Board board2 = new Board(new int[][] {
                new int[] { 2, 1 },
                new int[] { 3, 0 },
                });

        assert board1.equals(board2) : "eq \n" + board1.toString() + " \n" + board2.toString();
    }
}
