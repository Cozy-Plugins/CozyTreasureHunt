package com.github.cozyplugins.cozytreasurehunt.database.table;

import com.github.cozyplugins.cozytreasurehunt.Treasure;
import com.github.cozyplugins.cozytreasurehunt.database.record.TreasureRecord;
import com.github.smuddgge.squishydatabase.Query;
import com.github.smuddgge.squishydatabase.interfaces.TableAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <h1>Represents the treasure table.</h1>
 * Contains types of treasure that can be used.
 */
public class TreasureTable extends TableAdapter<TreasureRecord> {
    @Override
    public @NotNull String getName() {
        return "Treasure";
    }

    /**
     * <h1>Used to get a type of treasure from the database.</h1>
     *
     * @param treasureId The treasure's id.
     * @return The requested treasure instance.
     */
    public @Nullable Treasure getTreasure(@NotNull String treasureId) {
        TreasureRecord record = this.getFirstRecord(new Query().match("treasureId", treasureId));
        if (record == null) return null;
        return Treasure.create(record);
    }

    /**
     * <h1>Used to insert a treasure type</h1>
     * <li>Updates or inserts into the database depending on if it already exists.</li>
     *
     * @param treasure The instance of the treasure.
     * @return This instance.
     */
    public @NotNull TreasureTable insert(@NotNull Treasure treasure) {
        this.insertRecord(treasure.convertToRecord());
        return this;
    }
}
