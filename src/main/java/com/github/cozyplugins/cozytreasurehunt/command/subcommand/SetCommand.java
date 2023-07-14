package com.github.cozyplugins.cozytreasurehunt.command.subcommand;

import com.github.cozyplugins.cozylibrary.command.command.CommandType;
import com.github.cozyplugins.cozylibrary.command.datatype.CommandArguments;
import com.github.cozyplugins.cozylibrary.command.datatype.CommandStatus;
import com.github.cozyplugins.cozylibrary.command.datatype.CommandSuggestions;
import com.github.cozyplugins.cozylibrary.command.datatype.CommandTypePool;
import com.github.cozyplugins.cozylibrary.user.ConsoleUser;
import com.github.cozyplugins.cozylibrary.user.FakeUser;
import com.github.cozyplugins.cozylibrary.user.PlayerUser;
import com.github.cozyplugins.cozylibrary.user.User;
import com.github.cozyplugins.cozytreasurehunt.Treasure;
import com.github.cozyplugins.cozytreasurehunt.TreasureLocation;
import com.github.cozyplugins.cozytreasurehunt.storage.LocationStorage;
import com.github.cozyplugins.cozytreasurehunt.storage.TreasureStorage;
import com.github.smuddgge.squishyconfiguration.interfaces.ConfigurationSection;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Used to set treasure locations.
 * This command is used to set treasure locations at where the player is standing.
 */
public class SetCommand implements CommandType {

    @Override
    public @NotNull String getIdentifier() {
        return "set";
    }

    @Override
    public @Nullable String getSyntax() {
        return "/[parent] [name]";
    }

    @Override
    public @Nullable String getDescription() {
        return "Used to set a treasure location where you are standing.";
    }

    @Override
    public @Nullable CommandTypePool getSubCommandTypes() {
        return null;
    }

    @Override
    public @Nullable CommandSuggestions getSuggestions(@NotNull User user, @NotNull ConfigurationSection configurationSection, @NotNull CommandArguments commandArguments) {
        return new CommandSuggestions().append(TreasureStorage.getAllNames());
    }

    @Override
    public @Nullable CommandStatus onUser(@NotNull User user, @NotNull ConfigurationSection section, @NotNull CommandArguments arguments) {
        return null;
    }

    @Override
    public @Nullable CommandStatus onPlayer(@NotNull PlayerUser user, @NotNull ConfigurationSection section, @NotNull CommandArguments arguments) {
        // Check if there is no treasure specified.
        if (arguments.getArguments().isEmpty() || Objects.equals(arguments.getArguments().get(0), "")) {
            user.sendMessage(section.getString("invalid_treasure", "&7Treasure type does not exist."));
            return new CommandStatus();
        }

        // Get the treasure type.
        Treasure treasure = TreasureStorage.getFirst(arguments.getArguments().get(0));

        // Check if the treasure exists.
        if (treasure == null) {
            user.sendMessage(section.getString("invalid_treasure", "&7Treasure type does not exist."));
            return new CommandStatus();
        }

        // Get the player location.
        Location location = user.getPlayer().getLocation();

        boolean treasureInLocation = LocationStorage.get(location) != null;

        // Check if there is already treasure in this location
        // and treasure cannot be overridden.
        if (treasureInLocation && !section.getBoolean("override_treasure", true)) {
            user.sendMessage(section.getString(
                    "unable_to_override_treasure",
                    "&7Treasure already exists in this location and cannot be overridden.")
            );

            return new CommandStatus();
        }

        // Create treasure location.
        TreasureLocation treasureLocation = new TreasureLocation(treasure, location);
        treasureLocation.save();

        // Check if the treasure should be spawned immediately.
        if (section.getBoolean("spawn_immediately", false)) {
            treasureLocation.spawn();
        }

        if (treasureInLocation) {
            user.sendMessage(section.getString("replaced_treasure", "&7The treasure in this location has been replaced."));
            return new CommandStatus();
        }

        user.sendMessage(section.getString("placed_treasure", "&7Treasure has been placed in this location."));
        return new CommandStatus();
    }

    @Override
    public @Nullable CommandStatus onFakeUser(@NotNull FakeUser user, @NotNull ConfigurationSection section, @NotNull CommandArguments arguments) {
        return null;
    }

    @Override
    public @Nullable CommandStatus onConsole(@NotNull ConsoleUser user, @NotNull ConfigurationSection section, @NotNull CommandArguments arguments) {
        return null;
    }
}
