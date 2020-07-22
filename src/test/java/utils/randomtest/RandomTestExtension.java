package utils.randomtest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import utils.StreamUtils;
import utils.randomtest.RandomAssertions.RandomTestAssertException;

/**
 * Provide the {@link RandomTest} annotation to allow retrying
 * a test when an assertions about the random parameters failed.
 * This extension doesn't support parallel testing.
 * <p>
 * Regarding the estimated number of executions:
 * <p>
 * let p be the probability that the random arguments fulfill your assertions,
 * than the estimated number of executions in order to be successful first in the last execution
 * is <code>sum_{n = 1 -> inf}(n * (1 - p)^(n - 1) * p) = 1 / p</code> if <code>p < 1</code>
 * so if <code>p = 1 / x</code>, the estimated number of executions is <code>N = x</code>
 */
public class RandomTestExtension implements TestTemplateInvocationContextProvider, TestExecutionExceptionHandler {

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        return context.getRequiredTestMethod().isAnnotationPresent(RandomTest.class);
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        RandomTest test = context.getRequiredTestMethod().getAnnotation(RandomTest.class);

        RandomTestIterator iterator = new RandomTestIterator(test.iterations(), test.retries(), test.retriesAll());
        ContextStore.get(context).setTestIterator(iterator);

        return StreamUtils.infiniteStreamFromIterator(iterator);
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        ExtensionContext parentContext = context.getParent().get();

        if (throwable instanceof RandomTestAssertException) {
            RandomTestIterator iterator = ContextStore.get(parentContext).getTestIterator();
            
            if (iterator.hasRetriesLeft()) {
                iterator.retry();
            } else {
                iterator.stop();

                throw throwable;
            }
        } else {
            throw throwable;
        }
    }

    /**
     * Iterator for the test invocation context.
     * This iterator provides enough contexts for all requested iterations.
     * Each iteration can be retried until the maximum number of retries
     * for the iteration or for all iterations is exhausted.
     */
    private static class RandomTestIterator implements Iterator<TestTemplateInvocationContext> {

        /**
         * Create a new iteration.
         * 
         * Both retry values can be set to -1 to allow infinite retries.
         * 
         * @param iterations Number of iterations
         * @param retries Maximum number of retries per iteration
         * @param retriesAll Maximum number of retries for all iterations combined
         */
        public RandomTestIterator(int iterations, int retries, int retriesAll) {
            this.retries = retries;

            this.iterationsLeft = iterations;
            this.retriesLeft = retries;
            this.retriesAllLeft = retriesAll;

            this.wasSuccessful = false;
        }

        private final int retries;

        private int iterationsLeft;
        private int retriesLeft;
        private int retriesAllLeft;

        private boolean wasSuccessful;

        /**
         * Return if there are retries left
         * for the current iteration and for all iterations.
         */
        public boolean hasRetriesLeft() {
            return retriesLeft != 0 && retriesAllLeft != 0;
        }

        /**
         * Retry the current iteration.
         * This will only work if there are iterations left {@link RandomTestIterator#hasRetriesLeft()}
         */
        public void retry() {
            if (!hasRetriesLeft()) {
                throw new IllegalStateException();
            }

            iterationsLeft++;

            if (retriesLeft > 0) {
                retriesLeft--;
            }
            if (retriesAllLeft > 0) {
                retriesAllLeft--;
            }

            wasSuccessful = false;
        }

        /**
         * Stop the iterator.
         */
        public void stop() {
            iterationsLeft = 0;
        }

        @Override
        public boolean hasNext() {
            return iterationsLeft > 0;
        }

        @Override
        public TestTemplateInvocationContext next() {
            if (hasNext()) {
                if (wasSuccessful) {
                    retriesLeft = retries;
                }

                iterationsLeft--;
                wasSuccessful = true; // try to set wasSuccessful, may be reset in retry()
                
                return new TestTemplateInvocationContext() {};
            } else {
                throw new NoSuchElementException();
            }
        }

    }

    /**
     * Extension context store for this class.
     */
    private static class ContextStore {

        private static Namespace NAMESPACE = Namespace.create("utils", "randomtest", "RandomTestExtension");

        private ContextStore(ExtensionContext context) {
            store = context.getStore(NAMESPACE);
        }

        private Store store;

        public RandomTestIterator getTestIterator() {
            return store.get("iterator", RandomTestIterator.class);
        }

        public void setTestIterator(RandomTestIterator iterator) {
            store.put("iterator", iterator);
        }

        public static ContextStore get(ExtensionContext context) {
            return new ContextStore(context);
        }

    }

    /**
     * Annotation to allow retrying a test when
     * an assertions about the random parameters failed.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @ExtendWith(RandomTestExtension.class)
    @TestTemplate
    public @interface RandomTest {

        int iterations() default 1;
        int retries() default -1;
        int retriesAll() default -1;

    }

}
