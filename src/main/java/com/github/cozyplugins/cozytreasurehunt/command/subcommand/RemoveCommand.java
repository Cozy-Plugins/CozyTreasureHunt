package com.github.cozyplugins.cozytreasurehunt.command.subcommand;

import com.github.cozyplugins.cozylibrary.command.command.CommandType;
import com.github.cozyplugins.cozylibrary.command.datatype.CommandArguments;
import com.github.cozyplugins.cozylibrary.command.datatype.CommandStatus;
import com.github.cozyplugins.cozylibrary.command.datatype.CommandSuggestions;
import com.github.cozyplugins.cozylibrary.command.datatype.CommandTypePool;
import com.github.cozyplugins.cozylibrary.inventory.action.action.ConfirmAction;
import com.github.cozyplugins.cozylibrary.inventory.inventory.ConfirmationInventory;
import com.github.cozyplugins.cozylibrary.location.Region3D;
import com.github.cozyplugins.cozylibrary.user.ConsoleUser;
import com.github.cozyplugins.cozylibrary.user.FakeUser;
import com.github.cozyplugins.cozylibrary.user.PlayerUser;
import com.github.cozyplugins.cozylibrary.user.User;
import com.github.cozyplugins.cozytreasurehunt.TreasureLocation;
import com.github.cozyplugins.cozytreasurehunt.storage.LocationStorage;
import com.github.smuddgge.squishyconfiguration.interfaces.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the remove treasure command.
 * This command is used to remove all or nearby treasure locations.
 */
public class RemoveCommand implements CommandType {

    @Override
    public @NotNull String getIdentifier() {
        return "remove";
    }

    @Override
    public @Nullable String getSyntax() {
        return "/[parent] [name";
    }

    @Override
    public @Nullable String getDescription() {
        return "Used to remove treasure locations.";
    }

    @Override
    public @Nullable CommandTypePool getSubCommandTypes() {
        return null;
    }

    @Override
    public @Nullable CommandSuggestions getSuggestions(@NotNull User user, @NotNull ConfigurationSection section, @NotNull CommandArguments arguments) {
        return new CommandSuggestions().append(new String[]{"all"});
    }

    @Override
    public @Nullable CommandStatus onUser(@NotNull User user, @NotNull ConfigurationSection section, @NotNull CommandArguments arguments) {
        return null;
    }

    @Override
    public @Nullable CommandStatus onPlayer(@NotNull PlayerUser user, @NotNull ConfigurationSection section, @NotNull CommandArguments arguments) {
        // Check if the player wants all locations deleted.
        if (arguments.getArguments().contains("all")) {
            new ConfirmationInventory(
                    new ConfirmAction()
                            .setAnvilTitle("&8&lRemove all locations")
                            .setConfirm(user1 -> {
                                LocationStorage.removeAll();
                                user1.sendMessage(section.getString("remove_all", "&7All locations have been removed."));
                            })
            ).open(user.getPlayer());
            return new CommandStatus();
        }

        // Get closest treasure in region.
        Region3D region = new Region3D(user.getPlayer().getLocation(), user.getPlayer().getLocation());
        region.expand(4);

        TreasureLocation location = LocationStorage.getClosest(region);
        if (location == null) {
            user.sendMessage(section.getString("none", "&7There are no treasure locations nearby. (4 block radius)"));
            return new CommandStatus();
        }

        // Remove the location.
        location.removeSilently();
        LocationStorage.remove(location.getIdentifier());

        user.sendMessage(section.getString("removed", "&7Removed treasure at location {location}")
                .replace("{location}", location.toString())
        );

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
