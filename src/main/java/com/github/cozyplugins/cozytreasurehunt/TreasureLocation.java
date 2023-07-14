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

package com.github.cozyplugins.cozytreasurehunt;

import com.github.cozyplugins.cozylibrary.indicator.ConfigurationConvertable;
import com.github.cozyplugins.cozylibrary.indicator.Replicable;
import com.github.cozyplugins.cozytreasurehunt.event.TreasurePostSpawnEvent;
import com.github.cozyplugins.cozytreasurehunt.event.TreasurePreSpawnEvent;
import com.github.cozyplugins.cozytreasurehunt.storage.LocationStorage;
import com.github.cozyplugins.cozytreasurehunt.storage.TreasureStorage;
import com.github.cozyplugins.cozytreasurehunt.storage.indicator.Savable;
import com.github.smuddgge.squishyconfiguration.interfaces.ConfigurationSection;
import com.github.smuddgge.squishyconfiguration.memory.MemoryConfigurationSection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.UUID;

/**
 * Represents a location of a treasure.
 */
public class TreasureLocation implements ConfigurationConvertable, Savable, Replicable<TreasureLocation> {

    private final @NotNull Treasure treasure;
    private final @NotNull Location location;

    /**
     * This field exists because a player could
     * place a similar block where the treasure is meant to spawn.
     */
    private boolean isSpawned;

    /**
     * Used to create a treasure location.
     *
     * @param treasure The instance of the treasure.
     * @param location The instance of the location.
     */
    public TreasureLocation(@NotNull Treasure treasure, @NotNull Location location) {
        this.treasure = treasure;
        this.location = location;
        this.isSpawned = false;
    }

    /**
     * Used to get the locations unique identifier.
     *
     * @return The unique identifier.
     */
    public @NotNull String getIdentifier() {
        return TreasureLocation.getIdentifier(this.location);
    }

    /**
     * Used to get the instance of the treasure.
     *
     * @return The instance of the treasure.
     */
    public @NotNull Treasure getTreasure() {
        return this.treasure;
    }

    /**
     * Used to get the location where the treasure is located.
     *
     * @return The location of the treasure.
     */
    public @NotNull Location getLocation() {
        return this.location;
    }

    /**
     * Used to check if a treasure is spawned.
     *
     * @return True if the treasure is spawned.
     */
    public boolean isSpawned() {
        return this.isSpawned;
    }

    /**
     * Used to spawn the treasure at the location.
     * This will also set {@link TreasureLocation#isSpawned} to true.
     * <li>
     * Spawning silently will not broadcast with events.
     * This will ensure the spawn of this treasure.
     * </li>
     *
     * @return This instance.
     */
    public @NotNull TreasureLocation spawnSilently() {
        this.treasure.spawn(this.location);
        this.isSpawned = true;
        this.save();
        return this;
    }

    /**
     * Used to remove the treasure at the location.
     * This will also set {@link TreasureLocation#isSpawned} to false.
     * <li>This will not call the treasure click event.</li>
     *
     * @return This instance.
     */
    public @NotNull TreasureLocation removeSilently() {
        this.location.getBlock().setType(Material.AIR);
        this.isSpawned = false;
        this.save();
        return this;
    }

    /**
     * Used to spawn the treasure.
     * This will also call the spawn treasure events.
     * Calling the events may cause the spawning to be canceled.
     *
     * @return True if the treasure was spawned.
     */
    public boolean spawn() {
        // Call pre-spawn event.
        TreasurePreSpawnEvent preSpawnEvent = new TreasurePreSpawnEvent(this);
        Bukkit.getPluginManager().callEvent(preSpawnEvent);

        // Check if the spawn has been canceled.
        if (preSpawnEvent.isCancelled()) return false;

        // Call post-spawn event.
        TreasurePostSpawnEvent postSpawnEvent = new TreasurePostSpawnEvent(this);
        Bukkit.getPluginManager().callEvent(postSpawnEvent);

        this.spawnSilently();
        return true;
    }

    @Override
    public String toString() {
        return "{" + this.getIdentifier() + ": "
                + this.location.getBlockX() + " "
                + this.location.getBlockY() + " "
                + this.location.getBlockZ() + "}";
    }

    @Override
    public @NotNull ConfigurationSection convert() {
        ConfigurationSection section = new MemoryConfigurationSection(new HashMap<>());

        section.set("treasure_identifier", this.treasure.getIdentifier().toString());

        section.set("location.x", this.location.getBlockX());
        section.set("location.y", this.location.getBlockY());
        section.set("location.z", this.location.getBlockZ());
        section.set("location.world", this.location.getWorld() == null
                ? null : this.location.getWorld().getName());

        section.set("is_spawned", this.isSpawned);

        return section;
    }

    @Override
    public void convert(ConfigurationSection section) {
        this.isSpawned = section.getBoolean("is_spawned");
    }

    @Override
    public void save() {
        LocationStorage.insert(this);
    }

    @Override
    public TreasureLocation duplicate() {
        ConfigurationSection data = this.convert();
        return TreasureLocation.create(data);
    }

    /**
     * Used to create a treasure location based on a configuration section.
     *
     * @param data The instance of the data.
     * @return The treasure location.
     */
    public static @Nullable TreasureLocation create(@NotNull ConfigurationSection data) {

        // Get the treasure.
        UUID treasure_identifier = UUID.fromString(data.getString("treasure_identifier"));
        Treasure treasure = TreasureStorage.get(treasure_identifier);
        if (treasure == null) return null;

        // Get the location.
        Location location = new Location(
                Bukkit.getWorld(data.getString("location.world")),
                data.getInteger("location.x"),
                data.getInteger("location.y"),
                data.getInteger("location.z")
        );

        TreasureLocation treasureLocation = new TreasureLocation(treasure, location);
        treasureLocation.convert(data);
        return treasureLocation;
    }

    /**
     * Used to get the unique identifier of a block location.
     *
     * @param location The location.
     * @return The unique identifier of a block location.
     */
    public static @NotNull String getIdentifier(Location location) {
        if (location.getWorld() == null) return "id(world_null)";
        return "id(" + location.getWorld().getName() + ","
                + location.getBlockX() + ","
                + location.getBlockY() + ","
                + location.getBlockZ() + ")";
    }
}
