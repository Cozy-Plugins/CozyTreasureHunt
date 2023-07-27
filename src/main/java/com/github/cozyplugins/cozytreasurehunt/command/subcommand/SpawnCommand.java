package com.github.cozyplugins.cozytreasurehunt.command.subcommand;

import com.github.cozyplugins.cozylibrary.command.command.CommandType;
import com.github.cozyplugins.cozylibrary.command.datatype.CommandArguments;
import com.github.cozyplugins.cozylibrary.command.datatype.CommandStatus;
import com.github.cozyplugins.cozylibrary.command.datatype.CommandSuggestions;
import com.github.cozyplugins.cozylibrary.command.datatype.CommandTypePool;
import com.github.cozyplugins.cozylibrary.inventory.action.action.ConfirmAction;
import com.github.cozyplugins.cozylibrary.inventory.inventory.ConfirmationInventory;
import com.github.cozyplugins.cozylibrary.user.ConsoleUser;
import com.github.cozyplugins.cozylibrary.user.FakeUser;
import com.github.cozyplugins.cozylibrary.user.PlayerUser;
import com.github.cozyplugins.cozylibrary.user.User;
import com.github.cozyplugins.cozytreasurehunt.CozyTreasureHunt;
import com.github.cozyplugins.cozytreasurehunt.result.TreasureSpawnResult;
import com.github.smuddgge.squishyconfiguration.interfaces.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Used to spawn the treasure locations.
 */
public class SpawnCommand implements CommandType {

    @Override
    public @NotNull String getIdentifier() {
        return "spawn";
    }

    @Override
    public @Nullable String getSyntax() {
        return "/[parent] [name]";
    }

    @Override
    public @Nullable String getDescription() {
        return "Used to spawn all the treasure.";
    }

    @Override
    public @Nullable CommandTypePool getSubCommandTypes() {
        return null;
    }

    @Override
    public @Nullable CommandSuggestions getSuggestions(@NotNull User user, @NotNull ConfigurationSection section, @NotNull CommandArguments arguments) {
        return null;
    }

    @Override
    public @Nullable CommandStatus onUser(@NotNull User user, @NotNull ConfigurationSection section, @NotNull CommandArguments arguments) {
        return null;
    }

    @Override
    public @Nullable CommandStatus onPlayer(@NotNull PlayerUser user, @NotNull ConfigurationSection section, @NotNull CommandArguments arguments) {
        // Confirm action.
        new ConfirmationInventory(new ConfirmAction()
                .setAnvilTitle("&8&lDelete data and spawn")
                .setConfirm(user1 -> {
                    // Spawn all treasure locations.
                    TreasureSpawnResult result = CozyTreasureHunt.spawnTreasure();

                    // Display the result to the user.
                    user1.sendMessage(section.getString("spawned_treasure", "&7Attempted to spawn all treasure. {result}")
                            .replace("{result}", result.toString())
                    );
                })
        ).open(user.getPlayer());

        return new CommandStatus();
    }

    @Override
    public @Nullable CommandStatus onFakeUser(@NotNull FakeUser user, @NotNull ConfigurationSection section, @NotNull CommandArguments arguments) {
        // Spawn all treasure locations.
        TreasureSpawnResult result = CozyTreasureHunt.spawnTreasure();

        // Display the result to the user.
        user.sendMessage(section.getString("spawned_treasure", "&7Attempted to spawn all treasure. {result}")
                .replace("{result}", result.toString())
        );

        return new CommandStatus();
    }

    @Override
    public @Nullable CommandStatus onConsole(@NotNull ConsoleUser user, @NotNull ConfigurationSection section, @NotNull CommandArguments arguments) {
        // Spawn all treasure locations.
        TreasureSpawnResult result = CozyTreasureHunt.spawnTreasure();

        // Display the result to the user.
        user.sendMessage(section.getString("spawned_treasure", "&7Attempted to spawn all treasure. {result}")
                .replace("{result}", result.toString())
        );

        return new CommandStatus();
    }

}
