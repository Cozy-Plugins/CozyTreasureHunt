package com.github.cozyplugins.cozytreasurehunt.storage;

import com.github.cozyplugins.cozylibrary.CozyPlugin;
import com.github.smuddgge.squishyconfiguration.implementation.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * Represents the configuration file.
 */
public class ConfigFile {

    private static @NotNull YamlConfiguration config;

    /**
     * Used to create the default configuration file.
     * If it was created this method will return true.
     * If the file already exists it will not create the file.
     *
     * @return True if the file was created.
     */
    private static void createDefault() {
        File file = new File(CozyPlugin.getPlugin().getDataFolder(), "config.yml");
        if (file.exists()) return;

        // Attempt to copy the configuration file.
        try (InputStream input = CozyPlugin.class.getResourceAsStream("/config.yml")) {

            if (input != null) Files.copy(input, file.toPath());

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Used to set up the configuration file.
     */
    public static void setup() {
        File file = new File(CozyPlugin.getPlugin().getDataFolder(), "config.yml");

        // Attempt to create the default file.
        ConfigFile.createDefault();

        // Create configuration instance.
        ConfigFile.config = new YamlConfiguration(file);
        ConfigFile.config.load();
    }

    /**
     * Used to get the global treasure limit.
     *
     * @return The global treasure limit.
     */
    public static int getGlobalLimit() {
        return ConfigFile.config.getInteger("global_treasure_limit", -1);
    }

    /**
     * Used to get the global limit message.
     * This message is shown if a player is trying to
     * click treasure after reaching the global treasure limit.
     *
     * @return The global limit message.
     */
    public static @NotNull String getGlobalLimitMessage() {
        return ConfigFile.config.getString("global_treasure_limit_message",
                "&7You have reached the global treasure limit. You cannot redeem any more treasure.");
    }
}
