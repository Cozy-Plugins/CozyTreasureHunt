package com.github.cozyplugins.cozytreasurehunt.dependency;

import com.github.cozyplugins.cozylibrary.ConsoleManager;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the head database dependency.
 */
public class HeadDatabaseDependency {

    /**
     * Used to check if the head database api is enabled.
     *
     * @return True if it is enabled.
     */
    public static boolean isEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("HeadDatabase");
    }

    /**
     * Used to get the head database api.
     * If it is not loaded it will return null
     * and post a console warning.
     * The plugin should check if the head database
     * is enabled before attempting to get the database.
     *
     * @return The head database api.
     */
    public static @NotNull HeadDatabaseAPI get() {
        if (!HeadDatabaseDependency.isEnabled()) {
            ConsoleManager.warn("Attempted to get the head database api but the plugin is not loaded.");
        }

        return new HeadDatabaseAPI();
    }
}
