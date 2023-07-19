package com.github.cozyplugins.cozytreasurehunt.command.subcommand;

import com.github.cozyplugins.cozylibrary.MessageManager;
import com.github.cozyplugins.cozylibrary.command.command.CommandType;
import com.github.cozyplugins.cozylibrary.command.datatype.CommandArguments;
import com.github.cozyplugins.cozylibrary.command.datatype.CommandStatus;
import com.github.cozyplugins.cozylibrary.command.datatype.CommandSuggestions;
import com.github.cozyplugins.cozylibrary.command.datatype.CommandTypePool;
import com.github.cozyplugins.cozylibrary.user.ConsoleUser;
import com.github.cozyplugins.cozylibrary.user.FakeUser;
import com.github.cozyplugins.cozylibrary.user.PlayerUser;
import com.github.cozyplugins.cozylibrary.user.User;
import com.github.cozyplugins.cozytreasurehunt.TreasureLocation;
import com.github.cozyplugins.cozytreasurehunt.storage.LocationStorage;
import com.github.cozyplugins.cozytreasurehunt.storage.TreasureStorage;
import com.github.smuddgge.squishyconfiguration.interfaces.ConfigurationSection;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the find command.
 * Used to find treasure that is spawned.
 */
public class FindCommand implements CommandType {

    @Override
    public @NotNull String getIdentifier() {
        return "find";
    }

    @Override
    public @Nullable String getSyntax() {
        return "/[parent] [name] <optional treasure type>";
    }

    @Override
    public @Nullable String getDescription() {
        return "Used to find spawned treasure.";
    }

    @Override
    public @Nullable CommandTypePool getSubCommandTypes() {
        return null;
    }

    @Override
    public @Nullable CommandSuggestions getSuggestions(@NotNull User user, @NotNull ConfigurationSection section, @NotNull CommandArguments arguments) {
        return new CommandSuggestions().append(TreasureStorage.getAllNames());
    }

    @Override
    public @Nullable CommandStatus onUser(@NotNull User user, @NotNull ConfigurationSection section, @NotNull CommandArguments arguments) {
        return null;
    }

    @Override
    public @Nullable CommandStatus onPlayer(@NotNull PlayerUser user, @NotNull ConfigurationSection section, @NotNull CommandArguments arguments) {
        int amountOfTreasure = 0;
        List<TreasureLocation> foundTreasure = new ArrayList<>();

        // If the player is searching for a specific treasure.
        boolean specificTreasure = arguments.getArguments().size() > 1;

        // Loop though every treasure.
        for (TreasureLocation location : LocationStorage.getAll()) {
            // If they are searching for a specific treasure and this is not that treasure.
            if (specificTreasure && !location.getTreasure().getName().equals(arguments.getArguments().get(1))) continue;

            // Increase amount of treasure.
            amountOfTreasure++;

            // Add treasure location.
            foundTreasure.add(location);
        }

        // Send footer
        user.sendMessage(section.getString("header", "&aFound &f{amount} &atreasure.")
                .replace("{amount}", String.valueOf(amountOfTreasure))
        );

        // Get the maximum number of lines.
        int max = section.getInteger("max_lines", 5);
        int current = 0;

        // Loop though found treasure.
        for (TreasureLocation location : foundTreasure) {
            current++;
            if (current > max) return new CommandStatus();

            // Create a text component and send.
            TextComponent message = new TextComponent(
                    MessageManager.parse(
                            section.getString("line", "&e&l{name} &7is at &f{location}&7. &eClick to teleport.")
                                    .replace("{location}", location.getLocation().getBlockX()
                                            + " " + location.getLocation().getBlockY() + " " + location.getLocation().getBlockZ())
                                    .replace("{name}", location.getTreasure().getName())
                            , user.getPlayer()
                    )
            );

            String teleportCommand = "/tp " +
                    location.getLocation().getBlockX() + " " +
                    location.getLocation().getBlockY() + " " +
                    location.getLocation().getBlockZ();

            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, teleportCommand));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(MessageManager.parse("&6&lTeleport")).create()));

            user.getPlayer().spigot().sendMessage(message);
        }

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
