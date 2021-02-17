import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class KdTree {
    private static final double X_MIN = 0.0;
    private static final double X_MAX = 1.0;
    private static final double Y_MIN = 0.0;
    private static final double Y_MAX = 1.0;

    private KDNode parent;
    private int size = 0;

    public KdTree() { // construct an empty set of points

    }

    // is the set empty?
    public boolean isEmpty() {
        return parent == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    private KDNode insert(Point2D x, KDNode t, KDNode previous) {
        if (t == null) {
            t = new KDNode(x, previous != null && !previous.isHorizontal);
            size++;
        }
        else if (t.point.equals(x)) {
            // duplicate
            return t;
        }
        else if (t.isHorizontal) {
            if (x.y() < t.point.y()) {
                t.left = insert(x, t.left, t);
            }
            else {
                t.right = insert(x, t.right, t);
            }
        }
        else {
            if (x.x() < t.point.x()) {
                t.left = insert(x, t.left, t);
            }
            else {
                t.right = insert(x, t.right, t);
            }
        }
        return t;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (parent == null) {
            parent = insert(p, null, null);
            return;
        }

        insert(p, parent, null);
    }

    // does the set contain point p?
    public boolean contains(Point2D x) {
        if (x == null) {
            throw new IllegalArgumentException();
        }
        return contains(x, parent);
    }

    private boolean contains(Point2D x, KDNode t) {
        if (t == null) {
            return false;
        }
        else if (t.point.equals(x)) {
            return true;
        }
        else if (t.isHorizontal) {
            if (x.y() < t.point.y()) {
                return contains(x, t.left);
            }
            else {
                return contains(x, t.right);
            }
        }
        else {
            if (x.x() < t.point.x()) {
                return contains(x, t.left);
            }
            else {
                return contains(x, t.right);
            }
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(parent, 0, 1, 0, 1);
    }

    private void draw(KDNode n, double minX, double maxX, double minY, double maxY) {
        if (n == null) return;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(n.point.x(), n.point.y());

        if (n.isHorizontal) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(minX, n.point.y(), maxX, n.point.y());
            draw(n.left, minX, maxX, minY, n.point.y());
            draw(n.right, minX, maxX, n.point.y(), maxY);
        }
        else {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.point.x(), minY, n.point.x(), maxY);
            draw(n.left, minX, n.point.x(), minY, maxY);
            draw(n.right, n.point.x(), maxX, minY, maxY);
        }

        print(parent);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        List<Point2D> pointsInRect = new ArrayList<>();

        range(parent, rect, pointsInRect);

        return pointsInRect;
    }

    private void range(KDNode node, RectHV rect, List<Point2D> pointsInRect) {
        if (node == null) {
            return;
        }
        if (rect.contains(node.point)) {
            pointsInRect.add(node.point);
        }
        if (node.isHorizontal) {
            if (rect.ymin() < node.point.y()) {
                range(node.left, rect, pointsInRect);
            }
            if (rect.ymax() > node.point.y()) {
                range(node.right, rect, pointsInRect);
            }
        }
        else {
            if (rect.xmin() < node.point.x()) {
                range(node.left, rect, pointsInRect);
            }
            if (rect.xmax() > node.point.x()) {
                range(node.right, rect, pointsInRect);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (isEmpty()) {
            return null;
        }

        RectHV rectHV = new RectHV(X_MIN, Y_MIN, X_MAX, Y_MAX);
        KDNode bestNode = parent;
        return nearestNode(p, parent, rectHV, bestNode.point);
    }

    private Point2D nearestNode(Point2D p, KDNode subTree, RectHV rect, Point2D best) {

        // if this bounding box is too far, do nothing
        if (subTree == null) {
            return best;
        }

        if (best == null) {
            best = subTree.point;
        }

        double bestDistance = best.distanceSquaredTo(p);
        if (rect.distanceSquaredTo(p) > bestDistance) {
            return best;
        }

        // if this point is better than the best:
        double dist = p.distanceSquaredTo(subTree.point);
        if (dist < bestDistance) {
            best = subTree.point;
        }

        // visit subtrees is most promising order:
        if (subTree.isHorizontal) {
            RectHV trimRect = goBottom(rect, subTree.point);
            // StdOut.println("goB " + trimRect + " from " + rect);
            best = nearestNode(p, subTree.left, trimRect, best);

            trimRect = goTop(rect, subTree.point);
            // StdOut.println("goT " + trimRect + " from " + rect);
            best = nearestNode(p, subTree.right, trimRect, best);
        }
        else {
            RectHV trimRect = goLeft(rect, subTree.point);
            // StdOut.println("goL " + trimRect + " from " + rect);
            best = nearestNode(p, subTree.left, trimRect, best);

            trimRect = goRight(rect, subTree.point);
            // StdOut.println("goR " + trimRect + " from " + rect);
            best = nearestNode(p, subTree.right, trimRect, best);
        }

        return best;
    }

    private RectHV goLeft(RectHV rect, Point2D p) {
        return new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax());
    }

    private RectHV goRight(RectHV rect, Point2D p) {
        return new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax());
    }

    private RectHV goBottom(RectHV rect, Point2D p) {
        return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
    }

    private RectHV goTop(RectHV rect, Point2D p) {
        return new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
    }

    private static class KDNode {
        private final Point2D point;
        private KDNode left;
        private KDNode right;
        private final boolean isHorizontal;

        public KDNode(Point2D p, boolean isHorizontal) {
            this.point = p;
            this.isHorizontal = isHorizontal;
            this.left = null;
            this.right = null;
        }

        public String toString() {
            return String
                    .format("(%.3f, %.3f, %s)", point.x(), point.y(), (isHorizontal ? "H" : "V"));
        }

        public KDNode getLeft() {
            return left;
        }

        public KDNode getRight() {
            return right;
        }
    }

    private static void print(KDNode root) {
        System.out.println(
                "===============================================================================");
        List<List<String>> lines = new ArrayList<List<String>>();

        List<KDNode> level = new ArrayList<KDNode>();
        List<KDNode> next = new ArrayList<KDNode>();

        level.add(root);
        int nn = 1;

        int widest = 0;

        while (nn != 0) {
            List<String> line = new ArrayList<String>();

            nn = 0;

            for (KDNode n : level) {
                if (n == null) {
                    line.add(null);

                    next.add(null);
                    next.add(null);
                }
                else {
                    String aa = n.toString();
                    line.add(aa);
                    if (aa.length() > widest) widest = aa.length();

                    next.add(n.getLeft());
                    next.add(n.getRight());

                    if (n.getLeft() != null) nn++;
                    if (n.getRight() != null) nn++;
                }
            }

            if (widest % 2 != 0) widest++;

            lines.add(line);

            List<KDNode> tmp = level;
            level = next;
            next = tmp;
            next.clear();
        }

        int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);
        for (int i = 0; i < lines.size(); i++) {
            List<String> line = lines.get(i);
            int hpw = (int) Math.floor(perpiece / 2.0) - 1;

            if (i > 0) {
                for (int j = 0; j < line.size(); j++) {

                    // split node
                    char c = ' ';
                    if (j % 2 != 0) {
                        if (line.get(j - 1) != null) {
                            c = (line.get(j) != null) ? '┴' : '┘';
                        }
                        else {
                            if (j < line.size() && line.get(j) != null) c = '└';
                        }
                    }
                    System.out.print(c);

                    // lines and spaces
                    if (line.get(j) == null) {
                        for (int k = 0; k < perpiece - 1; k++) {
                            System.out.print(" ");
                        }
                    }
                    else {

                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? " " : "─");
                        }
                        System.out.print(j % 2 == 0 ? "┌" : "┐");
                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? "─" : " ");
                        }
                    }
                }
                System.out.println();
            }

            // print line of numbers
            for (String f : line) {

                // if (f == null) f = "";
                int gap1 = (int) Math.ceil(perpiece / 2.0 - f.length() / 2.0);
                int gap2 = (int) Math.floor(perpiece / 2.0 - f.length() / 2.0);

                // a number
                for (int k = 0; k < gap1; k++) {
                    System.out.print(" ");
                }
                System.out.print(f);
                for (int k = 0; k < gap2; k++) {
                    System.out.print(" ");
                }
            }
            System.out.println();

            perpiece /= 2;
        }
    }

    public static void main(String[] args) {
        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }

        for (int i = 0; i < 10; i++) {
            double x = StdRandom.uniform(0.0, 1.0);
            double y = StdRandom.uniform(0.0, 1.0);
            Point2D query = new Point2D(x, y);

            // draw all of the points
            Point2D expected = brute.nearest(query);
            Point2D nearest = kdtree.nearest(query);


            if (!nearest.equals(expected)) throw new RuntimeException(
                    "query " + query +
                            " expected " + expected.toString() + " nearest " + nearest.toString() +
                            " expected distance " + query.distanceSquaredTo(expected) + ", was "
                            + nearest
                            .distanceSquaredTo(query) + " test "
                            + i);
        }

        for (int i = 0; i < 100_000; i++) {
            double x1 = StdRandom.uniform(0.0, 1.0);
            double x2 = StdRandom.uniform(0.0, 1.0);
            double y1 = StdRandom.uniform(0.0, 1.0);
            double y2 = StdRandom.uniform(0.0, 1.0);
            RectHV rect = new RectHV(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2),
                                     Math.max(y1, y2));

            // draw all of the points
            List<Point2D> expectedRange = new ArrayList<>();
            Iterator<Point2D> it = brute.range(rect).iterator();
            while (it.hasNext()) {
                expectedRange.add(it.next());
            }
            List<Point2D> realRange = new ArrayList<>();
            it = kdtree.range(rect).iterator();

            while (it.hasNext()) {
                realRange.add(it.next());
            }

            realRange.sort(Point2D::compareTo);

            if (!Arrays.deepEquals(realRange.toArray(), expectedRange.toArray()))
                throw new RuntimeException(
                        "\nquery " + rect + "\nexpected " + expectedRange.toString() + "\nnearest "
                                + realRange.toString() + " test " + i);
        }


        for (Point2D p : brute.range(new RectHV(0.0, 0.0, 1.0, 1.0))) {

            if (!brute.contains(p)) {
                throw new RuntimeException("brute.contains failed");
            }

            if (!kdtree.contains(p)) {
                throw new RuntimeException("kdtree.contains failed");
            }
        }
    }
}
