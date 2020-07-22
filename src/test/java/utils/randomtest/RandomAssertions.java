package utils.randomtest;

import java.util.function.Supplier;

/**
 * Provide methods to make assertions about random parameters.
 * In case the assertion doesn't hold, a {@link RandomTestException} is thrown.
 */
public class RandomAssertions {
    
    private RandomAssertions() {}

    /**
     * Assert that the random parameter configuration is valid for your test.
     */
    public static void randomAssert(boolean assertion) {
        if (!assertion) throw new RandomTestAssertException();
    }

    /**
     * Assert that the random parameter configuration is valid for your test.
     */
    public static void randomAssert(Supplier<Boolean> assertion) {
        if (!assertion.get()) throw new RandomTestAssertException();
    }

    /**
     * Exception thrown in the context of random parameters.
     */
    public static abstract class RandomTestException extends RuntimeException {

        private static final long serialVersionUID = 6009856696442530446L;

    }

    /**
     * Exception for when a configuration of random parameters
     * does not meet the requested assertions.
     */
    public static class RandomTestAssertException extends RandomTestException {

        private static final long serialVersionUID = 948310266129715088L;

        @Override
        public String getMessage() {
            return "assertion for random variables could not be fulfilled";
        }

    }

}
