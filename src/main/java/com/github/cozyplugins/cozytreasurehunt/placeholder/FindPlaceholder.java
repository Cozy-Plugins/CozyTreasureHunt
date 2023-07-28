package com.github.cozyplugins.cozytreasurehunt.placeholder;

import com.github.cozyplugins.cozylibrary.placeholder.CozyPlaceholder;
import com.github.cozyplugins.cozytreasurehunt.storage.LocationStorage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the find placeholder.
 * Usd to find how many treasure locations are still spawned.
 * <p>
 * %_find%
 * %_find_[treasure name]%
 * </p>
 */
public class FindPlaceholder implements CozyPlaceholder {

    @Override
    public @NotNull String getIdentifier() {
        return "find";
    }

    @Override
    public @NotNull String getValue(@Nullable Player player, @NotNull String params) {
        String[] args = params.split("_");

        // Check if they are requesting the total amount.
        if (args.length == 1) {
            return String.valueOf(LocationStorage.getAmountSpawned());
        }

        return String.valueOf(LocationStorage.getAmountSpawned(args[1]));
    }
}
