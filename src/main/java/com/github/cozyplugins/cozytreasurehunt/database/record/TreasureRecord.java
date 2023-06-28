package com.github.cozyplugins.cozytreasurehunt.database.record;

import com.github.smuddgge.squishydatabase.record.Record;
import org.jetbrains.annotations.NotNull;

/**
 * <h1>Represents a treasure record.</h1>
 * Holds information about a type of treasure in the database.
 */
public class TreasureRecord extends Record {

    public String treasureId;
    public String material;
    public String hdb;
}
