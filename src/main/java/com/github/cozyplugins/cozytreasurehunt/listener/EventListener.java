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

package com.github.cozyplugins.cozytreasurehunt.listener;

import com.github.cozyplugins.cozytreasurehunt.TreasureLocation;
import com.github.cozyplugins.cozytreasurehunt.event.TreasurePostClickEvent;
import com.github.cozyplugins.cozytreasurehunt.event.TreasurePreClickEvent;
import com.github.cozyplugins.cozytreasurehunt.storage.LocationStorage;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Represents this plugin's event listener.
 */
public class EventListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onClick(PlayerInteractEvent event) {

        System.out.println("click");

        // Check if a treasure is clicked in the most efficient way.
        if (event.getClickedBlock() == null) return;
        if (!LocationStorage.contains(event.getClickedBlock().getLocation())) return;

        // Get the treasure location.
        TreasureLocation treasureLocation = LocationStorage.get(event.getClickedBlock().getLocation());
        if (treasureLocation == null) return;

        // Call the treasure click event.
        TreasurePreClickEvent treasureClickEvent = new TreasurePreClickEvent(treasureLocation, event);
        Bukkit.getPluginManager().callEvent(treasureClickEvent);

        // Check if the event has been canceled.
        if (treasureClickEvent.isCancelled()) return;

        // Call the treasure post click event.
        // This is where plugins will respond to a player clicking a treasure.
        Bukkit.getPluginManager().callEvent(new TreasurePostClickEvent(treasureLocation, event));
    }
}
