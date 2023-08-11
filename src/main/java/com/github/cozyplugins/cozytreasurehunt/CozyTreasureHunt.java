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

import com.github.cozyplugins.cozylibrary.ConsoleManager;
import com.github.cozyplugins.cozylibrary.CozyPlugin;
import com.github.cozyplugins.cozylibrary.datatype.ratio.Ratio;
import com.github.cozyplugins.cozytreasurehunt.command.TreasureCommand;
import com.github.cozyplugins.cozytreasurehunt.listener.EventListener;
import com.github.cozyplugins.cozytreasurehunt.listener.TreasureListener;
import com.github.cozyplugins.cozytreasurehunt.placeholder.FindPlaceholder;
import com.github.cozyplugins.cozytreasurehunt.placeholder.LeaderboardPlaceholder;
import com.github.cozyplugins.cozytreasurehunt.placeholder.TotalPlaceholder;
import com.github.cozyplugins.cozytreasurehunt.result.TreasureSpawnResult;
import com.github.cozyplugins.cozytreasurehunt.storage.ConfigFile;
import com.github.cozyplugins.cozytreasurehunt.storage.DataStorage;
import com.github.cozyplugins.cozytreasurehunt.storage.LocationStorage;
import com.github.cozyplugins.cozytreasurehunt.storage.TreasureStorage;
import org.bukkit.Raid;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Represents the main plugin class.
 */
public final class CozyTreasureHunt extends CozyPlugin {

    @Override
    public boolean enableCommandDirectory() {
        return true;
    }

    @Override
    public void onCozyEnable() {
        // Initialise the directory's.
        TreasureStorage.load();
        LocationStorage.load();

        // Set up the config file.
        ConfigFile.setup();

        // Add commands.
        this.addCommandType(new TreasureCommand());

        // Add listeners.
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
        this.getServer().getPluginManager().registerEvents(new TreasureListener(), this);

        // Add placeholders.
        this.addPlaceholder(new TotalPlaceholder());
        this.addPlaceholder(new LeaderboardPlaceholder());
        this.addPlaceholder(new FindPlaceholder());

        // Metrics.
        new Metrics(this, 19286);
    }


    /**
     * Used to spawn all the treasure registered
     * in this plugin.
     */
    public static @NotNull TreasureSpawnResult spawnTreasure() {
        TreasureSpawnResult result = new TreasureSpawnResult();

        // Remove all treasure in data file.
        DataStorage.removeAll();

        // The treasure with non-identical spawn ratios.
        HashMap<UUID, @NotNull List<TreasureLocation>> ratioTreasure = new HashMap<>();

        // Spawn identical ratios and find non-identical ratios.
        for (TreasureLocation location : LocationStorage.getAll()) {

            // Check if the spawn ratio is not identical.
            if (!location.getTreasure().getSpawnRatio().isIdentical()) {

                // Check if the map contains the treasure type.
                if (ratioTreasure.containsKey(location.getTreasure().getIdentifier())) {
                    List<TreasureLocation> list = ratioTreasure.get(location.getTreasure().getIdentifier());
                    list.add(location);
                    ratioTreasure.put(location.getTreasure().getIdentifier(), list);
                    continue;
                }

                List<TreasureLocation> list = new ArrayList<>();
                list.add(location);
                ratioTreasure.put(location.getTreasure().getIdentifier(), list);
                continue;
            }

            result.add(location, location.spawn());
        }

        // Spawn non-identical ratios.
        for (Map.Entry<UUID, @NotNull List<TreasureLocation>> entry : ratioTreasure.entrySet()) {
            int amount = entry.getValue().size();
            List<TreasureLocation> locations = entry.getValue();
            Treasure treasure = TreasureStorage.get(entry.getKey());

            // Check if the treasure is null.
            if (treasure == null) {
                ConsoleManager.warn("Treasure was null when spawning a ratio treasure. Identifier = " + entry.getKey());
                continue;
            }

            // Shuffle locations.
            Collections.shuffle(locations);

            // Get the spawn ratio.
            Ratio ratio = treasure.getSpawnRatio().getLeftScaled(amount);

            // Ensure the left is not over the number of locations.
            if (ratio.getLeft() > amount) ratio.setLeft(amount);

            // Loop "amount" times.
            for (int i = 0; i < amount; i++) {

                // Spawn location.
                TreasureLocation location = locations.get(i);
                result.add(location, location.spawn());
            }
        }

        return result;
    }

    /**
     * Used to spawn treasure at one random treasure location.
     *
     * @param treasureIdentifier The treasure's identifier to spawn.
     * @return The treasure spawn result.
     */
    public static @NotNull TreasureSpawnResult spawnOneTreasure(@NotNull UUID treasureIdentifier) {
        TreasureSpawnResult result = new TreasureSpawnResult();
        TreasureLocation location = LocationStorage.getRandomEmpty(treasureIdentifier);
        if (location == null) return result;

        result.add(location, location.spawn());

        return result;
    }


    /**
     * Used to un-spawn all the treasure at every
     * treasure location.
     */
    public static void unSpawnTreasure() {
        for (TreasureLocation location : LocationStorage.getAll()) {
            location.removeForever();
        }
    }
}
