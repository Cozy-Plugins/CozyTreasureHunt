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

import com.github.cozyplugins.cozylibrary.CozyPlugin;
import com.github.cozyplugins.cozytreasurehunt.command.TreasureCommand;
import com.github.cozyplugins.cozytreasurehunt.command.subcommand.EditorCommand;
import com.github.cozyplugins.cozytreasurehunt.listener.EventListener;
import com.github.cozyplugins.cozytreasurehunt.listener.TreasureListener;

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
        // Add commands.
        this.addCommandType(new TreasureCommand());

        // Add listeners.
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
        this.getServer().getPluginManager().registerEvents(new TreasureListener(), this);
    }
}
