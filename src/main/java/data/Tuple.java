package data;

/**
 * A tuple of two elements (first and second)
 * 
 * @param <A> Type of the first element.
 * @param <B> Type of the second element.
 */
public class Tuple<A, B> {

    private A first;
    private B second;
    
    /**
     * Create a new tuple of two elements
     * 
     * @param first The first element in the tuple
     * @param second The second element in the tuple
     */
    public Tuple(A first, B second) {
        this.first = first;
        this.second = second;
    }
    
    /**
     * Return the first element in the tuple
     * 
     * @return The first element in the tuple
     */
    public A getFirst() {
        return this.first;
    }
    
    /**
     * Return the second element in the tuple
     * 
     * @return The second element in the tuple
     */
    public B getSecond() {
        return this.second;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Tuple<?, ?>)) {
            return false;
        }

        Tuple<?, ?> otherTuple = (Tuple<?, ?>)other;
        if (!this.first.equals(otherTuple.first)) {
            return false;
        } else if (!this.second.equals(otherTuple.second)) {
            return false;
        } else {
            return true;
        }
    }
    
}
