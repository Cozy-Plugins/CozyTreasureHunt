package com.github.cozyplugins.cozytreasurehunt.database.table;

import com.github.cozyplugins.cozytreasurehunt.database.record.TreasureRecord;
import com.github.smuddgge.squishydatabase.interfaces.TableAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * <h1>Represents the treasure table.</h1>
 * Contains types of treasure that can be used.
 */
public class TreasureTable extends TableAdapter<TreasureRecord> {

    @Override
    public @NotNull String getName() {
        return "Treasure";
    }
}
