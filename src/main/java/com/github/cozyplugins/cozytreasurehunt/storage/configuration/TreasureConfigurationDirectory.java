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
