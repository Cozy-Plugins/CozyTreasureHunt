package com.github.cozyplugins.cozytreasurehunt.storage.indicator;

/**
 * Indicates if a class is cloneable.
 *
 * @param <T> The type of class that will be produced.
 */
public interface Cloneable<T> {

    /**
     * Used to clone the class.
     *
     * @return The new clone instance of the class.
     */
    T clone();
}
