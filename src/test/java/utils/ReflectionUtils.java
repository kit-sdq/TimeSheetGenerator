package utils;

import java.lang.invoke.MethodType;

/**
 * Collection of useful utils when working with reflection.
 */
public class ReflectionUtils {

    private ReflectionUtils() {}

    /**
     * Get the wrapper type for a primitive type.
     * 
     * @param primitiveType Must be a primitive type (i.e. {@link java.lang.Class#isPrimitive()} must return true)
     * @return Wrapper type for the primitive type
     */
    public static Class<?> getWrapperType(Class<?> primitiveType) {
        if (!primitiveType.isPrimitive()) {
            throw new IllegalArgumentException();
        }

        return MethodType.methodType(primitiveType).wrap().returnType();
    }

    /**
     * Get if the given type is a wrapper for a primitive type
     */
    public static boolean isWrapperType(Class<?> type) {
        return MethodType.methodType(type).hasWrappers();
    }

}
