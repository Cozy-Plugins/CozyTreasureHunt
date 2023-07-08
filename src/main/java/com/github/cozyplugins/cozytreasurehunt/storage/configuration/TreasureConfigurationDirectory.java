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

package com.github.cozyplugins.cozytreasurehunt.storage.configuration;

import com.github.cozyplugins.cozylibrary.configuration.ConfigurationDirectory;
import com.github.cozyplugins.cozytreasurehunt.CozyTreasureHunt;
import com.github.cozyplugins.cozytreasurehunt.Treasure;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a treasure configuration directory.
 * Used to store {@link Treasure} in yml files.
 */
public class TreasureConfigurationDirectory extends ConfigurationDirectory {

    /**
     * Used to create the treasure configuration directory.
     */
    public TreasureConfigurationDirectory() {
        super("treasure", CozyTreasureHunt.class);
    }

    @Override
    public @Nullable String getDefaultFileName() {
        return "treasure.yml";
    }

    @Override
    protected void onReload() {

    }
}
