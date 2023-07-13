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
import com.github.cozyplugins.cozylibrary.configuration.ConfigurationDirectory;
import com.github.cozyplugins.cozytreasurehunt.Treasure;
import com.github.cozyplugins.cozytreasurehunt.storage.configuration.TreasureConfigurationDirectory;
import com.github.smuddgge.squishyconfiguration.implementation.yaml.YamlConfiguration;
import com.github.smuddgge.squishyconfiguration.interfaces.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Represents the treasure storage.
 * <p>
 * Contains static methods to get treasure and set treasure.
 * </p>
 * <li>
 * These method names should not change.
 * However, the type of storage can change.
 * </li>
 */
public final class TreasureStorage {

    private final static TreasureConfigurationDirectory storage = new TreasureConfigurationDirectory();

    /**
     * Used to load the treasure data.
     */
    public static void load() {
        TreasureStorage.storage.reload();
    }

    /**
     * Used to insert treasure into the first file.
     *
     * @param treasure The instance of the treasure.
     */
    public static void insert(@NotNull Treasure treasure) {
        UUID identifier = treasure.getIdentifier();
        ConfigurationSection section = treasure.convert();
        YamlConfiguration configuration = Storage.getConfiguration(identifier.toString(), TreasureStorage.storage);

        if (configuration == null) return;

        // Save it to the file.
        configuration.load();
        configuration.set(identifier.toString(), section.getMap());
        configuration.save();
        TreasureStorage.load();
    }

    /**
     * Used to get the instance of a treasure class from the storage medium.
     *
     * @param identifier The treasure's identifier.
     * @return The instance of the treasure.
     */
    public static @Nullable Treasure get(UUID identifier) {
        ConfigurationSection section = TreasureStorage.storage.getSection(identifier.toString());
        if (section == null) return null;
        return Treasure.create(identifier, section);
    }

    /**
     * Used to get the first treasure with the treasure name.
     *
     * @param treasureName The treasure's name to search for.
     * @return The requested treasure.
     */
    public static @Nullable Treasure getFirst(@Nullable String treasureName) {
        if (treasureName == null) return null;
        for (String identifier : TreasureStorage.storage.getKeys()) {
            String tempName = TreasureStorage.storage.getString(identifier + ".name", null);
            if (treasureName.equals(tempName)) return TreasureStorage.get(UUID.fromString(identifier));
        }
        return null;
    }

    /**
     * Used to get the list of all treasure stored.
     *
     * @return The list of treasure.
     */
    public static List<Treasure> getAll() {
        List<Treasure> treasureList = new ArrayList<>();

        for (String key : TreasureStorage.storage.getKeys()) {
            treasureList.add(TreasureStorage.get(UUID.fromString(key)));
        }

        return treasureList;
    }

    /**
     * Used to get the names of the active treasure types.
     *
     * @return The list of treasure names.
     */
    public static List<String> getAllNames() {
        List<String> nameList = new ArrayList<>();
        for (String key : TreasureStorage.storage.getKeys()) {
            nameList.add(TreasureStorage.storage.getString(key + ".name"));
        }
        return nameList;
    }

    /**
     * Used to get the amount of treasure saved in storage.
     *
     * @return The amount of treasure.
     */
    public static int getAmount() {
        return TreasureStorage.storage.getKeys().size();
    }

    /**
     * Used to delete a treasure from storage.
     *
     * @param identifier The treasure's identifier.
     */
    public static void delete(UUID identifier) {
        YamlConfiguration configuration = Storage.getConfiguration(identifier.toString(), TreasureStorage.storage);

        if (configuration == null) {
            ConsoleManager.warn("Unable to delete treasure with identifier " + identifier);
            return;
        }

        configuration.load();
        configuration.set(identifier.toString(), null);
        configuration.save();
        TreasureStorage.load();
    }

    /**
     * Used to get the storage medium.
     * If the return value is changed, functionality
     * will need to also change elsewhere in the plugin.
     *
     * @return The command directory.
     */
    public static ConfigurationDirectory getMedium() {
        return TreasureStorage.storage;
    }
}
