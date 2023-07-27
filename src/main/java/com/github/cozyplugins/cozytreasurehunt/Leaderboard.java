package com.github.cozyplugins.cozytreasurehunt;

import com.github.cozyplugins.cozytreasurehunt.storage.PlayerData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.TreeMap;

/**
 * Represents a treasure leaderboard.
 */
public class Leaderboard {

    private final @NotNull List<PlayerData> playerDataList;

    /**
     * Used to create a leaderboard.
     *
     * @param playerData The list of player data to order.
     */
    public Leaderboard(@NotNull List<PlayerData> playerData) {
        this.playerDataList = playerData;
    }

    /**
     * Used to get who is at a certain rank in
     * total treasure found.
     * <li>1 is first place.</li>
     *
     * @param rank The rank in the leaderboard.
     * @return The requested player data.
     */
    public @Nullable PlayerData get(int rank) {
        TreeMap<Integer, PlayerData> map = new TreeMap<>();

        for (PlayerData playerData : this.playerDataList) {
            map.put(playerData.getAmountFound(), playerData);
        }

        return map.descendingMap().get(rank - 1);
    }

    /**
     * Used to get who is at a certain rank in
     * a specific treasure found.
     * <li>1 is first place.</li>
     *
     * @param rank         The rank in the leaderboard.
     * @param treasureName The treasures name.
     * @return The requested player data.
     */
    public @Nullable PlayerData get(int rank, @NotNull String treasureName) {
        TreeMap<Integer, PlayerData> map = new TreeMap<>();

        for (PlayerData playerData : this.playerDataList) {
            map.put(playerData.getTreasureFound(treasureName), playerData);
        }

        return map.descendingMap().get(rank - 1);
    }
}
