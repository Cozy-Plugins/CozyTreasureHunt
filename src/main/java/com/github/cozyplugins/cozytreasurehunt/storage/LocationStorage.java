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
import com.github.cozyplugins.cozylibrary.location.Region3D;
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
import java.util.ArrayList;
import java.util.List;

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
        LocationStorage.storage.reload();
    }

    /**
     * Used to get the instance of a treasure location class from the storage medium.
     *
     * @param identifier The location's identifier.
     * @return The instance of the treasure location.
     */
    public static @Nullable TreasureLocation get(String identifier) {
        LocationStorage.storage.reload(); // When files are deleted the config isn't reloaded.
        if (!LocationStorage.storage.getKeys().contains(identifier)) return null;
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
     * Used to get the list of all treasure locations.
     *
     * @return The list of all treasure locations.
     */
    public static @NotNull List<TreasureLocation> getAll() {
        LocationStorage.storage.reload(); // When files are deleted the config isn't reloaded.
        List<TreasureLocation> treasureLocationList = new ArrayList<>();
        boolean containsInvalidLocations = false;

        for (String key : LocationStorage.storage.getKeys()) {
            ConfigurationSection section = LocationStorage.storage.getSection(key);
            TreasureLocation location = TreasureLocation.create(section);

            // Check if the location is null.
            if (location == null) {
                containsInvalidLocations = true;
                continue;
            }

            treasureLocationList.add(location);
        }

        // Check if there were invalid locations.
        if (containsInvalidLocations) {
            ConsoleManager.warn(
                    "When spawning treasure, there was treasure that could " +
                            "not be spawned due to the treasure type no longer existing."
            );
        }

        return treasureLocationList;
    }

    /**
     * Used to get the closest treasure location
     * to the center of a region.
     *
     * @param region The instance of the region.
     * @return The closest treasure location.
     */
    public static @Nullable TreasureLocation getClosest(@NotNull Region3D region) {
        TreasureLocation toReturn = null;
        double distance = 1000000;

        for (TreasureLocation location : LocationStorage.getAll()) {
            if (!region.contains(location.getLocation())) continue;

            // Get distance.
            double distanceFromCenter = region.getDistanceFromCenter(location.getLocation());

            // Check if this distance is smaller than the current smallest.
            if (distanceFromCenter < distance) {
                toReturn = location;
                distance = distanceFromCenter;
            }
        }

        return toReturn;
    }

    /**
     * Used to get the total amount of spawn locations.
     *
     * @return The total amount of spawn locations.
     */
    public static int getTotalAmount() {
        return LocationStorage.storage.getKeys().size();
    }

    /**
     * Used to get the total amount of spawn locations.
     *
     * @return The total amount of spawn locations.
     */
    public static int getTotalAmount(@NotNull String treasureName) {
        int amount = 0;

        for (TreasureLocation location : LocationStorage.getAll()) {
            if (!location.getTreasure().getName().equals(treasureName)) continue;
            amount++;
        }

        return amount;
    }

    /**
     * Used to get the total amount of treasure that is spawned.
     *
     * @return The total amount spawned.
     */
    public static int getAmountSpawned() {
        int amount = 0;

        for (String key : LocationStorage.storage.getKeys()) {
            if (LocationStorage.storage.getSection(key).getBoolean("is_spawned", false)) amount++;
        }

        return amount;
    }

    /**
     * Used to get the total amount of treasure that is spawned.
     *
     * @param treasureName The name of the treasure.
     * @return The requested amount treasure that is spawned.
     */
    public static int getAmountSpawned(@NotNull String treasureName) {
        int amount = 0;

        for (TreasureLocation location : LocationStorage.getAll()) {
            if (!location.isSpawned()) continue;
            if (!location.getTreasure().getName().equals(treasureName)) continue;
            amount++;
        }

        return amount;
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
     * Used to remove a treasure location from storage.
     *
     * @param identifier The treasure's identifier.
     */
    public static void remove(@NotNull String identifier) {
        // Get the treasure location.
        TreasureLocation location = LocationStorage.get(identifier);
        if (location == null) return;

        // Remove the location from the world.
        location.removeSilently();

        // Remove the location from configuration.
        for (File file : LocationStorage.storage.getFiles()) {
            YamlConfiguration configuration = new YamlConfiguration(file);
            configuration.load();

            if (!configuration.getKeys().contains(identifier)) continue;

            configuration.set(identifier, null);
            configuration.save();

            LocationStorage.storage.reload();
            return;
        }
        ConsoleManager.warn("Unable to delete treasure location with identifier " + identifier);
    }

    /**
     * Used to remove all locations.
     */
    public static void removeAll() {
        // Remove the treasures from the worlds.
        for (TreasureLocation location : LocationStorage.getAll()) {
            location.removeSilently();
        }

        // Remove the treasures from the configuration file.
        for (File file : LocationStorage.storage.getFiles()) {
            YamlConfiguration configuration = new YamlConfiguration(file);
            configuration.set(null);
            configuration.save();
        }

        LocationStorage.storage.reload();
    }
}
