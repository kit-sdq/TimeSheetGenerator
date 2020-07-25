package utils;

import java.lang.reflect.Array;
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

    /**
     * Converts a stream to an array.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Stream<T> stream, Class<T> elementClass) {
        return stream.toArray(i -> (T[])Array.newInstance(elementClass, i));
    }

}
