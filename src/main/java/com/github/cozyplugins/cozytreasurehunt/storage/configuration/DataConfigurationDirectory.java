package com.github.cozyplugins.cozytreasurehunt.storage.configuration;

import com.github.cozyplugins.cozylibrary.configuration.ConfigurationDirectory;
import com.github.cozyplugins.cozytreasurehunt.CozyTreasureHunt;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the data configuration directory.
 * This is where player data and total treasure is stored.
 */
public class DataConfigurationDirectory extends ConfigurationDirectory {

    /**
     * <h1>Used to create a configuration directory</h1>
     */
    public DataConfigurationDirectory() {
        super("data", CozyTreasureHunt.class);
    }

    @Override
    public @Nullable String getDefaultFileName() {
        return null;
    }

    @Override
    protected void onReload() {

    }
}
