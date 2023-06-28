package com.github.cozyplugins.cozytreasurehunt.database;

import com.github.smuddgge.squishydatabase.record.Record;
import org.jetbrains.annotations.NotNull;

/**
 * <h1>Indicates if a class can be converted to a type of database record.</h1>
 *
 * @param <R> The record type.
 */
public interface ConvertableObject<R extends Record> {

    /**
     * <h1>Used to convert this instance into a database record.</h1>
     * <li>This will convert this instance into a database record specified as R</li>
     *
     * @return The instance of the database record.
     */
    @NotNull R convertToRecord();
}
