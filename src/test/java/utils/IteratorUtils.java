package utils;

import java.util.Iterator;
import java.util.function.Predicate;

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

}
