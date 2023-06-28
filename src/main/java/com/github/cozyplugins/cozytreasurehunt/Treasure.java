package com.github.cozyplugins.cozytreasurehunt;

import com.github.cozyplugins.cozytreasurehunt.database.ConvertableObject;
import com.github.cozyplugins.cozytreasurehunt.database.SaveableObject;
import com.github.cozyplugins.cozytreasurehunt.database.record.TreasureRecord;
import com.github.cozyplugins.cozytreasurehunt.database.table.TreasureTable;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <h1>Represents a type of treasure</h1>
 * <li>Defaults to {@link Material#CHEST}</li>
 */
public class Treasure implements ConvertableObject<TreasureRecord>, SaveableObject {

    private final @NotNull String identifier;
    private @NotNull Material material;
    private @Nullable String hdb;

    /**
     * <h1>Used to create a treasure type</h1>
     *
     * @param identifier The treasure's unique identifier.
     */
    public Treasure(@NotNull String identifier) {
        this.identifier = identifier;
        this.material = Material.CHEST;
    }

    /**
     * <h1>Used to get the treasure identifier</h1>
     * <li>A unique set of characters that defines this treasure type.</li>
     *
     * @return The identifier.
     */
    public @NotNull String getIdentifier() {
        return this.identifier;
    }

    /**
     * <h1>Used to get the treasures material</h1>
     *
     * @return The treasure's material.
     */
    public @NotNull Material getMaterial() {
        return this.material;
    }

    /**
     * <h1>Used to get the hdb value</h1>
     * @return The hdb value.
     */
    public @Nullable String getHdb() {
        return this.hdb;
    }

    /**
     * <h1>Used to set the treasures material</h1>
     *
     * @param material The material to set the treasure.
     * @return This instance.
     */
    public @NotNull Treasure setMaterial(@NotNull Material material) {
        this.material = material;
        return this;
    }

    /**
     * <h1>Used to set the hdb value</h1>
     * @param hdb The hdb value.
     * @return This instance.
     */
    public @NotNull Treasure setHdb(@Nullable String hdb) {
        this.hdb = hdb;
        return this;
    }

    /**
     * <h1>Used to create a treasure type based on a database record</h1>
     *
     * @param record The instance of the treasure record.
     * @return The requested treasure instance.
     */
    public static @NotNull Treasure create(@NotNull TreasureRecord record) {
        Treasure treasure = new Treasure(record.treasureId);

        // Check if the record's material is invalid.
        if (Material.getMaterial(record.material) == null) {
            throw new NullPointerException("Database contains treasure that has a invalid material!");
        }

        treasure.material = Material.getMaterial(record.material);

        return treasure;
    }

    @Override
    public @NotNull TreasureRecord convertToRecord() {
        TreasureRecord record = new TreasureRecord();

        record.treasureId = this.identifier;
        record.material = this.material.toString();

        return record;
    }

    @Override
    public void save() {
        CozyTreasureHunt.getDatabase()
                .getTable(TreasureTable.class)
                .insert(this);
    }
}
