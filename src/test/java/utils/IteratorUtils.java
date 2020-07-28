package utils;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import data.Tuple;

/**
 * Collection of useful utils when working with iterators.
 */
public class IteratorUtils {
    
    private IteratorUtils() {}

    /**
     * Move the iterator until the next item fulfills
     * the given predicate and return the item.
     */
    public static <T> T nextWhere(Iterator<T> iterator, Predicate<T> predicate) {
        T item;
        do {
            item = iterator.next();
        } while (!predicate.test(item));

        return item;
    }

    /**
     * Move the iterator until the next tuple fulfills
     * the given predicate and return the tuple.
     */
    public static <T, U> Tuple<T, U> nextWhere(Iterator<Tuple<T, U>> iterator, BiFunction<T, U, Boolean> predicate) {
        Tuple<T, U> item;
        do {
            item = iterator.next();
        } while (!predicate.apply(item.getFirst(), item.getSecond()));

        return item;
    }

    /**
     * Zip the two iterators to a single iterator with the item type {@code Tuple<T, U>}.
     * <p>
     * Using the same iterator for both arguments is allowed and yields tuples where the
     * item received from the iterator first is set as the first element and
     * the item received second is set as the second element.
     */
    public static <T, U> Iterator<Tuple<T, U>> zip(Iterator<? extends T> iteratorA, Iterator<? extends U> iteratorB) {
        return new ZipIterator<>(iteratorA, iteratorB, (a, b) -> new Tuple<>(a, b));
    }

    /**
     * Zip the two iterators to a single iterator using the given BiFunction
     * to combine the items from the two iterators.
     * <p>
     * Using the same iterator for both arguments is allowed and yields tuples where the
     * item received from the iterator first is set as the first element and
     * the item received second is set as the second element.
     */
    public static <T, U, V> Iterator<V> zip(Iterator<? extends T> iteratorA, Iterator<? extends U> iteratorB, BiFunction<T, U, V> zipFunction) {
        return new ZipIterator<>(iteratorA, iteratorB, zipFunction);
    }

    /**
     * Iterator used for zipping the outputs from two iterators.
     * The outputs are combined using the given zip function.
     */
    private static class ZipIterator<T, U, V> implements Iterator<V> {

        /**
         * Create a new zip iterator from the two iterators using the given zip function.
         * <p>
         * Using the same iterator for both arguments is allowed and yields tuples where the
         * item received from the iterator first is set as the first element and
         * the item received second is set as the second element.
         */
        public ZipIterator(Iterator<? extends T> iteratorA, Iterator<? extends U> iteratorB, BiFunction<T, U, V> zipFunction) {
            this.iteratorA = iteratorA;
            this.iteratorB = iteratorB;

            this.zipFunction = zipFunction;
        }

        private final Iterator<? extends T> iteratorA;
        private final Iterator<? extends U> iteratorB;

        private final BiFunction<T, U, V> zipFunction;

        @Override
        public boolean hasNext() {
            return iteratorA.hasNext() && iteratorB.hasNext();
        }

        @Override
        public V next() {
            if (!iteratorA.hasNext()) {
                throw new NoSuchElementException();
            }
            T a = iteratorA.next();

            if (!iteratorB.hasNext()) {
                throw new NoSuchElementException();
            }
            U b = iteratorB.next();

            return zipFunction.apply(a, b);
        }

    }

}
