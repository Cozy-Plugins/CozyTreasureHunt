package com.github.cozyplugins.cozytreasurehunt.placeholder;

import com.github.cozyplugins.cozylibrary.placeholder.CozyPlaceholder;
import com.github.cozyplugins.cozytreasurehunt.storage.DataStorage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a placeholder that gets the
 * total treasure found.
 *
 * <li>
 * If in context of a player it will return
 * the players total treasure
 * </li>
 * <li>
 * If a treasure is specified it will return the total
 * amount of that treasure found.
 * </li>
 * <p>
 * %_total%
 * %_total_[treasureName]%
 * </p>
 */
public class TotalPlaceholder implements CozyPlaceholder {

    @Override
    public @NotNull String getIdentifier() {
        return "total";
    }

    @Override
    public @NotNull String getValue(@Nullable Player player, @NotNull String params) {
        String[] args = params.split("_");

        // Check if the player is null.
        if (player == null) {
            // Check if there is no treasure specified.
            if (args.length > 1) {
                return String.valueOf(DataStorage.getTotalTreasureFound(args[1]));
            }

            return String.valueOf(DataStorage.getTotalTreasureFound());
        }

        // Check if there is no treasure specified.
        if (args.length > 1) {
            return String.valueOf(DataStorage.get(player.getUniqueId()).getTreasureFound(args[1]));
        }

        return String.valueOf(DataStorage.get(player.getUniqueId()).getAmountFound());
    }
}
