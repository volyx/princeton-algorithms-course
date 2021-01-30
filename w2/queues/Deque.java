import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first;
    private Node<Item> last;
    private int size = 0;

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {

        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node<Item> oldFirst = this.first;
        this.first = new Node<>(null, null, item);

        if (isEmpty()) {
            last = this.first;
        }
        else {
            this.first.next = oldFirst;
            oldFirst.prev = first;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {

        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node<Item> oldLast = last;
        last = new Node<>(null, null, item);
        last.prev = oldLast;

        if (isEmpty()) {
            this.first = last;
        }
        else {
            oldLast.next = last;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        size--;
        Item item = first.value;
        first = first.next;
        if (isEmpty()) {
            first = null;
            last = null;
        }
        else {
            first.prev = null;
        }
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        size--;
        Item item = this.last.value;
        last = last.prev;

        if (isEmpty()) {
            first = null;
            last = null;
        }
        else {
            last.next = null;
        }
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {

        if (isEmpty()) {
            return new Iterator<Item>() {
                public boolean hasNext() {
                    return false;
                }

                public Item next() {
                    throw new NoSuchElementException();
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        return new Iterator<Item>() {
            private Node<Item> current = first;

            public boolean hasNext() {
                return current != null;
            }

            public Item next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                Node<Item> oldCurrent = current;
                current = current.next;
                return oldCurrent.value;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // unit testing (required)
    public static void main(String[] args) {

        Deque<Integer> deque = new Deque<>();

        deque.addFirst(3);
        deque.addFirst(2);
        deque.addFirst(1);

        for (Integer value : deque) {
            StdOut.println(value);
        }

        if (!Objects.equals(3, deque.removeLast())) {
            throw new RuntimeException();
        }

        if (!Objects.equals(2, deque.removeLast())) {
            throw new RuntimeException();
        }

        if (!Objects.equals(1, deque.removeLast())) {
            throw new RuntimeException();
        }

        if (!deque.isEmpty()) {
            throw new RuntimeException();
        }

        deque.addFirst(-3);
        deque.addFirst(-2);
        deque.addFirst(-1);

        StdOut.println();

        for (Integer value : deque) {
            StdOut.println(value);
        }

        deque.addLast(30);
        deque.addLast(20);
        deque.addLast(10);

        StdOut.println();

        for (Integer value : deque) {
            StdOut.println(value);
        }

        if (!Objects.equals(-1, deque.removeFirst())) {
            throw new RuntimeException();
        }
        if (!Objects.equals(-2, deque.removeFirst())) {
            throw new RuntimeException();
        }
        if (!Objects.equals(-3, deque.removeFirst())) {
            throw new RuntimeException();
        }

        StdOut.println();

        int i = 0;
        for (Integer value : deque) {
            StdOut.println(value);
            i++;
        }

        if (i != 3) {
            throw new RuntimeException();
        }

        if (!Objects.equals(10, deque.removeLast())) {
            throw new RuntimeException();
        }

        if (!Objects.equals(20, deque.removeLast())) {
            throw new RuntimeException();
        }

        if (!Objects.equals(30, deque.removeLast())) {
            throw new RuntimeException();
        }

        if (!deque.isEmpty()) {
            throw new RuntimeException();
        }

        deque.addLast(21);
        deque.addLast(22);
        deque.addLast(23);

        StdOut.println();

        for (Integer value : deque) {
            StdOut.println(value);
        }

        if (!Objects.equals(23, deque.removeLast())) {
            throw new RuntimeException();
        }

        if (!Objects.equals(22, deque.removeLast())) {
            throw new RuntimeException();
        }

        if (!Objects.equals(21, deque.removeLast())) {
            throw new RuntimeException();
        }

        if (!deque.isEmpty()) {
            throw new RuntimeException();
        }

        deque = new Deque<Integer>();
        deque.addFirst(1);
        if (!Objects.equals(1, deque.removeLast())) {
            throw new RuntimeException();
        }

        deque.addFirst(3);

        if (!Objects.equals(3, deque.removeLast())) {
            throw new RuntimeException();
        }

        deque = new Deque<Integer>();
        deque.isEmpty();   //      ==> true
        deque.isEmpty();     //   ==> true
        deque.addLast(3);
        deque.removeLast(); //      ==> 3
        deque.addLast(5);
        deque.removeLast(); //      ==> 5
        deque.addLast(7);
        deque.removeLast(); //      ==> 7

        if (!deque.isEmpty()) {
            throw new RuntimeException();
        }
    }

    private class Node<NodeItem> {

        private Node<NodeItem> prev;
        private Node<NodeItem> next;
        private NodeItem value;

        private Node(Node<NodeItem> prev, Node<NodeItem> next, NodeItem value) {
            this.prev = prev;
            this.next = next;
            this.value = value;
        }
    }
}
