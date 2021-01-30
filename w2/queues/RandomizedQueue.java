import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items = (Item[]) new Object[1];
    private int size = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {

    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (size() == items.length) {
            resize();
        }
        items[size++] = item;
    }

    private void resize() {
        resize(this.items.length * 2);
    }

    private void shrink() {
        resize(this.items.length / 2);
    }

    private void resize(int n) {
        final Item[] newItems = (Item[]) new Object[n];

        for (int i = 0; i < Math.min(n, this.items.length); i++) {
            newItems[i] = this.items[i];
        }

        this.items = newItems;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (size() == items.length / 4) {
            shrink();
        }

        int rand = StdRandom.uniform(size);

        swap(items, rand, size - 1);

        Item item = items[size - 1];
        size--;
        return item;
    }

    private void swap(Item[] items, int a, int b) {
        Item temp = items[a];
        items[a] = items[b];
        items[b] = temp;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return this.items[StdRandom.uniform(0, size())];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator(this.items, this.size);

    }

    // unit testing (required)
    public static void main(String[] args) {

        RandomizedQueue<Integer> queue = new RandomizedQueue<>();

        queue.enqueue(16);
        queue.enqueue(15);
        queue.enqueue(14);
        queue.enqueue(13);
        queue.enqueue(12);
        queue.enqueue(11);
        queue.enqueue(10);
        queue.enqueue(9);
        queue.enqueue(8);
        queue.enqueue(7);
        queue.enqueue(6);
        queue.enqueue(5);
        queue.enqueue(4);
        queue.enqueue(3);
        queue.enqueue(2);
        queue.enqueue(1);

        StdOut.println();

        for (Integer value : queue) {
            StdOut.println(value);
        }

        StdOut.println();

        for (int i = 0; i < 14; i++) {
            StdOut.println(queue.dequeue());
        }

        StdOut.println();

        for (Integer value : queue) {
            StdOut.println(value);
        }

        StdOut.println();

        for (Integer value : queue) {
            StdOut.println(value);
        }
    }

    private class RandomIterator implements Iterator<Item> {

        private int index = 0;
        private final int size;
        private final Item[] copy;

        public RandomIterator(Item[] items, int size) {
            this.copy = items.clone();
            this.size = size;
            StdRandom.shuffle(copy, 0, size);
        }

        public boolean hasNext() {
            return index < size;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item value = copy[index];
            index++;
            return value;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
