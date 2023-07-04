package com.github.cozyplugins.cozytreasurehunt.storage.indicator;

import com.github.smuddgge.squishyconfiguration.interfaces.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

/**
 * Indicates if a class can be converted into a {@link ConfigurationSection}.
 */
public interface ConfigurationConvertable {

    /**
     * Used to convert the class into an unlinked configuration section.
     *
     * @return The unlinked configuration section instance.
     */
    @NotNull ConfigurationSection convert();
}
