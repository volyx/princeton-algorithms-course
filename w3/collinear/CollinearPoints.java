import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class CollinearPoints {
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        LineSegment[] fastSegments = new FastCollinearPoints(points).segments();

        LineSegment[] slowSegments = new BruteCollinearPoints(points).segments();

        if (fastSegments.length != slowSegments.length) {

            StdOut.println("fastSegments:");

            for (LineSegment lineSegment : fastSegments) {
                StdOut.println(lineSegment);
            }

            StdOut.println("slowSegments:");

            for (LineSegment lineSegment : slowSegments) {
                StdOut.println(lineSegment);
            }

            throw new RuntimeException("fastSegments.length " + fastSegments.length + " expected "
                                               + slowSegments.length);
        }
    }
}
