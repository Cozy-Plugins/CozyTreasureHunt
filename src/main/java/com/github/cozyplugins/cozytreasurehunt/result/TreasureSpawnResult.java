package com.github.cozyplugins.cozytreasurehunt.result;

import com.github.cozyplugins.cozytreasurehunt.TreasureLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the treasure spawning statistic.
 * Used to get information on what treasure has been spawned successfully.
 */
public class TreasureSpawnResult implements TreasureResult {

    public final @NotNull Map<TreasureLocation, Boolean> statisticMap;

    /**
     * Used to create a treasure spawn result.
     */
    public TreasureSpawnResult() {
        this.statisticMap = new HashMap<>();
    }

    /**
     * Used to add data to the result set.
     *
     * @param location   The instance of a treasure location.
     * @param hasSpawned True if the treasure was successfully spawned.
     * @return This instance.
     */
    public @NotNull TreasureSpawnResult add(@NotNull TreasureLocation location, boolean hasSpawned) {
        this.statisticMap.put(location, hasSpawned);
        return this;
    }

    /**
     * Used to get the amount of treasure spawned.
     *
     * @return The amount of treasure spawned.
     */
    public int getAmountSpawned() {
        int amountSpawned = 0;
        for (Map.Entry<TreasureLocation, Boolean> entry : this.statisticMap.entrySet()) {
            if (entry.getValue()) amountSpawned++;
        }

        return amountSpawned;
    }

    /**
     * Used to get the amount of treasure that was canceled.
     *
     * @return The amount of treasure that was not spawned.
     */
    public int getAmountNotSpawned() {
        int amountSpawned = 0;
        for (Map.Entry<TreasureLocation, Boolean> entry : this.statisticMap.entrySet()) {
            if (!entry.getValue()) amountSpawned++;
        }

        return amountSpawned;
    }

    @Override
    public String toString() {
        return "{amountSpawned: " + this.getAmountSpawned()
                + ", amountNotSpawned: " + this.getAmountNotSpawned() + "}";
    }
}
