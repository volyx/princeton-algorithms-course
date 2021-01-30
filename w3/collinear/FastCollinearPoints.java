import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private final List<LineSegment> segments = new ArrayList<>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("points is null");
        }
        if (points.length == 0) {
            throw new IllegalArgumentException("points are empty");
        }

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("point at index " + i + " is null");
            }
            for (int j = 0; j < i; j++) {
                if (points[j].compareTo(points[i]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }

        Point[] sortedPoints = Arrays.copyOf(points, points.length);

        for (Point origin : points) {
            Arrays.sort(sortedPoints);
            Arrays.sort(sortedPoints, origin.slopeOrder());

            final List<LineSegment> lineSegments = findSegments(origin, sortedPoints);

            if (!lineSegments.isEmpty()) {
                this.segments.addAll(lineSegments);
            }
        }
    }

    private static List<LineSegment> findSegments(Point origin, Point[] sortedPoints) {

        final List<LineSegment> originSegments = new ArrayList<>();
        final int n = sortedPoints.length;
        final List<Point> line = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Point secondPoint = sortedPoints[i];
            line.add(secondPoint);
            double angle = origin
                    .slopeTo(secondPoint);
            while (i < n - 1
                    && angle == origin.slopeTo(sortedPoints[i + 1])) {
                line.add(sortedPoints[++i]);
            }

            if (line.size() >= 3 && origin.compareTo(secondPoint) < 0) {
                originSegments.add(new LineSegment(origin, line.get(line.size() - 1)));
            }

            line.clear();
        }
        return originSegments;
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }


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

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
