package utils.randomtest;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import data.TimeSpan;
import utils.ReflectionUtils;

/**
 * Extension to allow random parameter annotations.
 */
public class RandomParameterExtension implements ParameterResolver {

    /**
     * Create a pseudo-random int
     * between start (inclusive) and end (exclusive).
     * 
     * see {@link java.util.concurrent.ThreadLocalRandom#nextInt(int, int)}
     */
    private int randomInt(int start, int end) {
        return ThreadLocalRandom.current().nextInt(start, end);
    }

    /**
     * Create a pseudo-random long
     * between start (inclusive) and end (exclusive).
     * 
     * see {@link java.util.concurrent.ThreadLocalRandom#nextInt(long, long)}
     */
    private long randomLong(long start, long end) {
        return ThreadLocalRandom.current().nextLong(start, end);
    }

    /**
     * Get a single value or an infinite stream from the supplier,
     * depending on the parameter context.
     */
    private <T> Object getValueOrIterator(ParameterContext parameterContext, Supplier<T> supplier) {
        if (parameterContext.getParameter().getType().isAssignableFrom(Iterator.class)) {
            return Stream.generate(supplier).iterator();
        } else {
            return supplier.get();
        }
    }

    /**
     * Return true, if the currently processed parameter is annotated with an annotation of the given annotation type
     * and its type is assignable from the given parameter type
     */
    private boolean isValidAnnotation(ParameterContext parameterContext, Class<? extends Annotation> annotationType, Class<?> parameterType) {
        return parameterContext.isAnnotated(annotationType) && (
            parameterContext.getParameter().getType().isAssignableFrom(parameterType) ||
            parameterContext.getParameter().getType().isAssignableFrom(Iterator.class)
        );
    }

    /**
     * Return true, if the currently processed parameter is annotated with an annotation of the given annotation type
     * and its type is equal to the given primitive parameter type or assignable from the wrapper type of the given primitive parameter type
     * (see {@link RandomParameterExtension#getWrapperType(Class)})
     */
    private boolean isValidAnnotationPrimitive(ParameterContext parameterContext, Class<? extends Annotation> annotationType, Class<?> parameterPrimitiveType) {
        Class<?> parameterWrapperType = ReflectionUtils.getWrapperType(parameterPrimitiveType);

        Class<?> actualParameterType = parameterContext.getParameter().getType();
        return parameterContext.isAnnotated(annotationType) && (
            (actualParameterType.isPrimitive() && actualParameterType == parameterPrimitiveType) ||
            actualParameterType.isAssignableFrom(parameterWrapperType) ||
            parameterContext.getParameter().getType().isAssignableFrom(Iterator.class)
        );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return  isValidAnnotationPrimitive(parameterContext, RandomInt.class, int.class) ||
                isValidAnnotation(parameterContext, RandomTimeSpan.class, TimeSpan.class) ||
                isValidAnnotation(parameterContext, RandomLocalDate.class, LocalDate.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Supplier<?> parameterSupplier;

        if (parameterContext.isAnnotated(RandomInt.class)) {
            // random int
            RandomInt randomContext = parameterContext.findAnnotation(RandomInt.class).get();

            parameterSupplier = () -> randomInt(randomContext.lowerBound(), randomContext.upperBound());
        } else if (parameterContext.isAnnotated(RandomTimeSpan.class)) {
            // random TimeSpan
            RandomTimeSpan randomContext = parameterContext.findAnnotation(RandomTimeSpan.class).get();

            parameterSupplier = () -> {
                int hour = randomInt(randomContext.lowerBoundHour(), randomContext.upperBoundHour());
                int minute = randomInt(
                    hour == randomContext.lowerBoundHour() ? randomContext.lowerBoundMinute() : 0,
                    hour == randomContext.upperBoundHour() - 1 ? randomContext.upperBoundMinute() : 60
                );

                return new TimeSpan(hour, minute);
            };
        } else if (parameterContext.isAnnotated(RandomLocalDate.class)) {
            // random LocalDate
            RandomLocalDate randomContext = parameterContext.findAnnotation(RandomLocalDate.class).get();

            parameterSupplier = () -> {
                long lowerBound = LocalDate.of(randomContext.lowerBoundYear(), 1, 1).toEpochDay();
                long upperBound = LocalDate.of(randomContext.upperBoundYear(), 1, 1).toEpochDay();

                long epochDay = randomLong(lowerBound, upperBound);
                return LocalDate.ofEpochDay(epochDay);
            };
        } else {
            return null;
        }

        return getValueOrIterator(parameterContext, parameterSupplier);
    }

    /**
     * Annotation for a random integer parameter.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public @interface RandomInt {

        /**
         * Inclusive lower bound.
         */
        int lowerBound() default 0;
        /**
         * Exclusive upper bound.
         */
        int upperBound();

    }

    /**
     * Annotation for a random TimeSpan parameter.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public @interface RandomTimeSpan {

        /**
         * Inclusive lower bound of the <code>hour</code> property.
         */
        int lowerBoundHour() default 0;
        /**
         * Exclusive upper bound of the <code>hour</code> property.
         */
        int upperBoundHour() default 24;

        /**
         * Inclusive lower bound of the <code>minute</code> property when <code>hour == lowerBoundHour</code>.
         */
        int lowerBoundMinute() default 0;
        /**
         * Exclusive upper bound of the <code>minute</code> property when <code>hour == upperBoundHour - 1</code>.
         */
        int upperBoundMinute() default 60;

    }

    /**
     * Annotation for a random LocalDate parameter.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public @interface RandomLocalDate {

        /**
         * Inclusive lower bound of the <code>year</code> property.
         */
        int lowerBoundYear() default 1990;

        /**
         * Exclusive upper bound of the <code>year</code> property.
         */
        int upperBoundYear() default 2030;

    }

}
