package com.github.cozyplugins.cozytreasurehunt.storage;

import com.github.cozyplugins.cozylibrary.ConsoleManager;
import com.github.cozyplugins.cozylibrary.configuration.ConfigurationDirectory;
import com.github.smuddgge.squishyconfiguration.implementation.yaml.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Represents a storage utility class.
 */
public class Storage {

    /**
     * Used to get the configuration file instance where the key is saved to.
     * If this key does not exist in any of the files, it will return the first configuration file.
     *
     * @param key       The instance of the key.
     * @param directory The instance of the configuration directory.
     * @return The configuration file.
     * Null, if there are no configuration files in the directory.
     */
    public static @Nullable YamlConfiguration getConfiguration(@NotNull String key, @NotNull ConfigurationDirectory directory) {
        for (File file : directory.getFiles()) {
            YamlConfiguration configuration = new YamlConfiguration(file);
            configuration.load();

            if (configuration.getKeys().contains(key)) {
                return configuration;
            }
        }

        // If it was not found, use the first file.
        File file = directory.getFiles().get(0);
        if (file == null) {
            ConsoleManager.error("There are no files in the directory where default should be " + directory.getDefaultFileName());
            return null;
        }

        return new YamlConfiguration(file);
    }
}
