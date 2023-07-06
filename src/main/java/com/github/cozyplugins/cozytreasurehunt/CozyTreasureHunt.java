package com.github.cozyplugins.cozytreasurehunt;

import com.github.cozyplugins.cozylibrary.CozyPlugin;
import com.github.cozyplugins.cozytreasurehunt.command.EditorCommand;

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
        this.addCommandType(new EditorCommand());
    }
}
