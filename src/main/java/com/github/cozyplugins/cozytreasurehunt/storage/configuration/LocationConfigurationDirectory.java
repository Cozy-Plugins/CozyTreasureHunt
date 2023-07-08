package com.github.cozyplugins.cozytreasurehunt.storage.configuration;

import com.github.cozyplugins.cozylibrary.configuration.ConfigurationDirectory;
import com.github.cozyplugins.cozytreasurehunt.CozyTreasureHunt;
import org.jetbrains.annotations.Nullable;

/**
 * The instance of the location configuration directory.
 * Used to store the locations of the treasure.
 */
public class LocationConfigurationDirectory extends ConfigurationDirectory {

    /**
     * <h1>Used to create a location configuration directory</h1>
     */
    public LocationConfigurationDirectory() {
        super("locations", CozyTreasureHunt.class);
    }

    @Override
    public @Nullable String getDefaultFileName() {
        return "locations.yml";
    }

    @Override
    protected void onReload() {

    }
}
