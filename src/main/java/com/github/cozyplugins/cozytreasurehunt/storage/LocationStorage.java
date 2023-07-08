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
import com.github.cozyplugins.cozytreasurehunt.TreasureLocation;
import com.github.cozyplugins.cozytreasurehunt.storage.configuration.LocationConfigurationDirectory;
import com.github.smuddgge.squishyconfiguration.implementation.yaml.YamlConfiguration;
import com.github.smuddgge.squishyconfiguration.interfaces.ConfigurationSection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Represents the treasure location storage.
 * <p>
 * Contains static methods to get stored treasure locations.
 * </p>
 * <li>
 * These method names should not change.
 * However, the type of storage can change.
 * </li>
 */
public final class LocationStorage {

    private static final LocationConfigurationDirectory storage = new LocationConfigurationDirectory();

    /**
     * Used to load the location data.
     */
    public static void load() {
        LocationStorage.storage.reload();
    }

    /**
     * Used to insert a treasure location into the first file.
     *
     * @param treasureLocation The instance of the treasure location.
     */
    public static void insert(@NotNull TreasureLocation treasureLocation) {
        String identifier = treasureLocation.getIdentifier();
        ConfigurationSection section = treasureLocation.convert();
        YamlConfiguration configuration = Storage.getConfiguration(identifier, LocationStorage.storage);

        if (configuration == null) return;

        // Save it to the file.
        configuration.load();
        configuration.set(identifier, section.getMap());
        configuration.save();
    }

    /**
     * Used to get the instance of a treasure location class from the storage medium.
     *
     * @param identifier The location's identifier.
     * @return The instance of the treasure location.
     */
    public static @Nullable TreasureLocation get(String identifier) {
        ConfigurationSection section = LocationStorage.storage.getSection(identifier);
        if (section == null) return null;
        return TreasureLocation.create(section);
    }

    /**
     * Used to get a treasure location at a location efficiently.
     *
     * @param location The location instance.
     * @return The treasure location.
     */
    public static @Nullable TreasureLocation get(@NotNull Location location) {
        String identifier = TreasureLocation.getIdentifier(location);
        return LocationStorage.get(identifier);
    }

    /**
     * Used to get a treasure location at a location efficiently.
     *
     * @param worldName The name of the world.
     * @param x         The exact block x cord.
     * @param y         The exact block y cord.
     * @param z         The exact block z cord.
     * @return The instance of the treasure location.
     * Null if there is no treasure at this location.
     */
    public static @Nullable TreasureLocation get(@NotNull String worldName, int x, int y, int z) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) return null;

        Location location = new Location(world, x, y, z);
        return LocationStorage.get(location);
    }

    /**
     * Used to check if the configuration contains a key.
     * This will check if there is a treasure in that
     * location in an efficient way.
     *
     * @param location The instance of the location.
     * @return True if there is a treasure in the location.
     */
    public static boolean contains(Location location) {
        return LocationStorage.storage.getKeys().contains(TreasureLocation.getIdentifier(location));
    }

    /**
     * Used to delete a treasure location from storage.
     *
     * @param identifier The treasure's identifier.
     */
    public static void delete(String identifier) {
        for (File file : LocationStorage.storage.getFiles()) {
            YamlConfiguration configuration = new YamlConfiguration(file);
            if (!configuration.getKeys().contains(identifier)) continue;

            configuration.set(identifier, null);
            return;
        }
        ConsoleManager.warn("Unable to delete treasure location with identifier " + identifier);
    }
}
