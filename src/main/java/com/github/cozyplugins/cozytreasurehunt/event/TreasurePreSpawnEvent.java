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
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called before spawning a treasure.
 */
public class TreasurePreSpawnEvent extends TreasurePostSpawnEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Used to get the handler list for the cozy events.
     *
     * @return The handler list.
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    private boolean isCanceled;

    /**
     * used to create a treasure post-spawn event.
     *
     * @param treasureLocation The instance of the treasure location.
     */
    public TreasurePreSpawnEvent(@NotNull TreasureLocation treasureLocation) {
        super(treasureLocation);
    }

    @Override
    public boolean isCancelled() {
        return this.isCanceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCanceled = cancel;
    }
}
