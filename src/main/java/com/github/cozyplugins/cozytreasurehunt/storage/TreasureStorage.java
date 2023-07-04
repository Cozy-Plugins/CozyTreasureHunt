package com.github.cozyplugins.cozytreasurehunt.storage;

import com.github.cozyplugins.cozylibrary.ConsoleManager;
import com.github.cozyplugins.cozytreasurehunt.Treasure;
import com.github.cozyplugins.cozytreasurehunt.storage.configuration.TreasureConfigurationDirectory;
import com.github.smuddgge.squishyconfiguration.implementation.yaml.YamlConfiguration;
import com.github.smuddgge.squishyconfiguration.interfaces.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Represents the treasure storage.
 * <p>
 * Contains static methods to get treasure and set treasure.
 * These method names should not change, however the type of storage can change.
 * </p>
 */
public final class TreasureStorage {

    private final static TreasureConfigurationDirectory storage = new TreasureConfigurationDirectory();

    /**
     * Used to insert treasure into the first file.
     *
     * @param treasure The instance of the treasure.
     */
    public static void insert(@NotNull Treasure treasure) {
        String identifier = treasure.getIdentifier();
        ConfigurationSection section = treasure.convert();

        // Get the first file.
        TreasureStorage.storage.reload();
        File file = TreasureStorage.storage.getFiles().get(0);
        if (file == null) {
            ConsoleManager.error("There are no files in the treasure configuration directory.");
            return;
        }

        // Save it to the first file.
        YamlConfiguration configuration = new YamlConfiguration(file);
        configuration.load();
        configuration.set(identifier, section);
        configuration.save();
    }

    /**
     * Used to get the instance of a treasure class from the storage medium.
     *
     * @param identifier The treasure's identifier.
     * @return The instance of the treasure.
     */
    public @Nullable Treasure get(String identifier) {
        ConfigurationSection section = TreasureStorage.storage.getSection(identifier);
        if (section == null) return null;
        return Treasure.create(identifier, section);
    }
}
