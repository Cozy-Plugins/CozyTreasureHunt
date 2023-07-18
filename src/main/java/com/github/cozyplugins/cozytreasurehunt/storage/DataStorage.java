/*
    This file is part of the project CozyTreasureHunt.
    Copyright (C) 2023  Smudge (Smuddgge), Cozy Plugins and contributors.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.cozyplugins.cozytreasurehunt.storage;

import com.github.cozyplugins.cozylibrary.ConsoleManager;
import com.github.cozyplugins.cozytreasurehunt.storage.configuration.DataConfigurationDirectory;
import com.github.smuddgge.squishyconfiguration.implementation.yaml.YamlConfiguration;
import com.github.smuddgge.squishyconfiguration.interfaces.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Represents the data storage.
 * <p>
 * Contains static methods to get stored player and treasure data.
 * </p>
 * <li>
 * These method names should not change.
 * However, the type of storage can change.
 * </li>
 */
public class DataStorage {

    private static final @NotNull DataConfigurationDirectory storage = new DataConfigurationDirectory();

    /**
     * Used to set the new save location without extensions.
     * This will be where player data is stored.
     *
     * @param fileName The name of the file.
     */
    public static void setSaveLocation(@NotNull String fileName) {
        YamlConfiguration store = storage.createStore();
        store.set("file_name", fileName);
        store.save();
    }

    /**
     * Used to get the save location file name without extensions.
     *
     * @return The save file name.
     */
    public static @NotNull String getSaveLocation() {
        YamlConfiguration store = storage.createStore();
        return store.getString("file_name", "data");
    }

    /**
     * Used to get the save configuration file.
     *
     * @return The instance of the configuration file.
     */
    public static @NotNull YamlConfiguration getSaveConfiguration() {
        String saveLocation = DataStorage.getSaveLocation();

        // Find the storage file and save the player data.
        for (File file : storage.getFiles()) {
            if (!file.getName().contains(saveLocation)) continue;

            YamlConfiguration configuration = new YamlConfiguration(file);
            configuration.load();
            return configuration;
        }

        // If there are no files with the name of the save location.
        try {
            // Attempt to create the file.
            File file = new File(storage.getDirectory().getAbsolutePath() + "/" + saveLocation + ".yml");
            file.createNewFile();

            YamlConfiguration configuration = new YamlConfiguration(file);
            configuration.load();
            return configuration;

        } catch (IOException exception) {
            ConsoleManager.error(
                    "Unable to create file in directory. File path : "
                            + storage.getDirectory().getAbsolutePath() + "/" + saveLocation + ".yml"
            );

            exception.printStackTrace();
        }

        return new YamlConfiguration(new File(storage.getDirectory().getAbsolutePath() + "/" + saveLocation + ".yml"));
    }

    /**
     * Used to put player data in to the storage medium.
     *
     * @param playerData The instance of the player data.
     */
    public static void insert(@NotNull PlayerData playerData) {
        YamlConfiguration saveLocation = DataStorage.getSaveConfiguration();
        saveLocation.set(playerData.getIdentifier().toString(), playerData.convert().getMap());
        saveLocation.save();
    }

    /**
     * Used to get player data using the player uuid.
     *
     * @param playerUuid The player uuid.
     */
    public static @NotNull PlayerData get(@NotNull UUID playerUuid) {
        ConfigurationSection section = DataStorage.getSaveConfiguration().getSection(playerUuid.toString());
        return PlayerData.create(playerUuid, section);
    }
}
