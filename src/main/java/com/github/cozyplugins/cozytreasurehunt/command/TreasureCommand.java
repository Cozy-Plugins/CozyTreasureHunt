package com.github.cozyplugins.cozytreasurehunt.command;

import com.github.cozyplugins.cozylibrary.command.command.CommandType;
import com.github.cozyplugins.cozylibrary.command.datatype.CommandArguments;
import com.github.cozyplugins.cozylibrary.command.datatype.CommandStatus;
import com.github.cozyplugins.cozylibrary.command.datatype.CommandSuggestions;
import com.github.cozyplugins.cozylibrary.command.datatype.CommandTypePool;
import com.github.cozyplugins.cozylibrary.user.ConsoleUser;
import com.github.cozyplugins.cozylibrary.user.FakeUser;
import com.github.cozyplugins.cozylibrary.user.PlayerUser;
import com.github.cozyplugins.cozylibrary.user.User;
import com.github.cozyplugins.cozytreasurehunt.command.subcommand.EditorCommand;
import com.github.smuddgge.squishyconfiguration.interfaces.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TreasureCommand implements CommandType {

    @Override
    public @NotNull String getIdentifier() {
        return "treasure";
    }

    @Override
    public @Nullable String getSyntax() {
        return "/[name] [subcommands]";
    }

    @Override
    public @Nullable String getDescription() {
        return "Used to interface with the treasure hunt plugin.";
    }

    @Override
    public @Nullable CommandTypePool getSubCommandTypes() {
        CommandTypePool pool = new CommandTypePool();

        pool.add(new EditorCommand());

        return pool;
    }

    @Override
    public @Nullable CommandSuggestions getSuggestions(@NotNull User user, @NotNull ConfigurationSection configurationSection, @NotNull CommandArguments commandArguments) {
        return null;
    }

    @Override
    public @Nullable CommandStatus onUser(@NotNull User user, @NotNull ConfigurationSection configurationSection, @NotNull CommandArguments commandArguments) {
        return null;
    }

    @Override
    public @Nullable CommandStatus onPlayer(@NotNull PlayerUser playerUser, @NotNull ConfigurationSection configurationSection, @NotNull CommandArguments commandArguments) {
        return null;
    }

    @Override
    public @Nullable CommandStatus onFakeUser(@NotNull FakeUser fakeUser, @NotNull ConfigurationSection configurationSection, @NotNull CommandArguments commandArguments) {
        return null;
    }

    @Override
    public @Nullable CommandStatus onConsole(@NotNull ConsoleUser consoleUser, @NotNull ConfigurationSection configurationSection, @NotNull CommandArguments commandArguments) {
        return null;
    }
}
