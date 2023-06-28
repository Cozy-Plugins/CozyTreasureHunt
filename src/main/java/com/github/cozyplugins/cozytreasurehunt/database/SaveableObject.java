package com.github.cozyplugins.cozytreasurehunt.database;

/**
 * <h1>Indicates if a class can be saved in the database</h1>
 */
public interface SaveableObject {

    /**
     * <h1>Saves this instance to the database.</h1>
     */
    void save();
}
