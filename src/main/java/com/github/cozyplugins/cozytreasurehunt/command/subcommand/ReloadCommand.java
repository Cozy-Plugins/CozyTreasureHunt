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
import com.github.cozyplugins.cozytreasurehunt.storage.ConfigFile;
import com.github.cozyplugins.cozytreasurehunt.storage.LocationStorage;
import com.github.cozyplugins.cozytreasurehunt.storage.TreasureStorage;
import com.github.smuddgge.squishyconfiguration.interfaces.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the reload command.
 * Reloads all configuration files.
 */
public class ReloadCommand implements CommandType {

    @Override
    public @NotNull String getIdentifier() {
        return "reload";
    }

    @Override
    public @Nullable String getSyntax() {
        return "/[parent] [name]";
    }

    @Override
    public @Nullable String getDescription() {
        return "Used to reload the configuration.";
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
        ConfigFile.setup();
        TreasureStorage.load();
        LocationStorage.load();
        user.sendMessage("&7Configuration reloaded.");
        return new CommandStatus();
    }

    @Override
    public @Nullable CommandStatus onPlayer(@NotNull PlayerUser user, @NotNull ConfigurationSection section, @NotNull CommandArguments arguments) {
        return null;
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
