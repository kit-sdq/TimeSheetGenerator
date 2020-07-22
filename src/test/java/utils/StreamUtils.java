package utils;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Collection of useful utils when working with streams.
 */
public class StreamUtils {

    private StreamUtils() {}

    /**
     * Create a potentially infinite stream from an iterator.
     */
    public static <T> Stream<T> infiniteStreamFromIterator(Iterator<T> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }

}
