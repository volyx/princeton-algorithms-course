import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int n;
    /**
     * 2 - full
     * 1 - open
     * 0 - closed
     */
    private byte[] sites;

    private int numberOfOpenSites;

    private final WeightedQuickUnionUF uf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        this.n = n;
        this.uf = new WeightedQuickUnionUF((n + 2) * (n + 2));
        this.sites = new byte[(n + 2) * (n + 2)];

        // virtual top and bottom
        for (int i = 0; i < n + 2; i++) {
            sites[xyTo1D(0, i)] = 2;
            if (xyTo1D(0, i + 1) < sites.length) {
                uf.union(xyTo1D(0, i), xyTo1D(0, i + 1));
            }

            sites[xyTo1D(n + 1, i)] = 1;
            if (xyTo1D(n + 1, i + 1) < sites.length) {
                uf.union(xyTo1D(n + 1, i), xyTo1D(n + 1, i + 1));
            }
        }

        this.numberOfOpenSites = 0;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkIndex(row, "row is ");
        checkIndex(col, "col is ");

        openVirtual(row, col);
    }

    // opens the site (row, col) if it is not open already
    private void openVirtual(int row, int col) {

        if (sites[xyTo1D(row, col)] == 0) {
            sites[xyTo1D(row, col)] = 1;
            this.numberOfOpenSites++;

            if (percolates(row, col)) {
                sites[xyTo1D(row, col)] = 2;
            }

            // top
            if (row - 1 >= 0 && sites[xyTo1D(row - 1, col)] == 1) {
                uf.union(xyTo1D(row, col), xyTo1D(row - 1, col));

                if (percolates(row - 1, col)) {
                    sites[xyTo1D(row - 1, col)] = 2;
                }
            }

            // right
            if (col + 1 < n + 2 && sites[xyTo1D(row, col + 1)] == 1) {
                uf.union(xyTo1D(row, col), xyTo1D(row, col + 1));

                if (percolates(row, col + 1)) {
                    sites[xyTo1D(row, col + 1)] = 2;
                }
            }

            // down
            if (row + 1 < n + 2 && sites[xyTo1D(row + 1, col)] == 1) {
                uf.union(xyTo1D(row, col), xyTo1D(row + 1, col));

                if (percolates(row + 1, col)) {
                    sites[xyTo1D(row + 1, col)] = 2;
                }
            }

            // left
            if (col - 1 >= 0 && sites[xyTo1D(row, col - 1)] == 1) {
                uf.union(xyTo1D(row, col), xyTo1D(row, col - 1));

                if (percolates(row, col - 1)) {
                    sites[xyTo1D(row, col - 1)] = 2;
                }
            }

            if (percolates()) {
                for (int i = 0; i < n + 2; i++) {
                    if (i != col) {
                        sites[xyTo1D(n + 1, i)] = 0;
                    }
                }
            }
        }
    }

    private int xyTo1D(int p, int q) {
        return (n + 2) * p + q;
    }

    private void checkIndex(int col, String s) {
        if (col < 1 || col > n) {
            throw new IllegalArgumentException(s + col);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkIndex(row, "row is ");
        checkIndex(col, "col is ");
        return sites[xyTo1D(row, col)] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkIndex(row, "row is ");
        checkIndex(col, "col is ");
        // return uf.find(xyTo1D(0, 0)) == uf.find(xyTo1D(row, col));
        return sites[xyTo1D(row, col)] == 2;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(xyTo1D(0, 0)) == uf.find(xyTo1D(n + 1, 0));
    }

    private boolean percolates(int row, int col) {
        return uf.find(xyTo1D(0, 0)) == uf.find(xyTo1D(row, col));
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(10);

        percolation.percolates();

        for (int i = 0; i < percolation.n + 2; i++) {
            for (int j = 0; j < percolation.n + 2; j++) {
                StdOut.print(percolation.sites[percolation.xyTo1D(i, j)]);
            }
            StdOut.println();
        }

        percolation.open(1, 1);
        percolation.open(2, 1);
        percolation.open(3, 1);
        percolation.open(4, 1);
        percolation.open(5, 1);

        percolation.percolates();

        StdOut.println();

        for (int i = 0; i < percolation.n + 2; i++) {
            for (int j = 0; j < percolation.n + 2; j++) {
                StdOut.print(percolation.sites[percolation.xyTo1D(i, j)]);
            }
            StdOut.println();
        }
    }
}
