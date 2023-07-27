package com.github.cozyplugins.cozytreasurehunt.placeholder;

import com.github.cozyplugins.cozylibrary.placeholder.CozyPlaceholder;
import com.github.cozyplugins.cozytreasurehunt.Leaderboard;
import com.github.cozyplugins.cozytreasurehunt.storage.DataStorage;
import com.github.cozyplugins.cozytreasurehunt.storage.PlayerData;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the leaderboard placeholder.
 * <p>
 * %_leaderboard_[rank]%
 * %_leaderboard_[rank]_amount%
 * %_leaderboard_[treasurename]_[rank]%
 * %_leaderboard_[treasurename]_[rank]_amount%
 * </p>
 */
public class LeaderboardPlaceholder implements CozyPlaceholder {

    @Override
    public @NotNull String getIdentifier() {
        return "leaderboard";
    }

    @Override
    public @NotNull String getValue(@Nullable Player player, @NotNull String params) {
        String[] args = params.split("_");
        Leaderboard leaderboard = DataStorage.getLeaderboard();

        // Check if there are arguments.
        if (args.length < 2) return "null";

        System.out.println(args[1]);

        // If a rank is specified.
        if (args[1].matches("[0-9]")) {

            PlayerData playerData = leaderboard.get(Integer.parseInt(args[1]));
            if (playerData == null) return "Empty";

            // If they want the players name.
            if (args.length == 2) {
                return playerData.getInformation().getString("name", "NoName");
            }

            return Integer.toString(playerData.getAmountFound());
        }

        PlayerData playerData = leaderboard.get(Integer.parseInt(args[2]));
        if (playerData == null) return "Empty";

        // If they want the players name.
        if (args.length == 3) {
            return playerData.getInformation().getString("name", "NoName");
        }

        return Integer.toString(playerData.getTreasureFound(args[0]));
    }
}
