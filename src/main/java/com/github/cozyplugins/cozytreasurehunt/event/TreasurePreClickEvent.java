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

package com.github.cozyplugins.cozytreasurehunt.event;

import com.github.cozyplugins.cozytreasurehunt.TreasureLocation;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a treasure click event.
 * <li>
 * Called before the {@link TreasurePostClickEvent}
 * </li>
 * <li>
 * This will be fired when a treasure has been
 * clicked, before it is processed.
 * </li>
 */
public class TreasurePreClickEvent extends TreasurePostClickEvent implements Cancellable {

    private boolean isCancelled;

    /**
     * Used to create a treasure click event.
     *
     * @param treasureLocation The instance of the treasure location.
     * @param event            The instance of the {@link PlayerInteractEvent} event.
     */
    public TreasurePreClickEvent(@NotNull TreasureLocation treasureLocation, @NotNull PlayerInteractEvent event) {
        super(treasureLocation, event);

        this.isCancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }
}
