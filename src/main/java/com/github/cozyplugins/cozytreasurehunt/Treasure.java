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
import java.util.UUID;

/**
 * Represents a type of treasure.
 * <li>
 * Defaults to {@link Material#CHEST}
 * </li>
 */
public class Treasure implements ConfigurationConvertable, Savable {

    private final @NotNull UUID identifier;
    private @NotNull String name;
    private @NotNull Material material;
    private @Nullable String hdb;

    /**
     * Used to create a treasure type.
     *
     * @param identifier The treasure's identifier.
     * @param name The treasures name.
     */
    public Treasure(@NotNull UUID identifier, @NotNull String name) {
        this.identifier = UUID.randomUUID();
        this.name = name;
        this.material = Material.CHEST;
    }

    /**
     * Used to get the treasure identifier.
     * <li>
     * A unique set of characters that defines this treasure type.
     * </li>
     *
     * @return The identifier.
     */
    public @NotNull UUID getIdentifier() {
        return this.identifier;
    }

    /**
     * Used to get the treasures name.
     *
     * @return The treasures name.
     */
    public @NotNull String getName() {
        return this.name;
    }

    /**
     * Used to get the treasures material.
     *
     * @return The treasure's material.
     */
    public @NotNull Material getMaterial() {
        return this.material;
    }

    /**
     * Used to get the hdb value.
     *
     * @return The hdb value.
     */
    public @Nullable String getHdb() {
        return this.hdb;
    }

    /**
     * Used to set the name of the treasure.
     *
     * @param name The treasures new name.
     * @return This instance.
     */
    public @NotNull Treasure setName(@NotNull String name) {
        this.name = name;
        return this;
    }

    /**
     * Used to set the treasures material.
     *
     * @param material The material to set the treasure.
     * @return This instance.
     */
    public @NotNull Treasure setMaterial(@NotNull Material material) {
        this.material = material;
        return this;
    }

    /**
     * Used to set the hdb value.
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
    public static @NotNull Treasure create(@NotNull UUID identifier, @NotNull ConfigurationSection section) {
        Treasure treasure = new Treasure(identifier, section.getString("name", "new treasure"));

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
