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
import org.jetbrains.annotations.NotNull;

/**
 * Called when the {@link TreasurePreSpawnEvent} has passed.
 */
public class TreasurePostSpawnEvent extends CozyEvent {

    private final @NotNull TreasureLocation treasureLocation;

    /**
     * used to create a treasure post-spawn event.
     *
     * @param treasureLocation The instance of the treasure location.
     */
    public TreasurePostSpawnEvent(@NotNull TreasureLocation treasureLocation) {
        this.treasureLocation = treasureLocation;
    }

    /**
     * Used to get the {@link TreasureLocation} class.
     *
     * @return The instance of the treasure location.
     */
    public @NotNull TreasureLocation getTreasureLocation() {
        return this.treasureLocation;
    }
}