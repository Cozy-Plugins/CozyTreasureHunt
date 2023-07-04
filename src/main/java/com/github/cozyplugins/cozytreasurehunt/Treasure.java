package com.github.cozyplugins.cozytreasurehunt;

import com.github.cozyplugins.cozylibrary.ConsoleManager;
import com.github.cozyplugins.cozytreasurehunt.storage.TreasureStorage;
import com.github.cozyplugins.cozytreasurehunt.storage.indicator.ConfigurationConvertable;
import com.github.cozyplugins.cozytreasurehunt.storage.indicator.Savable;
import com.github.smuddgge.squishyconfiguration.interfaces.ConfigurationSection;
import com.github.smuddgge.squishyconfiguration.memory.MemoryConfigurationSection;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * <h1>Represents a type of treasure</h1>
 * <li>Defaults to {@link Material#CHEST}</li>
 */
public class Treasure implements ConfigurationConvertable, Savable {

    private final @NotNull String identifier;
    private @NotNull Material material;
    private @Nullable String hdb;

    /**
     * <h1>Used to create a treasure type</h1>
     *
     * @param identifier The treasure's unique identifier.
     */
    public Treasure(@NotNull String identifier) {
        this.identifier = identifier;
        this.material = Material.CHEST;
    }

    /**
     * <h1>Used to get the treasure identifier</h1>
     * <li>A unique set of characters that defines this treasure type.</li>
     *
     * @return The identifier.
     */
    public @NotNull String getIdentifier() {
        return this.identifier;
    }

    /**
     * <h1>Used to get the treasures material</h1>
     *
     * @return The treasure's material.
     */
    public @NotNull Material getMaterial() {
        return this.material;
    }

    /**
     * <h1>Used to get the hdb value</h1>
     *
     * @return The hdb value.
     */
    public @Nullable String getHdb() {
        return this.hdb;
    }

    /**
     * <h1>Used to set the treasures material</h1>
     *
     * @param material The material to set the treasure.
     * @return This instance.
     */
    public @NotNull Treasure setMaterial(@NotNull Material material) {
        this.material = material;
        return this;
    }

    /**
     * <h1>Used to set the hdb value</h1>
     *
     * @param hdb The hdb value.
     * @return This instance.
     */
    public @NotNull Treasure setHdb(@Nullable String hdb) {
        this.hdb = hdb;
        return this;
    }

    @Override
    public @NotNull ConfigurationSection convert() {
        ConfigurationSection section = new MemoryConfigurationSection(new HashMap<>());

        section.set("material", this.material.toString());
        section.set("hdb", this.hdb);

        return section;
    }

    @Override
    public void save() {
        TreasureStorage.insert(this);
    }

    @SuppressWarnings("all")
    public static @NotNull Treasure create(@NotNull String identifier, @NotNull ConfigurationSection section) {
        Treasure treasure = new Treasure(identifier);

        String materialName = section.getString("material", "CHEST").toUpperCase();
        if (Material.getMaterial(materialName) == null) {
            ConsoleManager.error("The treasure with identifier &f" + identifier + " &chas an invalid material. The treasure will default to a &fCHEST&c.");
            materialName = "CHEST";
        }

        if (Material.getMaterial(materialName) == null) {
            ConsoleManager.error("The material CHEST is invalid on this server version, further errors may occur.");
            return treasure;
        }

        treasure.material = Material.getMaterial(materialName);
        treasure.hdb = section.getString("hdb");

        return treasure;
    }
}
